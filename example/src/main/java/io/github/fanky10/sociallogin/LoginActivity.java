package io.github.fanky10.sociallogin;

import com.facebook.FacebookSdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.fanky10.sociallogin.controllers.UsersController;
import io.github.fanky10.sociallogin.models.UserModel;

public class LoginActivity extends AppCompatActivity implements ISocialLogin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: move to Application
        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_login);

        // save mock User
        UserModel userModel = new UserModel();
        userModel.setUsername("test@email.com");
        userModel.setPassword("abc123");
        userModel.setScope("email");

        new UsersController(this).save(userModel);
    }

    @Override
    public void success(UserModel userModel) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void error(String message) {

    }
}
