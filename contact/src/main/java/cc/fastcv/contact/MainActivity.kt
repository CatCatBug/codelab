package cc.fastcv.contact

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.promeg.pinyinhelper.Pinyin
import java.util.*

class MainActivity : AppCompatActivity() {
    private val contacts = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                contacts.addAll(getAllContact())
                initView()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 1000)
            }
        } else {
            for (contact in getAllContact()) {
                Log.d("xcl_debug", "onRequestPermissionsResult: $contact")
            }
        }
    }

    private fun initView() {
        val rvContacts = findViewById<RecyclerView>(R.id.rv_contacts)
        rvContacts.adapter = ContactsAdapter(contacts, R.layout.item_contacts)

        val sideBar = findViewById<WaveSideBar>(R.id.side_bar)
        sideBar.setOnSelectIndexItemListener(object : OnSelectIndexItemListener {
            override fun onSelectIndexItem(index: String?) {
                for (i in contacts.indices) {
                    if (contacts[i].index == index) {
                        (rvContacts.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            i,
                            0
                        )
                        return
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            contacts.addAll(getAllContact())
            initView()
        }
    }

    private fun getAllContact(): List<Contact> {
        val listMembers: MutableList<Contact> = ArrayList<Contact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = contentResolver.query(
            uri, projection,
            null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY
        )
        cursor?.let {
            try {
                if (it.moveToFirst()) {
                    do {
                        var name = it.getString(0)

                        val phoneNumber = it.getString(1)
                        val sortKey: String = getSortKey(
                            Pinyin.toPinyin(
                                name!![0]
                            )
                        )
                        val contact = Contact(sortKey, name, phoneNumber)
                        if (TextUtils.isEmpty(name)) {
                            name = phoneNumber
                        }
                        if (name != null) listMembers.add(contact)
                    } while (it.moveToNext())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.close()
            }
        }

        return listMembers
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private fun getSortKey(sortKeyString: String): String {
        val key = sortKeyString.substring(0, 1).uppercase(Locale.ENGLISH)
        return if (key.matches(Regex("[A-Z]"))) {
            key
        } else "#"
    }
}