package cc.fastcv.standard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cc.fastcv.standard.ui.main.MainFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Gson().toJson(Any())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}