package assignment.home.taglocation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import assignment.home.taglocation.R
import assignment.home.taglocation.databinding.ActivityMapsBinding
import assignment.home.taglocation.extras.Constants
import assignment.home.taglocation.view.ui.MarkLocationFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var location: LatLng

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              checkLocationPermission()
          }*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initUi()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapLongClickListener(this)

        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMapToolbarEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = true
        }

    }

    /**
     * Function to initialise the UI contents of MapsActivity
     */
    fun initUi() {
        binding.fab.setOnClickListener { view ->

            if (!this::location.isInitialized || location.toString() == null) {
                Snackbar.make(view, "Please Mark Some Location", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                initMarkLocationFragment()
            }
        }
    }

    /**
     * function to show the screen to add property name nad property
     * coordinates
     */
    private fun initMarkLocationFragment() {
        val markLocationFragment = MarkLocationFragment()
        markLocationFragment.show(supportFragmentManager, Constants.MARK_LOCATION_TAG)
        markLocationFragment.propertyCoordinates = location.toString()
    }

    /**
     * function to check the location permission granted or not
     */
    fun checkLocationPermission(): Boolean {

        return if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    assignment.home.taglocation.view.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    assignment.home.taglocation.view.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        /*if (mGoogleApiClient == null) {
                           // buildGoogleApiClient()
                        }*/
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this, "permission denied", Toast.LENGTH_LONG
                    ).show()
                }
                return

            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {

        Toast.makeText(this, " Clicked Location ${location}", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onMapLongClick(latLng: LatLng) {

        location = latLng
        mMap.clear()

        mMap.addMarker(MarkerOptions().position(location))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))

    }

}