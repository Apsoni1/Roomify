package com.majorproject.roomify.feature.auth.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.majorproject.roomify.feature.auth.domain.repo.AuthRepository
import com.majorproject.roomify.feature.common.presentation.models.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LinkGoogleAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return authRepository.linkGoogleAccount(credential)
    }
}