package com.task.noteapp.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.task.noteapp.R
import com.task.noteapp.ui.customView.RoundedImageView.Companion.radius

data class RoundedImageViewDataModel(
    val cornerRadius: Float? = null
)

private fun readAttr(context: Context, attrs: AttributeSet?): RoundedImageViewDataModel {
    val attributes =
        context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, 0, 0)
    val cornerRadius: Float?


    return try {
        attributes.run {
            cornerRadius = getDimension(
                R.styleable.RoundedImageView_cornerRadius,
                radius
            )
        }

        return RoundedImageViewDataModel(
            cornerRadius
        )

    } catch (e: Exception) {
        RoundedImageViewDataModel()
    } finally {
        attributes.recycle()
    }
}

class RoundedImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var cornerRadiusFromAttr: Float? = null

    init {
        setAttributes(context, attrs)
    }

    private fun setAttributes(context: Context, attrs: AttributeSet?) {
        val data = readAttr(context, attrs)
        data.apply {
            cornerRadiusFromAttr = this.cornerRadius
        }
    }


    override fun onDraw(canvas: Canvas) {
        @SuppressLint("DrawAllocation") val clipPath = Path()
        @SuppressLint("DrawAllocation") val rect = RectF(
            0f, 0f, this.width.toFloat(), this.height.toFloat()
        )
        clipPath.addRoundRect(
            rect,
            cornerRadiusFromAttr ?: radius,
            cornerRadiusFromAttr ?: radius,
            Path.Direction.CW
        )
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    companion object {
        var radius = 10.0f
    }
}