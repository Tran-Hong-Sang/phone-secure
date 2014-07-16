package com.khanhtq.phonesecurity.utils;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;

/**
 * @author ****
 *
 */
public class T2_DeleteActivities {
	Activity act;
	public T2_DeleteActivities(Activity act) {
		this.act = act;
	}
	
	/**
	 * delete Call log
	 */
	public void deleteCallLog() {
		act.getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
	}
	
	/**
	 * delete All contact
	 */
	public void deleteAllContact() {
		ContentResolver cr = act.getContentResolver();
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
	            null, null, null, null);
	    while (cur.moveToNext()) {
	        try{
	            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
	            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
	            cr.delete(uri, null, null);
	        }
	        catch(Exception e)
	        {
	            System.out.println(e.getStackTrace());
	        }
	    }
	}
	
	/**
	 * Delete All file
	 */
	public void deleteAllFile(String extension) {
		File path = Environment.getExternalStorageDirectory();
		deleteFile(path, extension);
	}
	
	public static void deleteFile(File parentDir, String ext) {
    	File[] files = parentDir.listFiles();
        for (int i = 0; i < files.length; i++) {
        	if (files[i].isFile() && files[i].getPath().endsWith(ext)) {
        		files[i].delete();
        	}
        	if (files[i].isDirectory()) {
        		deleteFile(new File(files[i].getPath()), ext);
        	}
        }
    }
}
