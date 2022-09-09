package cc.fastcv.filemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileSystemOperationsActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "FileSystemOp"

        fun intoActivity(context: Context,filePath:String) {
            val intent = Intent(context,FileSystemOperationsActivity::class.java)
            intent.putExtra("filePath",filePath)
            context.startActivity(intent)
        }
    }

    private lateinit var root:File
    private lateinit var currentParentFile:File
    private lateinit var adapter: FileAdapter
    private lateinit var tvPath: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_op)

        val rvFile = findViewById<RecyclerView>(R.id.rv_file)
        tvPath = findViewById(R.id.tv_path)

        val filePath = intent.getStringExtra("filePath")
        Log.d(TAG, "onCreate: filePath = $filePath")

        if (filePath == null) {
            showExceptionDialogAndExits("传入路径为空，将退出此界面！！")
            return
        }

        //默认文件夹一定存在
        root = File(filePath)
        val listFiles = root.listFiles()?.asList()?.toMutableList()

        if (listFiles == null) {
            showExceptionDialogAndExits("文件夹访问异常，将退出此界面！！")
            return
        }

        currentParentFile = root
        tvPath.text = currentParentFile.absolutePath
        adapter = FileAdapter(listFiles)
        rvFile.adapter = adapter
        adapter.itemClickListener = object : OnItemClickListener<File> {
            override fun onItemClickListener(t: File, position: Int) {
                goDir(t)
            }
        }

        adapter.itemLongClickListener = object : OnItemLongClickListener<File> {
            override fun onItemLongClickListener(t: File, position: Int): Boolean {
                deleteFileOrFolder(t)
                return true
            }
        }
    }

    private fun deleteFileOrFolder(t: File) {
        val msg = "确认删除此${if (t.isDirectory) "文件夹" else "文件"}？"
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage(msg)
            .setPositiveButton("确定"
            ) { _, _ ->
                if (t.delete()) {
                    Toast.makeText(this,"${if (t.isDirectory) "文件夹" else "文件"}删除成功",Toast.LENGTH_SHORT).show()
                    goDir(currentParentFile)
                } else {
                    Toast.makeText(this,"${if (t.isDirectory) "文件夹" else "文件"}删除失败",Toast.LENGTH_SHORT).show()
                }
            }
            .create().show()
    }

    private fun showExceptionDialogAndExits(msg:String) {
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("确定"
            ) { _, _ -> finish()}
            .create().show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBackPressed() {
        val parentFile = currentParentFile.parentFile
        goDir(parentFile)
    }

    private fun goDir(file: File) {
        val listFiles = file.listFiles()?.asList()?.toMutableList()

        if (listFiles == null) {
            Toast.makeText(this,"访问的目录不存在或无法访问",Toast.LENGTH_SHORT).show()
            return
        }

        currentParentFile = file
        tvPath.text = currentParentFile.absolutePath
        adapter.list.clear()
        adapter.list.addAll(listFiles)
        adapter.notifyDataSetChanged()
    }

    fun goHome(v:View) {
        finish()
    }

    fun newFolder(v: View) {
        val newFolder = File(currentParentFile,"folder${adapter.list.size}")
        if (newFolder.mkdir()) {
            Toast.makeText(this,"文件夹创建成功！！",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"文件夹创建失败！！",Toast.LENGTH_SHORT).show()
        }
        goDir(currentParentFile)
    }

    fun newFile(v: View) {
        val newFile = File(currentParentFile,"folder${adapter.list.size}.txt")
        if (newFile.createNewFile()) {
            Toast.makeText(this,"文件创建成功！！",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"文件创建失败！！",Toast.LENGTH_SHORT).show()
        }
        goDir(currentParentFile)
    }
}