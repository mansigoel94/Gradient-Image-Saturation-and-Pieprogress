package com.example.mansigoel.gradientsaturation

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*


class MainActivity : AppCompatActivity(), AnkoLogger {
    var colorMatrix: ColorMatrix? = null
    var colorMatrixColorFilter: ColorMatrixColorFilter? = null
    var paint: Paint? = null

    var bitmapOriginal: Bitmap? = null
    var bitMapMutated: Bitmap? = null
    var canvas: Canvas? = null

    private var finalHeightGradient: Int? = 0
    private var finalWidthGradient: Int? = 0
    private var finalHeightSaturation: Int? = 0
    private var finalWidthSaturation: Int? = 0

    private val log = AnkoLogger<MainActivity>()
    private val logWithTag = AnkoLogger("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpSaturation()
        setUpPieChart()
        setUpGradient()
    }

    private fun generateRandomColorCodes(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setUpGradient() {
        val gd = GradientDrawable()
        gd.colors = intArrayOf(generateRandomColorCodes(),
                generateRandomColorCodes(), generateRandomColorCodes())

        // Set the GradientDrawable gradient type linear gradient and orientation
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.orientation = GradientDrawable.Orientation.TL_BR
        gd.shape = GradientDrawable.RECTANGLE
//        gd.setStroke(3, Color.BLUE)

        val vto = imageViewGradient.getViewTreeObserver()
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageViewGradient.getViewTreeObserver().removeOnPreDrawListener(this)
                finalHeightGradient = imageViewGradient.getMeasuredHeight()
                finalWidthGradient = imageViewGradient.getMeasuredWidth()
                info { "final width /*${finalWidthGradient} and height ${finalHeightGradient}*/" }

                gd.setSize(finalWidthGradient as Int, finalHeightGradient as Int)
                // Set GradientDrawable as ImageView source image
                imageViewGradient.setImageDrawable(gd)
                return true
            }
        })

        seekBar_gradient.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                gd.colors = intArrayOf(generateRandomColorCodes(), generateRandomColorCodes(), generateRandomColorCodes())
                imageViewGradient.setImageDrawable(gd)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun setUpSaturation() {
        imageViewGradient.setOnClickListener {

        }
        bitmapOriginal = BitmapFactory.decodeResource(resources, R.drawable.forest)
        colorMatrix = ColorMatrix()
        colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        paint = Paint()
        paint!!.colorFilter = colorMatrixColorFilter

        val vto = imageView.getViewTreeObserver()
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this)
                finalHeightSaturation = imageView.getMeasuredHeight()
                finalWidthSaturation = imageView.getMeasuredWidth()
                info { "final width /*${finalWidthSaturation} and height ${finalHeightSaturation}*/" }
                bitMapMutated = Bitmap.createBitmap(finalWidthSaturation as Int,
                        finalHeightGradient as Int,
                        Bitmap.Config.ARGB_8888)
                canvas = Canvas(bitMapMutated)

                //set initial saturation
                colorMatrix!!.setSaturation(seekBar_saturation.progress / 100.toFloat())
                colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
                paint!!.setColorFilter(colorMatrixColorFilter)
                canvas!!.drawBitmap(bitmapOriginal, 0f, 0f, paint)
                imageView.setImageBitmap(bitMapMutated)
                return true
            }
        })

        seekBar_saturation.keyProgressIncrement = 1

        seekBar_saturation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                colorMatrix!!.setSaturation(progress / 100.toFloat())
                colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
                paint!!.colorFilter = colorMatrixColorFilter

                canvas!!.drawBitmap(bitmapOriginal, 0f, 0f, paint)
                imageView.setImageBitmap(bitMapMutated)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun setUpPieChart() {

        progress_pie.progress = seekBar_pie.progress

        seekBar_pie.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                progress_pie.progress = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }
}

