package com.example.filterapp.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.filterapp.activities.editimage.EditImageActivity
import com.example.filterapp.activities.savedimage.SavedImagesActivity
import com.example.filterapp.databinding.ActivityMainBinding
import com.github.drjacky.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {
    companion object {
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonEditNewImage.setOnClickListener {
            launcher.launch(
                ImagePicker.with(this)
                    .galleryOnly()
                    .createIntent()
            )
        }
        binding.buttonViewSavedImages.setOnClickListener {
            Intent(applicationContext, SavedImagesActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                //toán tử !! tránh kiểm tra null khi biết chắc chắn biến không null
                intent = Intent(this, EditImageActivity::class.java)
                intent.putExtra(KEY_IMAGE_URI, uri)
                // Use the uri to load the image
                startActivity(intent)
            }
        }
}