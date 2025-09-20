package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vcmsa.projects.wil_hustlehub.databinding.FragmentOfferSkillsBinding

class OfferSkillsFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentOfferSkillsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_offer_skills.xml layout.
        _binding = FragmentOfferSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterAsProvider.setOnClickListener {
            val intent = Intent(requireContext(), RegisterService::class.java)
            startActivity(intent)
        }
        binding.btnManageListings.setOnClickListener {
            val intent = Intent(requireContext(), ProviderBookings::class.java)
            startActivity(intent)
        }
    }
}