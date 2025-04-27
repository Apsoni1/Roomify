package com.majorproject.roomify.feature.ai_bot.data.repo_impl

import com.majorproject.roomify.feature.ai_bot.data.data_source.AiBotLocalDataSource
import com.majorproject.roomify.feature.ai_bot.data.data_source.AiBotRemoteDataSource
import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import com.majorproject.roomify.feature.ai_bot.domain.repo.AiBotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AiBotRepositoryImpl @Inject constructor(
    private val remoteDataSource: AiBotRemoteDataSource,
    private val localDataSource: AiBotLocalDataSource
) : AiBotRepository {

    // Define Roomify AI assistant persona
    private val roomifyPersona = "You are Roomy an AI Assistant inside an app named roomify that displays furniture in augmented reality , a helpful and friendly virtual assistant specialized in interior design, furniture arrangement, and home organization . Your goal is to help users create beautiful, functional living spaces. Always introduce yourself as 'Roomify Assistant' and maintain a supportive, creative, and professional tone. Offer practical advice for home decoration, furniture selection, room layout, color schemes, and space optimization. If users ask questions outside your expertise, politely redirect them to topics related to home design and furnishing."

    // Keep track of whether welcome message has been added
    private var welcomeMessageAdded = false

    override suspend fun sendMessage(userMessage: String): Result<Message> {
        // Save user message to local storage
        val userMessageObj = Message(userMessage, true)
        localDataSource.addMessage(userMessageObj)

        // Add temporary typing indicator message
        val typingMessage = Message("Typing...", false)
        localDataSource.addMessage(typingMessage)

        // Send message to API
        val result = remoteDataSource.sendMessage(userMessage, roomifyPersona)

        // Get current messages without the typing indicator
        val messages = localDataSource.getMessages().first()
        val messagesWithoutTyping = messages.filter { it.text != "Typing..." }

        // Clear all messages and re-add everything except the typing indicator
        localDataSource.clearChat(preserveMessages = true)
        messagesWithoutTyping.forEach { localDataSource.addMessage(it) }

        return result.fold(
            onSuccess = { response ->
                // Create bot response message
                val botMessage = Message(response.result, false)
                localDataSource.addMessage(botMessage)
                Result.success(botMessage)
            },
            onFailure = { throwable ->
                // Create error message
                val errorMessage = Message(
                    "I apologize, but I'm having trouble connecting to my services. Please check your internet connection and try again later.",
                    false
                )
                localDataSource.addMessage(errorMessage)
                Result.failure(throwable)
            }
        )
    }

    override fun getMessages(): Flow<List<Message>> {
        return localDataSource.getMessages()
    }

    override suspend fun clearChat() {
        localDataSource.clearChat(preserveMessages = false)
        welcomeMessageAdded = false
        addWelcomeMessage()
    }

    override suspend fun addWelcomeMessage() {
        // Check if we already have messages
        val currentMessages = localDataSource.getMessages().first()

        // Only add welcome message if there are no messages or we haven't added it yet
        if (currentMessages.isEmpty() && !welcomeMessageAdded) {
            val welcomeMessage = Message(
                "👋 Hello! I'm Roomy Your Ai Assistant, here to help with all your interior design and furniture needs. How can I assist you today?",
                false
            )
            localDataSource.addMessage(welcomeMessage)
            welcomeMessageAdded = true
        }
    }
}