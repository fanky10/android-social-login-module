package io.github.fanky10.sociallogin;

import com.facebook.FacebookSdk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;
import io.github.fanky10.sociallogin.module.activity.BaseLoginActivity;

public class LoginActivity extends BaseLoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // save mock User
        UserModel userModel = new UserModel();
        userModel.setUsername("test@email.com");
        userModel.setPassword("abc123");
        userModel.setScope("email");

        new UsersController(this).save(userModel);
    }

    @Override
    protected void submitLogIn(String email, String password) {
        UserModel found = new UsersController(this).findByLogin(email, password);
        String message = "Not found, check credentials";

        if (found != null) {
            message = "Success!";
            // TODO: launch LoggedinActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Intent createRegisterIntent() {
        return new Intent(this, RegisterAccountActivity.class);
    }
}
