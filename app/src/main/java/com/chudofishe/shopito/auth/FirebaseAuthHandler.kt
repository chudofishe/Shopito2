package com.chudofishe.shopito.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.chudofishe.shopito.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

object FirebaseAuthHandler {

    private const val TAG = "FirebaseAuthHandler"

    // Instantiate a Google sign-in request
    private val googleIdOption = GetGoogleIdOption.Builder()
        // Your server's client ID, not your Android client ID.
        .setServerClientId("222933804693-83l593lsgi85e7lk0p6pqpbck1l7d86g.apps.googleusercontent.com")
        // Only show accounts previously used to sign in.
        .setFilterByAuthorizedAccounts(false)
        .build()

    // Create the Credential Manager request
    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    suspend fun handleSignIn(context: Activity, onSuccess: () -> Unit) {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        val credential = result.credential
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)

            // Sign in to Firebase with using the token
            val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        onSuccess
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    suspend fun handleSignOut(context: Activity) {
        Firebase.auth.signOut()
        val clearRequest = ClearCredentialStateRequest()
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(clearRequest)
    }

}