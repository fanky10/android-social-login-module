package io.github.fanky10.sociallogin;

import org.json.JSONObject;

import io.github.fanky10.sociallogin.module.fragments.BaseTwitterLoginFragment;

/**
 * Created by carlospienovi1 on 12/29/15.
 */
public class TwitterLoginFragment extends BaseTwitterLoginFragment {

    @Override
    protected int getTwitterLoginButtonId() {
        return 0;
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
