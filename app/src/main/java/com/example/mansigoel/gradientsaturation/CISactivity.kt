package com.example.mansigoel.gradientsaturation

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.mansigoel.gradientsaturation.R

class CISactivity : Activity() {
    //An ImageView and a SeekBar
    private var ivImage: ImageView? = null
    private var sbSeekBar: SeekBar? = null

    //These member variables responsible for modifying the color saturation
    private var colorMatrix: ColorMatrix? = null
    private var cmFilter: ColorMatrixColorFilter? = null
    private var cmPaint: Paint? = null

    //These member variables are responsible for drawing the Bitmap
    private var cv: Canvas? = null
    private var imgBitmap: Bitmap? = null
    private var canvasBitmap: Bitmap? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cisactivity)

        //Initialize the ColorMatrix object
        colorMatrix = ColorMatrix()
        //Initialize the ColorMatrixColorFilter object
        cmFilter = ColorMatrixColorFilter(this.colorMatrix!!)

        //Initialize the cmPaint
        cmPaint = Paint()
        //Set 'cmFilter' as the color filter of this paint
        cmPaint!!.colorFilter = cmFilter

        //Initialize the 'imgBitmap' by decoding the 'exampleimage.png' file
         imgBitmap = BitmapFactory.decodeResource(resources, R.drawable.exampleimage)
        //Create a new mutable Bitmap, with the same width and height as the 'imgBitmap'
        canvasBitmap = Bitmap.createBitmap (128, 128, Bitmap.Config.ARGB_8888)
        //Initialize the canvas assigning the mutable Bitmap to it
        cv = Canvas(canvasBitmap!!)

        //Initialize the ImageView and the SeekBar objects by inflating them
        // 'activity_cisactivity.xml' layout file
        ivImage = findViewById(R.id.iv_exampleimage) as ImageView
        sbSeekBar = findViewById(R.id.sb_seekbar) as SeekBar

        //Sets the range between 0 and 100
        sbSeekBar!!.max = 100
        //Set the seek bar increments to 1
        sbSeekBar!!.keyProgressIncrement = 1
        //Set the progress to 100
        sbSeekBar!!.progress = 50

        sbSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            //This method is called every time the SeekBar thumb is dragged
            override fun onProgressChanged(seekBar: SeekBar,
                                           progress: Int,
                                           fromUser: Boolean) {
                //Set the ColorMatrix saturation according to the SeekBar progress
                colorMatrix!!.setSaturation(progress / 100.toFloat())
                //Create a new ColorMatrixColorFilter with the recently altered colorMatrix
                cmFilter = ColorMatrixColorFilter(colorMatrix!!)

                //Assign the ColorMatrix to the paint object again
                cmPaint!!.colorFilter = cmFilter

                //Draw the Bitmap into the mutable Bitmap using the canvas.
                // Don't forget to pass the Paint as the last parameter
                cv!!.drawBitmap(imgBitmap!!, 0f, 0f, cmPaint)
                //Set the mutable Bitmap to be rendered by the ImageView
                ivImage!!.setImageBitmap(canvasBitmap)
            }
        })
    }
}
