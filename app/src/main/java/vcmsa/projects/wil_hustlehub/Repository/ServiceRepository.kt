package vcmsa.projects.wil_hustlehub.Repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.Report

class ServiceRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // using specific format
    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    // Add a new service
    fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availabileDay: List<String>, availabileTime: List<String>, location: String, callback: (Boolean, String?, Service?) -> Unit
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
            createdDate = createdDate
        )

        database.child("Services").child(serviceId).setValue(service)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
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
        database.child("Services").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = mutableListOf<Service>()
                    for (serviceSnapshot in snapshot.children) {
                        val service = serviceSnapshot.getValue(Service::class.java)
                        service?.let { services.add(it) }
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

                    // Delete the service
                    database.child("Services").child(serviceId).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
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
                    if (service != null) {
                        callback(true, null, service)
                    } else {
                        callback(false, "Service not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

  //for displaying all the services inside the database
    fun getAllServices(callback: (Boolean, String?, List<Service>?) -> Unit) {
        database.child("Services")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = mutableListOf<Service>()
                    for (serviceSnapshot in snapshot.children) {
                        val service = serviceSnapshot.getValue(Service::class.java)
                        service?.let { services.add(it) }
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }


    // when the user wants to search a service by name
    fun searchServicesByName(nameOfService: String, callback: (Boolean, String?, List<Service>?) -> Unit) {
        database.child("Services")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = mutableListOf<Service>()
                    for (serviceSnapshot in snapshot.children) {
                        val service = serviceSnapshot.getValue(Service::class.java)
                        service?.let {
                            if (it.serviceName.contains(nameOfService, ignoreCase = true)) {
                                services.add(it)
                            }
                        }
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    fun searchServicesByCategory(category: String, callback: (Boolean, String?, List<Service>?) -> Unit) {
        database.child("Services")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = mutableListOf<Service>()
                    for (serviceSnapshot in snapshot.children) {
                        val service = serviceSnapshot.getValue(Service::class.java)
                        service?.let {
                            if (it.category.equals(category, ignoreCase = true)) {
                                services.add(it)
                            }
                        }
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
        database.child("Services").orderByChild("userId").equalTo(userid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val services = mutableListOf<Service>()
                    for (serviceSnapshot in snapshot.children) {
                        val service = serviceSnapshot.getValue(Service::class.java)
                        service?.let { services.add(it) }
                    }
                    callback(true, null, services)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    fun reportServiceProvider(serviceProviderId: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: List<String> = emptyList(), callback: (Boolean, String?) -> Unit)
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
                        userId = userId,
                        serviceId = serviceId, //this is the specific service selected from the drop down list
                        reportIssue = reportedIssue,
                        additionalNotes = additionalNotes,
                        image = images,
                        createdDate = currentDate
                    )

                    // saving the report
                    database.child("Reports").child(reportId).setValue(report)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
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


}