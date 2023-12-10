package com.android.japri.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class DateTimeTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s.isNullOrEmpty()) {
//                    error = context.getString(R.string.empty_text_view)
//                    setErrorTextAndColor(error as String, context.getColor(R.color.orange))
//                }
//                error = context.resources.getString(R.string.empty_text_view)

            }

            override fun afterTextChanged(s: Editable?) {
                // Do Nothing
            }
        })
    }

    fun setErrorTextAndColor(errorText: String, errorColorResId: Int) {
        text = errorText
        setTextColor(ContextCompat.getColor(context, errorColorResId))
    }
}