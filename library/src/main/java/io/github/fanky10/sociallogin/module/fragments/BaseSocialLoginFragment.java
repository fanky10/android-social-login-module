package io.github.fanky10.sociallogin.module.fragments;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.Arrays;

import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public abstract class BaseSocialLoginFragment extends Fragment
        implements ISocialLogin {

    private CallbackManager callbackManager;

    protected abstract String[] getPermissions();

    protected abstract String[] getProfileInfo();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, getFacebookLoginCallback());
    }

    private FacebookCallback<LoginResult> getFacebookLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // perform profile login
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                // we use the new updated info
                                // if trying to use Profile.getCurrentProfile
                                // then add profile tracking
                                if (response.getError() != null) {
                                    onSocialProviderConnectionFailure(response.getError().getException());
                                } else {
                                    onSocialProviderConnected(AccessToken.getCurrentAccessToken().getToken(), me);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", TextUtils.join(",", getProfileInfo()));
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                onSocialProviderConnectionCanceled();
            }

            @Override
            public void onError(FacebookException exception) {
                onSocialProviderConnectionFailure(exception);
            }
        };
    }

    public void doFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList(getPermissions()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
