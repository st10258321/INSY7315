package vcmsa.projects.wil_hustlehub

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import vcmsa.projects.wil_hustlehub.View.BrowseServicesFragment
import vcmsa.projects.wil_hustlehub.View.HomeFragment
import vcmsa.projects.wil_hustlehub.View.LoginFragment
import vcmsa.projects.wil_hustlehub.View.OfferSkillsFragment
import vcmsa.projects.wil_hustlehub.View.ProfileFragment
import vcmsa.projects.wil_hustlehub.View.RegisterFragment
import vcmsa.projects.wil_hustlehub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted: Boolean ->
        if(isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.

        }
    }
    private fun askNotificationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        askNotificationPermission()

        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            task -> if(task.isSuccessful){
                Log.d("FCM_TOKEN",task.result?:"No token")
            }else{
                Toast.makeText(this,"Failed to get token",Toast.LENGTH_SHORT).show()
        }
        }



        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.nav_browse -> {
                    openFragment(BrowseServicesFragment())
                    true
                }
                R.id.nav_offer_skills -> {
                    openFragment(OfferSkillsFragment())
                    true
                }
                R.id.nav_profile -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.navHostFragment.id, fragment)
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 👇 Hide nav bar for Login & Register
//        if (fragment is LoginFragment || fragment is RegisterFragment) {
//            bottomNav.visibility = View.GONE
//        } else {
//            bottomNav.visibility = View.VISIBLE
//        }
        binding.bottomNavigation.visibility =
            if (fragment is LoginFragment || fragment is RegisterFragment) View.GONE else View.VISIBLE
    }
}
