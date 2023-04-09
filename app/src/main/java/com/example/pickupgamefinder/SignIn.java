package com.example.pickupgamefinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class SignIn extends AppCompatActivity{

    private static final String TAG = "ONE_TAP_SIGN_IN";
    private static final int ONE_TAP_SIGN_IN = 0;
    private static final int REGULAR_SIGN_IN = 1;

    private GoogleSignInClient signInClient;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null)
        {
            Intent intent = new Intent();
            intent.putExtra("ID", account.getId());
            intent.putExtra("USERNAME", account.getDisplayName());
            setResult(RESULT_OK, intent);
            finish();
        }

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

        tryOneTapSignIn();

    }
    private void tryOneTapSignIn()
    {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), ONE_TAP_SIGN_IN,
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
                        tryRegularSignIn();
                    }
                });
    }

    private void tryRegularSignIn()
    {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, REGULAR_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ONE_TAP_SIGN_IN) {
            Intent intent = new Intent();
            handleOneTapSignIn(data, intent);
        }
        else if(requestCode == REGULAR_SIGN_IN)
        {
            Intent intent = new Intent();
            handleRegularSignIn(data, intent);
        }

    }

    private void handleOneTapSignIn(@Nullable Intent data, Intent intent)
    {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String ID = credential.getId();
            String username = credential.getDisplayName();

            if(ID != null)
            {
                intent.putExtra("ID", ID);
            }
            if(username != null) {
                intent.putExtra("USERNAME", username);
            }

            setResult(RESULT_OK, intent);
            finish();
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

            tryRegularSignIn();
        }
    }

    private void handleRegularSignIn(@Nullable Intent data, Intent intent)
    {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            intent.putExtra("ID", account.getId());
            intent.putExtra("USERNAME", account.getDisplayName());
            setResult(RESULT_OK, intent);
            Log.d(TAG, "REGULAR ACCOUNT SIGN IN SUCCESSFUL");

        } catch (ApiException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED, intent);
            Log.d(TAG, "REGULAR ACCOUNT SIGN IN FAILED");
        }

        finish();
    }

}
