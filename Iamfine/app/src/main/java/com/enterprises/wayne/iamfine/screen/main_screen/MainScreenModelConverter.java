package com.enterprises.wayne.iamfine.screen.main_screen;

import android.content.Context;

import com.enterprises.wayne.iamfine.R;
import com.enterprises.wayne.iamfine.data_model.UserDataModel;
import com.enterprises.wayne.iamfine.data_model.WhoAskedDataModel;
import com.enterprises.wayne.iamfine.helper.TimeFormatter;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.UserViewModel;
import com.enterprises.wayne.iamfine.screen.main_screen.view_model.WhoAskedViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 2/19/2017.
 */

public class MainScreenModelConverter implements MainScreenContract.ModelConverter {

    private TimeFormatter mTimeFormatter;
    private Context mContext;

    public MainScreenModelConverter(Context context, TimeFormatter timeFormatter) {
        mTimeFormatter = timeFormatter;
        mContext = context;
    }

    @Override
    public List<UserViewModel> convertUser(List<UserDataModel> dataModels) {
        List<UserViewModel> viewModels = new ArrayList<>();
        for (UserDataModel dataModel : dataModels)
            viewModels.add(new UserViewModel(
                    dataModel.getId(),
                    dataModel.getName(),
                    mContext.getString(R.string.was_fine_when, mTimeFormatter.getDisplayTime(dataModel.getLastFineData())),
                    dataModel.getProfilePic()));
        return viewModels;
    }

    @Override
    public List<WhoAskedViewModel> convertWhoAsked(List<WhoAskedDataModel> dataModels) {
        List<WhoAskedViewModel> viewModels = new ArrayList<>();
        for (WhoAskedDataModel dataModel : dataModels)
            viewModels.add(new WhoAskedViewModel(
                    dataModel.getUser().getId(),
                    dataModel.getUser().getName(),
                    mTimeFormatter.getDisplayTime(dataModel.getWhenAsked()),
                    dataModel.getUser().getProfilePic()));
        return viewModels;
    }

}
