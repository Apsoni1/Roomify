// First, let's modify your ChatAdapter to handle streaming text
package com.majorproject.roomify.feature.ai_bot.presentation.adapter

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.ai_bot.domain.models.Message

class ChatAdapter(private var messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_USER = 0
    private val ITEM_BOT = 1
    private val ITEM_TYPING = 2

    // Flag to track if streaming is in progress
    private var isStreaming = false
    // Handler for posting delayed text updates
    private val handler = Handler(Looper.getMainLooper())
    // Current streaming position
    private var streamPosition = -1
    // Current text being streamed
    private var currentStreamText = ""
    // Currently visible portion of text
    private var visibleStreamText = ""
    // Character-by-character delay in milliseconds
    private val streamDelayMs = 20L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_USER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_message, parent, false)
                UserViewHolder(view)
            }
            ITEM_TYPING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_typing_indicator, parent, false)
                TypingViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bot_message, parent, false)
                BotViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is BotViewHolder -> {
                // If this is the last bot message and streaming is enabled, start streaming
                if (position == messages.size - 1 && !message.isUser && isStreaming && position == streamPosition) {
                    holder.bindPartial(visibleStreamText)
                } else {
                    holder.bind(message)
                }
            }
            is TypingViewHolder -> holder.startAnimation()
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when {
            message.isUser -> ITEM_USER
            message.text == "Typing..." -> ITEM_TYPING
            else -> ITEM_BOT
        }
    }

    fun updateMessages(newMessages: List<Message>) {
        // Check if new message was added that needs streaming
        val shouldStream = newMessages.size > messages.size &&
                !newMessages.last().isUser &&
                newMessages.last().text != "Typing..."

        val diffCallback = MessageDiffCallback(messages, newMessages)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.messages = newMessages
        diffResult.dispatchUpdatesTo(this)

        if (shouldStream) {
            startStreamingLastMessage()
        }
    }

    private fun startStreamingLastMessage() {
        // If already streaming, stop previous stream
        if (isStreaming) {
            handler.removeCallbacksAndMessages(null)
        }

        // Get the last message (bot response)
        val lastPosition = messages.size - 1
        val lastMessage = messages[lastPosition]

        // Set up streaming state
        isStreaming = true
        streamPosition = lastPosition
        currentStreamText = lastMessage.text
        visibleStreamText = ""

        // Start streaming characters one by one
        streamNextCharacter(0)
    }

    private fun streamNextCharacter(index: Int) {
        if (index >= currentStreamText.length) {
            // Streaming complete
            isStreaming = false
            streamPosition = -1
            return
        }

        // Add next character
        visibleStreamText += currentStreamText[index]

        // Update the view
        notifyItemChanged(streamPosition)

        // Schedule next character
        handler.postDelayed({
            streamNextCharacter(index + 1)
        }, streamDelayMs)
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.userMessageText)

        fun bind(message: Message) {
            messageText.text = message.text
        }
    }

    class BotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.botMessageText)

        fun bind(message: Message) {
            messageText.text = message.text
        }

        fun bindPartial(partialText: String) {
            messageText.text = partialText
        }
    }

    class TypingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dot1 = view.findViewById<View>(R.id.dot1)
        private val dot2 = view.findViewById<View>(R.id.dot2)
        private val dot3 = view.findViewById<View>(R.id.dot3)

        fun startAnimation() {
            // Animate each dot with a delay
            animateDot(dot1, 0)
            animateDot(dot2, 150)
            animateDot(dot3, 300)
        }

        private fun animateDot(dot: View, delay: Long) {
            val alphaAnimation = AlphaAnimation(0.5f, 1.0f)
            alphaAnimation.duration = 600
            alphaAnimation.repeatMode = ValueAnimator.REVERSE
            alphaAnimation.repeatCount = ValueAnimator.INFINITE
            alphaAnimation.startOffset = delay
            dot.startAnimation(alphaAnimation)
        }

        fun stopAnimation() {
            dot1.clearAnimation()
            dot2.clearAnimation()
            dot3.clearAnimation()
        }
    }

    // DiffUtil implementation for efficient updates
    private class MessageDiffCallback(
        private val oldMessages: List<Message>,
        private val newMessages: List<Message>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldMessages.size

        override fun getNewListSize(): Int = newMessages.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // You might want to add an ID field to Message for more accurate comparison
            return oldMessages[oldItemPosition] == newMessages[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMessages[oldItemPosition].text == newMessages[newItemPosition].text &&
                    oldMessages[oldItemPosition].isUser == newMessages[newItemPosition].isUser
        }
    }
}
