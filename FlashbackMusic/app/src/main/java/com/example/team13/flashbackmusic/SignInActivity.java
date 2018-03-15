package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SignInActivity extends AppCompatActivity {
    private GoogleUtility googleUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        googleUtility = new GoogleUtility(SignInActivity.this, this);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleUtility.connectClient();
        GoogleSignInAccount account = googleUtility.getLastAccount();
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Log.d("SignInActivity", "User signed in");
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        Log.d("SignInActivity", "Starting Sign-In Flow");
        Intent signInIntent = googleUtility.getSignIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SignInActivity", "running onActivityResult");

        SharedPreferences sp = getSharedPreferences("UserFriends", MODE_PRIVATE);
        GoogleSignInAccount acct = googleUtility.getAccount(requestCode, data, sp);
        updateUI(acct);
    }

}


