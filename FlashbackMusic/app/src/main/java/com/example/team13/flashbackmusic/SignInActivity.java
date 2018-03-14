package com.example.team13.flashbackmusic;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.android.gms.common.ConnectionResult;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 0;
    final static String serverAuthCode =
            "429092130830-j48ij24k28cit76hp23tti7mpm9ppqes.apps.googleusercontent.com";
    private List<Person> people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(serverAuthCode)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleServiceScopes.CONTACTS_READONLY))
                .build();

        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                // .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), 1);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
        mGoogleSignInClient.connect();
        //updateUI();
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
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SignInActivity", "running onActivityResult");

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("SignInActivity", "result=" + result.getStatus().toString());

            if (result.isSuccess()){
                GoogleSignInAccount acct = result.getSignInAccount();

                PeopleAsync async = new PeopleAsync();
                async.execute(acct.getServerAuthCode());

                updateUI(acct);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            updateUI(acct);
        } catch (Exception e) {
            Log.d("SignInActivity", "signInResult:failed code =" + e.toString());
            updateUI(null);
        }
    }

    private class PeopleAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String serverAuthCode = strings[0];
            PeopleService peopleService = null;
//            List<Person> connections = new ArrayList<>();

            try {
                peopleService = setUp(serverAuthCode);
                ListConnectionsResponse response = peopleService.people().connections().
                        list("people/me")
                        .setRequestMaskIncludeField("person.names").execute();

                people = response.getConnections();
                if(people != null) {
                    for (Person person : people) {
                        if (!person.getNames().isEmpty()) {
                            Log.d("SignInActivity", "name: " +
                                    person.getNames().get(0).getDisplayName());
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

//            people = connections;
            return null;
        }

//    @Override
//    protected void onPostExecute(List<Person> persons) {
//        super.onPostExecute(persons);
//        setUpContactListFromPersonList(persons);
//    }

        public PeopleService setUp(String serverAuthCode) throws IOException {
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            // Redirect URL for web based applications.
            // Can be empty too.
            String redirectUrl = "";
            String clientId = "429092130830-j48ij24k28cit76hp23tti7mpm9ppqes.apps.googleusercontent.com";
            String clientSecret = "acIDjVS2xktjLHFnaTJokr95";
            // STEP 1
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    clientId,
                    clientSecret,
                    serverAuthCode,
                    redirectUrl).execute();
            // STEP 2
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setClientSecrets(clientId, clientSecret)
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .build();
            credential.setFromTokenResponse(tokenResponse);

            // STEP 3
            return new PeopleService.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("Sign In Test")
                    .build();
        }
    }

}


