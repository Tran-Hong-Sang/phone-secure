package com.khanhtq.phonesecurity.utils;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
	 * Send a message
	 */
	public static void sendMessage(Message msg, Context cont){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(msg.getAddress(), null, msg.getBody(), null, null);
		T2_SQLiteUtility dbU = new T2_SQLiteUtility(cont).open("writeDB");
		dbU.addMessage(msg);
		dbU.close();
		
	}
}