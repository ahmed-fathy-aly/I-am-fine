package com.enterprises.wayne.iamfine.common.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Ahmed on 2/14/2017.
 */

@Entity
public class WhoAskedDataModel implements Serializable {
	@Embedded
	private UserDataModel user;
	private long whenAsked;


	@PrimaryKey
	private String userId;

	public WhoAskedDataModel(UserDataModel user, long whenAsked) {
		this.user = user;
		this.whenAsked = whenAsked;
		this.userId = user.getId();
	}

	@Ignore
	public WhoAskedDataModel() {
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDataModel getUser() {
		return user;
	}

	public void setUser(UserDataModel user) {
		this.user = user;
	}

	public long getWhenAsked() {
		return whenAsked;
	}

	public void setWhenAsked(long whenAsked) {
		this.whenAsked = whenAsked;
	}
}

