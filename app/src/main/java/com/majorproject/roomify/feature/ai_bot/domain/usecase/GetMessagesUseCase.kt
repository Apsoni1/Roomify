package com.majorproject.roomify.feature.ai_bot.domain.usecase

import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import com.majorproject.roomify.feature.ai_bot.domain.repo.AiBotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: AiBotRepository
) {
    operator fun invoke(): Flow<List<Message>> {
        return repository.getMessages()
    }
}