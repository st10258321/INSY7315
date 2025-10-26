package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.Report
import java.util.Date

class ServiceRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // using specific format
    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    // //created a cache to temporarily store service details instead of retrieving them from firebase every time.
    private var servicesCache: List<Service>? = null
    private var cacheTimestamp: Long = 0 //tracks when the cache was last updated.
    private val CACHE_VALIDITY_MS = 5 * 60 * 1000L // //the service details expires after 5 minutes.

    // Add a new service
    fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: String, availabileTime: String, location: String, callback: (Boolean, String?, Service?) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid
        val serviceId = database.child("Services").push().key ?: ""

        val service = Service(
            serviceId = serviceId,
            userId = userId,
            serviceName = serviceName,
            category = category,
            description = description,
            price = price,
            image = image,
            availabileDay = availabileDay,
            availabileTime = availabileTime,
            location = location,
            createdDate = createdDateFormat.format(Date())
        )

        database.child("Services").child(serviceId).setValue(service)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    invalidateCache() //clears the in-memory cache.
                    callback(true, null, service)
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }


    // getting all the services that were created by the current user that is logged in.
    fun getUserServices(callback: (Boolean, String?, List<Service>?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid

        //fetches only the services created by current user by building a query
        val query = database.child("Services")
            .orderByChild("userId")
            .equalTo(userId)

        query.keepSynced(true) //updates the data and caches the data

        //the database is only read once
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val services = snapshot.children.mapNotNull {
                    it.getValue(Service::class.java)
                }
                callback(true, null, services)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    // when the current user wants to delete a service that they created and exists.
    fun deleteService(serviceId: String, callback: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in")
            return
        }

        // First check if the service belongs to the current user
        database.child("Services").child(serviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val service = snapshot.getValue(Service::class.java)
                    if (service == null) {
                        callback(false, "Service not found")
                        return
                    }

                    if (service.userId != currentUser.uid) {
                        callback(false, "This service does not belong to you!")
                        return
                    }

                    database.child("Services").child(serviceId).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                invalidateCache() //clears any cached services so the UI can refresh.
                                callback(true, null)
                            } else {
                                callback(false, task.exception?.message)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })
    }


    // Get a single service by serviceId
    fun getServiceById(serviceId: String, callback: (Boolean, String?, Service?) -> Unit) {
        database.child("Services").child(serviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val service = snapshot.getValue(Service::class.java)
                    callback(service != null, if (service == null) "Service not found" else null, service)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

  //for displaying all the services inside the database
    fun getAllServices(refresh: Boolean = false, callback: (Boolean, String?, List<Service>?) -> Unit) {
      val currentTime = System.currentTimeMillis() //will be used to check if the cache is still available

      // checks if there is data that is cached and available and the cache was not refreshed
      if (!refresh && servicesCache != null && (currentTime - cacheTimestamp) < CACHE_VALIDITY_MS) {
          callback(true, null, servicesCache) //if true it returns the cached services
          return
      }

      //falls back to the database directly if the there isnt data inside the cache and saves data inside the cache
      database.child("Services")
          .addListenerForSingleValueEvent(object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                  val services = snapshot.children.mapNotNull {
                      it.getValue(Service::class.java)
                  }
                  servicesCache = services
                  cacheTimestamp = System.currentTimeMillis()
                  callback(true, null, services)
              }

              override fun onCancelled(error: DatabaseError) {
                  callback(false, error.message, null)
              }
          })
    }

    // retrieving a limited number of services at a time instead of loading all services all at once
    //last key is used to fetch the next page
    fun getServicesPaginated(lastKey: String? = null, pageSize: Int = 20, callback: (Boolean, String?, List<Service>?, String?) -> Unit
    ) {

        var query = database.child("Services")
            .orderByKey()
            .limitToFirst(pageSize + 1)

        //stores the last services key that was shown in the previous page
        //and then shows the next services after the "last key" in the next page
        lastKey?.let { query = query.startAfter(it) }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children.toList()
                val hasMore = children.size > pageSize //checks if there are more services to show
                val itemsToProcess = if (hasMore) children.dropLast(1) else children

                val services = itemsToProcess.mapNotNull {
                    it.getValue(Service::class.java)
                }
                val nextKey = if (hasMore) children.last().key else null //fetching more services if there is more

                callback(true, null, services, nextKey)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null, null)
            }
        })
    }

    // when the user wants to search a service by name
    fun searchServicesByName(nameOfService: String, callback: (Boolean, String?, List<Service>?) -> Unit) {

        // fetching from the cache first if available
        if (servicesCache != null && (System.currentTimeMillis() - cacheTimestamp) < CACHE_VALIDITY_MS) {
            val filtered = servicesCache!!.filter {
                it.serviceName.contains(nameOfService, ignoreCase = true)
            }
            callback(true, null, filtered)
            return
        }

        database.child("Services")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = snapshot.children.mapNotNull {
                        it.getValue(Service::class.java)
                    }.filter {
                        it.serviceName.contains(nameOfService, ignoreCase = true)
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    //using indexing to search by category
    fun searchServicesByCategory(category: String, callback: (Boolean, String?, List<Service>?) -> Unit) {

        //retrieves only the services of the searched category by reading the data once
        database.child("Services")
            .orderByChild("category")
            .equalTo(category)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = snapshot.children.mapNotNull {
                        it.getValue(Service::class.java)
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }


    // getting all services offered by a specific service provider
    fun getServicesByUserId(userid: String, callback: (Boolean, String?, List<Service>?) -> Unit) {
        database.child("Services")
            .orderByChild("userId")
            .equalTo(userid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = snapshot.children.mapNotNull {
                        it.getValue(Service::class.java)
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }
    fun getReports(callback: (Boolean, String?, MutableList<Report>?) -> Unit){
        database.child("Reports").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = mutableListOf<Report>()
                for(reportSnapshot in snapshot.children){
                    val report = reportSnapshot.getValue(Report::class.java)
                    report?.let { reports.add(it) }
                }
                callback(true, null, reports)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
    fun updateReportStatus(report: Report, callback: (Boolean, String?) -> Unit){
        val reportId = report.reportId
        database.child("Reports").child(reportId).setValue(report)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true, null)
                }
                else{
                    callback(false, it.exception?.message)
                }
            }
    }
    fun reportServiceProvider(serviceProviderId: String,serviceProviderName: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String, callback: (Boolean, String?) -> Unit)
    {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            callback(false, "User not logged in")
            return
        }

        val userId = currentUser.uid

        // to check if user is trying to report themselves
        if (serviceProviderId == userId) {
            callback(false, "You cannot report yourself")
            return
        }


        database.child("Services").child(serviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val service = snapshot.getValue(Service::class.java)
                    if (service == null) {
                        callback(false, "Selected service not found")
                        return
                    }

                    if (service.userId != serviceProviderId) {
                        callback(false, "Selected service does not belong to this provider")
                        return
                    }

                    val reportId = database.child("Reports").push().key ?: ""
                    val currentDate = createdDateFormat.format(java.util.Date())

                    val report = Report(
                        reportId = reportId,
                        serviceProviderId = serviceProviderId,
                        serviceProviderName = serviceProviderName,
                        userId = userId,
                        serviceId = serviceId, //this is the specific service selected from the drop down list
                        reportIssue = reportedIssue,
                        additionalNotes = additionalNotes,
                        image = images,
                        status = "Pending",
                        createdDate = currentDate
                    )

                    // saving the report
                    database.child("Reports").child(reportId).setValue(report)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true, "Report submitted successfully!")
                            } else {
                                callback(false, task.exception?.message)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })
    }

    // clears the in-memory cache.
    private fun invalidateCache() {
        servicesCache = null
        cacheTimestamp = 0
    }

    // stops the services from being temporarily stored, it should be called in viewmodel when repository is no longer needed.
    fun cleanup() {
        database.child("Services").keepSynced(false)
        invalidateCache()
    }

}