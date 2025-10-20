package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.wil_hustlehub.Adapters.ChatAdapter
import vcmsa.projects.wil_hustlehub.Model.Message
import vcmsa.projects.wil_hustlehub.databinding.FragmentChatBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private var currentUserId = "Me" // Dummy current user

    private var chatId = ""
    private var chatName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate your fragment layout for activity
        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get chat data from intent
        chatId = intent.getStringExtra("chatId") ?: "1"
        chatName = intent.getStringExtra("chatName") ?: "Unknown"

        binding.chatPartnerName.text = chatName

        // Setup RecyclerView
        chatAdapter = ChatAdapter(mutableListOf(), currentUserId)
        binding.chatMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatAdapter
        }

        // Dummy messages
        val dummyMessages = listOf(
            Message("1", chatId, "Alice", currentUserId, "Alice", "Hello!", "12:00"),
            Message("2", chatId, currentUserId, "Alice", currentUserId, "Hi $chatName!", "12:01")
        )
        chatAdapter.updateMessages(dummyMessages)
        binding.chatMessagesRecyclerView.scrollToPosition(dummyMessages.size - 1)

        // Send button
        binding.sendMessageBtn.setOnClickListener {
            val msgText = binding.messageInput.text.toString().trim()
            if (msgText.isNotEmpty()) {
                val newMsg = Message(
                    System.currentTimeMillis().toString(),
                    chatId,
                    currentUserId,
                    chatName,
                    currentUserId,
                    msgText,
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                )
                val updated = chatAdapter.messages.toMutableList()
                updated.add(newMsg)
                chatAdapter.updateMessages(updated)
                binding.chatMessagesRecyclerView.scrollToPosition(updated.size - 1)
                binding.messageInput.text.clear()
            }
        }

        // Back button
        binding.backToProfileBtn.setOnClickListener { finish() }
    }
}
