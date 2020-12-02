package com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbumsAndSongs
import kotlinx.android.synthetic.main.cardview_artists.view.*

class ArtistsAdapter :
    ListAdapter<ArtistWithAlbumsAndSongs, RecyclerView.ViewHolder>(ArtistDiffCallback()) {
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
        fun bind(item: ArtistWithAlbumsAndSongs) {
            itemView.artistTV.text = item.artist.artistName
            Glide
                .with(itemView.context)
                .load(item.artist.artistImageUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(itemView.artistIV)
            val albumsAdapter = AlbumsAdapter()
            itemView.albumRecyclerView.adapter = albumsAdapter
            albumsAdapter.modifyList(item.albums)
            itemView.searchViewAlbums.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    albumsAdapter.filter(newText)
                    return true
                }
            })
        }
    }
}

//TODO create reduce funcion for checking albums ids
private class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistWithAlbumsAndSongs>() {
    override fun areItemsTheSame(
        oldItem: ArtistWithAlbumsAndSongs,
        newItem: ArtistWithAlbumsAndSongs
    ): Boolean {
        return (oldItem.artist.artistId == newItem.artist.artistId)
    }

    override fun areContentsTheSame(
        oldItem: ArtistWithAlbumsAndSongs,
        newItem: ArtistWithAlbumsAndSongs
    ): Boolean {
        return oldItem == newItem
    }
}