package ru.ok.itmo.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View

class ChatAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
)
    : View(context, attrs,defStyleAttr) {

    private var initials = ""
    private val textPaint = Paint()
    private val backgroundPaint = Paint()
    private var bitmap: Bitmap? = null

    companion object {
        private val defaultBackGroundColor = arrayOf(
            Color.BLACK, Color.BLUE, Color.CYAN,
            Color.DKGRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA,
            Color.WHITE, Color.YELLOW
        )
    }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ChatAvatarView)

            backgroundPaint.color = a.getColor(
                R.styleable.ChatAvatarView_cav_backgroundColor,
                defaultBackGroundColor.random()
            )

            textPaint.color = a.getColor(
                R.styleable.ChatAvatarView_cav_textColor,
                Color.WHITE
            )
        } else {
            backgroundPaint.color = defaultBackGroundColor.random()
            textPaint.color = Color.WHITE
        }
    }

    override fun onDraw(canvas: Canvas) {
        val x = width / 2f
        val y = height / 2f
        val radius = x.coerceAtMost(y)
        val bitmap = this.bitmap
        if (bitmap == null) {
            textPaint.textSize = radius
            canvas.drawCircle(x, y, radius, backgroundPaint)
            canvas.drawText(initials, x - radius * 3 * initials.length / 8, y + radius / 2, textPaint)
        } else {
            bitmap.width = width.coerceAtMost(height)
            bitmap.height = width.coerceAtMost(height)
            backgroundPaint.isFilterBitmap = true
            canvas.drawBitmap(bitmap,
                x - radius,
                y - radius,
                backgroundPaint)
        }
    }

    fun setChatName(name: String) {
        var firstNotWhiteSpace = true
        for (symbol in name) {
            if (Character.isWhitespace(symbol)) {
                firstNotWhiteSpace = true
            } else if (firstNotWhiteSpace) {
                initials += symbol
                if (initials.length == 2) {
                    break
                }
                firstNotWhiteSpace = false
            }
        }
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    override fun setBackgroundColor(color: Int) {
        backgroundPaint.color = color
    }

    fun setTextColor(color: Int) {
        textPaint.color = color
    }
}