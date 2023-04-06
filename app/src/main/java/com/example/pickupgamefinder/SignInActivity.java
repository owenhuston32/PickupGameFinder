package com.example.pickupgamefinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity{

    private static final String TAG = "SIGN_IN_ACTIVITY";
    private static final int SIGN_IN_REQ = 0;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.auth_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        trySignIn();

    }
    private void trySignIn()
    {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), SIGN_IN_REQ,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.e(TAG, e.getLocalizedMessage());

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  SIGN_IN_REQ) {
            Intent intent = new Intent();
            handleSignIn(data, intent);
        }

    }

    private void handleSignIn(@Nullable Intent data, Intent intent)
    {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            String username = credential.getId();
            String password = credential.getPassword();
            if (idToken !=  null) {
                Log.e(TAG, "Got ID token.");
                Log.e(TAG, idToken);
                intent.putExtra("ID", idToken);
            }
            if(username != null) {
                Log.e(TAG, "Got username");
                Log.e(TAG, username);
                intent.putExtra("USERNAME", username);
            }
            if (password != null) {
                Log.e(TAG, "Got password.");
                Log.e(TAG, password);
            }
            setResult(RESULT_OK, intent);
        } catch (ApiException e) {
            setResult(RESULT_CANCELED, intent);
            switch (e.getStatusCode()) {
                case CommonStatusCodes.CANCELED:
                    Log.d(TAG, "One-tap dialog was closed.");

                    break;
                default:
                    Log.d(TAG, "Couldn't get credential from result."
                            + e.getLocalizedMessage());
                    break;
            }
        }

        finish();
    }

}
