package com.enterprises.wayne.iamfine.screen.main_screen.view_model;

/**
 * Created by Ahmed on 2/11/2017.
 */

public class UserViewModel {

    private String id;
    private String displayName;
    private String lastFineTimeStr;
    private String imageUrl;

    public UserViewModel(String id, String displayName, String lastFineTimeStr, String imageUrl) {
        this.id = id;
        this.displayName = displayName;
        this.lastFineTimeStr = lastFineTimeStr;
        this.imageUrl = imageUrl;
    }

    public UserViewModel(){
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLastFineTimeStr() {
        return lastFineTimeStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
