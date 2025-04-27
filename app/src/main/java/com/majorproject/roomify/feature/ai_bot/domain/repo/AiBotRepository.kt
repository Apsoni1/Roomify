package com.majorproject.roomify.feature.ai_bot.domain.repo

import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface AiBotRepository {
    suspend fun sendMessage(userMessage: String): Result<Message>
    fun getMessages(): Flow<List<Message>>
    suspend fun clearChat()
    suspend fun addWelcomeMessage()
}