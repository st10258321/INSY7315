package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.ChatListAdapter
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.R
import java.text.SimpleDateFormat
import java.util.*

class ChatListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)

        recyclerView = view.findViewById(R.id.rvChats)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy chat list
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        val chatList = listOf(
            Chat("1", "Alice", "", "", "", "", "Hey! How are you?", currentTime),
            Chat("2", "Bob", "", "", "", "", "Are you coming today?", currentTime),
            Chat("3", "Charlie", "", "", "", "", "Let's catch up tomorrow!", currentTime)
        )

        // Adapter and click listener
        chatAdapter = ChatListAdapter(chatList) { chat ->
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("chatId", chat.chatId)
            intent.putExtra("chatName", chat.userId)
            startActivity(intent)
        }

        recyclerView.adapter = chatAdapter

        return view
    }
}
