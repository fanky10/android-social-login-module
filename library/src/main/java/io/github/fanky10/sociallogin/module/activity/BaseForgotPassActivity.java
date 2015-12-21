package io.github.fanky10.sociallogin.module.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class BaseForgotPassActivity extends Activity implements View.OnClickListener{
       private EditText edit_email;
       private TextView txt_remembered;
       private Button btn_recover;
       private ProgressDialog ringProgressDialog;
        private ForgotPassTask mForgotTask = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.base_activity_forgot_pass);

            String email;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                email = (extras == null) ? "" : extras.getString(SocialLoginConstants.TAG_EMAIL);
            } else {
                email = savedInstanceState.getString(SocialLoginConstants.TAG_EMAIL);
            }

            edit_email = (EditText) findViewById(R.id.edit_email);
            edit_email.setText(email);

            txt_remembered = (TextView) findViewById(R.id.txt_remembered);
            txt_remembered.setOnClickListener(this);

            btn_recover = (Button) findViewById(R.id.btn_recover);
            btn_recover.setOnClickListener(this);

            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        /**
         * Attempts to recover the account specified by the login form.
         * If there are form errors (invalid email, missing fields, etc.), the
         * errors are presented and no actual login attempt is made.
         */
    public void attemptRecover() {
        // Store values at the time of the login attempt.
        String email = edit_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        ValidateUserInfo validate = new ValidateUserInfo();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edit_email.setError(getString(R.string.error_field_required));
            focusView = edit_email;
            cancel = true;
        } else if (validate.isEmailValid(email)) {
            edit_email.setError(getString(R.string.error_invalid_email));
            focusView = edit_email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //TODO Recover account logic
            // Show a progress spinner, and kick off a background task to
            // perform the user recover info attempt.
            mForgotTask = new ForgotPassTask(email);
            mForgotTask.execute((Void) null);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_recover) {
            attemptRecover();

        } else if (i == R.id.txt_remembered) {
            startActivity(new Intent(BaseForgotPassActivity.this, BaseLoginActivity.class));
            finish();

        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ForgotPassTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;

        ForgotPassTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: check if account already exists against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: if there's no account registered, register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mForgotTask = null;
            CheckNetwork checkNetwork = new CheckNetwork();
            if (checkNetwork.isConnected(BaseForgotPassActivity.this) && success) {
                Toast.makeText(BaseForgotPassActivity.this, edit_email.getText() + " your new passwork is ...", Toast.LENGTH_SHORT).show();//Or whatever your recovery method is...
            } else {
                Toast.makeText(BaseForgotPassActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mForgotTask = null;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BaseForgotPassActivity.this, BaseLoginActivity.class));
        finish();
    }
}

