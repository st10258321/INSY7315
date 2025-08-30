package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.wil_hustlehub.R

class OfferSkillsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_offer_skills)

        // Load fragment only if it's the first creation.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OfferSkillsFragment())
                .commit()
        }
    }
}