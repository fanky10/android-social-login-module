package io.github.fanky10.sociallogin;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.fragments.BaseTwitterLoginFragment;

/**
 * Created by carlospienovi1 on 12/29/15.
 */
public class TwitterLoginFragment extends BaseTwitterLoginFragment {

    TwitterLoginButton mTwitterLoginButton;
    private WeakReference<ISocialLogin> socialCallback;

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
        String email = response.optString(SocialLoginConstants.TWITTER_EMAIL);
        String name = response.optString(SocialLoginConstants.TWITTER_NAME);
        String screenName = response.optString(SocialLoginConstants.TWITTER_SCREEN_NAME);

        UserModel userModel = new UserModel();
        userModel.setUsername(email);
        userModel.setPassword("");
        userModel.setScope("twitter");

        new UsersController(getActivity()).save(userModel);
        socialCallback.get().success(userModel);
        Toast.makeText(getContext(), "Logged in as " + name + " / " + screenName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialProviderConnectionFailure(Exception e) {
        Toast.makeText(getContext(), "Connection failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        socialCallback.get().error("Connection Failed");
    }

    @Override
    public void onSocialProviderConnectionCanceled() {
        Toast.makeText(getContext(), "Login canceled", Toast.LENGTH_SHORT).show();
        socialCallback.get().error("User canceled");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if it's not implemented BOOM
        socialCallback = new WeakReference<ISocialLogin>((ISocialLogin) getActivity());
    }
}
