package uz.gita.myqqeng.repository

import android.database.Cursor
import uz.gita.myqqeng.data.database.MyDataBase
import uz.gita.myqqeng.data.model.DictionaryData

class AppRepository private constructor() {
    private val database = MyDataBase.getDatabase()

    companion object {
        private lateinit var instance: AppRepository
        fun getRepository(): AppRepository {
            if (!::instance.isInitialized) {
                instance = AppRepository()
            }
            return instance
        }
    }

    fun getDictionaryCursor(query: String): Cursor = database.getDictionaryCursor(query)

    fun getCursorByTranslation(query: String): Cursor = database.getCursorByTranslation(query)

    fun getFavouritesCursor(query: String): Cursor = database.getFavouritesCursor(query)

    fun updateFavourite(data: DictionaryData) = database.updateFavourite(data)

}