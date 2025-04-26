package com.majorproject.roomify.feature.auth.domain.usecase


import com.google.firebase.auth.AuthResult
import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import com.majorproject.roomify.feature.common.presentation.models.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> {
        return authRepository.loginUser(email, password)
    }
}