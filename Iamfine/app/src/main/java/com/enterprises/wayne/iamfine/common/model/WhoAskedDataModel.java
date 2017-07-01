package com.enterprises.wayne.iamfine.common.model;

import java.io.Serializable;

/**
 * Created by Ahmed on 2/14/2017.
 */

public class WhoAskedDataModel implements Serializable {
	private UserDataModel user;
	private long whenAsked;

	public WhoAskedDataModel(UserDataModel user, long whenAsked) {
		this.user = user;
		this.whenAsked = whenAsked;
	}

	public WhoAskedDataModel() {
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

