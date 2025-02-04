package com.yoda.cbt.data.remote


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.majorproject.roomify.data.internal.UserPreferencesRepo
import com.majorproject.roomify.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserPreferencesRepo,
    private val context: Context // Add context for Google sign-out
) : AuthRepository
{
    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val oldUser = firebaseAuth.currentUser
            val oldUserId = oldUser?.uid

            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun linkGoogleAccount(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())

            val currentUser = firebaseAuth.currentUser
                ?: throw Exception("No user is signed in to link the Google account")

            val oldUserId = currentUser.uid

            try {
                // Check if the Google account is already linked
                val linkedProviders = currentUser.providerData.map { it.providerId }
                if ("google.com" in linkedProviders) {
                    throw Exception("Google account is already linked to this user")
                }

                // Link the Google account
                val result = currentUser.linkWithCredential(credential).await()
                val linkedUser = result.user ?: throw Exception("Failed to link Google account")
                val googleEmail = linkedUser.email ?: throw Exception("Google email is missing")

                // Update the display name to Google email ID
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(googleEmail)
                    .build()

                linkedUser.updateProfile(profileUpdates).await()

                emit(Resource.Success(result))
            } catch (e: FirebaseAuthUserCollisionException) {
                // Handle the case where the Google account is already linked to another user
                emit(Resource.Error("This Google account is already linked to another user"))
            } catch (e: Exception) {
                emit(Resource.Error("Failed to link Google account: ${e.message}"))
            }
        }.catch { exception ->
            emit(Resource.Error("An error occurred: ${exception.message}"))
        }
    }


    override fun isGoogleSignIn(): Boolean {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.providerData?.any { it.providerId == "google.com" } == true
    }

    override fun signInAnonymously(): Flow<Resource<AuthResult>> {
        return flow {
            try {
                emit(Resource.Loading())
                val result = firebaseAuth.signInAnonymously().await()
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }


    override suspend fun deleteAccount(): Result<Unit> {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            try {
                userRepository.saveUserIdToken(" ")
                currentUser.unlink(GoogleAuthProvider.PROVIDER_ID).await()
                currentUser.delete().await() // Delete the user
                Result.success(Unit) // Successfully deleted
            } catch (e: Exception) {
                Result.failure(Exception("Failed to delete account: ${e.message}"))
            }
        } else {
            Result.failure(Exception("No user is signed in"))
        }
    }

    override fun signOut(): Flow<Resource<Unit>> {
        return flow {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                try {
                    // Start the Google sign-out process
                    val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                    googleSignInClient.signOut().await() // Wait for Google sign-out to finish
                    firebaseAuth.signOut()
                    userRepository.saveUserIdToken(" ")

                    // Automatically sign in anonymously after successful sign out
                    val anonymousResult = firebaseAuth.signInAnonymously().await()
                    if (anonymousResult.user != null) {
                        emit(Resource.Success(Unit))
                    } else {
                        emit(Resource.Error("Failed to sign in anonymously after sign out"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error("Sign-out failed: ${e.message}"))
                }
            } else {
                // If no user is signed in, try anonymous sign in directly
                try {
                    val anonymousResult = firebaseAuth.signInAnonymously().await()
                    if (anonymousResult.user != null) {
                        emit(Resource.Success(Unit))
                    } else {
                        emit(Resource.Error("Failed to sign in anonymously"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error("Anonymous sign-in failed: ${e.message}"))
                }
            }
        }
    }
}
