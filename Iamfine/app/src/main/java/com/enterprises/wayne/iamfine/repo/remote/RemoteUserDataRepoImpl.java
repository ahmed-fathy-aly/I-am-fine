package com.enterprises.wayne.iamfine.repo.remote;

import android.util.Log;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * returns dummy data till it's connected to the backend
 * Created by Ahmed on 2/18/2017.
 */
public class RemoteUserDataRepoImpl implements RemoteUserDataRepo {
    @Override
    public List<UserDataModel> searchUsers(String searchStr) throws NetworkErrorException, UnKnownErrorException {
        List<UserDataModel> result = new ArrayList<>();
        CountDownLatch queryDone = new CountDownLatch(1);
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .orderByChild("name")
                .startAt(searchStr)
                .endAt(searchStr + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("Game", "onDataChange " + dataSnapshot.getChildrenCount());
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            UserDataModel user = child.getValue(UserDataModel.class);
                            result.add(user);
                        }
                        queryDone.countDown();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Game", "onCancelled");
                        queryDone.countDown();
                    }
                });
        try {
            queryDone.await();
        } catch (InterruptedException e) {
        }
        return result;
    }

    @Override
    public List<UserDataModel> getSuggestedUsers() throws NetworkErrorException, UnKnownErrorException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new UserDataModel("1", "Hamada", "hamada@gmail.com", "", System.currentTimeMillis() - 100000),
                new UserDataModel("2", "Aba", "aba@gmail.com", "", System.currentTimeMillis() - 200000),
                new UserDataModel("3", "Yara", "yara@gmail.com", "", System.currentTimeMillis() - 400000));
    }

    @Override
    public boolean askIfUserIsFine(String userId) throws NetworkErrorException, UnKnownErrorException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
