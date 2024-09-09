package net.harutiro.mygeofence.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import net.harutiro.mygeofence.data.LatLon
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel: ViewModel() {

    // 監視する中心位置
    var monitorLocation: LatLon = LatLon(
        latitude = 35.1849266,
        longitude = 137.1092261
    )
    var monitorRadius: Double = 500.0 // 監視半径(メートル)

    private var getRate: Long = 10000//取得頻度(ms)
    private var minRate: Long = 5000//更新頻度(ms)

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    interface MyLocationCallback {
        fun onLocationResult(location: Location?, checkResult: Boolean)
        fun onLocationError(error: String)
    }

    fun initContent(
        context: Context
    ){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, getRate) // 10秒ごとに取得
            .setMinUpdateIntervalMillis(minRate) // 最小間隔5秒
            .build()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(callback: MyLocationCallback) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val checkResult = checkMonitorLocation(monitorLocation, LatLon(location.latitude, location.longitude), monitorRadius)
                    callback.onLocationResult(location, checkResult)
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    callback.onLocationError("Location unavailable")
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest!!, locationCallback!!, null)
    }

    fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }

    fun checkMonitorLocation(latlon1: LatLon, latlon2: LatLon , radius: Double): Boolean {
        val distance = distanceInMeters(latlon1.latitude, latlon1.longitude, latlon2.latitude, latlon2.longitude)

        Log.d("checkMonitorLocation", "distance: $distance")
        Log.d("checkMonitorLocation", "result: ${distance <= radius}")

        return distance <= radius
    }

    private fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // 地球の半径（メートル）

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }
}