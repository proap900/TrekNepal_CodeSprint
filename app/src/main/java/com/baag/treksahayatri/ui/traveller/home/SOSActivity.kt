package com.baag.treksahayatri.ui.traveller.ui.Home
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baag.treksahayatri.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class SOSActivity : AppCompatActivity() {

	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private lateinit var sendLocationBtn: Button

	private val requiredPermissions = arrayOf(
		Manifest.permission.SEND_SMS,
		Manifest.permission.ACCESS_FINE_LOCATION
	)
	private val permissionRequestCode = 101

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sosactivity)

		sendLocationBtn = findViewById(R.id.btn_send_location)
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

		sendLocationBtn.setOnClickListener {
			if (checkPermissions()) {
				getLocationAndSendSMS()
			} else {
				requestPermissions()
			}
		}
	}

	private fun checkPermissions(): Boolean {
		return requiredPermissions.all {
			ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
		}
	}

	private fun requestPermissions() {
		ActivityCompat.requestPermissions(this, requiredPermissions, permissionRequestCode)
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == permissionRequestCode) {
			if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
				getLocationAndSendSMS()
			} else {
				Toast.makeText(
					this,
					"Permissions denied! Some features may not work.",
					Toast.LENGTH_SHORT
				).show()
			}
		}
	}

	@SuppressLint("MissingPermission")
	private fun getLocationAndSendSMS() {
		fusedLocationClient.lastLocation
			.addOnSuccessListener { location: Location? ->
				if (location != null) {
					val address = getAddressFromLocation(location)
					val message = if (address.isNotEmpty()) {
						"My current location:\n$address\n\nGoogle Maps: https://maps.google.com/?q=${location.latitude},${location.longitude}"
					} else {
						"My GPS coordinates:\nLat: ${location.latitude}\nLon: ${location.longitude}\n\nGoogle Maps: https://maps.google.com/?q=${location.latitude},${location.longitude}"
					}
					sendSMS(message)
				} else {
					Toast.makeText(
						this,
						"Unable to get location. Please ensure location services are enabled.",
						Toast.LENGTH_LONG
					).show()
				}
			}
			.addOnFailureListener { e ->
				Toast.makeText(
					this,
					"Location error: ${e.message}",
					Toast.LENGTH_SHORT
				).show()
			}
	}

	private fun getAddressFromLocation(location: Location): String {
		return try {
			val geocoder = Geocoder(this, Locale.getDefault())
			val addresses: List<Address>? = geocoder.getFromLocation(
				location.latitude,
				location.longitude,
				1
			)

			addresses?.firstOrNull()?.let { address ->
				val addressParts = listOfNotNull(
					address.thoroughfare,
					address.subThoroughfare,
					address.locality,
					address.adminArea,
					address.postalCode,
					address.countryName
				)
				addressParts.joinToString(separator = ", ")
			} ?: ""
		} catch (e: IOException) {
			e.printStackTrace()
			""
		} catch (e: IllegalArgumentException) {
			e.printStackTrace()
			""
		}
	}

	private fun sendSMS(message: String) {
		try {
			// In real app, get number from contacts or input field
			val phoneNumber = "+9779843967796"  // Replace with actual number

			SmsManager.getDefault().sendTextMessage(
				phoneNumber,
				null,
				message,
				null,
				null
			)
			Toast.makeText(
				this,
				"Location sent successfully!",
				Toast.LENGTH_SHORT
			).show()
		} catch (e: SecurityException) {
			Toast.makeText(
				this,
				"SMS permission denied!",
				Toast.LENGTH_SHORT
			).show()
		} catch (e: Exception) {
			Toast.makeText(
				this,
				"SMS failed: ${e.message}",
				Toast.LENGTH_SHORT
			).show()
		}
	}
}