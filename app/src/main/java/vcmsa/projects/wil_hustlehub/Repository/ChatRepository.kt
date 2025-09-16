package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.Model.Service

class ChatRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    fun createChat(serviceId: String, callback: (Boolean, String?, Chat?) -> Unit) {
       val currentUser = auth.currentUser

        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid

        // getting the service table to get the serviceName and userId which is going to be assigned to the
        // serviceProviderId
        database.child("Services").child(serviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(serviceSnapshot: DataSnapshot) {

                    val service = serviceSnapshot.getValue(Service::class.java)

                    if (service == null) {
                        callback(false, "Service not found", null)
                        return
                    }

                    val serviceProviderId = service.userId  // getting service provider id from Service class/model using userId
                    val serviceName = service.serviceName
                    val chatId = "${userId}_${serviceProviderId}_${serviceId}"

                    // check if chat already exists before creating a new one
                    database.child("Chats").child(chatId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val existingChat = snapshot.getValue(Chat::class.java)
                                    callback(true, "Chat already exists", existingChat)
                                } else {
                                    // creating the new chat
                                    val chat = Chat(
                                        chatId = chatId,
                                        userId = userId,
                                        serviceProviderId = serviceProviderId,
                                        serviceId = serviceId,
                                        serviceName = serviceName,
                                        lastMessage = "Get started and send a message",
                                        lastMessageTime = createdDate,
                                        createdDate = createdDate
                                    )

                                    database.child("Chat").child(chatId).setValue(chat)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                callback(true, null, chat)
                                            } else {
                                                callback(false, task.exception?.message, null)
                                            }
                                        }
                                }
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
}