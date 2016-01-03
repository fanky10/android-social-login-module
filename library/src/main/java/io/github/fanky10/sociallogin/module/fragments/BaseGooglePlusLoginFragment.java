package io.github.fanky10.sociallogin.module.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import io.github.fanky10.sociallogin.module.interfaces.ISocialLogin;
import io.github.fanky10.sociallogin.module.providers.GooglePlusProvider;

/**
 * Created by carlospienovi1 on 12/30/15.
 */
public abstract class BaseGooglePlusLoginFragment extends Fragment implements
        ISocialLogin {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private GooglePlusProvider mGooglePlusProvider;

    public void doGoogleLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                doGoogleLoginProcess();
            }
        } else {
            doGoogleLoginProcess();
        }
    }

    private void doGoogleLoginProcess() {
        mGooglePlusProvider = new GooglePlusProvider(getActivity());
        mGooglePlusProvider.setCallback(this);

        if (mGooglePlusProvider != null) {
            if (!mGooglePlusProvider.isConnected()) {
                mGooglePlusProvider.connectApiClient();
            }
            mGooglePlusProvider.setUpSignIn();
        }
    }
}
