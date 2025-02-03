package com.yoda.cbt.data.remote


import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.majorproject.roomify.models.Resource
import kotlinx.coroutines.flow.Flow
interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
    fun isGoogleSignIn(): Boolean
    fun signInAnonymously(): Flow<Resource<AuthResult>>
    suspend fun deleteAccount(): Result<Unit>
    fun linkGoogleAccount(credential: AuthCredential): Flow<Resource<AuthResult>>
    fun signOut(): Flow<Resource<Unit>>  // New sign-out method
}
