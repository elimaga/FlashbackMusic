package com.example.team13.flashbackmusic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
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
    private final static String webClientID =
            "487097348986-q5ogibt021sgjtk1jcpe3mo4g48r2h2b.apps.googleusercontent.com";
    private static List<Person> people;
    private static FBMUser user;
    private Activity activity;

    public GoogleUtility (Activity activity, FragmentActivity fragmentActivity) {
        user = new FBMUser();
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(webClientID)
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

    public void setUser(FBMUser user) {
        this.user = user;
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

    public GoogleSignInAccount getLastAccount() {
        return GoogleSignIn.getLastSignedInAccount(activity);
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
                    Log.d("SignInActivity", "got list of friends");

//               TODO: for future reference
//                    for (Person person : people) {
//                        if (!person.getNames().isEmpty()) {
//                                    person.getNames().get(0).getDisplayName());
//                        }
//                    }
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
            String clientSecret = "xga1yqjSDEZOlF3GuA1qRnyG";
            // STEP 1
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    webClientID,
                    clientSecret,
                    serverAuthCode,
                    redirectUrl).execute();
            // STEP 2
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setClientSecrets(webClientID, clientSecret)
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .build();
            credential.setFromTokenResponse(tokenResponse);

            // STEP 3
            return new PeopleService.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("Sign In Test")
                    .build();
        }

        @Override
        protected void onPostExecute(Void v) {
            user.setConnections(people);
            Log.d("GoogleUtility", "user name: " + user.getName());
            Log.d("GoogleUtility", "connections: " + user.getFriendsID());
        }
    }
}
