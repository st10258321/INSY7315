package vcmsa.projects.wil_hustlehub.Model


    data class Chat(
        val id: String,            // Unique chat ID
        val contactName: String,   // Name of the contact
        val lastMessage: String,   // Last message preview
        val timestamp: Long        // Last message time
        )


