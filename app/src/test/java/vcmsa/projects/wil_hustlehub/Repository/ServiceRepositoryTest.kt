package vcmsa.projects.wil_hustlehub.Repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import vcmsa.projects.wil_hustlehub.Model.Service
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

//Testing fuctions of ServiceRepository Class
class ServiceRepositoryTest {

    private lateinit var repository: ServiceRepository
    private lateinit var authMock: MockedStatic<FirebaseAuth>
    private lateinit var dbMock: MockedStatic<FirebaseDatabase>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var dbRef: DatabaseReference

    @Before
    fun setUp() {
        // Mock FirebaseAuth.getInstance() and FirebaseDatabase.getInstance()
        authMock = mockStatic(FirebaseAuth::class.java)
        dbMock = mockStatic(FirebaseDatabase::class.java)

        firebaseAuth = mock()
        firebaseUser = mock()
        dbRef = mock()

        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn("user123")
        authMock.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(firebaseAuth)

        val firebaseDatabase: FirebaseDatabase = mock()
        whenever(firebaseDatabase.reference).thenReturn(dbRef)
        dbMock.`when`<FirebaseDatabase> { FirebaseDatabase.getInstance() }.thenReturn(firebaseDatabase)

        repository = ServiceRepository()
    }

    @After
    fun tearDown() {
        authMock.close()
        dbMock.close()
    }

    @Test
    fun `addService should succeed when user is logged in`() {
        val servicesRef: DatabaseReference = mock()
        val pushedRef: DatabaseReference = mock()
        val setValueTask: Task<Void> = mock()

        whenever(dbRef.child("Services")).thenReturn(servicesRef)
        whenever(servicesRef.push()).thenReturn(pushedRef)
        whenever(pushedRef.key).thenReturn("service123")
        whenever(servicesRef.child("service123")).thenReturn(pushedRef)
        whenever(pushedRef.setValue(any<Service>())).thenReturn(setValueTask)
        whenever(setValueTask.isSuccessful).thenReturn(true)

        whenever(setValueTask.addOnCompleteListener(any())).thenAnswer { invocation ->
            val listener = invocation.getArgument<com.google.android.gms.tasks.OnCompleteListener<Void>>(0)
            listener.onComplete(setValueTask)
            setValueTask
        }

        var callbackCalled = false
        repository.addService(
            serviceName = "Test Service",
            category = "Cleaning",
            description = "Deep cleaning",
            price = 100.0,
            image = "image.png",
            availability = "Available",
            location = "NYC"
        ) { success, _, service ->
            callbackCalled = true
            assertTrue(success)
            assertNotNull(service)
            assertEquals("service123", service?.serviceId)
            assertEquals("user123", service?.userId)
            assertEquals("Test Service", service?.serviceName)
        }

        assertTrue(callbackCalled)
    }

    @Test
    fun `searchServicesByName should return matching services`() {
        val listenerCaptor = argumentCaptor<ValueEventListener>()
        val servicesRef: DatabaseReference = mock()
        whenever(dbRef.child("Services")).thenReturn(servicesRef)

        repository.searchServicesByName("Test") { success, _, services ->
            assertTrue(success)
            assertEquals(1, services?.size)
            assertEquals("Test Service", services?.first()?.serviceName)
        }

        verify(servicesRef).addListenerForSingleValueEvent(listenerCaptor.capture())
        val listener = listenerCaptor.firstValue

        val snapshot: DataSnapshot = mock()
        val child: DataSnapshot = mock()
        whenever(snapshot.children).thenReturn(listOf(child))
        whenever(child.getValue(Service::class.java)).thenReturn(
            Service(serviceId = "service123", userId = "user123", serviceName = "Test Service")
        )

        listener.onDataChange(snapshot)
    }

    @Test
    fun `getServicesByUserId should return user services`() {
        val listenerCaptor = argumentCaptor<ValueEventListener>()
        val servicesRef: DatabaseReference = mock()
        whenever(dbRef.child("Services")).thenReturn(servicesRef)

        val query: Query = mock()
        whenever(servicesRef.orderByChild("userId")).thenReturn(query)
        whenever(query.equalTo("user123")).thenReturn(query)

        repository.getServicesByUserId("user123") { success, _, services ->
            assertTrue(success)
            assertEquals(1, services?.size)
            assertEquals("Test Service", services?.first()?.serviceName)
        }

        verify(query).addListenerForSingleValueEvent(listenerCaptor.capture())
        val listener = listenerCaptor.firstValue

        val snapshot: DataSnapshot = mock()
        val child: DataSnapshot = mock()
        whenever(snapshot.children).thenReturn(listOf(child))
        whenever(child.getValue(Service::class.java)).thenReturn(
            Service(serviceId = "service123", userId = "user123", serviceName = "Test Service")
        )

        listener.onDataChange(snapshot)
    }

    @Test
    fun `getServiceById should return service when found`() {
        val listenerCaptor = argumentCaptor<ValueEventListener>()
        val servicesRef: DatabaseReference = mock()
        whenever(dbRef.child("Services")).thenReturn(servicesRef)
        whenever(servicesRef.child("service123")).thenReturn(servicesRef)

        repository.getServiceById("service123") { success, _, service ->
            assertTrue(success)
            assertNotNull(service)
            assertEquals("Test Service", service?.serviceName)
        }

        verify(servicesRef).addListenerForSingleValueEvent(listenerCaptor.capture())
        val listener = listenerCaptor.firstValue

        val snapshot: DataSnapshot = mock()
        whenever(snapshot.getValue(Service::class.java)).thenReturn(
            Service(serviceId = "service123", userId = "user123", serviceName = "Test Service")
        )

        listener.onDataChange(snapshot)
    }
}
