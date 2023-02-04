package uz.gita.myqqeng.utils

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import uz.gita.myqqeng.app.App

fun color(@ColorRes colorResID : Int) : Int =
    ContextCompat.getColor(App.instance, colorResID)


/*
infix fun String.spannableText(query: String): SpannableString {
    val spanBuild = SpannableString(this)
    var startPos = -1
    var endPos = -1

    val fcs = ForegroundColorSpan(App.instance.resources.getColor(R.color.colorAccent))
    startPos = this.indexOf(query, 0, ignoreCase = true)
    endPos = startPos + query.length
    if (startPos > -1) {
        spanBuild.setSpan(fcs, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spanBuild
}*/
