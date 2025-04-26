package com.majorproject.roomify.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.majorproject.roomify.feature.auth.domain.usecase.*
import com.majorproject.roomify.feature.common.presentation.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val isGoogleSignInUseCase: IsGoogleSignInUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val linkGoogleAccountUseCase: LinkGoogleAccountUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    // We need to change the type parameter to accept nullable FirebaseUser
    private val _authState = MutableStateFlow<Resource<FirebaseUser?>>(Resource.Loading(null))
    val authState: StateFlow<Resource<FirebaseUser?>> = _authState

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginUserUseCase(email, password).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        _authState.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            registerUserUseCase(email, password).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        _authState.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            googleSignInUseCase(credential).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        _authState.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }

    fun isGoogleSignIn(): Boolean {
        return isGoogleSignInUseCase()
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            signInAnonymouslyUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        _authState.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val result = deleteAccountUseCase()
            result.fold(
                onSuccess = {
                    // Now this is valid since _authState accepts nullable FirebaseUser
                    _authState.value = Resource.Success(null)
                },
                onFailure = { e ->
                    _authState.value = Resource.Error(e.message ?: "Failed to delete account")
                }
            )
        }
    }

    fun linkGoogleAccount(credential: AuthCredential) {
        viewModelScope.launch {
            linkGoogleAccountUseCase(credential).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        _authState.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        // Now this is valid since _authState accepts nullable FirebaseUser
                        _authState.value = Resource.Success(null)
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message ?: "Unknown error occurred")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            }
        }
    }
}