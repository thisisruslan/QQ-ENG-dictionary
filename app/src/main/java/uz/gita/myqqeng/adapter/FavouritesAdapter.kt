package uz.gita.myqqeng.adapter

import android.database.Cursor
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_dictionary.view.*
import uz.gita.myqqeng.R
import uz.gita.myqqeng.data.model.DictionaryData
import uz.gita.myqqeng.utils.color

class FavouritesAdapter(var cursor: Cursor, var query: String) :
    RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {
    private var clickFavouriteListener: ((DictionaryData) -> Unit)? = null
    private var clickItemListener: ((DictionaryData) -> Unit)? = null

    inner class FavouritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                clickItemListener?.invoke(getDictionaryDataByPos(adapterPosition))
            }
            itemView.remember.setOnClickListener {
                val data = getDictionaryDataByPos(adapterPosition)
                if (data.isFavourite == 0) {
                    data.isFavourite = 1
                    it.remember.setImageResource(R.drawable.ic_bookmark_select)
                } else {
                    data.isFavourite = 0
                    it.remember.setImageResource(R.drawable.ic_bookmark)
                }
                clickFavouriteListener?.invoke(getDictionaryDataByPos(adapterPosition))
            }
        }

        fun bind(data: DictionaryData) {
            val spanSt = SpannableString(data.word)
            val foregroundColorSpan = ForegroundColorSpan(color(R.color.red))
            val startIndex = data.word.indexOf(query, 0, true)
            val endIndex = startIndex + query.length
            spanSt.setSpan(
                foregroundColorSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            itemView.title.text = spanSt
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.subTitle.text =
                    Html.fromHtml(data.translation, Html.FROM_HTML_MODE_COMPACT)
            } else Html.fromHtml(data.translation)
            if (data.isFavourite == 1)
                itemView.remember.setImageResource(R.drawable.ic_bookmark_select)
            else itemView.remember.setImageResource(R.drawable.ic_bookmark)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dictionary, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(getDictionaryDataByPos(position))
    }

    override fun getItemCount(): Int = cursor.count

    private fun getDictionaryDataByPos(pos: Int): DictionaryData {
        cursor.moveToPosition(pos)
        return DictionaryData(
            cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("word")),
            cursor.getString(cursor.getColumnIndex("translation")),
            cursor.getInt(cursor.getColumnIndex("isFavourite")),
        )
    }

    fun setClickFavouriteListener(f: (DictionaryData) -> Unit) {
        clickFavouriteListener = f
    }

    fun setClickItemListener(f: (DictionaryData) -> Unit) {
        clickItemListener = f
    }


}