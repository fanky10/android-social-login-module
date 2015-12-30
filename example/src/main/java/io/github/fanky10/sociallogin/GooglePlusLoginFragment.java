package io.github.fanky10.sociallogin;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import io.github.fanky10.sociallogin.module.fragments.BaseGooglePlusLoginFragment;

/**
 * Created by carlospienovi1 on 12/30/15.
 */
public class GooglePlusLoginFragment extends BaseGooglePlusLoginFragment {

    Button mGoogleLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_google_login, container, false);

        mGoogleLogin = (Button) view.findViewById(R.id.google_login_button);

        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGoogleLogin();
            }
        });

        return view;
    }

    @Override
    public void onSocialProviderConnected(String token, JSONObject response) {
        Toast.makeText(getContext(), "Logged in as ", Toast.LENGTH_SHORT).show();
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
