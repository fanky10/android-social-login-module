package io.github.fanky10.sociallogin;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.fragments.BaseGooglePlusLoginFragment;

/**
 * Created by carlospienovi1 on 12/30/15.
 */
public class GooglePlusLoginFragment extends BaseGooglePlusLoginFragment {

    private Button mGoogleLogin;
    private WeakReference<ISocialLogin> socialCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_login, container, false);

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
        String email = response.optString(SocialLoginConstants.GOOGLE_EMAIL);
        String firstName = response.optString(SocialLoginConstants.GOOGLE_FIRST_NAME);
        String lastName = response.optString(SocialLoginConstants.GOOGLE_LAST_NAME);

        UserModel userModel = new UserModel();
        userModel.setUsername(email);
        userModel.setPassword("");
        userModel.setScope("google");

        new UsersController(getActivity()).save(userModel);
        socialCallback.get().success(userModel);
        Toast.makeText(getContext(), "Logged in as " + firstName + " " + lastName + " / " + email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {
        socialCallback.get().error("Connection Failed");
        Toast.makeText(getContext(), "Connection failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialProviderConnectionCanceled() {
        socialCallback.get().error("User canceled");
        Toast.makeText(getContext(), "Login canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if it's not implemented BOOM
        socialCallback = new WeakReference<ISocialLogin>((ISocialLogin) getActivity());
    }
}
