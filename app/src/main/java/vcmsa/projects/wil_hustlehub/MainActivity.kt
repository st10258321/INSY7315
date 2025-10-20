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
import vcmsa.projects.wil_hustlehub.View.AccountFragment
import vcmsa.projects.wil_hustlehub.View.BrowseServicesFragment
import vcmsa.projects.wil_hustlehub.View.ChatRoomActivity
import vcmsa.projects.wil_hustlehub.View.ChatListFragment
import vcmsa.projects.wil_hustlehub.View.HomeFragment
import vcmsa.projects.wil_hustlehub.View.LoginFragment
import vcmsa.projects.wil_hustlehub.View.OfferSkillsFragment
import vcmsa.projects.wil_hustlehub.View.ProfileFragment
import vcmsa.projects.wil_hustlehub.View.RegisterFragment
import vcmsa.projects.wil_hustlehub.databinding.ActivityMainBinding
import java.security.MessageDigest
import android.util.Base64
import androidx.annotation.RequiresApi


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                when (entry.key) {
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        if (entry.value) {
                            Toast.makeText(this, "Notifications allowed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Notifications denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Manifest.permission.CAMERA -> {
                        if (entry.value) {
                            Toast.makeText(this, "Camera granted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Camera denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO -> {
                        if (entry.value) {
                            Toast.makeText(this, "${entry.key} granted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "${entry.key} denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    private fun askAllPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        // Storage / Media
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ → granular media access
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            // Pre-Android 13 → legacy storage
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // Launch request
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun printSHA1() {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signatures = info.signingInfo?.apkContentsSigners
            for (signature in signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sha1 = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("GoogleSignInSHA", "SHA-1: $sha1")
            }
        } catch (e: Exception) {
            Log.e("GoogleSignInSHA", "Error retrieving SHA-1", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        printSHA1()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        askAllPermissions()

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
                R.id.nav_chat ->{
                    openFragment(ChatListFragment())
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
            if (fragment is LoginFragment || fragment is RegisterFragment || fragment is AccountFragment) View.GONE else View.VISIBLE
    }
}
