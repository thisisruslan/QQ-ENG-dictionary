package uz.gita.myqqeng

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.gita.myqqeng.data.database.MyDataBase
import uz.gita.myqqeng.data.pref.SharePref

class SplashLoadActivity : AppCompatActivity() {
    private val pref = SharePref.getSharePref()
    private val scope by lazy { CoroutineScope(Dispatchers.Main) }
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_load)

        val progressBar = findViewById<ProgressBar>(R.id.spin_kit)
        val wave: Sprite = Wave()
        progressBar.indeterminateDrawable = wave

        if (pref.isFirstRunning) {
            //TODO add handler
            scope.launch(Dispatchers.IO) {
                MyDataBase.setFinishListener {
                    val intent = Intent(this@SplashLoadActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                MyDataBase.getDatabase()
            }
            pref.isFirstRunning = false
        } else {
            handler.postDelayed({
                val intent = Intent(this@SplashLoadActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1600)

        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacksAndMessages(null)
        finish()
    }


}