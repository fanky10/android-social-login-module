package io.github.fanky10.sociallogin;

import com.facebook.FacebookSdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
        userModel.setUsername("email@example.com");
        userModel.setPassword("example");
        userModel.setScope("email");

        new UsersController(this).save(userModel);
    }

    @Override
    public void success(UserModel userModel) {
        Toast.makeText(this, "Success - Scope: " + userModel.getScope(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.google_login_fragment);

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
