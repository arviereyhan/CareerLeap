package com.example.carrerleap.ui.homescreen.ui.buttons

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.carrerleap.R

class LogoutButton: AppCompatButton {

    private lateinit var buttonBackground: Drawable
    private lateinit var buttonPencil: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = buttonBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        canvas.drawText("LOGOUT", (width/2f)-30f, 106.5f, paint)

        buttonPencil.setBounds(30, 60, 110, 130)
        buttonPencil.draw(canvas)

    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.white)
        buttonBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        buttonPencil = ContextCompat.getDrawable(context, R.drawable.logout_button) as Drawable
    }
}