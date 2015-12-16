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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import io.github.fanky10.sociallogin.module.R;
import io.github.fanky10.sociallogin.module.constants.enums.ExternalLoginProviders;
import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public class BaseFacebookLoginFragment extends Fragment implements ISocialLogin {

    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, getFacebookLoginCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        view.findViewById(R.id.fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFacebookLogin();
            }
        });
        return view;
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
                                String email = me.optString("email");
                                String firstName = me.optString("first_name");
                                String lastName = me.optString("last_name");

                                if (response.getError() != null) {
                                    onSocialProviderConnectionFailure(response.getError().getException());
                                } else {
                                    onSocialProviderConnected(ExternalLoginProviders.Facebook,
                                            AccessToken.getCurrentAccessToken().getToken(),
                                            email,
                                            firstName,
                                            lastName);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // fire cancel
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
                Arrays.asList("email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {
    }

    @Override
    public void onSocialProviderConnected(ExternalLoginProviders provider, String token, String email, String firstName, String lastName) {
    }

}
