package io.github.fanky10.sociallogin;

import io.github.fanky10.sociallogin.models.UserModel;

/**
 * Created by fanky on 12/30/15.
 */
public interface ISocialLogin {
    void success(UserModel userModel);
    void error(String message);
}
