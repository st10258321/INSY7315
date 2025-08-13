package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vcmsa.projects.wil_hustlehub.R

class LoginFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_login.xml layout.
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}