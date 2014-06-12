package com.gueei.applocker.khanhtq.utils;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.gueei.applocker.khanhtq.models.Message;

/**
 * Utility helps interact with SMS from inbox
 * @author Khanh Tran
 *
 */

public class T2_SMSUtility {
	Activity con;
	public T2_SMSUtility(Activity c){
		this.con = c;
	}
	/**
	 * Get list of original messages from inbox
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<Message> getMessageListFromInbox(){
		ArrayList<Message> smsList = new ArrayList<Message>();

		Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = con.getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext()) {
               String address = cur.getString(cur.getColumnIndex("address"));
               String body = cur.getString(cur.getColumnIndexOrThrow("body"));
               //sms.add("Number: " + address + " .Message: " + body);  
          }
	   return smsList;
	}
	/**
	 * Get list of original messages from sent folder
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<Message> getMessageListFromSent(){
		ArrayList<Message> smsList = new ArrayList<Message>();

       Uri uri = Uri.parse("content://sms/sent");
       Cursor c= con.getContentResolver().query(uri, null, null ,null,null);
       con.startManagingCursor(c);

       // Read the sms data and store it in the list
       if(c.moveToFirst()) {
           for(int i=0; i < c.getCount(); i++) {
        	   Message sms = new Message();
               //sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
               ///sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
               smsList.add(sms);
               c.moveToNext();
           }
       }
       c.close();
	   return smsList;
	}
}