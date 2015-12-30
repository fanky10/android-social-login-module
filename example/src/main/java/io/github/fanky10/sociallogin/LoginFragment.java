package io.github.fanky10.sociallogin;

import android.content.Intent;
import android.widget.Toast;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.fragments.BaseLoginFragment;

/**
 * Created by fanky on 12/30/15.
 */
public class LoginFragment extends BaseLoginFragment {

    @Override
    protected void submitLogIn(String email, String password) {
        UserModel found = new UsersController(getActivity()).findByLogin(email, password);
        String message = "Not found, check credentials";

        if (found != null) {
            message = "Success!";
            // TODO: launch LoggedinActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Intent createRegisterIntent() {
        return new Intent(getActivity(), RegisterAccountActivity.class);
    }
}
