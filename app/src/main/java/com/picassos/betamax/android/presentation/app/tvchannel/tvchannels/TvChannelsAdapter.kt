package com.picassos.betamax.android.presentation.app.tvchannel.tvchannels

import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.picassos.betamax.android.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.picassos.betamax.android.domain.listener.OnTvChannelClickListener
import com.picassos.betamax.android.domain.model.TvChannels

class TvChannelsAdapter(private val onClickListener: OnTvChannelClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal class TvChannelsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: SimpleDraweeView = itemView.findViewById(R.id.tvchannel_thumbnail)
        val title: TextView = itemView.findViewById(R.id.tvchannel_title)

        fun setData(tvChannel: TvChannels.TvChannel) {
            title.text = tvChannel.title

            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(tvChannel.banner))
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

        fun bind(item: TvChannels.TvChannel, onClickListener: OnTvChannelClickListener) {
            itemView.setOnClickListener {
                onClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tvchannel_vertical, parent, false)
        return TvChannelsHolder(view)
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<TvChannels.TvChannel>() {
        override fun areItemsTheSame(oldItem: TvChannels.TvChannel, newItem: TvChannels.TvChannel): Boolean {
           return oldItem.id == newItem.id
               && oldItem.title == newItem.title
               && oldItem.banner == newItem.banner
        }

        override fun areContentsTheSame(oldItem: TvChannels.TvChannel, newItem: TvChannels.TvChannel): Boolean {
            return oldItem.id == newItem.id
                && oldItem.title == newItem.title
                && oldItem.banner == newItem.banner
        }
    })

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvChannels = differ.currentList[position]
        (holder as TvChannelsHolder).apply {
            setData(tvChannels)
            bind(tvChannels, onClickListener)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}