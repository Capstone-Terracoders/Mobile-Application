package com.terracode.blueharvest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class HeightGaugeActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val lightGreyColor = ContextCompat.getColor(context, R.color.lightGrey)
    private val blackColor = ContextCompat.getColor(context, R.color.black)

    private var currentHeight: Float = 6f // Default current height
    private var maxHeight: Float = 30f // Default maximum height

    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)

    private val barPaint = Paint().apply {
        color = blueBerryColor
        style = Paint.Style.FILL
    }

    private val lightGreyBarPaint = Paint().apply {
        color = lightGreyColor
        style = Paint.Style.FILL
    }

    private val blackOutlinePaint = Paint().apply {
        color = blackColor
        style = Paint.Style.STROKE
        strokeWidth = 4f // Increased stroke width for a bolder outline
    }

    private val textPaint = Paint().apply {
        color = blackColor
        textSize = 40f // Adjust text size as needed
        textAlign = Paint.Align.RIGHT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barWidth = width.toFloat() * 0.5f // Adjust as needed
        val barHeight = height.toFloat() * 0.8f // Adjust as needed
        val cornerRadius = barWidth * 0.2f // Adjust as needed

        val startX = (width - barWidth) / 2
        val startY = (height - barHeight) / 2

        // Draw rounded rectangle for blue bar
        canvas.drawRoundRect(
            startX, startY,
            startX + barWidth, startY + barHeight,
            cornerRadius, cornerRadius,
            barPaint
        )

        // Calculate position and dimensions of the horizontal bar
        val horizontalBarWidth = barWidth + 10 // Adjust as needed
        val horizontalBarHeight = 20f // Adjust as needed
        val horizontalBarX = startX - 5 // Centered with the vertical bar
        val horizontalBarY = startY + (barHeight - horizontalBarHeight) * (1 - currentHeight / maxHeight)

        // Draw rounded rectangle for horizontal bar
        canvas.drawRoundRect(
            horizontalBarX, horizontalBarY,
            horizontalBarX + horizontalBarWidth, horizontalBarY + horizontalBarHeight,
            cornerRadius, cornerRadius,
            lightGreyBarPaint
        )

        // Draw outline for horizontal bar
        canvas.drawRoundRect(
            horizontalBarX, horizontalBarY,
            horizontalBarX + horizontalBarWidth, horizontalBarY + horizontalBarHeight,
            cornerRadius, cornerRadius,
            blackOutlinePaint
        )

        // Draw labels
        val labelCount = (maxHeight / 5).toInt() + 1 // Number of labels including 0 and max height
        val labelSpacing = barHeight / (labelCount - 1) // Spacing between labels
        val labelTextSize = 40f // Adjust text size as needed
        val labelValueStep = maxHeight / (labelCount - 1) // Value step between labels

        for (i in 0 until labelCount) {
            val labelValue = labelValueStep * i
            val labelY = startY + barHeight - i * labelSpacing
            canvas.drawText(labelValue.toInt().toString(), startX - 20f, labelY + labelTextSize / 2, textPaint)
        }
    }
}






