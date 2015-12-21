package io.github.fanky10.sociallogin;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.fragments.BaseSocialLoginFragment;
import io.github.fanky10.sociallogin.module.interfaces.IFacebook;

/**
 * Created by carlospienovi1 on 12/16/15.
 */
public class SocialLoginFragment extends BaseSocialLoginFragment {

    private Button mFacebookLogin;

    @Override
    protected int getTwitterLoginButtonId() {
        return R.id.tw_login_button;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        mFacebookLogin = (Button) view.findViewById(R.id.fb_login_button);

        mFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFacebookLogin(new IFacebook() {
                    @Override
                    public String[] getPermissions() {
                        return new String[]{SocialLoginConstants.FACEBOOK_EMAIL};
                    }

                    @Override
                    public String[] getProfileInfo() {
                        return new String[]{
                                SocialLoginConstants.FACEBOOK_EMAIL,
                                SocialLoginConstants.FACEBOOK_FIRST_NAME,
                                SocialLoginConstants.FACEBOOK_LAST_NAME};
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onSocialProviderConnected(String token, JSONObject response) {
        String email = response.optString(SocialLoginConstants.FACEBOOK_EMAIL);
        String firstName = response.optString(SocialLoginConstants.FACEBOOK_FIRST_NAME);
        String lastName = response.optString(SocialLoginConstants.FACEBOOK_LAST_NAME);

        Toast.makeText(getContext(), "Logged in as " + firstName + " " + lastName + " / " + email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {
        Toast.makeText(getContext(), "Connection failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialProviderConnectionCanceled() {
        Toast.makeText(getContext(), "Login canceled", Toast.LENGTH_SHORT).show();
    }

}
