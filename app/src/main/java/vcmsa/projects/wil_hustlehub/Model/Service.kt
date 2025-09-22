package vcmsa.projects.wil_hustlehub.Model

import android.net.Uri

data class Service (
    val serviceId: String = "",
    val userId: String = "",
    val serviceName: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val availabileDay: Any? = emptyList<String>(),
    val availabileTime: Any? = emptyList<String>(),
    val location: String = "",
    val createdDate: String = ""
)
