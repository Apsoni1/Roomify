package com.majorproject.roomify.feature.auth.domain.usecase

import com.google.firebase.auth.AuthResult
import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import com.majorproject.roomify.feature.common.presentation.models.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<AuthResult>> {
        return authRepository.signInAnonymously()
    }
}