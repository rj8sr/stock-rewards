package com.way.stock.rewards.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	public static String getCurrentDateTime() {
		Timestamp currentTimeStamp = null;
		Calendar cal = Calendar.getInstance();
		currentTimeStamp = new Timestamp(cal.getTime().getTime());
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(currentTimeStamp);
	}

	public static String getTimeStampFromString(String signedAt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf1.format(sdf.parse(signedAt));
	}

	public static String getTimeStampFromResponseDate(String submittedAt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf1.format(sdf.parse(submittedAt));
	}

}
