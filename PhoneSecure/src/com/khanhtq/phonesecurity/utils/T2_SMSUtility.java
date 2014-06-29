package com.khanhtq.phonesecurity.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;

import com.khanhtq.phonesecurity.models.Message;

/**
 * Utility helps interact with SMS from inbox
 * 
 * @author Khanh Tran
 * 
 */

public class T2_SMSUtility {
	
	/**
	 * Get list of all original messages from inbox
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<Message> getMessageListFromInbox(Activity activity) {
		ArrayList<Message> lstSms = new ArrayList<Message>();
		Message objSms = new Message();
		Uri msgURI = Uri.parse("content://sms/");
		ContentResolver cr = activity.getContentResolver();

		Cursor c = cr.query(msgURI, null, null, null, null);
		activity.startManagingCursor(c);
		int totalSMS = c.getCount();

		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {

				objSms = new Message();
				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					objSms.setType(Message.TYPE_INBOX);
				} else if (c.getString(c.getColumnIndexOrThrow("type"))
						.contains("2")) {
					objSms.setType(Message.TYPE_SENT);
				} else continue;
				objSms.set_id(c.getInt(c.getColumnIndexOrThrow("_id")));
				objSms.setAddress(c.getString(c
						.getColumnIndexOrThrow("address")));
				objSms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setRead(c.getInt(c.getColumnIndex("read")));
				objSms.setDate(c.getLong(c.getColumnIndexOrThrow("date")));
				lstSms.add(objSms);
				c.moveToNext();
			}
		}
		c.close();

		return lstSms;
	}
	/**
	 * Get name in contact from number
	 * @param c
	 * @param phoneNumber
	 * @return
	 */
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
	/**
	 * Send a message
	 */
	public void sendMessage(Message msg){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(msg.getAddress(), null, msg.getBody(), null, null);
	}
}