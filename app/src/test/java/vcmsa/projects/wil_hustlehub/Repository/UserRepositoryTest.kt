package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import vcmsa.projects.wil_hustlehub.Model.User

//Testing fuctions of UserRepository Class
class UserRepositoryTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockDb: DatabaseReference
    private lateinit var mockUser: FirebaseUser
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        mockAuth = mock()
        mockDb = mock()
        mockUser = mock()
        repository = UserRepository(mockAuth, mockDb)
    }

    @Test
    fun `register new user with callback`() {
        val fakeTask: Task<AuthResult> = mock()
        val fakeSaveTask: Task<Void> = mock()
        val callback: (Boolean, String?, User?) -> Unit = mock()

        whenever(mockAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("123")
        whenever(fakeTask.isSuccessful).thenReturn(true)
        whenever(mockAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(fakeTask)
        whenever(mockDb.child(any())).thenReturn(mockDb)
        whenever(mockDb.setValue(any())).thenReturn(fakeSaveTask)
        whenever(fakeSaveTask.isSuccessful).thenReturn(true)

        // Simulate completion listeners for Auth and Database tasks
        doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(fakeTask)
            null
        }.whenever(fakeTask).addOnCompleteListener(any())

        doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<Void>
            listener.onComplete(fakeSaveTask)
            null
        }.whenever(fakeSaveTask).addOnCompleteListener(any())

        repository.register("John", "john@email.com", "12345", "password", callback)

        verify(callback).invoke(eq(true), isNull(), check { user ->
            assert(user?.name == "John")
            assert(user?.userID == "123")
        })
    }

    @Test
    fun `user login success callback`() {
        val fakeTask: Task<AuthResult> = mock()
        val fakeGetTask: Task<com.google.firebase.database.DataSnapshot> = mock()
        val callback: (Boolean, String?, User?) -> Unit = mock()

        whenever(fakeTask.isSuccessful).thenReturn(true)
        whenever(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(fakeTask)
        whenever(mockAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("123")

        doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(fakeTask)
            null
        }.whenever(fakeTask).addOnCompleteListener(any())

        whenever(mockDb.child(any())).thenReturn(mockDb)
        whenever(mockDb.get()).thenReturn(fakeGetTask)
        whenever(fakeGetTask.isSuccessful).thenReturn(true)

        val snapshot: com.google.firebase.database.DataSnapshot = mock()
        whenever(snapshot.getValue(User::class.java)).thenReturn(
            User("123", "John", "john@email.com", "12345", "pass", "2025-08-25")
        )

        // Simulate database get success
        doAnswer {
            val listener = it.arguments[0] as com.google.android.gms.tasks.OnSuccessListener<com.google.firebase.database.DataSnapshot>
            listener.onSuccess(snapshot)
            fakeGetTask
        }.whenever(fakeGetTask).addOnSuccessListener(any())

        doAnswer { fakeGetTask }.whenever(fakeGetTask).addOnFailureListener(any())

        repository.login("john@email.com", "password", callback)

        verify(callback).invoke(eq(true), isNull(), check { user ->
            assert(user?.name == "John")
            assert(user?.userID == "123")
        })
    }

    @Test
    fun `user login fail callback`() {
        val fakeTask: Task<AuthResult> = mock()
        val callback: (Boolean, String?, User?) -> Unit = mock()

        whenever(fakeTask.isSuccessful).thenReturn(false)
        whenever(fakeTask.exception).thenReturn(Exception("Login failed"))
        whenever(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(fakeTask)

        doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(fakeTask)
            null
        }.whenever(fakeTask).addOnCompleteListener(any())

        repository.login("wrong@email.com", "badpassword", callback)

        verify(callback).invoke(eq(false), eq("Login failed"), isNull())
    }

    @Test
    fun `user logout`() {
        repository.logout()
        verify(mockAuth).signOut()
    }
}
