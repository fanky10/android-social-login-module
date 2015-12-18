package io.github.fanky10.sociallogin.module.fragments;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import io.github.fanky10.sociallogin.module.R;
import io.github.fanky10.sociallogin.module.interfaces.IFacebook;
import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public abstract class BaseSocialLoginFragment extends Fragment
        implements ISocialLogin {

    private CallbackManager mFacebookCallbackManager;
    private IFacebook mIFacebook;
    private TwitterLoginButton mTwitterLoginButton;

    protected int getTwitterLoginButtonId() {
        return -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, getFacebookLoginCallback());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_app_key), getString(R.string.twitter_app_secret));
        Fabric.with(getContext(), new TwitterCore(authConfig));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTwitterLoginButton = (TwitterLoginButton) view.findViewById(getTwitterLoginButtonId());

        if (mTwitterLoginButton != null) {
            mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession session = result.data;

                    final String userName = session.getUserName();
                    final long userId = session.getUserId();
                    String msg = "@" + userName + " logged in! (#" + userId + ")";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                    twitterApiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
                        @Override
                        public void success(Result<User> userResult) {
                            String tFullName = userResult.data.name;
                            String tProfileImage = userResult.data.profileImageUrl;
                            Log.d("GATA", "User FullName: " + tFullName);
                            Log.d("GATA", "User Profile: " + tProfileImage);
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Toast.makeText(getContext(), "twitter login failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void failure(TwitterException e) {
                    onSocialProviderConnectionFailure(e);
                }
            });
        }
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
                parameters.putString("fields", TextUtils.join(",", mIFacebook.getProfileInfo()));
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

    public void doFacebookLogin(IFacebook iFacebook) {
        mIFacebook = iFacebook;
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList(iFacebook.getPermissions()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (mTwitterLoginButton != null) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

}
