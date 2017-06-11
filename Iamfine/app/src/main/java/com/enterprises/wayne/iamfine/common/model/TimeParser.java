package com.enterprises.wayne.iamfine.common.model;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeParser {

	/**
	 * @param timeStr like 2017-06-11T10:51:40.741Z
	 * @return milliseconds respresenting that time or -1 if the format is incorrect
	 */
	public long parseServerTime(@Nullable String timeStr) {
		if (timeStr == null) {
			return -1;
		}

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		try {
			return df.parse(timeStr).getTime();
		} catch (ParseException e) {
			return -1;
		}
	}
}
