package vcmsa.projects.wil_hustlehub.Network

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

object PushApiClient {
    fun sendBookingNotification(
        context : Context,
        providerFcmToken : String?,
        userName: String?,
        serviceName: String?
    ){
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
            ?.addOnSuccessListener { result ->
                val idToken = result.token

                val requestBody = JSONObject().apply {
                    put("providerToken", providerFcmToken)
                    put("userName", userName)
                    put("serviceName",serviceName)
                }
                Log.d("API", "Booking notification sent: $requestBody")
                val request = object : StringRequest(
                    Method.POST,
                    "https://push-api-k1q5.onrender.com/booking",
                    { response -> Log.d("API", "Booking notification sent: $response") },
                    { error -> Log.e("API", "Failed to send booking notification: ${error.message}") }
                ) {
                    override fun getBody(): ByteArray = requestBody.toString().toByteArray()
                    override fun getBodyContentType(): String = "application/json"
                    override fun getHeaders(): MutableMap<String, String> =
                        mutableMapOf("Authorization" to "Bearer $idToken")
                }

                Volley.newRequestQueue(context).add(request)

            }
    }
    fun sendMessageNotification(
        context : Context,
        recipientToken : String,
        senderName: String,
        text: String
    ){
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
            ?.addOnSuccessListener { result ->
                val idToken = result.token

                val requestBody = JSONObject().apply {
                    put("recipientToken", recipientToken)
                    put("senderName", senderName)
                    put("text",text)
                }
                val request = object : StringRequest(
                    Method.POST,
                    "https://push-api-k1q5.onrender.com",
                    { response -> Log.d("API", "Booking notification sent: $response") },
                    { error -> Log.e("API", "Failed to send booking notification: ${error.message}") }
                ) {
                    override fun getBody(): ByteArray = requestBody.toString().toByteArray()
                    override fun getBodyContentType(): String = "application/json"
                    override fun getHeaders(): MutableMap<String, String> =
                        mutableMapOf("Authorization" to "Bearer $idToken")
                }

                Volley.newRequestQueue(context).add(request)

            }
    }

}