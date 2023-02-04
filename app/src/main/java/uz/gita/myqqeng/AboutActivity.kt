package uz.gita.myqqeng

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        backHome.setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.instagram_tv).setOnClickListener {
            val uri =
                Uri.parse("http://instagram.com/_u/ruslan_jumatov")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.instagram.android")
            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/ruslan_jumatov")
                    )
                )
            }
        }

        findViewById<View>(R.id.telegram_tv).setOnClickListener {
            val uri = Uri.parse("http://t.me/ruslan_jumatov")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.telegram.android")
            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://t.me/ruslan_jumatov")
                    )
                )
            }
        }


    }
}