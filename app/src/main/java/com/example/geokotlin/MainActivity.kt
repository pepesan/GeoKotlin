package com.example.geokotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var texto: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        texto = findViewById(R.id.texto)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_get_location) {
            requestLocationPermissions()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private val MY_PERMISSIONS_FINE_LOCATION = 1

    fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                !== PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Please grant permissions to fine location", Toast.LENGTH_LONG).show()

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_FINE_LOCATION)
                Log.d("app", "Pidiendo permisos")
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_FINE_LOCATION)
                Log.d("app", "Mostrando diálogo")
            }
        } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                === PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            Toast.makeText(this, "Permissions granted to fine location", Toast.LENGTH_LONG).show()
            requestPosition()
        }
    }
    //Handling callback
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("app", "Cuando devuelven resultados de permisos")
        when (requestCode) {
            MY_PERMISSIONS_FINE_LOCATION -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    requestPosition()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to fine location", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }



    private fun requestPosition() {
        Log.d("app", "Pidiendo posición")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        mFusedLocationClient!!.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d("app", "" + location)
                        texto!!.text = "Longitud: " + location.longitude + ":Latitud: " + location.latitude
                    } else {
                        Log.d("app", "Location es null")
                    }
                }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }
}