package uz.gita.myqqeng

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_event.*

class EventDialog : BottomSheetDialogFragment() {
    private var copyTranslateListener : (() -> Unit)? =null
    private var shareListener : (() -> Unit)? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_event,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

         copy_translate.setOnClickListener {
            copyTranslateListener?.invoke()
            dismiss()
        }


        share.setOnClickListener {
            shareListener?.invoke()
            dismiss()
        }
    }



    fun setCopyTranslateListener(f: () -> Unit) {
        copyTranslateListener = f
    }

    fun setShareListener(f : () -> Unit) {
        shareListener = f
    }
}