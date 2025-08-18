package vcmsa.projects.wil_hustlehub

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val accountLayout = findViewById<View>(R.id.account_layout)
        val loginLayout = findViewById<View>(R.id.login_layout)
        val registerLayout = findViewById<View>(R.id.register_layout)
        val homeLayout = findViewById<View>(R.id.home_layout)
        val offerSkillsLayout = findViewById<View>(R.id.offer_skills_layout)
        val browseServicesLayout = findViewById<View>(R.id.browse_services_layout)
        val categoriesLayout = findViewById<View>(R.id.categories_layout)
        val chatLayout = findViewById<View>(R.id.chat_layout)
        val bookServiceLayout = findViewById<View>(R.id.book_service_layout)

        // Find the Button inside the account layout
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            // Switch visibility
            registerLayout.visibility = View.VISIBLE
            accountLayout.visibility = View.GONE
        }

        // Find the Button inside the account layout
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            // Switch visibility
            loginLayout.visibility = View.VISIBLE
            accountLayout.visibility = View.GONE
        }


        // Find the TextView inside the login layout
        val signupPrompt = loginLayout.findViewById<TextView>(R.id.tvSignUpPrompt)

        signupPrompt.setOnClickListener {
            // Switch visibility
            loginLayout.visibility = View.GONE
            registerLayout.visibility = View.VISIBLE
        }
        // Find the TextView inside the register layout
        val loginPrompt = registerLayout.findViewById<TextView>(R.id.tvLoginPrompt)

        loginPrompt.setOnClickListener {
            // Switch visibility
            loginLayout.visibility = View.GONE
            registerLayout.visibility = View.VISIBLE
        }
        // Find the Button inside the login layout
        val loginbtn = loginLayout.findViewById<Button>(R.id.btnLoginSubmit)

        loginbtn.setOnClickListener {
            // Switch visibility
            loginLayout.visibility = View.GONE
            homeLayout.visibility = View.VISIBLE
        }
        // Find the Button inside the register layout
        val btnFindService = findViewById<Button>(R.id.btnFindService)

        btnFindService.setOnClickListener {
            // Switch visibility
            homeLayout.visibility = View.GONE
            browseServicesLayout.visibility = View.VISIBLE
        }
        // Find the Button inside the register layout
        val btnOfferSkills = findViewById<Button>(R.id.btnOfferSkills)

        btnOfferSkills.setOnClickListener {
            // Switch visibility
            homeLayout.visibility = View.GONE
            offerSkillsLayout.visibility = View.VISIBLE
        }
        // Find the LinearLayout inside the home layout
        val discoverLayout = findViewById<LinearLayout>(R.id.discoverLayout)

        discoverLayout.setOnClickListener {
            // Switch visibility
            homeLayout.visibility = View.GONE
            categoriesLayout.visibility = View.VISIBLE
        }
        // Find the LinearLayout inside the home layout
        val connectLayout = findViewById<LinearLayout>(R.id.connectLayout)

        connectLayout.setOnClickListener {
            // Switch visibility
            homeLayout.visibility = View.GONE
            chatLayout.visibility = View.VISIBLE
        }
        // Find the LinearLayout inside the home layout
        val thriveLayout = findViewById<LinearLayout>(R.id.thriveLayout)

        thriveLayout.setOnClickListener {
            // Switch visibility
            homeLayout.visibility = View.GONE
            bookServiceLayout.visibility = View.VISIBLE
        }
    }
}