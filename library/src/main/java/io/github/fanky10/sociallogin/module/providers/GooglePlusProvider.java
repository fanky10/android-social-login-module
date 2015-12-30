package io.github.fanky10.sociallogin.module.providers;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;

import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;

/**
 * Created by carlospienovi1 on 12/30/15.
 */
public class GooglePlusProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE_DEFAULT = 19;

    private boolean mGoogleSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mGoogleConnectionResult;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mGoogleIntentInProgress;

    private Activity mContext;

    private String mEmail;
    private String mFirstName;
    private String mLastName;

    //Boolean helpers to identify amount of retries to Google
    private boolean mAuthExceptionSecondTry = false;
    private boolean mGPlayExcSecondTry = false;

    private ISocialLogin mCallback;

    public GooglePlusProvider(Activity context) {
        mContext = context;
    }

    public void connectApiClient() {
        setUpGoogleClientApi();
        mGoogleApiClient.connect();
    }

    public void setCallback(ISocialLogin callback) {
        mCallback = callback;
    }

    //set the mSignInClicked flag and the resolveSignInError helper to resolve any connection errors
    // returned in onConnectionFailed. Possible connection errors include prompting the user to select an account,
    // and granting access to the app.
    public void setUpSignIn() {
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleSignInClicked = true;
            resolveSignInError();
        } else {
            mGoogleSignInClicked = true;
            mGoogleApiClient.connect();
        }
    }

    private void setUpGoogleClientApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult != null && mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                int requestCode = REQUEST_CODE_DEFAULT;
                mContext.startIntentSenderForResult(mGoogleConnectionResult.getResolution().getIntentSender(),
                        requestCode, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        } else {
            mGoogleIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        mGoogleIntentInProgress = false;
    }

    public boolean isConnected() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    public void handleAuthResult(int resultCode) {
        if (resultCode != Activity.RESULT_OK) {
            mGoogleSignInClicked = false;
        }
        mGoogleIntentInProgress = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mGoogleSignInClicked = false;

        //Get user info based on G+ profile
        retrievePeopleInformation();

    }

    public void retrievePeopleInformation() {
        if (mGoogleApiClient.isConnected()
                && Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            mFirstName = currentPerson.getName().getGivenName();
            mLastName = currentPerson.getName().getFamilyName();
            mEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            new getGoogleTokenTask().execute(mEmail);
        } else {
            mCallback.onSocialProviderConnectionFailure(new Exception("generic eror"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mGoogleIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            mGoogleConnectionResult = connectionResult;
            if (mGoogleSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            } else {
                mCallback.onSocialProviderConnectionFailure(new Exception("generic error"));
            }
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    //Handling get token exceptions based on:
    // http://developer.android.com/reference/com/google/android/gms/auth/GoogleAuthUtil.html
    public String getProviderAccessToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mContext, mEmail, "oauth2:profile email");
        } catch (GooglePlayServicesAvailabilityException playEx) {
            if (!mGPlayExcSecondTry) {
                //mContext.handleGooglePlusException(playEx);
                mCallback.onSocialProviderConnectionFailure(playEx);
                mGPlayExcSecondTry = true;
                return null;
            } else {
                return null;
            }
        } catch (UserRecoverableAuthException userAuthEx) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            if (!mAuthExceptionSecondTry) {
                //mContext.handleGooglePlusException(userAuthEx);
                mCallback.onSocialProviderConnectionFailure(userAuthEx);
                mAuthExceptionSecondTry = true;
                return null;
            } else {
                return null;
            }
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            return null;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private class getGoogleTokenTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);
            if (token != null) {
                JSONObject json = new JSONObject();
                try {
                    json.put("first_name", mFirstName);
                    json.put("last_name", mLastName);
                    json.put("email", mEmail);
                } catch (JSONException ignore) {

                }
                mCallback.onSocialProviderConnected(token, json);
            } else {
                mCallback.onSocialProviderConnectionFailure(new Exception("generic error"));
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String token = getProviderAccessToken();
                if (token != null) {
                    return token;
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }
    }

}
