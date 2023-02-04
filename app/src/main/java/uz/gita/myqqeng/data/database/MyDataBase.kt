package uz.gita.myqqeng.data.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import uz.gita.myqqeng.app.App
import uz.gita.myqqeng.data.model.DictionaryData

class MyDataBase private constructor(context: Context, listener: EventListener) :
    DBHelper(context, "Dictionary.db", 1, listener) {

    companion object {
        private var finishListener: (() -> Unit)? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: MyDataBase

        fun getDatabase(): MyDataBase {
            if (!::instance.isInitialized) {
                instance = MyDataBase(App.instance) {
                    finishListener?.invoke()
                }
            }
            return instance
        }

        fun setFinishListener(f: () -> Unit) {
            finishListener = f
        }
    }

    fun updateFavourite(data: DictionaryData) {
        val cv = ContentValues()
        if (data.isFavourite == 0) cv.put("isFavourite", 1)
        else cv.put("isFavourite", 0)
        instance.database().update("mytable", cv, "mytable.id = ${data.id}", null)
    }

    fun getDictionaryCursor(query: String): Cursor {
        return instance.database()
            .rawQuery("SELECT * FROM mytable WHERE mytable.word LIKE '%$query%'", null)
    }

    fun getCursorByTranslation(query: String): Cursor {
        return instance.database()
            .rawQuery("SELECT * FROM mytable WHERE mytable.translation LIKE '%$query%'", null)
    }

    fun getFavouritesCursor(query: String): Cursor {
        return instance.database().rawQuery(
            "SELECT * FROM mytable WHERE mytable.isFavourite = 1 AND mytable.word LIKE '%$query%'",
            null
        )
    }
}