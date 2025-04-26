package com.majorproject.roomify.feature.auth.data.datasource


import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun loginUser(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun registerUser(email: String, password: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun googleSignIn(credential: AuthCredential): AuthResult {
        return firebaseAuth.signInWithCredential(credential).await()
    }

    fun isGoogleSignIn(): Boolean {
        val currentUser = firebaseAuth.currentUser ?: return false
        return currentUser.providerData.any { it.providerId == "google.com" }
    }

    suspend fun signInAnonymously(): AuthResult {
        return firebaseAuth.signInAnonymously().await()
    }

    suspend fun deleteAccount() {
        firebaseAuth.currentUser?.delete()?.await()
    }

    suspend fun linkGoogleAccount(credential: AuthCredential): AuthResult {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.linkWithCredential(credential)?.await()
            ?: throw IllegalStateException("No user is currently signed in")
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }
}