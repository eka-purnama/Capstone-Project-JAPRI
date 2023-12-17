package com.android.japri.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.android.japri.R

class EditTextPhoneNumber : AppCompatEditText {

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
                    !s.toString().startsWith("0") -> {
                        context.getString(R.string.wrong_phone_number_format)
                    }
                    s.length < 10 -> {
                        context.getString(R.string.not_enough_digit)
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