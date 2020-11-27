package com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import kotlinx.android.synthetic.main.cardview_albums.view.*

class AlbumsAdapter : ListAdapter<Album, RecyclerView.ViewHolder>(AlbumsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_albums, parent, false)
        return AlbumsAdapter.AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = getItem(position)
        (holder as AlbumsAdapter.AlbumViewHolder).bind(album)
    }

    class AlbumViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: Album) {
            Glide
                .with(itemView.context)
                .load(album.albumUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(itemView.albumIV)
            itemView.albumTitleTV.text = album.albumName
        }
    }
}

private class AlbumsDiffCallback : DiffUtil.ItemCallback<Album>() {

    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.albumId == newItem.albumId
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}