package io.github.fanky10.sociallogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.fanky10.sociallogin.module.constants.enums.ExternalLoginProviders;
import io.github.fanky10.sociallogin.module.fragments.BaseFacebookLoginFragment;

/**
 * Created by carlospienovi1 on 12/16/15.
 */
public class FacebookLoginFragment extends BaseFacebookLoginFragment {

    @Override
    protected String[] getPermissions() {
        return new String[]{"email"};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        view.findViewById(R.id.fb_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFacebookLogin();
            }
        });

        return view;
    }

    @Override
    public void onSocialProviderConnected(ExternalLoginProviders provider, String token, String email, String firstName, String lastName) {
        Log.d("GATA", "connected");
        Log.d("GATA", "token = " + token);
        Log.d("GATA", "email = " + email);
        Log.d("GATA", "name = " + firstName);
        Log.d("GATA", "last name = " + lastName);
    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {
        Log.d("GATA", "connection failure " + e);
    }

}
