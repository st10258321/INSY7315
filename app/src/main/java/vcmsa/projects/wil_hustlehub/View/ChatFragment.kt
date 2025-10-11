package vcmsa.projects.wil_hustlehub.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
    private var currentUserId : String =""  // TODO: Replace with logged-in userId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatAdapter = ChatAdapter(mutableListOf(), currentUserId)
        binding.chatMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
        val editor = context?.getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        currentUserId = editor?.getString("uid", "").toString()
        userViewModel.getUserData(currentUserId).observe(viewLifecycleOwner) {user ->
            if(user != null){
                chatAdapter.currentUserId = currentUserId


                userViewModel.messageList.observe(viewLifecycleOwner) { (success, error, messages) ->
                    if (success && messages != null) {
                        Log.d("message-list", "${messages.size}")
                        chatAdapter.updateMessages(messages)
                        binding.chatMessagesRecyclerView.scrollToPosition(messages.size - 1)
                    }else{
                        Log.d("Failed to load","${messages?.size}")
                    }
                }
            }
        }

        userViewModel.loadMessages("chat1")
        Log.d("current-user-id", "$currentUserId")
        //the problem lies here, why doesn't the user's data persist from login
//        userViewModel.currentUserData.observe(viewLifecycleOwner) { user ->
//            Log.d("message--", "${user?.email}" ?: "it doesnt persist from the login")
//            if (user != null){
//                Toast.makeText(requireContext(), "Logged in as ${user.name}", Toast.LENGTH_SHORT).show()
//                currentUserId = user.userID
//                chatAdapter.currentUserId = currentUserId
//                Log.d("current-user-id", "$currentUserId")
//            }else{
//                Log.d("2nd check","$currentUserId")
//                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
//            }
//        }



        // Handle Send button click
        binding.sendMessageBtn.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(
                    messageId = System.currentTimeMillis().toString(),
                    chatId = "chat1",
                    senderId = currentUserId,
                    receiverId = "eOzy8HdGhbbilmY9x93uea4ogom1",
                    senderName = "Me",
                    message = messageText,
                    timeSent = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )

                // Add to adapter immediately
                val updatedList = chatAdapter.messages.toMutableList()
                    updatedList.add(newMessage)
                    chatAdapter.updateMessages(updatedList)
                Log.d("updated-list", "${updatedList.size}")


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