package com.example.myapplication.ui.content

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentContentBinding

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())

        // Проверяем разрешение
        checkCalendarPermission()

        return root
    }

    private fun checkCalendarPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR), 102)
        } else {
            loadCalendarEvents()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 102 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCalendarEvents() {
        val eventsList = mutableListOf<CalendarEvent>()

        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, CalendarContract.Events.DTSTART + " ASC")

        cursor?.use {
            while (it.moveToNext()) {
                val title = it.getString(0) ?: "No Title"
                val date = it.getLong(1)
                val formattedDate = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", date).toString()

                eventsList.add(CalendarEvent(title, formattedDate))
            }
        }

        binding.recyclerViewEvents.adapter = EventAdapter(eventsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
