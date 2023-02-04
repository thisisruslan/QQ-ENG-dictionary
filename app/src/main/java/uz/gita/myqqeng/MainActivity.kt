package uz.gita.myqqeng

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_nav.*
import uz.gita.myqqeng.adapter.CursorAdapter
import uz.gita.myqqeng.repository.AppRepository

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val repository = AppRepository.getRepository()
    private lateinit var adapter: CursorAdapter
    private var querySt = ""
    private lateinit var handler: Handler
    private val REQ_CODE_SPEECH_INPUT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        nav_view.setNavigationItemSelectedListener(this)

        imageHome.setOnClickListener {
            searchView.clearFocus()
            nav_drawer.openDrawer(GravityCompat.START)
        }

        adapter = CursorAdapter(repository.getDictionaryCursor(querySt), querySt)
        dictionaryList.adapter = adapter
        dictionaryList.layoutManager = LinearLayoutManager(this)

        adapter.setClickFavouriteListener {
            repository.updateFavourite(it)
            adapter.cursor = repository.getDictionaryCursor(querySt)
            adapter.notifyDataSetChanged()
        }

        //move to definition act.
        adapter.setClickItemListener {
            val intent = Intent(this, DefinitionActivity::class.java)
            val bundle = Bundle()
            bundle.putString("word", it.word)
            bundle.putString("definition", it.translation)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        //bottom sheet
        adapter.setListener { data ->
            val bottomDialog = EventDialog()
            bottomDialog.setCopyTranslateListener {
                val cm: ClipboardManager =
                    this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.text = "${data.word}\n ${
                    data.translation.replace(
                        "\\s+".toRegex(),
                        " "
                    )
                }"
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            bottomDialog.setShareListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val text = "${data.word}\n ${
                    data.translation.replace(
                        "\\s+".toRegex(),
                        " "
                    )
                }"
                intent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(intent, "Share:"))
            }
            bottomDialog.show(supportFragmentManager, "event")
        }

        handler = Handler(Looper.getMainLooper())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    querySt = it.trim()
                    if (querySt.isNotEmpty()) {
                        adapter.cursor = repository.getDictionaryCursor(querySt)
                        if (adapter.cursor.count == 0) {
                            no_found_view.visibility = View.VISIBLE
                        } else {
                            no_found_view.visibility = View.GONE
                        }
                    } else {
                        no_found_view.text.text = ""
                        no_found_view.visibility = View.VISIBLE
                    }
                    adapter.query = querySt
                    adapter.notifyDataSetChanged()
                    searchView.setQuery(querySt, false)
                }

                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        querySt = it.trim()
                        if (querySt.isNotEmpty()) {
                            adapter.cursor = repository.getDictionaryCursor(querySt)
                            if (adapter.cursor.count == 0) {
                                no_found_view.text.text = "Sóz tabılmadı"
                                no_found_view.visibility = View.VISIBLE
                            } else {
                                no_found_view.visibility = View.GONE
                            }

                        } else {
                            no_found_view.text.text = ""
                            no_found_view.visibility = View.VISIBLE
                        }
                        adapter.query = querySt
                        adapter.notifyDataSetChanged()
                        searchView.setQuery(querySt, false)
                    }
                }, 500)
                return true
            }

        })

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            searchView.setQuery(null, false)
//            searchView.clearFocus()
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }


    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-EN")

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, "Keshirersiz, sizdiń telefonıńız ushın bul funkcionallıq islemeydi",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val message = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (message != null) {
//                    adapter.cursor = repository.getCursorByTranslation(message[0])
//                    adapter.notifyDataSetChanged()
//                    searchView.setQuery(message[0], false);
                }
            }
        }

    }

    fun updateResults(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        adapter.cursor = repository.getDictionaryCursor(querySt)
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val text = """
             https://play.google.com/store/apps/details?id=${applicationContext.packageName}
             """.trimIndent()
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Bólisiw:"))
    }

    private fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=${applicationContext.packageName}")
        startActivity(intent)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tts -> {
                nav_drawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this@MainActivity,  TTSActivity::class.java)
                startActivity(intent)
            }
            R.id.share -> {
                shareApp()
            }

            R.id.rate -> {
                rateApp()
            }

            R.id.favourites -> {
                nav_drawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this@MainActivity, FavouritesActivity::class.java)
                startActivity(intent)
            }

            R.id.about -> {
                nav_drawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.exit -> {
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (nav_drawer.isDrawerOpen(GravityCompat.START)) {
            nav_drawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }


}