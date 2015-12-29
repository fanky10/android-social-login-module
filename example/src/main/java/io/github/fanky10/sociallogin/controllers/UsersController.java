package io.github.fanky10.sociallogin.controllers;

import android.content.Context;

import java.util.List;

import io.github.fanky10.sociallogin.dao.UsersDAO;
import io.github.fanky10.sociallogin.models.UserModel;

/**
 * Created by facundo.scoccia on 12/29/2015.
 */
public class UsersController {
    private UsersDAO usersDAO;

    public UsersController(Context context) {
        usersDAO = new UsersDAO(context);
    }

    public boolean isUserLoggedIn() {
        // TODO: get data from Shared Prefs
        return false;
    }

    public void loginUser(UserModel userModel) {
        // TODO: add data to Shared Prefs
    }

    public UserModel findUserByLogin(String email, String password) {
        List<UserModel> users = usersDAO.findAll();
        UserModel userModelFound = null;
        // draft find user.
        for (UserModel um: users) {
            if (um.getUsername().equals(email) && um.getPassword().equals(password)) {
                userModelFound = um;
                break;
            }
        }
        return userModelFound;
    }

    public UserModel findUserBySocialLogin(String email, String scope) {
        List<UserModel> users = usersDAO.findAll();
        UserModel userModelFound = null;
        // draft find user.
        for (UserModel um: users) {
            if (um.getUsername().equals(email) && um.getScope().equals(scope)) {
                userModelFound = um;
                break;
            }
        }
        return userModelFound;
    }

    public void addUser(UserModel userModel) {

    }


}
