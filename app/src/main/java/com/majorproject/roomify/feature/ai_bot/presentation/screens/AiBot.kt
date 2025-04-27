
// AiBot.kt (Updated fragment with proper adapter handling)
package com.majorproject.roomify.feature.ai_bot.presentation.screens

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.ai_bot.domain.models.Message
import com.majorproject.roomify.feature.ai_bot.presentation.adapter.ChatAdapter
import com.majorproject.roomify.feature.ai_bot.presentation.viewmodel.AiBotUiState
import com.majorproject.roomify.feature.ai_bot.presentation.viewmodel.AiBotViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiBot : Fragment() {

    private val viewModel: AiBotViewModel by viewModels()

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatAdapter: ChatAdapter
    private var vibrator: Vibrator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ai_bot, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true  // This will ensure new messages appear at the bottom
        }

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        return view
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(emptyList())
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                // This ensures new messages appear at the bottom and the list scrolls up
                stackFromEnd = true
                reverseLayout = false
            }
            adapter = chatAdapter
        }
    }

    private fun setupClickListeners() {
        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString()
            if (userMessage.isNotBlank()) {
                vibrate()
                viewModel.sendMessage(userMessage)
                messageEditText.setText("")

                // Ensure RecyclerView scrolls to the last item after sending a message
                chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }
    private fun vibrate() {
        if (vibrator?.hasVibrator() == true) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // For API 26+, use VibrationEffect
                val vibrationEffect = VibrationEffect.createOneShot(
                    50, // Vibration duration in milliseconds
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
                vibrator?.vibrate(vibrationEffect)
            } else {
                // For API 25 and below
                @Suppress("DEPRECATION")
                vibrator?.vibrate(50)
            }
        }
    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is AiBotUiState.Success -> {
                        updateChatMessages(state.messages)
                    }
                    is AiBotUiState.Loading -> {
                        // Show loading state if needed
                    }
                }
            }
        }
    }

    private fun updateChatMessages(messages: List<Message>) {
        chatAdapter.updateMessages(messages)

        // After updating messages, ensure RecyclerView scrolls to the bottom
        chatRecyclerView.post {
            chatRecyclerView.scrollToPosition(messages.size - 1)

            // Scroll the NestedScrollView to the bottom
            (view?.parent as? NestedScrollView)?.post {
                (view?.parent as NestedScrollView).smoothScrollTo(0, chatRecyclerView.bottom)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clearChat -> {
                viewModel.clearChat()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
