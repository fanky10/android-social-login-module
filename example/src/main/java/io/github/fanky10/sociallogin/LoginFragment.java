package io.github.fanky10.sociallogin;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.fragments.BaseLoginFragment;

/**
 * Created by fanky on 12/30/15.
 */
public class LoginFragment extends BaseLoginFragment {

    private WeakReference<ISocialLogin> socialCallback;

    @Override
    protected void submitLogIn(String email, String password) {
        UserModel found = new UsersController(getActivity()).findByLogin(email, password);
        String message = "Not found, check credentials";

        if (found != null) {
            message = "Success!";
            socialCallback.get().success(found);
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Intent createRegisterIntent() {
        return new Intent(getActivity(), RegisterAccountActivity.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if it's not implemented BOOM
        socialCallback = new WeakReference<ISocialLogin>((ISocialLogin) getActivity());
    }
}
