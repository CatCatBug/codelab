package cc.fastcv.filemanager

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


//fun getPath(uri: Uri,context: Context) {
//    if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme,true)) {
//        val contentResolver = context.contentResolver
//        val cursor = contentResolver.query(uri, null, null, null, null)
//        cursor?.let {
//            if (it.moveToFirst()) {
//
//            }
//            it.close();
//        }
//    }
//}

fun getFilePathFromContentUri(
    selectedVideoUri: Uri,
    contentResolver: ContentResolver
): String {
    var filePath = ""
    val filePathColumn = arrayOf<String>(MediaStore.MediaColumns.DATA)
    val cursor: Cursor? =
        contentResolver.query(selectedVideoUri, filePathColumn, null, null, null)
    cursor?.let {
        if (it.moveToFirst()) {
            val columnIndex: Int = it.getColumnIndex(filePathColumn[0])
            filePath = it.getString(columnIndex)
        }
        it.close()
    }
    return filePath
}
