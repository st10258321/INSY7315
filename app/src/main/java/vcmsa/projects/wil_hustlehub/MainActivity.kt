package vcmsa.projects.wil_hustlehub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vcmsa.projects.wil_hustlehub.View.BrowseServicesFragment
import vcmsa.projects.wil_hustlehub.View.HomeFragment
import vcmsa.projects.wil_hustlehub.View.LoginFragment
import vcmsa.projects.wil_hustlehub.View.OfferSkillsFragment
import vcmsa.projects.wil_hustlehub.View.ProfileFragment
import vcmsa.projects.wil_hustlehub.View.RegisterFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        if (savedInstanceState == null) {
            openFragment(HomeFragment())
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
            .replace(R.id.nav_host_fragment, fragment)
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 👇 Hide nav bar for Login & Register
        if (fragment is LoginFragment || fragment is RegisterFragment) {
            bottomNav.visibility = View.GONE
        } else {
            bottomNav.visibility = View.VISIBLE
        }
    }
}
