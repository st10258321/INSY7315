package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.ChatListAdapter
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentChatListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.getValue

class ChatListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatListAdapter
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    var chatList = mutableListOf<Chat>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        val reviewRepo = ReviewRepository()
        val chatRepo = ChatRepository()

        val viewModelFactory =
            ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
        val userViewModel: UserViewModel by viewModels { viewModelFactory }

        recyclerView = binding.rvChats
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        chatAdapter = ChatListAdapter(chatList, onItemClick = {chat->
            val bundle = Bundle().apply{
                putString("chatID", chat.chatId)
                putString("serviceProviderId", chat.serviceProviderId)
            }
            val fragment = ChatFragment().apply {
                arguments = bundle
            }
           parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment,fragment)
               .addToBackStack(null)
               .commit()

        })
        //get the actual chat in the database and display here
        recyclerView.adapter = chatAdapter
        userViewModel.getUserChats()



        userViewModel.userChats.observe(viewLifecycleOwner) { chats ->
            if (chats != null && chats.isNotEmpty()) {
                chatAdapter.uploadChats(chats.toMutableList())
                chatAdapter.notifyDataSetChanged()
                Log.d("--checking chatList","${chats.size}")

            }else{
                Log.d("--chat Failed","${chats?.size}")
            }
        }

    }
}

