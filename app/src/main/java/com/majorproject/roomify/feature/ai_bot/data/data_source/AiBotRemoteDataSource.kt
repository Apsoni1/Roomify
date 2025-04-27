package com.majorproject.roomify.feature.ai_bot.data.data_source

import com.majorproject.roomify.core.di.AIBotApiService
import com.majorproject.roomify.core.di.AIBotRequest
import com.majorproject.roomify.core.di.AIBotResponse
import com.majorproject.roomify.core.di.MessageRequest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AiBotRemoteDataSource @Inject constructor(
    private val aiBotApiService: AIBotApiService
)  {
     suspend fun sendMessage(prompt: String, systemMessage: String): Result<AIBotResponse> = withContext(Dispatchers.IO) {
        try {
            val request = AIBotRequest(
                messages = listOf(
                    MessageRequest(role = "system", content = systemMessage),
                    MessageRequest(role = "user", content = prompt)
                )
            )

            val response = aiBotApiService.sendMessage(request).execute()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

