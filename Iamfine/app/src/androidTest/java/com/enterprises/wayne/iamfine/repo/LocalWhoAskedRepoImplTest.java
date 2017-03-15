package com.enterprises.wayne.iamfine.repo;

import android.support.test.InstrumentationRegistry;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepo;
import com.enterprises.wayne.iamfine.repo.local.LocalWhoAskedRepoImpl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ahmed on 2/18/2017.
 */
public class LocalWhoAskedRepoImplTest {

    LocalWhoAskedRepo localWhoAskedRepo;

    WhoAskedDataModel WHO_ASKED_1 = new WhoAskedDataModel(new UserDataModel("id1", "name1", "mail1", "pp1", 1000), 1000);
    WhoAskedDataModel WHO_ASKED_2 = new WhoAskedDataModel(new UserDataModel("id2", "name2", "mail2", "pp2", 2000), 2000);

    @Before
    public void setup() {
        localWhoAskedRepo = new LocalWhoAskedRepoImpl(InstrumentationRegistry.getTargetContext());
        localWhoAskedRepo.clear();
    }

    @Test
    public void testUpdatingAndReading() {
        List<WhoAskedDataModel> data = Arrays.asList(WHO_ASKED_1, WHO_ASKED_2);
        localWhoAskedRepo.updateWhoAsked(data);

        List<WhoAskedDataModel> whoAsked = localWhoAskedRepo.getWhoAsked();
        Collections.sort(whoAsked, (l, r) -> {return (int)(l.getWhenAsked() - r.getWhenAsked());});
        Assert.assertEquals(2, whoAsked.size());
        verifySameDataModel(WHO_ASKED_1, whoAsked.get(0));
        verifySameDataModel(WHO_ASKED_2, whoAsked.get(1));
    }

    @Test
    public void testAddAndRead(){

        // add an entry and read it back
        localWhoAskedRepo.addWhoAsked(WHO_ASKED_1);
        List<WhoAskedDataModel> whoAsked = localWhoAskedRepo.getWhoAsked();
        Assert.assertEquals(1, whoAsked.size());
        verifySameDataModel(WHO_ASKED_1, whoAsked.get(0));

        // add another and read them back
        localWhoAskedRepo.addWhoAsked(WHO_ASKED_2);
        whoAsked = localWhoAskedRepo.getWhoAsked();
        Collections.sort(whoAsked, (l, r) -> {return (int)(l.getWhenAsked() - r.getWhenAsked());});
        Assert.assertEquals(2, whoAsked.size());
        verifySameDataModel(WHO_ASKED_1, whoAsked.get(0));
        verifySameDataModel(WHO_ASKED_2, whoAsked.get(1));
    }

    private void verifySameDataModel(WhoAskedDataModel expected, WhoAskedDataModel actual) {
        Assert.assertEquals(expected.getWhenAsked(), actual.getWhenAsked());
        Assert.assertEquals(expected.getUser().getEmail(), actual.getUser().getEmail());
        Assert.assertEquals(expected.getUser().getName(), actual.getUser().getName());
        Assert.assertEquals(expected.getUser().getId(), actual.getUser().getId());
        Assert.assertEquals(expected.getUser().getProfilePic(), actual.getUser().getProfilePic());
    }

}