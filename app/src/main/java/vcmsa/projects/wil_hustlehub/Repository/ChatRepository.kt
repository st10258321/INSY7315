package vcmsa.projects.wil_hustlehub.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.Model.Message
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.User

class ChatRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private val createdDateFormat =
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    //this function creates the chat and also loads the chat history between the user and
    //the service provider if it exists

    fun createChat(
        serviceProviderId: String,
        callback: (Boolean, String?, Chat?, Boolean) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null, false)
            return
        }

        val userId = currentUser.uid

        // preventing the users from sending messages to themselves
        if (userId == serviceProviderId) {
            callback(false, "Cannot chat with yourself", null, false)
            return
        }

        // checking if a chat already exists between these two users
        checkExistingChat(userId, serviceProviderId) { existingChat ->
            if (existingChat != null) {
                // chat history is retrieved
                callback(true, null, existingChat, false)
            } else {
                // creating a new chat
                createNewChat(serviceProviderId, userId, callback)
            }
        }
    }

    // checks if chat already exists between user and service provider
    private fun checkExistingChat(
        userId: String,
        serviceProviderId: String,
        callback: (Chat?) -> Unit
    ) {
        // get the chats where current user is the client
        database.child("Chats")
            .orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var foundChat: Chat? = null

                    // Check if any existing chat matches the service provider
                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        if (chat != null && chat.serviceProviderId == serviceProviderId) {
                            foundChat = chat
                            break
                        }
                    }

                    callback(foundChat)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

    //creating a new chat after fetching service details
    private fun createNewChat(
        serviceProviderId: String,
        userId: String,
        callback: (Boolean, String?, Chat?, Boolean) -> Unit
    ) {

        val chatId = database.child("Chats").push().key

        if (chatId == null) {
            callback(false, "Failed to generate chat ID", null, false)
            return
        }

        //saving to the database and creating the chat
        database.child("users").child(serviceProviderId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    val serviceProvider = userSnapshot.getValue(User::class.java)

                    if (serviceProvider == null) {
                        callback(false, "Service provider not found", null, false)
                        return
                    }

                    val serviceProviderName = serviceProvider.name
                    val createdDate = createdDateFormat.format(java.util.Date())

                    val chat = Chat(
                        chatId = chatId,
                        userId = userId,
                        serviceProviderId = serviceProviderId,
                        serviceProviderName = serviceProviderName,
                        lastMessage = "Get started and send a message",
                        lastMessageTime = createdDate,
                        createdDate = createdDate
                    )

                    database.child("Chats").child(chatId).setValue(chat)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true, null, chat, true)
                            } else {
                                callback(false, task.exception?.message, null, false)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null, false)
                }
            })
    }


    // getting all chats for the current user
    fun getUserChats(callback: (Boolean, String?, List<Chat>?) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid

        // chats for the customer
        database.child("Chats").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userChats = mutableListOf<Chat>()

                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        chat?.let { userChats.add(it) }
                    }

                    // chats for the service provider
                    database.child("Chats").orderByChild("serviceProviderId").equalTo(userId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(providerSnapshot: DataSnapshot) {
                                val providerChats = mutableListOf<Chat>()
                                for (chatSnapshot in providerSnapshot.children) {
                                    val chat = chatSnapshot.getValue(Chat::class.java)
                                    chat?.let { providerChats.add(it) }
                                }

                                val allChats = userChats + providerChats
                                callback(true, null, allChats)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                callback(false, error.message, null)
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    // getting a chat by id
    fun getChatById(chatId: String, callback: (Boolean, String?, Chat?) -> Unit) {
        database.child("Chats").child(chatId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val chat = snapshot.getValue(Chat::class.java)

                    if (chat != null) {
                        callback(true, null, chat)
                    } else {
                        callback(false, "Chat not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    fun loadMessages(
        chatId: String,
        callback: (Boolean, String?, List<Message>?) -> Unit
    ) {
        Log.d("-loadMessage fun-", "$chatId")
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }
        database.child("Messages").child(chatId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()
                    for (messageSnapshot in snapshot.children) {
                        val message = messageSnapshot.getValue(Message::class.java)
                        message?.let { messages.add(it) }
                    }
                    callback(true, null, messages)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    // send a message in the chat
    fun sendMessage(
        chatId: String,
        message: Message,
        callback: (Boolean, String?, Message?) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }
        Log.d("chat-id", "$chatId")

        val messageId = database.child("Messages").child(chatId).push().key ?: ""
        val senderId = currentUser.uid



        val chatMessage = Message(
            messageId = messageId,
            chatId = chatId,
            senderId = senderId,
            receiverId = message.receiverId,
            senderName = message.senderName,
            message = message.message,
            timeSent = createdDate,
        )

        val messageRef = database.child("Messages").child(chatId)
        //only will execute if it the users havent spoken before
        messageRef.get().addOnSuccessListener { snapshot ->
            if(!snapshot.exists()){
                Log.d("new message", "new message")
                database.child("Messages").child(chatId).child(messageId).setValue(chatMessage)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            updateLastMessageSent(chatId, message.message)
                            callback(true, null, chatMessage)
                        }else{
                            callback(false, task.exception?.message, null)
                        }
                    }
            }

        }

        Log.d("chat-message", "${chatMessage.chatId}")
                database.child("Messages").child(chatId).child(messageId).setValue(chatMessage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update chat with last message
                    updateLastMessageSent(chatId, message.message)
                    callback(true, null, chatMessage)
                } else {
                    callback(false, task.exception?.message, null)
                }
            }


    }

    // update the last message sent in the chat
    private fun updateLastMessageSent(chatId: String, lastMessage: String) {
        val updates = mapOf(
            "lastMessage" to lastMessage,
            "lastMessageTime" to createdDate
        )
        database.child("Chats").child(chatId).updateChildren(updates)
    }
}
