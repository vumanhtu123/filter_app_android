package com.example.filterapp.apdaters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filterapp.databinding.ItemContainerSavedImageBinding
import com.example.filterapp.listeners.SavedImagesListener
import java.io.File

class SavedImagesAdapter(
    private val savedImage : List<Pair<File,Bitmap>>,
    private val SavedImageListener : SavedImagesListener
):
RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>(){
    inner class SavedImagesViewHolder(val binding: ItemContainerSavedImageBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        with(holder){
            with(savedImage[position]){
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener {
                    SavedImageListener.onImageClicked(first)
                }
            }
        }
    }

    override fun getItemCount(): Int = savedImage.size
}