package com.d121211017.stroyappsubmission.ui.custom

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import com.d121211017.stroyappsubmission.R
import com.google.android.material.textfield.TextInputEditText

class CustomEditTextView : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if(!text.isNullOrEmpty()){
            if(inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1 && text.length < 8) {
                error = context.getString(R.string.password_invalid)
            } else if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1 && !Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                error = context.getString(R.string.email_invalid)
            }
        }
    }
}