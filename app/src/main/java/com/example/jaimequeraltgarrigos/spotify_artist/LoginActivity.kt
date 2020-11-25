package com.example.jaimequeraltgarrigos.spotify_artist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jaimequeraltgarrigos.spotify_artist.utils.TokenManager
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class LoginActivity : AppCompatActivity() {
    private var mAccessToken: String = ""
    private lateinit var myApplication: MyApplication

    companion object {
        private const val CLIENT_ID = "972b5264690045fba3744ed1adebf270"
        private const val AUTH_TOKEN_REQUEST_CODE = 0x10
        private const val REDIRECT_URI = "http://spotify-artist.com/callback/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val request: AuthorizationRequest =
            getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
        myApplication = application as MyApplication
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
            //TODO set error message
        } else if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.accessToken
            TokenManager.token = mAccessToken
            navigateToMainActivity(mAccessToken)
        }
    }

    private fun navigateToMainActivity(mAccessToken: String?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(getString(R.string.AccessToken), mAccessToken)
        startActivity(intent)
    }
}