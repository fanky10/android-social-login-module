package io.github.fanky10.sociallogin.module.fragments;

import com.google.gson.Gson;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;

/**
 * Created by carlospienovi1 on 12/29/15.
 */
public abstract class BaseTwitterLoginFragment extends Fragment implements
        ISocialLogin {

    private TwitterLoginButton mTwitterLoginButton;

    protected abstract int getTwitterLoginButtonId();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTwitterLoginButton = (TwitterLoginButton) view.findViewById(getTwitterLoginButtonId());

        if (mTwitterLoginButton != null) {
            mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(final Result<TwitterSession> result) {
                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

                    twitterApiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
                        @Override
                        public void success(Result<User> userResult) {
                            JSONObject json = new JSONObject();
                            try {
                                json = new JSONObject(new Gson().toJson(userResult.data));
                            } catch (JSONException ignore) {

                            }

                            // get email and add it to response
                            TwitterAuthClient authClient = new TwitterAuthClient();
                            final JSONObject finalJson = json;
                            authClient.requestEmail(result.data, new Callback<String>() {
                                @Override
                                public void success(Result<String> email) {
                                    try {
                                        finalJson.put(SocialLoginConstants.TWITTER_EMAIL, email);
                                    } catch (JSONException ignore) {

                                    }
                                    onSocialProviderConnected(result.data.getAuthToken().toString(), finalJson);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    // unable to retrieve email but we send user's info anyways
                                    onSocialProviderConnected(result.data.getAuthToken().toString(), finalJson);
                                }
                            });

                        }

                        @Override
                        public void failure(TwitterException e) {
                            onSocialProviderConnectionFailure(e);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTwitterLoginButton != null) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }
}
