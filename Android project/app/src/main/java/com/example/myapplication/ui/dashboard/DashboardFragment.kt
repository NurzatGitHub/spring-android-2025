package com.example.myapplication.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.service.MusicService
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        checkNotificationPermission()

        binding.buttonStart.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java)
            intent.action = "START"
            requireContext().startService(intent)
        }

        binding.buttonPause.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java)
            intent.action = "PAUSE"
            requireContext().startService(intent)
        }

        binding.buttonStop.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java)
            intent.action = "STOP"
            requireContext().startService(intent)
        }

        return root
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
