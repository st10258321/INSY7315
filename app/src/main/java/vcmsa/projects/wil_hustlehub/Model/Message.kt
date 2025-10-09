package vcmsa.projects.wil_hustlehub.Model

data class Message(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId : String = "",
    val senderName: String = "",
    val message: String = "",
    val timeSent: String = ""
)
