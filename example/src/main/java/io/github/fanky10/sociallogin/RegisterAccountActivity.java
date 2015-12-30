package io.github.fanky10.sociallogin;

import android.widget.Toast;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.activity.BaseRegisterActivity;

/**
 * Created by fanky on 12/30/15.
 */
public class RegisterAccountActivity extends BaseRegisterActivity {

    @Override
    protected void submitRegister(String firstName, String email, String password) {
        UserModel userModel = new UserModel();
        userModel.setUsername(email);
        userModel.setPassword(password);
        userModel.setScope("register");

        new UsersController(this).save(userModel);
        Toast.makeText(this, "New account saved!", Toast.LENGTH_LONG).show();
    }
}
