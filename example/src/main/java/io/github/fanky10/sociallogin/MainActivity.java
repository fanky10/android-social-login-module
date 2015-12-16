package io.github.fanky10.sociallogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import io.github.fanky10.sociallogin.module.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Qzxya8qGE5HJV9okWP0u1A";
    private static final String TWITTER_SECRET = "BwvFPux5GUYR0ag5uajdaQTkEVYc8SDogiAHZWKZlQ";

    ProgressDialog progress;

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);
        Log.d("TwitterKit", "starting the onCreate method");

        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage(getResources().getString(R.string.please_wait_login));
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                progress.show();
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model

                final String userName = session.getUserName();
                final long userId = session.getUserId();
                String msg = "@" + userName + " logged in! (#" + userId + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                twitterApiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        String tFullName = userResult.data.name;
                        String tProfileImage = userResult.data.profileImageUrl;
                        Log.d("TwitterKit", "User FullName: " + tFullName);
                        Log.d("TwitterKit", "User Profile: " + tProfileImage);
                     /*   Intent i = new Intent(MainActivity.this, OurRegistration.class);
                        i.putExtra("type", "twitter");
                        i.putExtra("twitter_name", userName);
                        i.putExtra("twitter_id", String.valueOf(userId));
                        i.putExtra("tProfileImage", tProfileImage);
                        i.putExtra("tFullName", tFullName);

                        progress.dismiss();
                        startActivity(i);
                        finish();
                    */
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
                ///////////////////////

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
