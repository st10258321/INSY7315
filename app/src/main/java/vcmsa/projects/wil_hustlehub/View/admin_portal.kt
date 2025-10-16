package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vcmsa.projects.wil_hustlehub.R
import com.google.android.material.card.MaterialCardView

class admin_portal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_portal2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userManagementCard = findViewById<MaterialCardView>(R.id.admin_userMan_card)
        userManagementCard.setOnClickListener {
            // Handle user management card click
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }
        val serviceManagementCard = findViewById<MaterialCardView>(R.id.admin_serviceMan_card)
        serviceManagementCard.setOnClickListener {
            val intent = Intent(this, ServiceManagementActivity::class.java)
            startActivity(intent)
        }
    }
}