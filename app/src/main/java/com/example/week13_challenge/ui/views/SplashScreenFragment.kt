package com.example.week13_challenge.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.week13_challenge.R
import com.example.week13_challenge.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the Toolbar

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        requireActivity().findViewById<View>(R.id.toolbarInclude)?.visibility = View.GONE

        // Delay screen and then show home screen

        view.postDelayed({
            // show toolbar again
            requireActivity().findViewById<View>(R.id.toolbarInclude)?.visibility = View.VISIBLE
            (activity as? AppCompatActivity)?.supportActionBar?.show()

            // navigate to home fragment
            findNavController().navigate(
                R.id.action_splashScreenFragment_to_homeFragment
            )

        },6000) // 3 second delay
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}