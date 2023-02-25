package com.example.splashdayan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        this.mMap.apply {
            setOnMapClickListener(this@MapActivity)
            setOnMapLongClickListener(this@MapActivity)
        }
        val mexico = LatLng(19.432608, -99.133209)
        mMap.addMarker(MarkerOptions().position(mexico).title("Mexico"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))
    }

    override fun onMapClick(p0: LatLng) {
        etLatitud.setText(p0.latitude.toString())
        etLongitud.setText(p0.longitude.toString())

        mMap.clear()
        val mexico = LatLng(p0.latitude, p0.longitude)
        mMap.addMarker(MarkerOptions().position(mexico).title("Mexico"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))
    }

    override fun onMapLongClick(p0: LatLng) {
        etLatitud.setText(p0.latitude.toString())
        etLongitud.setText(p0.longitude.toString())
    }
}