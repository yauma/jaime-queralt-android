package com.example.jaimequeraltgarrigos.spotify_artist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import com.example.jaimequeraltgarrigos.spotify_artist.utils.TokenManager
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private var mAccessToken: String = ""
    private var goldentifyQuery: String? = null
    private lateinit var myApplication: MyApplication

    companion object {
        private const val CLIENT_ID = "972b5264690045fba3744ed1adebf270"
        private const val AUTH_TOKEN_REQUEST_CODE = 0x10
        private const val REDIRECT_URI = "http://spotify-artist.com/callback/"
        private const val PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goldentifyQuery = intent?.getStringExtra(CalendarSyncWorker.DESCRIPTION)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
            == PackageManager.PERMISSION_DENIED
        ) {
            checkForCalendarPermission()
        }

        val request: AuthorizationRequest =
            getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)

        myApplication = application as MyApplication
    }

    private fun checkForCalendarPermission() {
        lifecycleScope.launch {
            //CoroutineScope
            val permissionResult =
                PermissionManager.requestPermissions(           //Suspends the coroutine
                    this@LoginActivity,
                    PERMISSION_CODE,
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                )

            //Resume coroutine once result is ready
            when (permissionResult) {
                is PermissionResult.PermissionGranted -> {
                    Toast.makeText(applicationContext, "GRANTED", Toast.LENGTH_LONG).show()
                }
                is PermissionResult.PermissionDenied -> {
                    Toast.makeText(applicationContext, "DENIED", Toast.LENGTH_LONG).show()
                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    Toast.makeText(applicationContext, "DENIED PERMANENTLY", Toast.LENGTH_LONG)
                        .show()
                    //Ideally you should ask user to manually go to settings and enable permission(s)
                }
                is PermissionResult.ShowRational -> {
                    Toast.makeText(applicationContext, "RATIONAL", Toast.LENGTH_LONG).show()
                    //If user denied permission frequently then she/he is not clear about why you are asking this permission.
                    //This is your chance to explain them why you need permission.
                }
            }

        }
    }

    private fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            LoginActivity.CLIENT_ID,
            type,
            REDIRECT_URI
        )
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email"))
            .setCampaign("your-campaign-token")
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthorizationClient.getResponse(resultCode, data)

        if (response.error != null && response.error.isNotEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please loggin to Spotify to search artists",
                Toast.LENGTH_LONG
            ).show()

        } else if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.accessToken
            TokenManager.token = mAccessToken
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        if (goldentifyQuery != null) {
            intent.putExtra(CalendarSyncWorker.DESCRIPTION, goldentifyQuery)
        }
        startActivity(intent)
    }
}