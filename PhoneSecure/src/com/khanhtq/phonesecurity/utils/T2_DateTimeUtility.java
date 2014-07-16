package com.khanhtq.phonesecurity.utils;

import java.util.Date;

/**
 * Contain function to format Date/time for displaying
 * 
 * @author Khanh Tran
 * 
 */
@SuppressWarnings("deprecation")
public class T2_DateTimeUtility {
	public static String getTimeInString(long time) {
		Date d = new Date(time);
		int sec = d.getSeconds();
		int min = d.getMinutes();
		int hr = d.getHours();
		int day = d.getDate();
		int mo = d.getMonth();
		int yr = d.getYear();
		String ret = ((hr < 10) ? "0" + hr : hr) + ":";
		ret += (min < 10) ? "0" + min : min;
		ret += ":";
		ret += (sec < 10) ? "0" + sec : sec;
		ret += " ";
		Date today = new Date();
		if (day == today.getDate() && mo == today.getMonth()
				&& yr == today.getYear()) {
			ret += " Today ";
		} else {
			ret += (day < 10) ? "0" + day : day;
			ret += "/";
			mo += 1;
			yr += 1900;
			ret += (mo < 10) ? "0" + mo : mo;
			ret += "/" + yr;
		}
		return ret;
	}
}
