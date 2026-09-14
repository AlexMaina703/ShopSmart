package com.shopsmart.app.features.auth.presentation.components

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.shopsmart.app.R
import com.shopsmart.app.features.auth.domain.model.AuthProvider

private const val GOOGLE_WEB_CLIENT_ID =
    "404852968671-da30govd04v6080uf1us5u5jrn9sv59q.apps.googleusercontent.com"

@Composable
fun SocialSignInButtons(
    enabled: Boolean,
    onSignInWith: (AuthProvider, String) -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    // ------------------------------------------------------------
    //  GOOGLE
    // ------------------------------------------------------------
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrEmpty()) {
                onError("Google did not return an idToken")
            } else {
                onSignInWith(AuthProvider.GOOGLE, idToken)
            }
        } catch (e: ApiException) {
            onError("Google sign-in failed: ${e.statusCode}")
        }
    }

    // ------------------------------------------------------------
    //  FACEBOOK
    // ------------------------------------------------------------
    val fbCallbackManager = remember { CallbackManager.Factory.create() }

    // Registering the callback on LoginManager (SDK 16+ API).
    // This runs once, on first composition — `remember` keeps it stable.
    remember(fbCallbackManager) {
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    onSignInWith(AuthProvider.FACEBOOK, result.accessToken.token)
                }

                override fun onCancel() {
                    // User dismissed the dialog — do nothing.
                }

                override fun onError(error: FacebookException) {
                    onError(error.message ?: "Facebook sign-in failed")
                }
            },
        )
        true   // remember needs a value; `true` = "registration done"
    }

    // ------------------------------------------------------------
    //  UI
    // ------------------------------------------------------------
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // ---------- GOOGLE ----------
        OutlinedButton(
            onClick = {
                val opts = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(GOOGLE_WEB_CLIENT_ID)
                    .requestEmail()
                    .build()
                val client = GoogleSignIn.getClient(context, opts)
                googleLauncher.launch(client.signInIntent)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = enabled && activity != null,
            shape = MaterialTheme.shapes.medium,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("Continue with Google")
        }

        // ---------- FACEBOOK ----------
        OutlinedButton(
            onClick = {
                val act = activity ?: return@OutlinedButton
                LoginManager.getInstance().logInWithReadPermissions(
                    act,
                    fbCallbackManager,
                    listOf("email", "public_profile"),
                )
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = enabled && activity != null,
            shape = MaterialTheme.shapes.medium,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_facebook_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("Continue with Facebook")
        }
    }
}

// ------------------------------------------------------------
//  Helpers
// ------------------------------------------------------------

private tailrec fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}