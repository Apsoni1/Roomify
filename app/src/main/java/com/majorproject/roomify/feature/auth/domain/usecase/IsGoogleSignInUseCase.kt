package com.majorproject.roomify.feature.auth.domain.usecase

import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import javax.inject.Inject

class IsGoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isGoogleSignIn()
    }
}