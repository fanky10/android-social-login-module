package io.github.fanky10.sociallogin.module.interfaces;

import io.github.fanky10.sociallogin.module.constants.enums.ExternalLoginProviders;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public interface ISocialLogin {

    public void onSocialProviderConnected(ExternalLoginProviders provider, String token, String email, String firstName, String lastName);

    public void onSocialProviderConnectionFailure(Exception e);
}
