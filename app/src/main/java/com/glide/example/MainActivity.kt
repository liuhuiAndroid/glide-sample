package com.glide.example

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation

/**
 * TODO ShapeableImageView
 */
class MainActivity : AppCompatActivity() {

    lateinit var image_view: AppCompatImageView
    val imageUrl =
        "https://www.baidu.com/img/bd_logo1.png"
        // "https://imgsa.baidu.com/forum/w%3D580/sign=b783c82d22a446237ecaa56aa8237246/aa66a7ec8a136327b8717064938fa0ec09fac707.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image_view = findViewById(R.id.image_view)
        Glide.with(this).load(imageUrl).preload()
    }

    fun loadImage(view: View) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher)
            // .override(Target.SIZE_ORIGINAL) // OOM risk
            .skipMemoryCache(false) // 禁用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 禁用硬盘缓存
            .transform(BlurTransformation(), GrayscaleTransformation())
            .centerCrop()

        Glide.with(this).load(imageUrl).apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean = false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean = false
            })
            .into(image_view)
    }
}