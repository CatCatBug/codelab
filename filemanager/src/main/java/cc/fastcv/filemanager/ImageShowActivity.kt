package cc.fastcv.filemanager

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pisto.fuckcode.file_system.ImageShowViewModel
import com.pisto.fuckcode.file_system.MediaStoreImage

class ImageShowActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ImageShowActivity"
    }

    private val viewModel: ImageShowViewModel by viewModels()

    private lateinit var rv : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iv_show)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val volumeNames: Set<String> = MediaStore.getExternalVolumeNames(this)
            Log.d(TAG, "onCreate: firstVolumeName = ${volumeNames.joinToString()}")
        }
        

        val galleryAdapter = GalleryAdapter { image ->
            Log.d(TAG, "onCreate: --------------- $image")

            //文件描述符
//            val readOnlyMode = "r"
//            contentResolver.openFileDescriptor(image.contentUri, readOnlyMode).use { pfd ->
//                // Perform operations on "pfd".
//            }

            //文件流
//            val resolver = applicationContext.contentResolver
//            resolver.openInputStream(image.contentUri).use { stream ->
//                // Perform operations on "stream".
//            }



        }

        rv = findViewById(R.id.gallery)
        rv.adapter = galleryAdapter

        viewModel.images.observe(this) { images ->
            galleryAdapter.submitList(images)
        }
        viewModel.loadImages()
    }


    private inner class GalleryAdapter(val onClick: (MediaStoreImage) -> Unit) :
        ListAdapter<MediaStoreImage, ImageViewHolder>(MediaStoreImage.DiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.gallery_layout, parent, false)
            return ImageViewHolder(view, onClick)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val mediaStoreImage = getItem(position)
            holder.rootView.tag = mediaStoreImage

            val thumbnail: Bitmap? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    applicationContext.contentResolver.loadThumbnail(
                        mediaStoreImage.contentUri, Size(640, 480), null)
                } else {
                    null
                }

            Glide.with(holder.imageView)
                .load(thumbnail?:mediaStoreImage.contentUri)
                .thumbnail(0.33f)
                .centerCrop()
                .into(holder.imageView)
        }
    }

}

/**
 * Basic [RecyclerView.ViewHolder] for our gallery.
 */
private class ImageViewHolder(view: View, onClick: (MediaStoreImage) -> Unit) :
    RecyclerView.ViewHolder(view) {
    val rootView = view
    val imageView: ImageView = view.findViewById(R.id.image)

    init {
        imageView.setOnClickListener {
            val image = rootView.tag as? MediaStoreImage ?: return@setOnClickListener
            onClick(image)
        }
    }
}