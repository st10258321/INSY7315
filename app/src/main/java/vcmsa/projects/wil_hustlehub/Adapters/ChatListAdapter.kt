package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.R

class ChatListAdapter(
    private val chatList: MutableList<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.tvContactName.text = chat.serviceProviderName
        holder.tvLastMessage.text = chat.lastMessage
        holder.itemView.setOnClickListener { onItemClick(chat) }
    }
    fun uploadChats(chats : MutableList<Chat>) {
        chatList.clear()
        chatList.addAll(chats)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chatList.size
}
