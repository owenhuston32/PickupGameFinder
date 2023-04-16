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
    private static final String USERNAME_KEY = "USERNAME";
    private static final String ID_KEY = "ID";
    private static final int REGULAR_SIGN_IN = 1;

    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        tryRegularSignIn();

    }

    private void tryRegularSignIn()
    {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, REGULAR_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGULAR_SIGN_IN)
        {
            Intent intent = new Intent();
            handleRegularSignIn(data, intent);
        }

    }

    private void handleRegularSignIn(@Nullable Intent data, Intent intent)
    {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            intent.putExtra(ID_KEY, account.getId());
            intent.putExtra(USERNAME_KEY, account.getDisplayName());
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
