package com.enterprises.wayne.iamfine.main_screen.search_users.view;

public class UserCardData {
	private String id;
	private String displayName;
	private String lastFineTimeStr;
	private String imageUrl;

	public UserCardData(String id, String displayName, String lastFineTimeStr, String imageUrl) {
		this.id = id;
		this.displayName = displayName;
		this.lastFineTimeStr = lastFineTimeStr;
		this.imageUrl = imageUrl;
	}

	public UserCardData(){
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
