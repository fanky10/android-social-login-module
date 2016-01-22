package io.github.fanky10.sociallogin.module.fragments;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.io.IOException;

import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;


/**
 * Created by carlospienovi1 on 12/30/15.
 */
public abstract class BaseGooglePlusLoginFragment extends Fragment implements
        ISocialLogin,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {

    private static final int REQUEST_GET_ACCOUNTS_PERMISSION = 102;
    public static final int REQUEST_RESOLVE_ERROR = 1001;


    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    public void doGoogleLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNTS_PERMISSION);
            } else {
                doGoogleLoginProcess();
            }
        } else {
            doGoogleLoginProcess();
        }
    }

    private void doGoogleLoginProcess() {
        if (mGoogleApiClient != null) {
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Plus.PeopleApi.load(mGoogleApiClient, "signed_in_user_account_id").setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        onSocialProviderConnectionCanceled();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // https://developers.google.com/android/guides/api-client
                // There was an error with the resolution intent. Try again.
                doGoogleLoginProcess();
            }
        } else {
            onSocialProviderConnectionFailure(new Exception(connectionResult.getErrorMessage()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR && resultCode == Activity.RESULT_OK) {
            doGoogleLoginProcess();
        }
    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
        final Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... email) {
                try {
                    return GoogleAuthUtil.getToken(getContext(), email[0], "oauth2:profile email");
                } catch (IOException | GoogleAuthException ignored) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put(SocialLoginConstants.GOOGLE_FIRST_NAME, person.getName().getGivenName());
                        json.put(SocialLoginConstants.GOOGLE_LAST_NAME, person.getName().getFamilyName());
                        json.put(SocialLoginConstants.GOOGLE_EMAIL, email);
                        onSocialProviderConnected(token, json);
                    } catch (JSONException e) {
                        onSocialProviderConnectionFailure(e);
                    }
                } else {
                    onSocialProviderConnectionFailure(new Exception("generic error"));
                }
            }

        }.execute(email);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GET_ACCOUNTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doGoogleLoginProcess();
            } else {
                onSocialProviderConnectionFailure(new Exception("GET ACCOUNTS permission was not granted"));
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
