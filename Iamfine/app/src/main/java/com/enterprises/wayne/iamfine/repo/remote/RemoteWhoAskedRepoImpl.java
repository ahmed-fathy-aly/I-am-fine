package com.enterprises.wayne.iamfine.repo.remote;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.exception.NetworkErrorException;
import com.enterprises.wayne.iamfine.exception.UnKnownErrorException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

/**
 * returns dummy data till it's connected to the backend
 * Created by Ahmed on 2/18/2017.
 */
public class RemoteWhoAskedRepoImpl implements RemoteWhoAskedRepo {
    @Override
    public List<WhoAskedDataModel> getWhoAsked() throws NetworkErrorException, UnKnownErrorException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new WhoAskedDataModel(
                        new UserDataModel("1", "Hamada", "hamada@gmail.com", "", System.currentTimeMillis() - 100000),
                        System.currentTimeMillis() - 100000),
                new WhoAskedDataModel(new UserDataModel("2", "Aba", "aba@gmail.com", "", System.currentTimeMillis() - 200000),
                        System.currentTimeMillis() - 200000),
                new WhoAskedDataModel(new UserDataModel("3", "Yara", "yara@gmail.com", "", System.currentTimeMillis() - 400000),
                        System.currentTimeMillis() - 400000));
    }

    /**
     * TODO convert to a cloud function
     * this repo depends on tokens and stuff
     */
    @Override
    public Void sayIAmFine() throws NetworkErrorException, UnKnownErrorException {

        // get current user id
        FirebaseUser currentUser = FirebaseAuth
                .getInstance()
                .getCurrentUser();
        if (currentUser == null){
            throw new UnKnownErrorException();
        }
        String currentUserId = currentUser.getUid();

        // update fine date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long currTime = cal.getTimeInMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Task<Void> request = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(currentUserId)
                .child("lastFineData")
                .setValue(currTime)
                .addOnCompleteListener(task -> countDownLatch.countDown());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw  new UnKnownErrorException();
        }

        // check result exception's type
        if (!request.isSuccessful()) {
            if (request.getException() instanceof FirebaseNetworkException)
                throw new NetworkErrorException();
            throw new UnKnownErrorException();
        }

        // request successful
        return null;
    }
}
