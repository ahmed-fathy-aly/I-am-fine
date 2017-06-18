package com.enterprises.wayne.iamfine.data_model;

import java.io.Serializable;

/**
 * Created by Ahmed on 2/12/2017.
 */

public class UserDataModel implements Serializable{
    private String id;
    private String name;
    private String email;
    private String profilePic;
    private long lastFineData;

    public UserDataModel(String id, String name, String email, String profilePic, long  lastFineData) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
        this.lastFineData = lastFineData;
    }

    public UserDataModel(){

    }

    public UserDataModel(String id) {
        this.id = id;
    }

	public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public long getLastFineData() {
        return lastFineData;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setLastFineData(long lastFineData) {
        this.lastFineData = lastFineData;
    }
}
