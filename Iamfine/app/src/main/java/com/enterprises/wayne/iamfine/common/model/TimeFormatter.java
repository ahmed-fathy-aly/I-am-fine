package com.enterprises.wayne.iamfine.common.model;

/**
 * Created by Ahmed on 2/14/2017.
 */

public class TimeFormatter {

	public String getDisplayTime(long timeInMillis) {
		long curr = System.currentTimeMillis();
		long diff = curr - timeInMillis;

		// less than a minute
		long SECOND = 1000l;
		long MINUTE = 60l * SECOND;
		if (diff <= 60 * 1000)
			return "Just now";

		// less than an hour
		long HOUR = 60l * MINUTE;
		if (diff <= HOUR) {
			int nMinutes = (int) (diff / MINUTE);
			if (nMinutes > 1)
				return nMinutes + " minutes ago";
			else
				return "a minute ago";
		}

		// less than a day
		long DAY = 24l * HOUR;
		if (diff <= DAY) {
			int nHours = (int) (diff / HOUR);
			if (nHours > 1)
				return nHours + " hours ago";
			else
				return "an hour ago";
		}

		// many day
		int nDays = (int) (diff / DAY);
		if (nDays > 1)
			return nDays + " days ago";
		else
			return "yesterday";
	}
}
