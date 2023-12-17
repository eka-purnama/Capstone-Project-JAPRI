package com.android.japri.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.android.japri.R

class EditTextName : AppCompatEditText {

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
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = when {
                    s.toString().isEmpty() -> {
                        context.getString(R.string.empty_input)
                    }
                    s.length < 2 -> {
                        context.getString(R.string.not_enough_name_digit)
                    }
                    !s.matches("[^0-9]+".toRegex()) -> {
                        context.getString(R.string.contain_number)
                    }
                    else -> {
                        null
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}