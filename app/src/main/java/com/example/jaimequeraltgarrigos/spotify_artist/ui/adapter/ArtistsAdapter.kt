package com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbums
import kotlinx.android.synthetic.main.cardview_artists.view.*

class ArtistsAdapter : ListAdapter<ArtistWithAlbums, RecyclerView.ViewHolder>(ArtistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_artists, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val artistWithAlbums = getItem(position)
        (holder as ArtistViewHolder).bind(artistWithAlbums)
    }

    class ArtistViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ArtistWithAlbums) {
            Glide
                .with(itemView.context)
                .load(item.artist.artistImageUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(itemView.artistIV)
            val albumsAdapter = AlbumsAdapter()
            itemView.albumRecyclerView.adapter = albumsAdapter
            albumsAdapter.submitList(item.albums)
        }
    }
}

private class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistWithAlbums>() {
    override fun areItemsTheSame(oldItem: ArtistWithAlbums, newItem: ArtistWithAlbums): Boolean {
        return oldItem.artist.artistId == newItem.artist.artistId
    }

    override fun areContentsTheSame(oldItem: ArtistWithAlbums, newItem: ArtistWithAlbums): Boolean {
        return oldItem == newItem
    }
}