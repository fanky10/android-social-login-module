package io.github.fanky10.sociallogin;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.fanky10.sociallogin.module.fragments.BaseTwitterLoginFragment;

/**
 * Created by carlospienovi1 on 12/29/15.
 */
public class TwitterLoginFragment extends BaseTwitterLoginFragment {

    TwitterLoginButton mTwitterLoginButton;

    @Override
    protected int getTwitterLoginButtonId() {
        return R.id.tw_login_button;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_login, container, false);
    }

    @Override
    public void onSocialProviderConnected(String token, JSONObject response) {

    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {

    }

    @Override
    public void onSocialProviderConnectionCanceled() {

    }
}
