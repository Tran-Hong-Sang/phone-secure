package com.khanhtq.phonesecurity.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class T2_ContactUtils {
	/**
	 * Get name in contact from number
	 * @param c
	 * @param phoneNumber
	 * @return
	 */
	public static String getContactName(Context c, String phoneNumber) {
		String name = phoneNumber;
		ContentResolver cr = c.getContentResolver();
		Cursor cur = cr.query(
				Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
						Uri.encode(phoneNumber)),
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cur != null) {
			try {
				if (cur.getCount() > 0) {
					if (cur.moveToFirst()) {
						name = cur.getString(cur
								.getColumnIndex(PhoneLookup.DISPLAY_NAME));
					}
				}
			} finally {
				cur.close();
			}
		}
		return name;
	}
	/**
	 * Get all contactlist from contacts
	 */

	public static String getOriginalAddress(String address) {
		if(address.contains("+84")) address = address.substring(3);
		else address = address.substring(1);
		return address;
	}
}
