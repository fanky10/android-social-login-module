package io.github.fanky10.sociallogin.module.interfaces;

import org.json.JSONObject;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public interface ISocialLogin {

    void onSocialProviderConnected(String token, JSONObject response);

    void onSocialProviderConnectionFailure(Exception e);

    void onSocialProviderConnectionCanceled();
}
