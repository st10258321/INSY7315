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
import vcmsa.projects.wil_hustlehub.Network.PushApiClient
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
    private var currentUserName : String = "John Doe"
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
        val chatId = arguments?.getString("chatID")
        val serviceProviderId = arguments?.getString("serviceProviderId")
        Log.d("chat-id", "$chatId")
        Log.d("service-provider-id", "$serviceProviderId")

        chatAdapter = ChatAdapter(mutableListOf(), currentUserId)
        binding.chatMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
        //this fragment must receive the chat id from the previous fragment
        //then it must load the messages from the chat id (Messages collection in the firebase database)
        val editor = context?.getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        currentUserId = editor?.getString("uid", "").toString()
        userViewModel.getUserData(currentUserId).observe(viewLifecycleOwner) { user ->
            if(user != null){
                chatAdapter.currentUserId = currentUserId
                currentUserName = user.name

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

        userViewModel.loadMessages(chatId!!)
        Log.d("current-user-id", "$currentUserId")

        // Handle Send button click
        binding.sendMessageBtn.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(
                    messageId = System.currentTimeMillis().toString(),
                    chatId = chatId,
                    senderId = currentUserId,
                    receiverId = serviceProviderId!!,
                    senderName = "Me",
                    message = messageText,
                    timeSent = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )
                //get the token of the receiver by the receiver id
                try{
                    userViewModel.getUserData(serviceProviderId).observe(viewLifecycleOwner){user->
                        if(user != null){
                            PushApiClient.sendMessageNotification(
                                requireContext(),
                                user.fcmToken ?: "",
                                currentUserName,
                                messageText
                            )
                        }
                    }
                }catch (e : Exception){
                    Log.d("push notification failes", "$e")
                }

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
                userViewModel.sendMessage(chatId, newMessage)
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