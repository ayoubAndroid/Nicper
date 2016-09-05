package com.example.ayoub.nicper.MainActivity.Intro;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.ActivityMap;
import com.example.ayoub.nicper.Object.AppGeneral.User;
import com.example.ayoub.nicper.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.ProgressView;

public class GoogleLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userReference = root.child("data").child("users");
    private Button googleLogin;
    ProgressView progressView;

    private TextView title, description;

    private static String ACCOUNT_PERMISSIONS[] = new String[]{
            Manifest.permission.GET_ACCOUNTS
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        // Configure Google Sign In
        new ConfigureGoogleSignIn().execute("");

        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        progressView = (ProgressView) findViewById(R.id.progressView);


        googleLogin = (Button) findViewById(R.id.buttonGoogle);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    progressView.stop();
                    User userApp = new User(user.getEmail(), user.getDisplayName(), user.getDisplayName(), 0);
                    userReference.child(user.getUid()).child("profil").setValue(userApp);
                    Intent intent = new Intent(GoogleLogin.this, ActivityMap.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        changeFont();


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            LongOperation longOperation = new LongOperation(mAuth, data);
            progressView.start();
            longOperation.execute();
        }
    }

    public void setListner(FirebaseAuth mAuth){
        this.mAuth = mAuth;

    }
    private class ConfigureGoogleSignIn extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]

            mGoogleApiClient = new GoogleApiClient.Builder(GoogleLogin.this)
                    .enableAutoManage(GoogleLogin.this , GoogleLogin.this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Finish

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private Intent data;

        public  LongOperation(FirebaseAuth mAuth, Intent data){
            this.mAuth = mAuth;
            this.data = data;
        }
        @Override
        protected String doInBackground(String... params) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(GoogleLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithCredential", task.getException());
                                    Toast.makeText(GoogleLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Snackbar.make(googleLogin, "Can't connect to firebase",  Snackbar.LENGTH_LONG).show();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Finish
            setListner(this.mAuth);

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void changeFont(){
        Typeface tf = Typeface.createFromAsset(getAssets(), "Geomanist-Regular.otf");
        title.setTypeface(tf);
        description.setTypeface(tf);
        googleLogin.setTypeface(tf);
    }




}
