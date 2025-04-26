package com.majorproject.roomify.feature.auth.data.repo_impl


import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.majorproject.roomify.feature.auth.data.datasource.AuthDataSource
import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import com.majorproject.roomify.feature.common.presentation.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = authDataSource.loginUser(email, password)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred during login"))
        }
    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = authDataSource.registerUser(email, password)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred during registration"))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = authDataSource.googleSignIn(credential)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred during Google sign-in"))
        }
    }

    override fun isGoogleSignIn(): Boolean {
        return authDataSource.isGoogleSignIn()
    }

    override fun signInAnonymously(): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = authDataSource.signInAnonymously()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred during anonymous sign-in"))
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            authDataSource.deleteAccount()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun linkGoogleAccount(credential: AuthCredential): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = authDataSource.linkGoogleAccount(credential)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred when linking Google account"))
        }
    }

    override fun signOut(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            authDataSource.signOut()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred during sign out"))
        }
    }
}