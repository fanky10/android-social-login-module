package io.github.fanky10.sociallogin.module.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.fanky10.sociallogin.module.R;

public class BaseForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_forgot_pass);
    }
}
