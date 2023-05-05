package cc.fastcv.filemanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FileSystemActivity"
    }

    private val permissionsContract = ActivityResultContracts.RequestMultiplePermissions()

    private val permissionCallback =
        ActivityResultCallback<Map<String, @JvmSuppressWildcards Boolean>> { result ->
            Log.d("xcl_debug", "checkPermission: $result")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val builder = AlertDialog.Builder(this)
                        .setMessage("本程序需要您同意允许访问所有文件权限")
                        .setPositiveButton("确定") { _, _ ->
                            startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                        }
                    builder.show()
                }
            }
        }

    private var storeLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        permissionsContract, permissionCallback
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d(TAG, "onCreate: Android 12 ----------------------------")
            storeLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                )
            )
        } else {
            storeLauncher.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun intoInner(v: View) {
        FileSystemOperationsActivity.intoActivity(this, filesDir.absolutePath)
    }

    fun intoOuter(v: View) {
        if ((Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)) {
            val externalFilesDir = getExternalFilesDir(null)
            Log.d(TAG, "intoOuter: externalFilesDir = ${externalFilesDir?.absolutePath}")
            FileSystemOperationsActivity.intoActivity(this, externalFilesDir?.absolutePath ?: "")
        } else {
            Toast.makeText(this, "外部存储器不存在！！", Toast.LENGTH_SHORT).show()
        }
    }

    fun accessMediaFiles(v: View) {
        startActivity(Intent(this, ImageShowActivity::class.java))
    }

    fun accessOtherFiles(v: View) {
        if ((Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)) {
            val externalFilesDir = getExternalFilesDir(null)
            Log.d(TAG, "intoOuter: externalFilesDir = ${externalFilesDir?.absolutePath}")
            FileSystemOperationsActivity.intoActivity(
                this,
                Environment.getExternalStorageDirectory().absolutePath
            )
        } else {
            Toast.makeText(this, "外部存储器不存在！！", Toast.LENGTH_SHORT).show()
        }
    }

    private val retriever = MediaMetadataRetriever()

    fun accessVideoFiles(v: View) {
        GlobalScope.launch (Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED
            )


            val selection = "${MediaStore.Video.Media.DATE_ADDED} >= ?"

            val selectionArgs = arrayOf(
                // Release day of the G1. :)
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )


            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

            contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)

                Log.i("xcl_debug", "Found ${cursor.count} images")
                while (cursor.moveToNext()) {

                    // Here we'll use the column indexs that we found above.
                    val id = cursor.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    extractVideoLocationInfo(contentUri)

                    val image = MediaStoreImage(id, displayName, dateModified, contentUri,0.0,0.0)
                    Log.v("xcl_debug", "Added image: $image")
                }
            }
        }
    }

    private fun extractVideoLocationInfo(videoUri: Uri) {
        try {
            retriever.setDataSource(this, videoUri)
        } catch (e: RuntimeException) {
            Log.e("xcl_debug", "Cannot retrieve video file", e)
        }
        // Metadata should use a standardized format.
        val locationMetadata: String? =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)
        Log.d(TAG, "extractVideoLocationInfo: locationMetadata = $locationMetadata")
    }

    fun accessAudioFiles(v: View) {
        GlobalScope.launch (Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATE_ADDED
            )


            val selection = "${MediaStore.Audio.Media.DATE_ADDED} >= ?"

            val selectionArgs = arrayOf(
                // Release day of the G1. :)
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )


            val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

                Log.i("xcl_debug", "Found ${cursor.count} images")
                while (cursor.moveToNext()) {

                    // Here we'll use the column indexs that we found above.
                    val id = cursor.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val image = MediaStoreImage(id, displayName, dateModified, contentUri,0.0,0.0)
                    Log.v("xcl_debug", "Added image: $image")
                }
            }
        }
    }

    fun accessDownloadFiles(v: View) {
        GlobalScope.launch (Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.DATE_ADDED
            )


            val selection = "${MediaStore.Downloads.DATE_ADDED} >= ?"

            val selectionArgs = arrayOf(
                // Release day of the G1. :)
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )


            val sortOrder = "${MediaStore.Downloads.DATE_ADDED} DESC"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentResolver.query(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )?.use { cursor ->

                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                    val dateModifiedColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATE_ADDED)
                    val displayNameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)

                    Log.i("xcl_debug", "Found ${cursor.count} images")
                    while (cursor.moveToNext()) {

                        // Here we'll use the column indexs that we found above.
                        val id = cursor.getLong(idColumn)
                        val dateModified =
                            Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                        val displayName = cursor.getString(displayNameColumn)

                        val contentUri = ContentUris.withAppendedId(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            id
                        )

                        val image = MediaStoreImage(id, displayName, dateModified, contentUri,0.0,0.0)
                        Log.v("xcl_debug", "Added image: $image")
                    }
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }


    fun createFile(v: View) {
        createFile(Uri.fromFile(Environment.getRootDirectory()))
    }

    private val CREATE_FILE = 1

    private fun createFile(pickerInitialUri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "invoice.pdf")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
            startActivityForResult(intent, CREATE_FILE)
        }
    }

    fun openFile(view: View) {
        openFile(Uri.fromFile(Environment.getRootDirectory()))
    }

    private val PICK_PDF_FILE = 2

    fun openFile(pickerInitialUri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }

            startActivityForResult(intent, PICK_PDF_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE) {
            Log.d(TAG, "onActivityResult: CREATE_FILE ${data?.data}")
            val filePathFromContentUri = getFilePathFromContentUri(data?.data!!, contentResolver)
            Log.d(TAG, "onActivityResult: CREATE_FILE filePathFromContentUri = $filePathFromContentUri")
        } else if (requestCode == PICK_PDF_FILE) {
            Log.d(TAG, "onActivityResult: PICK_PDF_FILE ${data?.data}")
            val filePathFromContentUri = getFilePathFromContentUri(data?.data!!, contentResolver)
            Log.d(TAG, "onActivityResult: PICK_PDF_FILE filePathFromContentUri = $filePathFromContentUri")
        }
    }
}