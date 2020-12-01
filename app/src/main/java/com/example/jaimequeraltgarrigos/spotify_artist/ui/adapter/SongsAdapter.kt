package com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Song
import kotlinx.android.synthetic.main.cardview_songs.view.*

class SongsAdapter : ListAdapter<Song, RecyclerView.ViewHolder>(SongsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_songs, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song = getItem(position)
        (holder as SongViewHolder).bind(song)
    }

    class SongViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) {
            itemView.songNameTV.text = song.songName
        }
    }

    private fun convertMillosToSongFormat(long: Long): String{
        return ""
    }
}

private class SongsDiffCallback : DiffUtil.ItemCallback<Song>() {

    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.songId == newItem.songId
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}