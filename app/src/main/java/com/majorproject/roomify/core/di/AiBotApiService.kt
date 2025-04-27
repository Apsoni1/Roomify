package com.majorproject.roomify.core.di

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AIBotApiService {
    @Headers(
        "X-RapidAPI-Key: 4bfe58a1dbmshfa6784793d3989bp128347jsnb6a800d51f0f",        // <-- Put your actual RapidAPI Key here
        "X-RapidAPI-Host: chatgpt-42.p.rapidapi.com",             // <-- Example: openai80.p.rapidapi.com
        "Content-Type: application/json"
    )
    @POST("conversationgpt4")                         // <-- depends on API endpoint path
    fun sendMessage(@Body body: AIBotRequest): Call<AIBotResponse>
}

data class AIBotRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<MessageRequest>
)

data class MessageRequest(
    val role: String,
    val content: String
)

data class AIBotResponse(
    val result: String,
    val status: Boolean,
    val server_code: Int
)

data class Choice(
    val message: MessageResponse
)

data class MessageResponse(
    val role: String,
    val content: String
)
