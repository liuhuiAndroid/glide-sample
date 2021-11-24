package com.glide.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class ImageLoader {
    private val requestManager: RequestManager

    private val requestOptions by lazy {
        RequestOptions()
    }

    constructor(activity: FragmentActivity) {
        requestManager = Glide.with(activity)
    }

    constructor(fragment: Fragment) {
        requestManager = Glide.with(fragment)
    }

    constructor(view: View) {
        requestManager = Glide.with(view)
    }

    constructor(context: Context) {
        requestManager = Glide.with(context)
    }

    fun display(
        view: ImageView,
        url: String?,
        placeholder: Int = 0,
        error: Int = 0,
        centerCrop: Boolean = false,
        radius: Int = 0,//圆角
        crossFade: Boolean = false,//缩放动画
        transformation: BitmapTransformation? = null,
        callback: ((Boolean) -> Unit)? = null
    ) {
        requestManager
            .load(url)
            .apply(
                requestOptions.autoClone()
                    .placeholder(placeholder)
                    .error(error)
                    .fallback(error)
            )
            .apply {
                if (crossFade) {
                    transition(DrawableTransitionOptions.withCrossFade())
                }
                val list = mutableListOf<BitmapTransformation>()
                if (centerCrop) {
                    list.add(CenterCrop())
                }
                if (transformation != null) {
                    list.add(transformation)
                }
                if (radius > 0) {
                    list.add(RoundedCorners(radius))
                }
                transform(*list.toTypedArray())
                if (callback != null) {
                    addCallback(callback)
                }
            }
            .into(view)
    }

    fun resources(view: ImageView, resourceId: Int) {
        requestManager.load(resourceId).into(view)
    }

    fun downloadBitmap(url: String, block: (Bitmap) -> Unit) {
        requestManager.asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                block.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    }

    private fun RequestBuilder<Drawable>.addCallback(block: (Boolean) -> Unit): RequestBuilder<Drawable> {
        return this.addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                block.invoke(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                block.invoke(true)
                return false
            }
        })
    }
}