package com.enterprises.wayne.iamfine.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.enterprises.wayne.iamfine.repo.local.AuthenticatedUserRepoImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AuthenticatedUserRepoImplTest {

    private AuthenticatedUserRepoImpl repo;
    private SharedPreferences prefs;

    @Before
    public void setup(){
        prefs =
                InstrumentationRegistry.getTargetContext().getSharedPreferences("test", Context.MODE_PRIVATE);

        repo = new AuthenticatedUserRepoImpl(prefs);
    }

    @After
    public void clear(){
        prefs.edit().clear().commit();
    }

    @Test
    public void testSaveAndLoad(){
        // initially empty
        assertEquals(null, repo.getToken());
        assertEquals(null, repo.getUserId());

        // save somthing
        repo.saveUser("42", "tok");

        // read it back
        assertEquals("42", repo.getUserId());
        assertEquals("tok", repo.getToken());


        // save something else
        repo.saveUser("43", "toktok");

        // read it back
        assertEquals("43", repo.getUserId());
        assertEquals("toktok", repo.getToken());

        // clear
        repo.clear();

        // eventually empty
        assertEquals(null, repo.getToken());
        assertEquals(null, repo.getUserId());

    }
}
