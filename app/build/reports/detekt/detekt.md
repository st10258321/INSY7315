# detekt

## Metrics

* 528 number of properties

* 259 number of functions

* 77 number of classes

* 8 number of packages

* 65 number of kt files

## Complexity Report

* 5,307 lines of code (loc)

* 4,318 source lines of code (sloc)

* 2,851 logical lines of code (lloc)

* 293 comment lines of code (cloc)

* 489 cyclomatic complexity (mcc)

* 475 cognitive complexity

* 200 number of total code smells

* 6% comment source ratio

* 171 mcc per 1,000 lloc

* 70 code smells per 1,000 lloc

## Findings (200)

### complexity, ComplexCondition (1)

Complex conditions should be simplified and extracted into well-named methods if necessary.

[Documentation](https://detekt.dev/docs/rules/complexity#complexcondition)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:86:16
```
This condition is too complex (4). Defined complexity threshold for conditions is set to '4'
```
```kotlin
83             val price = binding.etServicePrice.text.toString()
84             val availability = binding.etAvailability.text.toString()
85             val location = binding.actvLocation.selectedItem.toString()
86             if(serviceName.isEmpty()|| category.isEmpty() || description.isEmpty() || price.isEmpty()){
!!                ^ error
87                 Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
88             }else{
89                 val price = price.toDouble()

```

### complexity, LongMethod (4)

One method should have one responsibility. Long methods tend to handle many things at once. Prefer smaller methods to make them easier to understand.

[Documentation](https://detekt.dev/docs/rules/complexity#longmethod)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BookServiceFragment.kt:63:18
```
The function onViewCreated is too long (80). The maximum length is 60.
```
```kotlin
60         return binding.root
61     }
62 
63     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
!!                  ^ error
64         super.onViewCreated(view, savedInstanceState)
65 
66         serviceID = arguments?.getString("serviceID")

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:57:18
```
The function onViewCreated is too long (77). The maximum length is 60.
```
```kotlin
54     private var servicesLoaded = emptyList<Service>()
55     private var filteredServices = emptyList<Service>()
56 
57     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
!!                  ^ error
58         super.onViewCreated(view, savedInstanceState)
59         userViewModel.getAllUsers()
60         userViewModel.getAllServices()

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:60:18
```
The function onViewCreated is too long (82). The maximum length is 60.
```
```kotlin
57     }
58 
59     @SuppressLint("SuspiciousIndentation")
60     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
!!                  ^ error
61         super.onViewCreated(view, savedInstanceState)
62         val chatId = arguments?.getString("chatID")
63         val serviceProviderId = arguments?.getString("serviceProviderId")

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:41:18
```
The function onViewCreated is too long (83). The maximum length is 60.
```
```kotlin
38         return binding.root
39     }
40 
41     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
!!                  ^ error
42         super.onViewCreated(view, savedInstanceState)
43         val userRepo = UserRepository()
44         val serviceRepo = ServiceRepository()

```

### complexity, LongParameterList (7)

The more parameters a function has the more complex it is. Long parameter lists are often used to control complex algorithms and violate the Single Responsibility Principle. Prefer functions with short parameter lists.

[Documentation](https://detekt.dev/docs/rules/complexity#longparameterlist)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:34:26
```
The function createBookService(serviceId: String, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit) has too many parameters. The current threshold is set to 6.
```
```kotlin
31     /*this function takes the information like servicename,
32      date and time and saves it to the database
33      and the current userId of the user that is logged in using indexing and denormalization*/
34     fun createBookService(serviceId: String, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
!!                          ^ error
35     ) {
36         val userId = auth.currentUser?.uid
37         if (userId == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:74:33
```
The function createBookingWithService(userId: String, serviceId: String, service: Service, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit) has too many parameters. The current threshold is set to 6.
```
```kotlin
71     }
72 
73     /*this method does the actually saving to the database after getting the cached service*/
74     fun createBookingWithService(userId: String, serviceId: String ,service: Service, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
!!                                 ^ error
75     ) {
76 
77         // Create the booking service ID

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ReviewRepository.kt:64:27
```
The function saveReview(reviewId: String, userId: String, serviceId: String, reviewerName: String, stars: Int, reviewText: String, reviewDate: String, callback: (Boolean, String?, Review?) -> Unit) has too many parameters. The current threshold is set to 6.
```
```kotlin
61         }
62     }
63 
64     private fun saveReview(
!!                           ^ error
65         reviewId: String,
66         userId: String,
67         serviceId: String,

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:26:19
```
The function addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit) has too many parameters. The current threshold is set to 6.
```
```kotlin
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit
!!                   ^ error
27     ) {
28         val currentUser = auth.currentUser
29         if (currentUser == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:309:30
```
The function reportServiceProvider(serviceProviderId: String, serviceProviderName: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String, callback: (Boolean, String?) -> Unit) has too many parameters. The current threshold is set to 6.
```
```kotlin
306                 }
307             }
308     }
309     fun reportServiceProvider(serviceProviderId: String,serviceProviderName: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String, callback: (Boolean, String?) -> Unit)
!!!                              ^ error
310     {
311         val currentUser = auth.currentUser
312 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:159:19
```
The function addService(serviceName: String, category: String, description: String, price: Double, image: String, availability: List<String>, availableTime: List<String>, location: String) has too many parameters. The current threshold is set to 6.
```
```kotlin
156     }
157 
158     // Function to add a new service
159     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availability: List<String>, availableTime : List<String>, location: String) {
!!!                   ^ error
160         serviceRepo.addService(serviceName, category, description, price, image, availability,availableTime, location) { success, message, service ->
161             // Update UI state based on the result
162             serviceStatus.postValue(Pair(success, message))

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:415:30
```
The function reportServiceProvider(serviceProviderId: String, serviceProviderName: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String) has too many parameters. The current threshold is set to 6.
```
```kotlin
412 
413 
414 
415     fun reportServiceProvider(serviceProviderId:String,serviceProviderName : String ,serviceId :String, reportedIssue : String,additionalNotes :String, images : String){
!!!                              ^ error
416         serviceRepo.reportServiceProvider(serviceProviderId,serviceProviderName, serviceId, reportedIssue, additionalNotes, images) { success, message ->
417                 if(success){
418                     reviewStatus.postValue(Pair(true,message))

```

### complexity, TooManyFunctions (3)

Too many functions inside a/an file/class/object/interface always indicate a violation of the single responsibility principle. Maybe the file/class/object/interface wants to manage too many things at once. Extract functionality which clearly belongs together.

[Documentation](https://detekt.dev/docs/rules/complexity#toomanyfunctions)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:13:7
```
Class 'BookServiceRepository' with '11' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
10 import vcmsa.projects.wil_hustlehub.Model.Service
11 import java.util.Date
12 
13 class BookServiceRepository {
!!       ^ error
14 
15     private val auth = FirebaseAuth.getInstance()
16     private val database = FirebaseDatabase.getInstance().reference

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:12:7
```
Class 'ServiceRepository' with '14' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
9  import vcmsa.projects.wil_hustlehub.Model.Report
10 import java.util.Date
11 
12 class ServiceRepository {
!!       ^ error
13     private val auth = FirebaseAuth.getInstance()
14     private val database = FirebaseDatabase.getInstance().reference
15 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:27:7
```
Class 'UserViewModel' with '32' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
24 import javax.inject.Inject
25 import kotlin.math.log
26 
27 class UserViewModel @Inject constructor(
!!       ^ error
28     private val userRepo: UserRepository,
29     private val serviceRepo: ServiceRepository,
30     private val bookRepo: BookServiceRepository,

```

### empty-blocks, EmptyClassBlock (1)

Empty block of code detected. As they serve no purpose they should be removed.

[Documentation](https://detekt.dev/docs/rules/empty-blocks#emptyclassblock)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/navbar.kt:3:14
```
The class or object navbar is empty.
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
2 
3 class navbar {
!              ^ error
4 }

```

### exceptions, SwallowedException (3)

The caught exception is swallowed. The original exception could be lost.

[Documentation](https://detekt.dev/docs/rules/exceptions#swallowedexception)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ReviewRepository.kt:112:34
```
The caught exception is swallowed. The original exception could be lost.
```
```kotlin
109                     val sortedReviews = reviews.sortedByDescending {
110                         try {
111                             dateFormat.parse(it.reviewDate)?.time ?: 0L
112                         } catch (e: Exception) {
!!!                                  ^ error
113                             0L
114                         }
115                     }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:155:17
```
The caught exception is swallowed. The original exception could be lost.
```
```kotlin
152     private fun openCameraOrGallery() {
153         val photoFile : File? = try{
154             createImageFile()
155         }catch (e: IOException) {
!!!                 ^ error
156             Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
157             null
158 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:161:17
```
The caught exception is swallowed. The original exception could be lost.
```
```kotlin
158     private fun openCameraOrGallery() {
159         val photoFile : File? = try{
160             createImageFile()
161         }catch (e: IOException) {
!!!                 ^ error
162             Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
163             null
164 

```

### exceptions, TooGenericExceptionCaught (6)

The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptioncaught)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:121:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
118                 val sha1 = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
119                 Log.d("GoogleSignInSHA", "SHA-1: $sha1")
120             }
121         } catch (e: Exception) {
!!!                  ^ error
122             Log.e("GoogleSignInSHA", "Error retrieving SHA-1", e)
123         }
124     }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ReviewRepository.kt:112:34
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
109                     val sortedReviews = reviews.sortedByDescending {
110                         try {
111                             dateFormat.parse(it.reviewDate)?.time ?: 0L
112                         } catch (e: Exception) {
!!!                                  ^ error
113                             0L
114                         }
115                     }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BookServiceFragment.kt:161:30
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
158                         )
159                         val intent = Intent(requireContext(), MainActivity::class.java)
160                         startActivity(intent)
161                     } catch (e: Exception) {
!!!                              ^ error
162                         Log.e("BookService", "Failed to send notification", e)
163                     }
164                 } else {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:121:25
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
118                             )
119                         }
120                     }
121                 }catch (e : Exception){
!!!                         ^ error
122                     Log.d("push notification failes", "$e")
123                 }
124 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:103:22
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
100                     Log.d("--account email", account.email.toString())
101                     userViewModel.googleLogin(idToken)
102                 }
103             } catch (e: Exception) {
!!!                      ^ error
104                 Toast.makeText(requireContext(), "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
105             }
106         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:129:16
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
126                     }
127                 }
128             }
129         }catch(e : Exception){
!!!                ^ error
130             Log.d("ProfiileFragment- Service Provider error", e.message.toString())
131         }
132 

```

### naming, ClassNaming (2)

A class or object name should fit the naming pattern defined in the projects configuration.

[Documentation](https://detekt.dev/docs/rules/naming#classnaming)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/navbar.kt:3:7
```
Class and Object names should match the pattern: [A-Z][a-zA-Z0-9]*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
2 
3 class navbar {
!       ^ error
4 }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/admin_portal.kt:12:7
```
Class and Object names should match the pattern: [A-Z][a-zA-Z0-9]*
```
```kotlin
9  import vcmsa.projects.wil_hustlehub.R
10 import com.google.android.material.card.MaterialCardView
11 
12 class admin_portal : AppCompatActivity() {
!!       ^ error
13     override fun onCreate(savedInstanceState: Bundle?) {
14         super.onCreate(savedInstanceState)
15         enableEdgeToEdge()

```

### naming, InvalidPackageDeclaration (1)

Kotlin source files should be stored in the directory corresponding to its package statement.

[Documentation](https://detekt.dev/docs/rules/naming#invalidpackagedeclaration)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ReportedUserAdapter.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapter
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

### naming, MatchingDeclarationName (1)

If a source file contains only a single non-private top-level class or object, the file name should reflect the case-sensitive name plus the .kt extension.

[Documentation](https://detekt.dev/docs/rules/naming#matchingdeclarationname)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ReportedUserAdapter.kt:14:7
```
The file name 'ReportedUserAdapter' does not match the name of the single top-level declaration 'ReportedUsersAdapter'.
```
```kotlin
11 import vcmsa.projects.wil_hustlehub.Model.Report
12 import vcmsa.projects.wil_hustlehub.R
13 
14 class ReportedUsersAdapter(
!!       ^ error
15     private var reports: MutableList<Report>,
16     private val onSuspendClick: (Report, Int) -> Unit,
17     private val onDeleteClick: (Report, Int) -> Unit

```

### naming, PackageNaming (65)

Package names should match the naming convention set in the configuration.

[Documentation](https://detekt.dev/docs/rules/naming#packagenaming)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/BrowseServiceAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ChatAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.ViewGroup

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ChatListAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/MessageAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3     import android.view.LayoutInflater
4     import android.view.ViewGroup

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ProfileServiceAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ProviderBookingsAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.ViewGroup

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ReportedUserAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapter
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ServiceAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/UserBookingsAdapter.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Adapters
! ^ error
2 
3 import android.view.LayoutInflater
4 import android.view.View

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub
! ^ error
2 
3 import android.Manifest
4 import android.content.pm.PackageManager

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/BookService.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class BookService(
4     val bookingId: String = "",

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/BookingActionResult.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class BookingActionResult (
4     val success : Boolean,

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/Chat.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 
4     data class Chat(

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/Message.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class Message(
4     val messageId: String = "",

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/Report.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class Report(
4     val reportId: String = "",

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/Review.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class Review(
4     val reviewId: String = "",

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/Service.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 import android.net.Uri
4 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/User.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Model
! ^ error
2 
3 data class User (
4     var userID: String = "",

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Network/PushApiClient.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Network
! ^ error
2 
3 import android.content.Context
4 import android.util.Log

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Repository
! ^ error
2 
3 import android.util.Log
4 import com.google.firebase.auth.FirebaseAuth

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ChatRepository.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Repository
! ^ error
2 
3 import android.util.Log
4 import com.google.firebase.auth.FirebaseAuth

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ReviewRepository.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Repository
! ^ error
2 
3 import com.google.firebase.auth.FirebaseAuth
4 import com.google.firebase.database.DataSnapshot

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Repository
! ^ error
2 
3 import com.google.firebase.auth.FirebaseAuth
4 import com.google.firebase.database.DataSnapshot

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/UserRepository.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.Repository
! ^ error
2 
3 import com.google.firebase.auth.FirebaseAuth
4 import com.google.firebase.auth.FirebaseAuthException

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/FirebaseMessagingService.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
! ^ error
2 
3 import android.Manifest
4 import android.R

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
! ^ error
2 
3 import android.net.Uri
4 import android.util.Log

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/ViewModelFactory.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
! ^ error
2 
3 import androidx.lifecycle.ViewModel
4 import androidx.lifecycle.ViewModelProvider

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/navbar.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
! ^ error
2 
3 class navbar {
4 }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AboutUsActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AboutUsFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.view.LayoutInflater

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AccountActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AccountFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AddReviewFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.util.Log

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BookServiceActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BookServiceFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.R
4 import android.content.Context

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.R
4 import android.content.Intent

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/CategoriesActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/CategoriesFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.view.LayoutInflater

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.annotation.SuppressLint
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatListFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatRoomActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.widget.Toast

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ContactActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ContactFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.view.LayoutInflater

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/HomeActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/HomeFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Context
4 import android.content.Intent

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/OfferSkillsActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/OfferSkillsFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Context
4 import android.content.Intent

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProviderBookings.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProviderBookingsFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 
4 import android.content.Intent

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.view.LayoutInflater

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.pm.PackageManager
4 import android.os.Build

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.Manifest
4 import android.content.Intent

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReviewsActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.activity.enableEdgeToEdge

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReviewsFragment.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import androidx.fragment.app.Fragment

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ServiceManagementActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.os.Bundle
4 import android.widget.Toast

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/UserBookingsActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Context
4 import android.os.Bundle

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/UserManagementActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 import android.os.Bundle
3 import android.widget.Toast
4 import androidx.activity.viewModels

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/admin_portal.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.View
! ^ error
2 
3 import android.content.Intent
4 import android.os.Bundle

```

### naming, VariableNaming (3)

Variable names should follow the naming convention set in the projects configuration.

[Documentation](https://detekt.dev/docs/rules/naming#variablenaming)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:23:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
20     // //created a cache to temporarily store service details instead of retrieving them from firebase every time.
21     private var servicesCache: List<Service>? = null
22     private var cacheTimestamp: Long = 0 //tracks when the cache was last updated.
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
!!                 ^ error
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:29:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
26     private val binding get() = _binding!!
27 
28     private lateinit var googleSignInClient: GoogleSignInClient
29     private val RC_SIGN_IN = 1001
!!                 ^ error
30 
31     private val userViewModel: UserViewModel by viewModels {
32         ViewModelFactory(

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:47:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
44     }
45     private var imageString : String = ""
46     private var photoUri : Uri? = null
47     private val CAMERA_REQUEST_CODE = 100
!!                 ^ error
48     private val requestPermissionsLauncher =
49         registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
50             val denied = permissions.filter { !it.value }.keys

```

### style, ForbiddenComment (2)

Flags a forbidden comment.

[Documentation](https://detekt.dev/docs/rules/style#forbiddencomment)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:49:45
```
This comment contains 'TODO:' that has been defined as forbidden in detekt.
```
```kotlin
46     private val userViewModel: UserViewModel by viewModels { viewModelFactory }
47     private var currentUserName : String = "John Doe"
48     private lateinit var chatAdapter: ChatAdapter
49     private var currentUserId : String =""  // TODO: Replace with logged-in userId
!!                                             ^ error
50 
51     override fun onCreateView(
52         inflater: LayoutInflater, container: ViewGroup?,

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:98:21
```
This comment contains 'TODO:' that has been defined as forbidden in detekt.
```
```kotlin
95                  val account = task.getResult(Exception::class.java)
96                  val idToken = account?.idToken
97                  if (idToken != null) {
98                      // TODO: handle Firebase Auth with idToken
!!                      ^ error
99                      Log.d("GoogleSignIn", "ID Token: $idToken")
100                     Log.d("--account email", account.email.toString())
101                     userViewModel.googleLogin(idToken)

```

### style, MagicNumber (11)

Report magic numbers. Magic number is a numeric literal that is not defined as a constant and hence it's unclear what the purpose of this number is. It's better to declare such numbers as constants and give them a proper name. By default, -1, 0, 1, and 2 are not considered to be magic numbers.

[Documentation](https://detekt.dev/docs/rules/style#magicnumber)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:29:31
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
26     private val serviceCache = mutableMapOf<String, Service>()
27 
28     //the service details expires after 5 minutes.
29     private val cacheExpiry = 5 * 60 * 1000L
!!                               ^ error
30 
31     /*this function takes the information like servicename,
32      date and time and saves it to the database

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:29:35
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
26     private val serviceCache = mutableMapOf<String, Service>()
27 
28     //the service details expires after 5 minutes.
29     private val cacheExpiry = 5 * 60 * 1000L
!!                                   ^ error
30 
31     /*this function takes the information like servicename,
32      date and time and saves it to the database

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:29:40
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
26     private val serviceCache = mutableMapOf<String, Service>()
27 
28     //the service details expires after 5 minutes.
29     private val cacheExpiry = 5 * 60 * 1000L
!!                                        ^ error
30 
31     /*this function takes the information like servicename,
32      date and time and saves it to the database

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:23:37
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
20     // //created a cache to temporarily store service details instead of retrieving them from firebase every time.
21     private var servicesCache: List<Service>? = null
22     private var cacheTimestamp: Long = 0 //tracks when the cache was last updated.
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
!!                                     ^ error
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:23:41
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
20     // //created a cache to temporarily store service details instead of retrieving them from firebase every time.
21     private var servicesCache: List<Service>? = null
22     private var cacheTimestamp: Long = 0 //tracks when the cache was last updated.
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
!!                                         ^ error
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:23:46
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
20     // //created a cache to temporarily store service details instead of retrieving them from firebase every time.
21     private var servicesCache: List<Service>? = null
22     private var cacheTimestamp: Long = 0 //tracks when the cache was last updated.
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
!!                                              ^ error
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:104:34
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
101             if(binding.filterContainer.isVisible){
102                 binding.filterContainer.animate()
103                     .alpha(0f)
104                     .setDuration(200)
!!!                                  ^ error
105                     .withEndAction { binding.filterContainer.isVisible = false }
106                     .start()
107                 binding.toggleFilterBtn.text = "Show Filters"

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:116:34
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
113             if(binding.filterContainer.isVisible){
114                 binding.filterContainer.animate()
115                     .alpha(0f)
116                     .setDuration(200)
!!!                                  ^ error
117                     .withEndAction { binding.filterContainer.isVisible = false }
118                     .start()
119                     binding.toggleFilterBtn.text = "Show Filters"

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:125:34
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
122                 binding.filterContainer.isVisible = true
123                 binding.filterContainer.animate()
124                     .alpha(1f)
125                     .setDuration(200)
!!!                                  ^ error
126                     .start()
127                 binding.toggleFilterBtn.text ="Hide Filters"
128             }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:29:30
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
26     private val binding get() = _binding!!
27 
28     private lateinit var googleSignInClient: GoogleSignInClient
29     private val RC_SIGN_IN = 1001
!!                              ^ error
30 
31     private val userViewModel: UserViewModel by viewModels {
32         ViewModelFactory(

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:47:39
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
44     }
45     private var imageString : String = ""
46     private var photoUri : Uri? = null
47     private val CAMERA_REQUEST_CODE = 100
!!                                       ^ error
48     private val requestPermissionsLauncher =
49         registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
50             val denied = permissions.filter { !it.value }.keys

```

### style, MaxLineLength (38)

Line detected, which is longer than the defined maximum line length in the code style.

[Documentation](https://detekt.dev/docs/rules/style#maxlinelength)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:70:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
67 
68         // Notifications (Android 13+)
69         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
70             ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
!!             ^ error
71             permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
72         }
73 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:82:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
79         // Storage / Media
80         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
81             // Android 13+ → granular media access
82             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
!!             ^ error
83                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
84             }
85             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:85:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
82             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
83                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
84             }
85             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
!!             ^ error
86                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
87             }
88         } else {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:90:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
87             }
88         } else {
89             // Pre-Android 13 → legacy storage
90             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
!!             ^ error
91                 permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
92             }
93             if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:94:17
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
91                 permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
92             }
93             if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
94                 ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
!!                 ^ error
95                 permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
96             }
97         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/MainActivity.kt:186:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
183 //            bottomNav.visibility = View.VISIBLE
184 //        }
185         binding.bottomNavigation.visibility =
186             if (fragment is LoginFragment || fragment is RegisterFragment || fragment is AccountFragment) View.GONE else View.VISIBLE
!!!             ^ error
187     }
188 }
189 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:34:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
31     /*this function takes the information like servicename,
32      date and time and saves it to the database
33      and the current userId of the user that is logged in using indexing and denormalization*/
34     fun createBookService(serviceId: String, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
!!     ^ error
35     ) {
36         val userId = auth.currentUser?.uid
37         if (userId == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:74:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
71     }
72 
73     /*this method does the actually saving to the database after getting the cached service*/
74     fun createBookingWithService(userId: String, serviceId: String ,service: Service, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
!!     ^ error
75     ) {
76 
77         // Create the booking service ID

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:113:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
110         val updates = hashMapOf<String, Any>(
111             "/Book_Service/$bookServiceId" to bookService,
112             // Optional: Create index for service owner's bookings
113             // "ServiceOwnerBookings" creates an index inside the database to store the bookings that belong to a specific service provider
!!!             ^ error
114             "/ServiceProviderBookings/${service.userId}/$bookServiceId" to true
115         )
116 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:130:168
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
127    }
128 
129     /* with this function we are getting all the bookings that are inside the database
130      that belong to the user that is logged in and returns then as a list, using indexing and limiting the amount of booked services that is shown from the database */
!!!                                                                                                                                                                        ^ error
131     fun getUserBookServices(limit: Int = 50, callback: (Boolean, String?, List<BookService>?) -> Unit) {
132         val userId = auth.currentUser?.uid
133         if (userId == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:160:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
157     /* this function is for the admin portal where it gets all the bookings that are saved inside
158      the databaseThis function retrieves every single booking in the entire database from all users,
159      */
160     fun getAllBookServices(limit: Int = 100, startAfter: String? = null, callback: (Boolean, String?, List<BookService>?) -> Unit) {
!!!     ^ error
161         var query = database.child("Book_Service")
162             .orderByChild("createdDate")
163             .limitToFirst(limit)

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:204:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
201     }
202 
203 
204     //  getting all bookings for users that are service providers, optimized the code by using the denormalized serviceProviderId
!!!     ^ error
205     fun getBookingsForMyServices(callback: (Boolean, String?, List<BookService>?) -> Unit) {
206         val userId = auth.currentUser?.uid
207         if (userId == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:264:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
261     }
262 
263 
264     /* to confirm a booking this can only be done by the person who registered/owns the service, by using the serviceProviderId for validation */
!!!     ^ error
265     fun confirmBooking(bookServiceId: String, callback: (Boolean, String?, BookService?) -> Unit) {
266         val userId = auth.currentUser?.uid
267         if (userId == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:26:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
23     private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.
24 
25     // Add a new service
26     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit
!!     ^ error
27     ) {
28         val currentUser = auth.currentUser
29         if (currentUser == null) {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:181:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
178 
179     // retrieving a limited number of services at a time instead of loading all services all at once
180     //last key is used to fetch the next page
181     fun getServicesPaginated(lastKey: String? = null, pageSize: Int = 20, callback: (Boolean, String?, List<Service>?, String?) -> Unit
!!!     ^ error
182     ) {
183 
184         var query = database.child("Services")

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:309:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
306                 }
307             }
308     }
309     fun reportServiceProvider(serviceProviderId: String,serviceProviderName: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String, callback: (Boolean, String?) -> Unit)
!!!     ^ error
310     {
311         val currentUser = auth.currentUser
312 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:380:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
377         cacheTimestamp = 0
378     }
379 
380     // stops the services from being temporarily stored, it should be called in viewmodel when repository is no longer needed.
!!!     ^ error
381     fun cleanup() {
382         database.child("Services").keepSynced(false)
383         invalidateCache()

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:159:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
156     }
157 
158     // Function to add a new service
159     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availability: List<String>, availableTime : List<String>, location: String) {
!!!     ^ error
160         serviceRepo.addService(serviceName, category, description, price, image, availability,availableTime, location) { success, message, service ->
161             // Update UI state based on the result
162             serviceStatus.postValue(Pair(success, message))

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:160:9
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
157 
158     // Function to add a new service
159     fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availability: List<String>, availableTime : List<String>, location: String) {
160         serviceRepo.addService(serviceName, category, description, price, image, availability,availableTime, location) { success, message, service ->
!!!         ^ error
161             // Update UI state based on the result
162             serviceStatus.postValue(Pair(success, message))
163         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:300:17
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
297     fun updateBooking(bookService : BookService){
298         bookRepo.updateBooking(bookService){success, message, bookService ->
299             Log.d("--checking","${bookService?.serviceName}")
300                 bookingActionStatus.postValue(BookingActionResult(success,message,bookService?.bookingId ?: "",bookService?.status!!))
!!!                 ^ error
301         }
302     }
303 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:304:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
301         }
302     }
303 
304 //    fun reportServiceProvider(serviceProviderId: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String) {
!!! ^ error
305 //        serviceRepo.reportServiceProvider(serviceProviderId, serviceId, reportedIssue, additionalNotes, images) { success, message ->
306 //            serviceStatus.postValue(Pair(success, message))
307 //        }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:305:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
302     }
303 
304 //    fun reportServiceProvider(serviceProviderId: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String) {
305 //        serviceRepo.reportServiceProvider(serviceProviderId, serviceId, reportedIssue, additionalNotes, images) { success, message ->
!!! ^ error
306 //            serviceStatus.postValue(Pair(success, message))
307 //        }
308 //

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:415:5
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
412 
413 
414 
415     fun reportServiceProvider(serviceProviderId:String,serviceProviderName : String ,serviceId :String, reportedIssue : String,additionalNotes :String, images : String){
!!!     ^ error
416         serviceRepo.reportServiceProvider(serviceProviderId,serviceProviderName, serviceId, reportedIssue, additionalNotes, images) { success, message ->
417                 if(success){
418                     reviewStatus.postValue(Pair(true,message))

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:416:9
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
413 
414 
415     fun reportServiceProvider(serviceProviderId:String,serviceProviderName : String ,serviceId :String, reportedIssue : String,additionalNotes :String, images : String){
416         serviceRepo.reportServiceProvider(serviceProviderId,serviceProviderName, serviceId, reportedIssue, additionalNotes, images) { success, message ->
!!!         ^ error
417                 if(success){
418                     reviewStatus.postValue(Pair(true,message))
419                 }else{

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:147:17
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
144         binding.backToProfileBtn.setOnClickListener {
145             val fragment = ProfileFragment()
146             requireActivity().supportFragmentManager.beginTransaction()
147                 .replace((requireActivity() as AppCompatActivity).findViewById<ViewGroup>(R.id.nav_host_fragment).id, fragment)
!!!                 ^ error
148                 .addToBackStack(null) // so user can press back to return here
149                 .commit()
150         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:102:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
99          binding.profileServicesRecycler.adapter = adapter
100 
101         try{
102             //first check if this page was navigated from the browser service page,if not display the logged in user data
!!!             ^ error
103 
104             if(!serviceProId.isNullOrEmpty()) {
105                 userViewModel.getMyServices(serviceProId!!)

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:119:25
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
116                 if(userid != null) {
117                     userViewModel.getMyServices(userid)
118                     userViewModel.getUserData(userid).observe(viewLifecycleOwner) { user ->
119                         //fill the page with the user's section, check if they have any services they provide and fill that section too.
!!!                         ^ error
120                         binding.providerName.text = user?.name
121 
122                         userViewModel.userServices.observe(viewLifecycleOwner) { services ->

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProviderBookingsFragment.kt:86:17
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
83             if (bookings.isNullOrEmpty()) {
84                 Toast.makeText(requireContext(), getString(R.string.toast_no_bookings), Toast.LENGTH_SHORT).show()
85             } else {
86                 Toast.makeText(requireContext(), getString(R.string.toast_bookings_fetched, bookings.size), Toast.LENGTH_SHORT).show()
!!                 ^ error
87             }
88         }
89 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:68:9
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
65         binding = FragmentRegisterServiceBinding.inflate(layoutInflater)
66         setContentView(binding.root)
67 
68         binding.actvCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_categories, android.R.layout.simple_spinner_item))
!!         ^ error
69         binding.actvLocation.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_locations, android.R.layout.simple_spinner_item))
70         binding.actvPricingType.setAdapter(ArrayAdapter.createFromResource(this, R.array.pricing_types, android.R.layout.simple_spinner_item))
71 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:69:9
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
66         setContentView(binding.root)
67 
68         binding.actvCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_categories, android.R.layout.simple_spinner_item))
69         binding.actvLocation.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_locations, android.R.layout.simple_spinner_item))
!!         ^ error
70         binding.actvPricingType.setAdapter(ArrayAdapter.createFromResource(this, R.array.pricing_types, android.R.layout.simple_spinner_item))
71 
72 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:70:9
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
67 
68         binding.actvCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_categories, android.R.layout.simple_spinner_item))
69         binding.actvLocation.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_locations, android.R.layout.simple_spinner_item))
70         binding.actvPricingType.setAdapter(ArrayAdapter.createFromResource(this, R.array.pricing_types, android.R.layout.simple_spinner_item))
!!         ^ error
71 
72 
73 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:118:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
115         }
116 
117         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
118             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
!!!             ^ error
119                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
120             }
121         } else {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:122:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
119                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
120             }
121         } else {
122             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
!!!             ^ error
123                 permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
124             }
125         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:129:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
126 
127         // Notifications
128         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
129             ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
!!!             ^ error
130         ) {
131             permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
132         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:100:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
97              val selectedid = selected.serviceId
98              val reportedIssue = binding.complaintSpinner.selectedItem.toString()
99              val additionalNotes = binding.edAdditionalNotes.text.toString()
100             userViewModel.reportServiceProvider(serviceProId,spUsername,selectedid,reportedIssue,additionalNotes,imageString)
!!!             ^ error
101         }
102         userViewModel.reportResult.observe(this){(success, message) ->
103             if(success){

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:124:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
121         }
122 
123         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
124             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
!!!             ^ error
125                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
126             }
127         } else {

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:128:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
125                 permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
126             }
127         } else {
128             if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
!!!             ^ error
129                 permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
130             }
131         }

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:135:13
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
132 
133         // Notifications
134         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
135             ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
!!!             ^ error
136         ) {
137             permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
138         }

```

### style, NewLineAtEndOfFile (43)

Checks whether files end with a line separator.

[Documentation](https://detekt.dev/docs/rules/style#newlineatendoffile)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/BrowseServiceAdapter.kt:48:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Adapters\BrowseServiceAdapter.kt is not ending with a new line.
```
```kotlin
45     }
46 
47     override fun getItemCount(): Int = services.size
48 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ChatAdapter.kt:67:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Adapters\ChatAdapter.kt is not ending with a new line.
```
```kotlin
64             binding.messageTimestamp.text = msg.timeSent
65         }
66     }
67 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/ProfileServiceAdapter.kt:69:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Adapters\ProfileServiceAdapter.kt is not ending with a new line.
```
```kotlin
66         services = newServices
67         notifyDataSetChanged()
68     }
69 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Adapters/UserBookingsAdapter.kt:79:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Adapters\UserBookingsAdapter.kt is not ending with a new line.
```
```kotlin
76     }
77 
78 
79 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Model/BookingActionResult.kt:8:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Model\BookingActionResult.kt is not ending with a new line.
```
```kotlin
5      val message : String?,
6      val bookingId : String,
7      val newStatus: String
8  )
!   ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Network/PushApiClient.kt:76:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Network\PushApiClient.kt is not ending with a new line.
```
```kotlin
73             }
74     }
75 
76 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:349:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Repository\BookServiceRepository.kt is not ending with a new line.
```
```kotlin
346         serviceCache.clear()
347     }
348 
349 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ReviewRepository.kt:192:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Repository\ReviewRepository.kt is not ending with a new line.
```
```kotlin
189             })
190     }
191 
192 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/ServiceRepository.kt:386:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\Repository\ServiceRepository.kt is not ending with a new line.
```
```kotlin
383         invalidateCache()
384     }
385 
386 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/FirebaseMessagingService.kt:58:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\ViewModel\FirebaseMessagingService.kt is not ending with a new line.
```
```kotlin
55         }
56 
57     }
58 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/UserViewModel.kt:425:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\ViewModel\UserViewModel.kt is not ending with a new line.
```
```kotlin
422         }
423     }
424 
425 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/ViewModelFactory.kt:24:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\ViewModel\ViewModelFactory.kt is not ending with a new line.
```
```kotlin
21         }
22         throw IllegalArgumentException("Unknown ViewModel class")
23     }
24 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/ViewModel/navbar.kt:4:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\ViewModel\navbar.kt is not ending with a new line.
```
```kotlin
1 package vcmsa.projects.wil_hustlehub.ViewModel
2 
3 class navbar {
4 }
!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AboutUsActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\AboutUsActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AboutUsFragment.kt:27:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\AboutUsFragment.kt is not ending with a new line.
```
```kotlin
24         super.onViewCreated(view, savedInstanceState)
25 
26     }
27 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AccountActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\AccountActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AccountFragment.kt:36:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\AccountFragment.kt is not ending with a new line.
```
```kotlin
33 
34         }
35     }
36 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/AddReviewFragment.kt:114:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\AddReviewFragment.kt is not ending with a new line.
```
```kotlin
111             }
112         }
113     }
114 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BookServiceActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\BookServiceActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesActivity.kt:24:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\BrowseServicesActivity.kt is not ending with a new line.
```
```kotlin
21 
22 
23     }
24 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/BrowseServicesFragment.kt:177:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\BrowseServicesFragment.kt is not ending with a new line.
```
```kotlin
174 
175         }
176     }
177 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/CategoriesActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\CategoriesActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/CategoriesFragment.kt:27:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\CategoriesFragment.kt is not ending with a new line.
```
```kotlin
24         super.onViewCreated(view, savedInstanceState)
25 
26     }
27 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ChatFragment.kt:168:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ChatFragment.kt is not ending with a new line.
```
```kotlin
165         super.onDestroyView()
166         _binding = null
167     }
168 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ContactActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ContactActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ContactFragment.kt:26:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ContactFragment.kt is not ending with a new line.
```
```kotlin
23         super.onViewCreated(view, savedInstanceState)
24 
25     }
26 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/HomeActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\HomeActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/HomeFragment.kt:45:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\HomeFragment.kt is not ending with a new line.
```
```kotlin
42                 .commit()
43         }
44     }
45 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\LoginActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/OfferSkillsActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\OfferSkillsActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/OfferSkillsFragment.kt:35:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\OfferSkillsFragment.kt is not ending with a new line.
```
```kotlin
32             startActivity(intent)
33         }
34     }
35 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ProfileActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProfileFragment.kt:142:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ProfileFragment.kt is not ending with a new line.
```
```kotlin
139 
140 
141     }
142 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProviderBookings.kt:22:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ProviderBookings.kt is not ending with a new line.
```
```kotlin
19                 .commit()
20         }
21     }
22 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ProviderBookingsFragment.kt:96:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ProviderBookingsFragment.kt is not ending with a new line.
```
```kotlin
93          super.onDestroyView()
94          _binding = null
95      }
96  }
!!   ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterActivity.kt:21:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\RegisterActivity.kt is not ending with a new line.
```
```kotlin
18                 .commit()
19         }
20     }
21 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterFragment.kt:91:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\RegisterFragment.kt is not ending with a new line.
```
```kotlin
88 
89 
90     }
91 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:173:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\RegisterService.kt is not ending with a new line.
```
```kotlin
170         return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
171 
172     }
173 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReportUserActvity.kt:188:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ReportUserActvity.kt is not ending with a new line.
```
```kotlin
185              return serviceName
186          }
187      }
188 }
!!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReviewsActivity.kt:22:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ReviewsActivity.kt is not ending with a new line.
```
```kotlin
19                 .commit()
20         }
21     }
22 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ReviewsFragment.kt:81:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\ReviewsFragment.kt is not ending with a new line.
```
```kotlin
78         super.onDestroyView()
79         _binding = null
80     }
81 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/UserBookingsActivity.kt:81:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\UserBookingsActivity.kt is not ending with a new line.
```
```kotlin
78             }
79         }
80     }
81 }
!!  ^ error

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/admin_portal.kt:34:2
```
The file C:\Users\ashfo\Downloads\INSY7315-updates-z\INSY7315-updates-z\app\src\main\java\vcmsa\projects\wil_hustlehub\View\admin_portal.kt is not ending with a new line.
```
```kotlin
31             startActivity(intent)
32         }
33     }
34 }
!!  ^ error

```

### style, UnusedPrivateMember (6)

Private member is unused and should be removed.

[Documentation](https://detekt.dev/docs/rules/style#unusedprivatemember)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:23:17
```
Private property `createdDate` is unused.
```
```kotlin
20     private val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
21 
22     private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
23     private val createdDate = createdDateFormat.format(java.util.Date())
!!                 ^ error
24 
25     //created a cache to temporarily store service details instead of retrieving them from firebase every time.
26     private val serviceCache = mutableMapOf<String, Service>()

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/BookServiceRepository.kt:29:17
```
Private property `cacheExpiry` is unused.
```
```kotlin
26     private val serviceCache = mutableMapOf<String, Service>()
27 
28     //the service details expires after 5 minutes.
29     private val cacheExpiry = 5 * 60 * 1000L
!!                 ^ error
30 
31     /*this function takes the information like servicename,
32      date and time and saves it to the database

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/Repository/UserRepository.kt:64:34
```
Function parameter `forceRefresh` is unused.
```
```kotlin
61     /**
62      * This function gets the user's information using their id
63      */
64     fun getUserData(uid: String, forceRefresh: Boolean = false, callback: (User?) -> Unit) {
!!                                  ^ error
65         // check if this user is cached and return it immediately
66         if (cachedUser?.userID == uid) {
67             callback(cachedUser)

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:47:17
```
Private property `CAMERA_REQUEST_CODE` is unused.
```
```kotlin
44     }
45     private var imageString : String = ""
46     private var photoUri : Uri? = null
47     private val CAMERA_REQUEST_CODE = 100
!!                 ^ error
48     private val requestPermissionsLauncher =
49         registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
50             val denied = permissions.filter { !it.value }.keys

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/RegisterService.kt:84:17
```
Private property `availability` is unused.
```
```kotlin
81             val category = binding.actvCategory.text.toString()
82             val description = binding.etServiceDescription.text.toString()
83             val price = binding.etServicePrice.text.toString()
84             val availability = binding.etAvailability.text.toString()
!!                 ^ error
85             val location = binding.actvLocation.selectedItem.toString()
86             if(serviceName.isEmpty()|| category.isEmpty() || description.isEmpty() || price.isEmpty()){
87                 Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/UserBookingsActivity.kt:49:13
```
Private property `userid` is unused.
```
```kotlin
46             insets
47         }
48         val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
49         val userid = sharedPreferences.getString("uid", null)
!!             ^ error
50         binding.addPetBackButton.setOnClickListener {
51             finish()
52         }

```

### style, WildcardImport (3)

Wildcard imports should be replaced with imports using fully qualified class names. Wildcard imports can lead to naming conflicts. A library update can introduce naming clashes with your classes which results in compilation errors.

[Documentation](https://detekt.dev/docs/rules/style#wildcardimport)

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/LoginFragment.kt:17:1
```
vcmsa.projects.wil_hustlehub.Repository.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
14 import com.google.android.gms.auth.api.signin.GoogleSignInClient
15 import com.google.android.gms.auth.api.signin.GoogleSignInOptions
16 import vcmsa.projects.wil_hustlehub.MainActivity
17 import vcmsa.projects.wil_hustlehub.Repository.*
!! ^ error
18 import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
19 import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
20 import vcmsa.projects.wil_hustlehub.databinding.FragmentLoginBinding

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/ServiceManagementActivity.kt:14:1
```
vcmsa.projects.wil_hustlehub.Repository.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
11 import androidx.recyclerview.widget.RecyclerView
12 import vcmsa.projects.wil_hustlehub.Adapters.ServiceAdapter
13 import vcmsa.projects.wil_hustlehub.R
14 import vcmsa.projects.wil_hustlehub.Repository.*
!! ^ error
15 import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
16 import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
17 

```

* C:/Users/ashfo/Downloads/INSY7315-updates-z/INSY7315-updates-z/app/src/main/java/vcmsa/projects/wil_hustlehub/View/UserManagementActivity.kt:11:1
```
vcmsa.projects.wil_hustlehub.Repository.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
8  import vcmsa.projects.wil_hustlehub.Adapter.ReportedUsersAdapter
9  import vcmsa.projects.wil_hustlehub.Model.Report
10 import vcmsa.projects.wil_hustlehub.R
11 import vcmsa.projects.wil_hustlehub.Repository.*
!! ^ error
12 import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
13 import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
14 

```

generated with [detekt version 1.22.0](https://detekt.dev/) on 2025-10-26 11:21:38 UTC
