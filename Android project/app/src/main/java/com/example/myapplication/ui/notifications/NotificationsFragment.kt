package com.example.myapplication.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.broadcast.AirplaneModeReceiver
import com.example.myapplication.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var airplaneModeReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Создаём и регистрируем BroadcastReceiver
        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                    val message = if (isAirplaneModeOn) "Airplane Mode ON ✈️" else "Airplane Mode OFF ✅"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    binding.textNotifications.text = message // ✅ Обновляем UI
                }
            }
        }

        requireActivity().registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(airplaneModeReceiver)
        _binding = null
    }
}
