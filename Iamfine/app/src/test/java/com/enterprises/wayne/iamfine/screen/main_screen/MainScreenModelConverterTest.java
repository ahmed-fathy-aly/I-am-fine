package com.enterprises.wayne.iamfine.screen.main_screen;

import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by Ahmed on 2/19/2017.
 */
public class MainScreenModelConverterTest {

    @Mock
    TimeFormatter timeFormatter;
    MainScreenContract.ModelConverter converter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        converter = new MainScreenModelConverter(timeFormatter);
    }

    @Test
    public void testConvertUser(){
        UserDataModel DATA_MODEL = new UserDataModel("id", "name", "email", "pp", 100);
        when(timeFormatter.getDisplayTime(100)).thenReturn("e");

        List<UserViewModel> viewModelList = converter.convertUser(Arrays.asList(DATA_MODEL));

        Assert.assertEquals(1, viewModelList.size());
        UserViewModel viewModel = viewModelList.get(0);
        Assert.assertEquals("id", viewModel.getId());
        Assert.assertEquals("name", viewModel.getDisplayName());
        Assert.assertEquals("pp", viewModel.getImageUrl());
        Assert.assertEquals("e", viewModel.getLastFineTimeStr());
    }

    @Test
    public void testConvertWhoAsked(){
        WhoAskedDataModel DATA_MODEL = new WhoAskedDataModel(new UserDataModel("id", "name", "email", "pp", -1), 100);
        when(timeFormatter.getDisplayTime(100)).thenReturn("e");

        List<WhoAskedViewModel> viewModelList = converter.convertWhoAsked(Arrays.asList(DATA_MODEL));

        Assert.assertEquals(1, viewModelList.size());
        WhoAskedViewModel viewModel = viewModelList.get(0);
        Assert.assertEquals("id", viewModel.getId());
        Assert.assertEquals("name", viewModel.getDisplayName());
        Assert.assertEquals("pp", viewModel.getImageUrl());
        Assert.assertEquals("e", viewModel.getTimeStr());
    }

}