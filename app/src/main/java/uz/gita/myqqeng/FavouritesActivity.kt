package uz.gita.myqqeng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favourites.*
import uz.gita.myqqeng.adapter.FavouritesAdapter
import uz.gita.myqqeng.repository.AppRepository

class FavouritesActivity : AppCompatActivity() {
    private val repository = AppRepository.getRepository()
    private lateinit var adapter: FavouritesAdapter
    private var querySt = ""
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        if (repository.getFavouritesCursor(querySt).count ==0) {
            empty_view.visibility = View.VISIBLE
        } else empty_view.visibility = View.GONE

        adapter = FavouritesAdapter(repository.getFavouritesCursor(querySt), querySt)
        dictionaryList.adapter = adapter
        dictionaryList.layoutManager = LinearLayoutManager(this)

        adapter.setClickFavouriteListener {
            repository.updateFavourite(it)
            adapter.cursor = repository.getFavouritesCursor(querySt)
            adapter.notifyDataSetChanged()
        }

        backHome.setOnClickListener {
            finish()
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

        handler = Handler(Looper.getMainLooper())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    querySt = it.trim()
                    adapter.cursor = repository.getFavouritesCursor(querySt)

                    if (adapter.cursor.count == 0) {
                        no_found_view.visibility = View.VISIBLE
                    } else {
                        no_found_view.visibility = View.GONE
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
                        adapter.cursor = repository.getFavouritesCursor(querySt)
                        if (adapter.cursor.count == 0) {
                            no_found_view.visibility = View.VISIBLE
                        } else {
                            no_found_view.visibility = View.GONE
                        }
                        adapter.query = querySt
                        adapter.notifyDataSetChanged()
                        searchView.setQuery(querySt, false)
                    }
                }, 400)
                return true
            }
        })

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            searchView.setQuery(null, false)
            searchView.clearFocus()
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
}