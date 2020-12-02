package com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.AlbumWithSongs
import kotlinx.android.synthetic.main.cardview_albums.view.*
import kotlinx.android.synthetic.main.cardview_artists.view.*
import java.util.*
import kotlin.collections.ArrayList

class AlbumsAdapter : ListAdapter<AlbumWithSongs, RecyclerView.ViewHolder>(AlbumsDiffCallback()) {

    private var unfilteredList: List<AlbumWithSongs> = listOf<AlbumWithSongs>()

    fun modifyList(list: List<AlbumWithSongs>) {
        unfilteredList = list
        submitList(list)
    }

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
        fun bind(albumWithSongs: AlbumWithSongs) {
            Glide
                .with(itemView.context)
                .load(albumWithSongs.album.albumUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(itemView.albumIV)
            itemView.albumTitleTV.text = albumWithSongs.album.albumName
            val songsAdapter = SongsAdapter()
            itemView.songsRecyclerView.adapter = songsAdapter
            songsAdapter.submitList(albumWithSongs.songs)
        }
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<AlbumWithSongs>()

        // perform the data filtering
        if (!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.album.albumName.toLowerCase(Locale.getDefault())
                    .contains(query.toString().toLowerCase(Locale.getDefault()))
            })
        } else {
            list.addAll(unfilteredList)
        }
        submitList(list)
    }

    private class AlbumsDiffCallback : DiffUtil.ItemCallback<AlbumWithSongs>() {

        override fun areItemsTheSame(
            oldItem: AlbumWithSongs,
            newItem: AlbumWithSongs
        ): Boolean {
            return oldItem.album.albumId == newItem.album.albumId
        }

        override fun areContentsTheSame(
            oldItem: AlbumWithSongs,
            newItem: AlbumWithSongs
        ): Boolean {
            return oldItem == newItem
        }
    }
}