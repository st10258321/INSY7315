// src/test/java/vcmsa/projects/wil_hustlehub/Repository/ChatRepositoryTest.kt
package vcmsa.projects.wil_hustlehub.Repository

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

// Dummy implementations for unit testing
class ChatRepository(private val chats: MutableMap<String, String> = mutableMapOf()) {
    fun sendMessage(chatId: String, message: String) {
        chats[chatId] = message
    }

    fun getChatMessage(chatId: String): String? {
        return chats[chatId]
    }

    fun getAllUserChats(): Map<String, String> {
        return chats
    }
}

class ChatRepositoryTest {

    private lateinit var repository: ChatRepository

    @Before
    fun setup() {
        repository = ChatRepository()
    }

    @Test
    fun `send message stores message in chat`() {
        repository.sendMessage("chat1", "Hello")
        val result = repository.getChatMessage("chat1")
        assertEquals("Hello", result)
    }

    @Test
    fun `get chat message returns correct message`() {
        repository.sendMessage("chat2", "Hi there")
        val result = repository.getChatMessage("chat2")
        assertEquals("Hi there", result)
    }

    @Test
    fun `get all user chats returns all messages`() {
        repository.sendMessage("chat1", "Hello")
        repository.sendMessage("chat2", "Hi")
        val allChats = repository.getAllUserChats()
        assertEquals(2, allChats.size)
        assertEquals("Hello", allChats["chat1"])
        assertEquals("Hi", allChats["chat2"])
    }
}
