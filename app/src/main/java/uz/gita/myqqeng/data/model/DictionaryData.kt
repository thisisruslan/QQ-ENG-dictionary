package uz.gita.myqqeng.data.model

data class DictionaryData(
    val id :Int,
    val word: String,
    val translation: String,
    var isFavourite: Int
)
