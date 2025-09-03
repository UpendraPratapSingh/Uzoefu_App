package com.travel.uzoefuapp.businessActivities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.PhotoAdapter
import com.travel.uzoefuapp.databinding.ActivityBusinessUploadPhotoBinding

class BusinessUploadPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessUploadPhotoBinding
    private lateinit var adapter: PhotoAdapter
    private val photos = mutableListOf<Uri>()
    private val PICK_IMAGES = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        adapter = PhotoAdapter(photos) { position ->
            photos.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        binding.uploadPhotosRecyclerview.layoutManager = GridLayoutManager(this, 3)
        binding.uploadPhotosRecyclerview.adapter = adapter

        binding.addPhotosLayout.setOnClickListener {
            if (photos.size >= 10) {
                Toast.makeText(this, "Maximum 10 photos allowed", Toast.LENGTH_SHORT).show()
            } else {
                pickImages()
            }
        }
    }

    private fun pickImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES && resultCode == RESULT_OK) {
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    if (photos.size >= 10) break
                    photos.add(clipData.getItemAt(i).uri)
                }
            } ?: data?.data?.let { uri ->
                if (photos.size < 10) photos.add(uri)
            }
            adapter.notifyDataSetChanged()
        }
    }
}
