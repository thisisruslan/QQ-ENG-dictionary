package uz.gita.myqqeng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ttsactivity.*
import java.util.*

class TTSActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ttsactivity)

        tts = TextToSpeech(this, this)
        backHomeTTS.setOnClickListener {
            finish()
        }

        btn_speak.setOnClickListener { speakOut() }

    }

    private fun speakOut() {
        val text = et_word.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Keshirersiz, sizdiń telefonıńız ushın bul funkcionallıq islemeydi", Toast.LENGTH_LONG).show()
            } else {
                btn_speak!!.isEnabled = true
            }
        } else {
            Toast.makeText(this, "Internet penen baylanıslı mashqala boldı!", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}