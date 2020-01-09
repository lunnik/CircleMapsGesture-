package com.lionsquare.circlemapsgesturelibrary

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class CircleMapsGesture : View {

    var mTouchMode = MODE_DONT_CARE
    private lateinit var callBackCircle: CallBackCircle
    private var circle: Circle? = null
    private var googleMap: GoogleMap? = null
    private lateinit var mScaleDetector: ScaleGestureDetector
    private var mScaleFactor = 1f

    private val INVALID_POINTER_ID = -1
    private var mActivePointerId = INVALID_POINTER_ID

    lateinit var attrs: AttributeSet
    var defStyle: Int = 0


    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : super(context, attrs, defStyle) {
        INVISIBLE
        if (attrs != null) {
            this.attrs = attrs
        }
        this.defStyle = defStyle
    }

    fun setMap(googleMap: GoogleMap) {
        this.visibility= GONE
        invalidate()

        this.googleMap = googleMap
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        invalidate()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID


            }
            MotionEvent.ACTION_POINTER_UP -> if (event.getActionIndex() <= 1) {
                mTouchMode = MODE_DONT_CARE


                //enableGestureMap()
            }
            else -> super.onTouchEvent(event)
        }
        return true
    }

    fun createCircle(latLng: LatLng) {
        mScaleFactor=1F
        callBackCircle.gestureState(true)
        this.visibility= VISIBLE
        invalidate()

        desableGestureMap()

        val circleOptions = CircleOptions()
            .center(latLng)
            .radius(1000.0)
            .strokeWidth(2.0f)
            .strokeColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            .fillColor(ContextCompat.getColor(context!!, R.color.fill_color))
        circle?.remove() // Remove old circle.
        circle = googleMap?.addCircle(circleOptions) // Draw new circle.


        googleMap?.addMarker(MarkerOptions().position(latLng))
        val newCamPos = CameraPosition(
            latLng,
            13.5f,
            googleMap?.cameraPosition!!.tilt, //use old tilt
            googleMap?.cameraPosition!!.bearing
        ); //use old bearing
        googleMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(newCamPos),
            1000,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                }

                override fun onCancel() {
                }
            });
    }

    fun deleteCircle() {
        callBackCircle.gestureState(false)
        this.visibility= INVISIBLE
        invalidate()

        enableGestureMap()
        mActivePointerId = INVALID_POINTER_ID

    }

    @SuppressLint("RestrictedApi")
    fun enableGestureMap() {
        circle = null
        googleMap?.uiSettings?.isZoomGesturesEnabled = true
        googleMap?.uiSettings?.isRotateGesturesEnabled = true
        googleMap?.uiSettings?.isScrollGesturesEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        googleMap?.clear()
    }

    fun desableGestureMap() {
        googleMap?.uiSettings?.isZoomGesturesEnabled = false
        googleMap?.uiSettings?.isRotateGesturesEnabled = false
        googleMap?.uiSettings?.isScrollGesturesEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= (detector.scaleFactor)
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 35.0f))
            val raduis = (mScaleFactor.toDouble() * 500) + 500 //progreso normal
            modifiGeoClose(raduis)//this
            return true
        }
    }

    fun setCallbackCircle(callBackCircle: CallBackCircle) {

        this.callBackCircle = callBackCircle
    }

    private fun modifiGeoClose(radius: Double) {
        if (radius < 1000 || radius > 35000) {

            return
        }
        if (circle != null) {
            if (radius > 35000) {


            }
            circle?.radius = radius
        }
    }

    override fun onDraw(canvas: Canvas) {}

    interface CallBackCircle {
        fun getCircule(circle: Circle?)
        fun gestureState(status: Boolean)
    }

    companion object {
        const val MODE_PINCH = 0
        const val MODE_DONT_CARE = 1
    }
}