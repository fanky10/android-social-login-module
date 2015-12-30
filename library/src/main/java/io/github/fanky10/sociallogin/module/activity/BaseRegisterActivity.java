package io.github.fanky10.sociallogin.module.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.github.fanky10.sociallogin.module.R;
import io.github.fanky10.sociallogin.module.constants.SocialLoginConstants;
import io.github.fanky10.sociallogin.module.utils.CheckNetwork;
import io.github.fanky10.sociallogin.module.utils.ValidateUserInfo;

public abstract class BaseRegisterActivity extends Activity implements View.OnClickListener{
    private EditText edit_nome, edit_email, edit_password;
    private TextView txt_alreadyHave;
    private Button btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_register);
        String email;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            email = extras == null ? "" : extras.getString(SocialLoginConstants.TAG_EMAIL);
        } else {
            email = savedInstanceState.getString(SocialLoginConstants.TAG_EMAIL);
        }

        edit_nome = (EditText) findViewById(R.id.edit_nome);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_email.setText(email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        txt_alreadyHave = (TextView) findViewById(R.id.txt_already_have);
        txt_alreadyHave.setOnClickListener(this);

        btn_registrar = (Button) findViewById(R.id.btn_register);
        btn_registrar.setOnClickListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptCreate() {
        // Store values at the time of the login attempt.
        String name = edit_nome.getText().toString();
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        ValidateUserInfo validate = new ValidateUserInfo();

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            edit_nome.setError(getString(R.string.error_field_required));
            focusView = edit_nome;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            edit_email.setError(getString(R.string.error_field_required));
            focusView = edit_email;
            cancel = true;
        } else if (!validate.isEmailValid(email)) {
            edit_email.setError(getString(R.string.error_invalid_email));
            focusView = edit_email;
            cancel = true;
        } else {
            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password) || !validate.isPasswordValid(password)) {
                edit_password.setError(getString(R.string.error_invalid_password));
                focusView = edit_password;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            submitRegister(name, email, password);
            finish();
        }
    }

    protected abstract void submitRegister(String firstName, String email, String password);

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_register) {
            attemptCreate();
        } else if (i == R.id.txt_already_have) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
