package io.github.fanky10.sociallogin.dao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.github.fanky10.sociallogin.models.UserModel;

/**
 * Created by fanky on 12/21/15.
 */
@RunWith(AndroidJUnit4.class)
public class UsersDaoTest extends InstrumentationTestCase {
    public static final String TAG = "UsersDaoTest";

    private UserModel mUserModel;
    private UsersDAO mUsersDAO;
    private Context mContext;

    @Before
    public void setUp() {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mContext = getInstrumentation().getTargetContext().getApplicationContext();
        mUsersDAO = new UsersDAO(mContext);
        mUserModel = new UserModel();
        mUserModel.setScope("premium");
        mUserModel.setUsername("mock");
        mUserModel.setPassword("mockedPasswd");//TODO: add md5

        mUsersDAO.save(mUserModel);
    }

    @After
    public void tearDown() {
        mUsersDAO.delete(mUserModel.getUsername());
    }


    @Test
    public void testFindOne() {
        UserModel found = mUsersDAO.findOne(mUserModel.getUsername());
        assertTrue(found != null);
        assertTrue(found.getPassword().equals(mUserModel.getPassword()));
        assertTrue(found.getScope().equals(mUserModel.getScope()));
        assertTrue(found.getUsername().equals(mUserModel.getUsername()));
    }

    @Test
    public void testFindAll() {
        List<UserModel> found = mUsersDAO.findAll();
        assertTrue(found != null && !found.isEmpty());
    }
}