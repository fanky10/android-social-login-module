package io.github.fanky10.sociallogin.module.fragments;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTwitterLoginButton != null) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }
}
