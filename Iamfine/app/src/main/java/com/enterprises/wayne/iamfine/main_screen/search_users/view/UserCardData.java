package com.enterprises.wayne.iamfine.main_screen.search_users.view;

public class UserCardData {
	private String id;
	private String displayName;
	private String timeStr;
	private String imageUrl;
	private AskAboutButtonState askAboutButtonState;

	public UserCardData(String id, String displayName, String lastFineTimeStr, String imageUrl, AskAboutButtonState askAboutButtonState) {
		this.id = id;
		this.displayName = displayName;
		this.timeStr = lastFineTimeStr;
		this.imageUrl = imageUrl;
		this.askAboutButtonState = askAboutButtonState;
	}

	public UserCardData() {
	}

	public String getId() {
		return id;
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

	public AskAboutButtonState getAskAboutButtonState() {
		return askAboutButtonState;
	}

	public void setAskAboutButtonState(AskAboutButtonState askAboutButtonState) {
		this.askAboutButtonState = askAboutButtonState;
	}

	public enum AskAboutButtonState {
		ENABLED,
		LOADING,
		ASKED,
		HIDDEN
	}
}
