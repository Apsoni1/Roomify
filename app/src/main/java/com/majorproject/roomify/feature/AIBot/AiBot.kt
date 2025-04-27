package com.majorproject.roomify.feature.AIBot

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.core.di.AIBotApiService
import com.majorproject.roomify.core.di.AIBotRequest
import com.majorproject.roomify.core.di.AIBotResponse
import com.majorproject.roomify.core.di.MessageRequest
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class AiBot : Fragment() {
    @Inject
    lateinit var aiBotApiService: AIBotApiService

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()

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

        chatAdapter = ChatAdapter(messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString()
            if (userMessage.isNotBlank()) {
                val message = Message(userMessage, true)
                addMessage(message)
                messageEditText.setText("")
                getBotResponse(userMessage)
            }
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clearChat -> {
                messageList.clear()
                chatAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addMessage(message: Message) {
        messageList.add(message)
        chatAdapter.notifyItemInserted(messageList.size - 1)
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun getBotResponse(prompt: String) {
        val typingMessage = Message("Typing...", false)
        addMessage(typingMessage)

        val request = AIBotRequest(
            messages = listOf(
                MessageRequest(role = "user", content = prompt)
            )
        )

        aiBotApiService.sendMessage(request).enqueue(object : Callback<AIBotResponse> {
            override fun onResponse(call: Call<AIBotResponse>, response: Response<AIBotResponse>) {
                val typingMessageIndex = messageList.indexOf(typingMessage)
                if (typingMessageIndex != -1) {
                    messageList.removeAt(typingMessageIndex)
                    chatAdapter.notifyItemRemoved(typingMessageIndex)
                }

                if (response.isSuccessful) {
                    val reply = response.body()?.result ?: "No response."
                    val botMessage = Message(reply, false)
                    addMessage(botMessage)
                } else {
                    val errorMessage = Message("Error: ${response.message()}", false)
                    addMessage(errorMessage)
                }
            }

            override fun onFailure(call: Call<AIBotResponse>, t: Throwable) {
                val typingMessageIndex = messageList.indexOf(typingMessage)
                if (typingMessageIndex != -1) {
                    messageList.removeAt(typingMessageIndex)
                    chatAdapter.notifyItemRemoved(typingMessageIndex)
                }
                val failureMessage = Message("Failure: ${t.message}", false)
                addMessage(failureMessage)
            }
        })
    }
}