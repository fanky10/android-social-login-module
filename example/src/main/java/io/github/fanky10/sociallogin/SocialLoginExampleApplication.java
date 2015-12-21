package io.github.fanky10.sociallogin;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import android.app.Application;
import android.content.Context;

import io.fabric.sdk.android.Fabric;

/**
 * Created by fanky on 12/21/15.
 */
public class SocialLoginExampleApplication extends Application {

    private static SocialLoginExampleApplication instance;

    public static SocialLoginExampleApplication getInstance() {
        return instance;
    }

    public SocialLoginExampleApplication() {
        instance = this;
    }
}
