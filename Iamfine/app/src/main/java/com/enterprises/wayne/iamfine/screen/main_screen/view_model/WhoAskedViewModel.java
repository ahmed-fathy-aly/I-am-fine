package com.enterprises.wayne.iamfine.screen.main_screen.view_model;

/**
 * Created by Ahmed on 2/11/2017.
 */

public class WhoAskedViewModel {

    private String id;
    private String displayName;
    private String timeStr;
    private String imageUrl;

    public WhoAskedViewModel(String id,String displayName, String timeStr, String imageUrl) {
        this.id = id;
        this.displayName = displayName;
        this.timeStr = timeStr;
        this.imageUrl = imageUrl;
    }

    public WhoAskedViewModel() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId(){
        return id;
    }
}
