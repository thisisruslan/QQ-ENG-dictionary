package uz.gita.myqqeng

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_definition.*
import java.util.*

class DefinitionActivity : AppCompatActivity(){
    private var _word: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definition)
        val bundle = intent.extras

        _word = bundle!!.getString("word", "Sóz tabılmadı")
        var definition = bundle.getString("definition", "Sóz tabılmadı").replace("\\s+".toRegex(), " ")

        Log.i("OOOO", "1->$definition")
        definition = definition.replace("2)", "<br /><br />2)")
        definition = definition.replace("3)", "<br /><br />3)")
        definition = definition.replace("4)", "<br /><br />4)")
        Log.i("OOOO", "2->$definition")
        word.text = _word
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            translate.text = Html.fromHtml(definition, Html.FROM_HTML_MODE_COMPACT)
        } else Html.fromHtml(definition)

        share_definition.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val text = "$_word ->\n $definition"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Bólisiw:"))
        }

        backHome.setOnClickListener {
            finish()
        }
    }





}