package com.majorproject.roomify.feature.ai_bot.domain.usecase

import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import com.majorproject.roomify.feature.ai_bot.domain.repo.AiBotRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: AiBotRepository
) {
    suspend operator fun invoke(message: String): Result<Message> {
        return repository.sendMessage(message)
    }
}