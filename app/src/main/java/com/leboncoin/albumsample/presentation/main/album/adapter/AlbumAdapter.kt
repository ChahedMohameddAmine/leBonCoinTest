package com.leboncoin.albumsample.presentation.main.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.leboncoin.albumsample.R
import com.leboncoin.albumsample.databinding.ItemAlbumBinding
import com.leboncoin.albumsample.presentation.main.album.mapping.AlbumUi
import com.leboncoin.albumsample.presentation.tools.animateIconAddToFavorite
import com.leboncoin.albumsample.presentation.tools.animateIconDeleteFavorite


class AlbumAdapter(
    val onAlbumClicked: (AlbumUi) -> Unit,
    val onAlbumFavoriteClicked: (AlbumUi) -> Unit
) :
    ListAdapter<AlbumUi, AlbumAdapter.AlbumViewHolder>(DifferCallback()) {


    class DifferCallback : DiffUtil.ItemCallback<AlbumUi>() {

        override fun getChangePayload(oldItem: AlbumUi, newItem: AlbumUi): Any? {
            if (newItem.isFavorite != oldItem.isFavorite) {
                return PayLoad.FAVORITE_CHANGE;
            }
            return null
        }

        override fun areItemsTheSame(oldItem: AlbumUi, newItem: AlbumUi): Boolean {
            return oldItem.albumId == newItem.albumId
        }

        override fun areContentsTheSame(oldItem: AlbumUi, newItem: AlbumUi): Boolean {
            return oldItem.isFavorite == newItem.isFavorite &&
                    oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, PayLoad.FAVORITE_CHANGE)
    }

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                PayLoad.FAVORITE_CHANGE -> {
                    holder.bind(item, PayLoad.FAVORITE_CHANGE)
                }
            }
        } else
            holder.bind(item, null)

        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onAlbumClicked(item)
                    }
                }
            }
        }

        fun bind(albumUi: AlbumUi, payLoad: PayLoad? = null) {
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            binding.apply {
                //avoid the effect flashing the whole view when certain field is changed
                if (payLoad != null) {
                    when (payLoad) {
                        PayLoad.FAVORITE_CHANGE -> {
                            setFavoriteIcon(albumUi, withoutAnimation = true)
                        }
                    }
                }

                setFavoriteIcon(albumUi, true)
                favoriteView.setOnClickListener {
                    onAlbumFavoriteClicked.invoke(albumUi.copy(isFavorite = !albumUi.isFavorite))
                    albumUi.isFavorite = !albumUi.isFavorite
                    setFavoriteIcon(albumUi)
                }

                val glide = Glide.with(itemView.context)
                albumName.text = albumUi.title
                glide
                    .load(albumUi.url)
                    .thumbnail(
                        glide.load(albumUi.thumbnailUrl)
                    )
                    .placeholder(circularProgressDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade(700))
                    .into(albumImg)
            }
        }

        private fun setFavoriteIcon(albumUi: AlbumUi,withoutAnimation : Boolean = false) {
            if (albumUi.isFavorite)
                binding.favoriteView.animateIconAddToFavorite(R.drawable.ic_favorite_star_on, withoutAnimation = withoutAnimation)
            else
                binding.favoriteView.animateIconDeleteFavorite(R.drawable.ic_favorite_star_off, withoutAnimation = withoutAnimation)
        }


    }
}


enum class PayLoad {
    FAVORITE_CHANGE
}

