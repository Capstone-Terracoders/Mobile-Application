package com.terracode.blueharvest

import Notifications
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.PreferenceManager
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Suppress("PrivatePropertyName")
class RpmSpeedometerActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //Initialize Data
    init {
        //Initialize Preference Manager
        PreferenceManager.init(context)
    }

    //Constants
    //Default widths
    private val DEFAULT_STROKE_WIDTH = 20f
    private val DEFAULT_NEEDLE_WIDTH = 10f

    //Offsets for adjusting length of needle
    private val NEEDLE_LENGTH_OFFSET = 40f

    //Radius offset for placing labels outside the speedometer
    private val LABEL_OFFSET = 60f

    //Length of the bigger ticks (will also impact size of small ticks)
    private val BIG_TICK_LENGTH = 40f
    private val BIG_TICK_WIDTH = 15f

    //Angles of arc
    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f

    //Number of ticks - both small and big
    private var NUM_BIG_TICKS = 6
    private var NUM_SMALL_TICKS = 5

    //Animation duration in milliseconds
    private var ANIMATION_DURATION = 1000L

    //Data
    private var currentRpm = PreferenceManager.getRpm()
    private var optimalRpm = PreferenceManager.getOptimalRakeRpm()
    private var maxRpmDisplayed = PreferenceManager.getMaxRPMDisplayedInput()
    private var maxRakeRpm = PreferenceManager.getMaxRakeRPMInput()
    private var optimalRpmRange = PreferenceManager.getOptimalRPMRangeInput()

    //Colors
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val blackColor = ContextCompat.getColor(context, R.color.black)
    private val redColor = ContextCompat.getColor(context, R.color.red)
    private val greenColor = ContextCompat.getColor(context, R.color.green)

    //Labels
    private val normalTextSize = PreferenceManager.getSelectedTextSize()

    //Notifications
    private val maxRPMDisplayedReachedNotification = Notifications.getMaxRPMDisplayedReachedNotification(context)
    private val maxRPMReachedNotification = Notifications.getMaxRPMReachedNotification(context)
    private val rpmBelowZeroNotification = Notifications.getRpmBelowZeroNotification(context)

    //Animator
    private var needleRotationAnimator: ObjectAnimator? = null

    // Paint objects
    private val dialAndTickPaint = Paint().apply {
        color = blueBerryColor
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val minRpmPaint = Paint().apply {
        color = redColor
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val optimalRpmPaint = Paint().apply {
        color = greenColor
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val needlePaint = Paint().apply {
        color = blackColor
        style =
            Paint.Style.FILL_AND_STROKE // Use FILL_AND_STROKE to fill the shape and draw its outline
        strokeWidth = DEFAULT_NEEDLE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to create rounded ends
    }

    private val tickLabelTextPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = normalTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    //Initializer for Drawing Speedometer
    init {
        //Logic for notifications:
        if (currentRpm!! > maxRpmDisplayed) {
            currentRpm = maxRpmDisplayed.toDouble()
            //Allows us to continue seeing the preview in split mode
            if (!isInEditMode) {
                PreferenceManager.setNotification(maxRPMDisplayedReachedNotification)
            }
        } else if (currentRpm!! > maxRakeRpm) {
            if (!isInEditMode) {
                PreferenceManager.setNotification(maxRPMReachedNotification)
            }
        } else if (currentRpm!! < 0) {
            if (!isInEditMode) {
                currentRpm = 0.0
                PreferenceManager.setNotification(rpmBelowZeroNotification)
            }
        }

        if (maxRakeRpm > maxRpmDisplayed){
            maxRakeRpm = maxRpmDisplayed
        }

        //Needle Animation
        needleRotationAnimator = ObjectAnimator.ofFloat(
            this,
            "needleRotation",
            currentRpm!!.toFloat()
        ).apply {
            duration = ANIMATION_DURATION // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
            repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
        }
    }


    override fun onDraw(canvas: Canvas) {
        //Draw canvas
        super.onDraw(canvas)

        // Declare & initialize ark parameters:
        val radiusOffset = 0.7f
        val centerXCoordinate = width / 2f      //X-Coordinate of the center of the activity page
        val centerYCoordinate = height / 2f     //Y-Coordinate of the center of the activity page
        val radius = min(centerXCoordinate, centerYCoordinate) * radiusOffset

        //Create top-left coordinates of the arc
        val arcStartXCoordinate = centerXCoordinate - radius
        val arcStartYCoordinate = centerYCoordinate - radius

        //Create the bottom-right coordinates of the arc
        val arcEndXCoordinate = centerXCoordinate + radius
        val arcEndYCoordinate = centerYCoordinate + radius

        //Draw the arc for the speedometer
        canvas.drawArc(
            arcStartXCoordinate,
            arcStartYCoordinate,
            arcEndXCoordinate,
            arcEndYCoordinate,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            dialAndTickPaint
        )

        //Function to draw the speedometer ticks and labels for RPM
        drawSpeedometerTicksAndLabels(canvas,
            centerXCoordinate,
            centerYCoordinate,
            radius)

        //Draw arc for optimal rpm
        var lowerRange = optimalRpm!!.toFloat() - optimalRpmRange
        var upperRange = optimalRpm!!.toFloat() + optimalRpmRange

        if (lowerRange < 0){
            lowerRange = 0f
        }

        if (upperRange > maxRakeRpm){
            upperRange = maxRakeRpm.toFloat()
        }

        val optimalRangeValueRatioStart = (lowerRange) / maxRpmDisplayed.toFloat()
        val optimalRangeValueRatioEnd = (upperRange) / maxRpmDisplayed.toFloat()

        val optimalRpmStartAngle = START_ANGLE + (optimalRangeValueRatioStart) * SWEEP_ANGLE
        val optimalRpmEndAngle = (START_ANGLE + (optimalRangeValueRatioEnd) * SWEEP_ANGLE)
        val finalRangeAngle = optimalRpmEndAngle - optimalRpmStartAngle

        canvas.drawArc(
            arcStartXCoordinate,
            arcStartYCoordinate,
            arcEndXCoordinate,
            arcEndYCoordinate,
            optimalRpmStartAngle,
            finalRangeAngle,
            false,
            optimalRpmPaint
        )

    //Draw arc for safety max rpm
        val safetyValueRatio = maxRakeRpm.toFloat() / maxRpmDisplayed.toFloat()
        val maxRpmStartAngle = START_ANGLE + (safetyValueRatio) * SWEEP_ANGLE
        val maxRpmEndAngle = (START_ANGLE + SWEEP_ANGLE) - maxRpmStartAngle

        canvas.drawArc(
            arcStartXCoordinate,
            arcStartYCoordinate,
            arcEndXCoordinate,
            arcEndYCoordinate,
            maxRpmStartAngle,
            maxRpmEndAngle,
            false,
            minRpmPaint
        )

        //Sets the angle of the needle based off the currentSpeed and handles currentRpm > maxRpm
        val angle = if (maxRpmDisplayed != 0 && currentRpm != null) {
            START_ANGLE + (currentRpm!! / maxRpmDisplayed) * SWEEP_ANGLE
        } else {
            0.0
        }

        //Sets up needle length/ending coordinates
        val needleLength = radius - NEEDLE_LENGTH_OFFSET
        val needleLengthXCoordinate = centerXCoordinate + needleLength * cos(Math.toRadians(angle)).toFloat()
        val needleLengthYCoordinate = centerYCoordinate + needleLength * sin(Math.toRadians(angle)).toFloat()

        //Draw the speedometer needle
        canvas.drawLine(
            centerXCoordinate,
            centerYCoordinate,
            needleLengthXCoordinate,
            needleLengthYCoordinate,
            needlePaint)

        needleRotationAnimator?.start()
    }


    private fun drawSpeedometerTicksAndLabels(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        //Declare the interval between each tick
        val angleInterval = SWEEP_ANGLE / ((NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS)

        //Declare small tick length && width
        val smallTickLength = BIG_TICK_LENGTH / 2
        val smallTickWidth = BIG_TICK_WIDTH / 3

        //Declare needle length
        val needleLength = radius - NEEDLE_LENGTH_OFFSET

        //Create all the ticks
        for (tick in 0 until (NUM_BIG_TICKS * NUM_SMALL_TICKS) - (NUM_SMALL_TICKS-1)) {
            //Declare if tick is big or small based off index
            val bigTickIndex = tick / NUM_SMALL_TICKS
            val smallTickIndex = tick % NUM_SMALL_TICKS
            val isBigTick = smallTickIndex == 0

            //Tick angle
            val angle = START_ANGLE + tick * angleInterval // Calculate the angle for each tick

            //Determine what tick length and width to use
            val tickLength = if (isBigTick) BIG_TICK_LENGTH else smallTickLength
            val tickWidth = if (isBigTick) BIG_TICK_WIDTH else smallTickWidth

            //Declare top-left coordinate of tick
            val startTickXCoordinate = centerX + (needleLength - tickLength) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val startTickYCoordinate = centerY + (needleLength - tickLength) * sin(Math.toRadians(angle.toDouble())).toFloat()

            //Declare bottom-right coordinate of tick
            val endTickXCoordinate = centerX + needleLength * cos(Math.toRadians(angle.toDouble())).toFloat()
            val endTickYCoordinate = centerY + needleLength * sin(Math.toRadians(angle.toDouble())).toFloat()

            //Draw the tick
            canvas.drawLine(
                startTickXCoordinate,
                startTickYCoordinate,
                endTickXCoordinate,
                endTickYCoordinate,
                dialAndTickPaint.apply {
                strokeWidth = tickWidth
            })

            // Draw label for big ticks only
            if (isBigTick) {
                //Calculate label position
                val labelRadius = radius + LABEL_OFFSET
                val labelXCoordinate = centerX + labelRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val labelYCoordinate = centerY + labelRadius * sin(Math.toRadians(angle.toDouble())).toFloat()

                //Generate label text
                val labelText = ((maxRpmDisplayed / 5) * bigTickIndex).toString()

                // Draw label
                canvas.drawText(
                    labelText,
                    labelXCoordinate,
                    labelYCoordinate,
                    tickLabelTextPaint)
            }
        }
    }
    @Suppress("unused")
    fun setNeedleRotation(rotation: Float) {
        invalidate()
    }
}


