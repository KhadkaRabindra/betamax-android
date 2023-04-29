package com.picassos.betamax.android.presentation.app.movie.movies

import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.picassos.betamax.android.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.picassos.betamax.android.core.utilities.Helper
import com.picassos.betamax.android.domain.model.Movies
import com.picassos.betamax.android.domain.listener.OnMovieClickListener

class MoviesAdapter(private val isHorizontal: Boolean = false, private val onClickListener: OnMovieClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal class MoviesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.movie_title)
        val date: TextView = itemView.findViewById(R.id.movie_date)
        val thumbnail: SimpleDraweeView = itemView.findViewById(R.id.movie_thumbnail)

        fun setData(movie: Movies.Movie) {
            title.text = movie.title
            date.text = Helper.getFormattedDateString(movie.date, "yyyy")

            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(movie.thumbnail))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(true)
                .build()
            thumbnail.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(thumbnail.controller)
                .setControllerListener(object : BaseControllerListener<ImageInfo>() {
                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        imageRequest.sourceUri?.let { Fresco.getImagePipeline().evictFromMemoryCache(it) }
                    }
                })
                .build()
        }

        fun bind(item: Movies.Movie, onClickListener: OnMovieClickListener) {
            itemView.setOnClickListener {  onClickListener.onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when (isHorizontal) {
            false -> LayoutInflater.from(parent.context).inflate(R.layout.item_movie_vertical, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.item_movie_horizontal, parent, false)
        }
        return MoviesHolder(view)
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Movies.Movie>() {
        override fun areItemsTheSame(oldItem: Movies.Movie, newItem: Movies.Movie): Boolean {
           return oldItem.id == newItem.id
               && oldItem.title == newItem.title
               && oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: Movies.Movie, newItem: Movies.Movie): Boolean {
            return oldItem.id == newItem.id
                && oldItem.title == newItem.title
                && oldItem.description == newItem.description
        }
    })

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movies = differ.currentList[position]
        (holder as MoviesHolder).apply {
            setData(movies)
            bind(movies, onClickListener)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}