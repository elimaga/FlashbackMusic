package com.example.team13.flashbackmusic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.List;

/**
 * Created by Luzanne on 3/13/18.
 */

public class GoogleUtility implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 0;
    private final static String serverAuthCode =
            "429092130830-j48ij24k28cit76hp23tti7mpm9ppqes.apps.googleusercontent.com";
    private List<Person> people;
    private Activity activity;


    public GoogleUtility (Activity activity, FragmentActivity fragmentActivity) {
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(serverAuthCode)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleServiceScopes.CONTACTS_READONLY))
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(fragmentActivity, this)
                .addOnConnectionFailedListener(this)
                // .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(activity, connectionResult.getErrorCode(), 1);
        dialog.show();
    }

    public void connectClient() {
        mGoogleSignInClient.connect();
    }

    public Intent getSignIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
    }

    public GoogleSignInAccount getAccount(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("SignInActivity", "result=" + result.getStatus().toString());

            if (result.isSuccess()){
                GoogleSignInAccount acct = result.getSignInAccount();

                PeopleAsync async = new PeopleAsync();
                async.execute(acct.getServerAuthCode());
                return acct;
            }
        }

        return null;
    }

    public void userSignOut() {
        if (mGoogleSignInClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleSignInClient);
            mGoogleSignInClient.disconnect();
            mGoogleSignInClient.connect();
        }
    }

    private class PeopleAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String serverAuthCode = strings[0];
            PeopleService peopleService = null;

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
            return null;
        }

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
