package com.khanhtq.phonesecurity.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

import com.khanhtq.phonesecurity.models.Message;

/**
 * Utility helps interact with SMS from inbox
 * 
 * @author Khanh Tran
 * 
 */

public class T2_SMSUtility {
	Activity con;

	public T2_SMSUtility(Activity c) {
		this.con = c;
	}

	/**
	 * Get list of all original messages from inbox
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<Message> getMessageListFromInbox() {
		ArrayList<Message> lstSms = new ArrayList<Message>();
		Message objSms = new Message();
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = con.getContentResolver();

		Cursor c = cr.query(message, null, null, null, null);
		con.startManagingCursor(c);
		int totalSMS = c.getCount();

		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {

				objSms = new Message();
				objSms.set_id(c.getInt(c.getColumnIndexOrThrow("_id")));
				objSms.setAddress(c.getString(c
						.getColumnIndexOrThrow("address")));
				objSms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setRead(c.getInt(c.getColumnIndex("read")));
				objSms.setDate(c.getLong(c.getColumnIndexOrThrow("date")));
				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					objSms.setType(Message.TYPE_INBOX);
				} else if (c.getString(c.getColumnIndexOrThrow("type"))
						.contains("2")) {
					objSms.setType(Message.TYPE_SENT);
				}

				lstSms.add(objSms);
				c.moveToNext();
			}
		}
		c.close();

		return lstSms;
	}

	public static CharSequence getAddress(Context c, String phoneNumber) {
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

}