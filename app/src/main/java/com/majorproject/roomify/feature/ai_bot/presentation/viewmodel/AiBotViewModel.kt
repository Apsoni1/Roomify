package com.majorproject.roomify.feature.ai_bot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import com.majorproject.roomify.feature.ai_bot.domain.usecase.AddWelcomeMessageUseCase
import com.majorproject.roomify.feature.ai_bot.domain.usecase.ClearChatUseCase
import com.majorproject.roomify.feature.ai_bot.domain.usecase.GetMessagesUseCase
import com.majorproject.roomify.feature.ai_bot.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiBotViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val clearChatUseCase: ClearChatUseCase,
    private val addWelcomeMessageUseCase: AddWelcomeMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AiBotUiState>(AiBotUiState.Loading)
    val uiState: StateFlow<AiBotUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMessagesUseCase().collect { messages ->
                if (messages.isEmpty()) {
                    addWelcomeMessage()
                }
                _uiState.value = AiBotUiState.Success(messages)
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            // Update UI state to show typing indicator while waiting for response
            (_uiState.value as? AiBotUiState.Success)?.let { currentState ->
                _uiState.value = currentState.copy(isTyping = true)
            }

            sendMessageUseCase(message)
                .onFailure {
                    // Error is already handled in repository which adds error message to the chat
                    // Update UI state to hide typing indicator
                    (_uiState.value as? AiBotUiState.Success)?.let { currentState ->
                        _uiState.value = currentState.copy(isTyping = false)
                    }
                }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            clearChatUseCase()
        }
    }

    private fun addWelcomeMessage() {
        viewModelScope.launch {
            addWelcomeMessageUseCase()
        }
    }
}

sealed class AiBotUiState {
    data object Loading : AiBotUiState()
    data class Success(
        val messages: List<Message>,
        val isTyping: Boolean = false
    ) : AiBotUiState()
}