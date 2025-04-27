package com.majorproject.roomify.feature.ai_bot.data.data_source

import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiBotLocalDataSource @Inject constructor()  {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())

     fun getMessages(): Flow<List<Message>> = _messages.asStateFlow()

     suspend fun addMessage(message: Message) {
        _messages.value = _messages.value + message
    }

     suspend fun clearChat(preserveMessages: Boolean) {
        _messages.value = emptyList()
    }
}