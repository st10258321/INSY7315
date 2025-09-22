package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vcmsa.projects.wil_hustlehub.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_home.xml layout.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindService.setOnClickListener {
            val intent = Intent(requireContext(), BrowseServicesActivity::class.java)
            startActivity(intent)
        }
        binding.btnOfferSkills.setOnClickListener {
            val intent = Intent(requireContext(), OfferSkillsActivity::class.java)
            startActivity(intent)
        }
    }
}