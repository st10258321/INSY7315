package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.ChatAdapter
import vcmsa.projects.wil_hustlehub.Model.Message
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentChatBinding
import vcmsa.projects.wil_hustlehub.databinding.ItemChatMessageReceivedBinding
import vcmsa.projects.wil_hustlehub.databinding.ItemChatMessageSentBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val viewModelFactory =
        ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)

    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    private lateinit var chatAdapter: ChatAdapter
    private val currentUserId = "dummyUser123" // TODO: Replace with logged-in userId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        chatAdapter = ChatAdapter(mutableListOf(), currentUserId)
        binding.chatMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        // Dummy starting messages
        val dummyMessages = listOf(
            Message(
                messageId = "1",
                chatId = "chat1",
                senderId = currentUserId,
                senderName = "Me",
                message = "Hey, how are you?",
                timeSent = "10:30 AM"
            ),
            Message(
                messageId = "2",
                chatId = "chat1",
                senderId = "provider456",
                senderName = "Provider",
                message = "I’m good, thanks! You?",
                timeSent = "10:31 AM"
            )
        )
        chatAdapter.updateMessages(dummyMessages)

        // Handle Send button click
        binding.sendMessageBtn.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(
                    messageId = System.currentTimeMillis().toString(),
                    chatId = "chat1",
                    senderId = currentUserId,
                    senderName = "Me",
                    message = messageText,
                    timeSent = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )

                // Add to adapter immediately
                val updatedList = chatAdapter.let { adapter ->
                    val current = adapter.messages.toMutableList()
                    current.add(newMessage)
                    current
                }
                chatAdapter.updateMessages(updatedList)

                // Scroll to latest
                binding.chatMessagesRecyclerView.scrollToPosition(updatedList.size - 1)

                // Clear input field
                binding.messageInput.text.clear()

                // (Optional) Send to ViewModel/Firebase
                userViewModel.sendMessage("chat1", messageText)
            }
        }

        // Configured chat back button
        binding.backToProfileBtn.setOnClickListener {
            val fragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace((requireActivity() as AppCompatActivity).findViewById<ViewGroup>(R.id.nav_host_fragment).id, fragment)
                .addToBackStack(null) // so user can press back to return here
                .commit()
        }

        // Observe message status from ViewModel
        userViewModel.messageStatus.observe(viewLifecycleOwner) { (success, error) ->
            if (!success) {
                Toast.makeText(
                    requireContext(),
                    error ?: "Failed to send message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}