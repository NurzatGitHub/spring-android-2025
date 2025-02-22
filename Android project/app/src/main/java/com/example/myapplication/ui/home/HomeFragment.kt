package com.example.myapplication.ui.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Открываем галерею для выбора изображения
        binding.buttonPickImage.setOnClickListener {
            pickImageFromGallery()
        }

        // Отправляем изображение в Instagram Stories
        binding.buttonShareInstagram.setOnClickListener {
            if (selectedImageUri != null) {
                shareImageToInstagramStory(selectedImageUri!!)
            } else {
                Toast.makeText(requireContext(), "Select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    // Открываем галерею для выбора изображения
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    // Обработчик результата выбора изображения
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    Toast.makeText(requireContext(), "Image selected!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Проверяем, установлен ли Instagram
    private fun isInstagramInstalled(): Boolean {
        val packageManager = requireActivity().packageManager
        return try {
            packageManager.getPackageInfo("com.instagram.android", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun shareImageToInstagramStory(imageUri: Uri) {
        if (!isInstagramInstalled()) {
            Toast.makeText(requireContext(), "Instagram is not installed", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(imageUri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra("source_application", requireActivity().packageName)
        }

        requireActivity().grantUriPermission(
            "com.instagram.android", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Instagram sharing not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
