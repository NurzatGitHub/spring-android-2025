package com.example.myapplication.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var counter = 0  // Счетчик

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Загружаем сохраненное значение счетчика из SharedPreferences
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        counter = sharedPref.getInt("counter_value", 0)
        binding.textCounter.text = counter.toString()

        // Увеличиваем счетчик по нажатию кнопки
        binding.buttonIncrement.setOnClickListener {
            counter++
            binding.textCounter.text = counter.toString()

            // Сохраняем значение в SharedPreferences
            with(sharedPref.edit()) {
                putInt("counter_value", counter)
                apply()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}