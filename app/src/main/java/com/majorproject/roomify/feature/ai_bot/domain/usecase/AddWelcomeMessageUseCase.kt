package com.majorproject.roomify.feature.ai_bot.domain.usecase

import com.majorproject.roomify.feature.ai_bot.domain.repo.AiBotRepository
import javax.inject.Inject

class AddWelcomeMessageUseCase @Inject constructor(
    private val repository: AiBotRepository
) {
    suspend operator fun invoke() {
        repository.addWelcomeMessage()
    }
}