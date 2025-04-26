package com.majorproject.roomify.feature.auth.domain.usecase

import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.deleteAccount()
    }
}