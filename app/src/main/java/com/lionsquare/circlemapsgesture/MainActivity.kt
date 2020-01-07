package com.lionsquare.circlemapsgesture

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lionsquare.circlemapsgesturelibrary.CircleMapsGesture

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    CircleMapsGesture.CallBackCircle {

    var circleMapsGesture: CircleMapsGesture? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            circleMapsGesture?.deleteCircle()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        circleMapsGesture = findViewById(R.id.cmgCircle)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        circleMapsGesture ?.setMap(mMap)
        circleMapsGesture?.setCallbackCircle(this)
        //circleMapsGesture?.visibility=View.GONE

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMapClickListener(this)

    }

    override fun onMapClick(latLng: LatLng) {
        Log.e(",","siii")
        circleMapsGesture?.createCircle(latLng)
        //circleMapsGesture?.visibility=View.VISIBLE
    }

    override fun getCircule(circle: Circle?) {

    }

    @SuppressLint("RestrictedApi")
    override fun gestureState(status: Boolean) {
        if (status) {
            fab.visibility = View.VISIBLE
        } else {
            fab.visibility = View.GONE
        }
    }


}
