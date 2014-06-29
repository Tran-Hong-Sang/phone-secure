package com.khanhtq.phonesecurity.utils;

import java.util.ArrayList;
import java.util.List;

import com.khanhtq.phonesecurity.models.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Khanh Tran
 *
 */
public class T2_SQLiteUtility {
	
	private static final String KEY_ROWID = "_id";
	private static final String KEY_FROM = "from";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_DATE = "date";
	private static final String KEY_TYPE = "type";
	private static final String KEY_READ = "read";
	private static final String KEY_STATUS = "status";
	private static final String KEY_BODY = "body";
	
	private static final String DATABASE_NAME = "phone_secure";
	private static final String DATABASE_TABLE = "message";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper dbHelper;
	private Context context;
	private SQLiteDatabase sqliteDB;
	
	private static class DbHelper extends SQLiteOpenHelper {
		
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_FROM + " TEXT NOT NULL, " +
					KEY_ADDRESS + " TEXT NOT NULL, " +
					KEY_DATE + " LONG NOT NULL, " +
					KEY_TYPE + " INTEGER NOT NULL, " +
					KEY_READ + " INTEGER NOT NULL, " +
					KEY_STATUS + " INTEGER NOT NULL," +
					KEY_BODY + " TEXT NOT NULL" +
					")");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	
	/**
	 * @param context
	 * Constructor
	 */
	public T2_SQLiteUtility(Context context) {
		this.context = context;
	}
	
	// mode = readDB or writeDB
	public T2_SQLiteUtility open(String mode) {
		this.dbHelper = new DbHelper(context);
		if ("writeDB".equals(mode)) {
			this.sqliteDB = dbHelper.getWritableDatabase();
		} else {
			this.sqliteDB = dbHelper.getReadableDatabase();
		}
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void addMessage(Message message) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_FROM, message.getFrom());
		cv.put(KEY_ADDRESS, message.getAddress());
		cv.put(KEY_DATE, message.getDate());
		cv.put(KEY_TYPE, message.getType());
		cv.put(KEY_READ, message.getRead());
		cv.put(KEY_STATUS, message.getStatus());
		cv.put(KEY_BODY, message.getBody());
		sqliteDB.insert(DATABASE_TABLE, null, cv);
	}
	
	public void deleteMessageById(int id) {
		sqliteDB.delete(DATABASE_TABLE, KEY_ROWID + " = ?", new String[] { String.valueOf(id) });
	}
	
	// type = 0: get all
	// type = 1: inbox
	// type = 2: sent
	public List<Message> getMessagesByType(int type) {
		List<Message> messages = new ArrayList<Message>();
        String query = "SELECT  * FROM " + DATABASE_TABLE;
        if (type != 0) {
        	query += "WHERE " + KEY_TYPE + " = " + type;
        }
        Log.i(T2_SQLiteUtility.class.getName(), query);
        Cursor cursor = sqliteDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
        	Message message = null;
            do {
                message = new Message();
                message.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
                message.setFrom(cursor.getString(cursor.getColumnIndex(KEY_FROM)));
                message.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                message.setBody(cursor.getString(cursor.getColumnIndex(KEY_BODY)));
                message.setDate(cursor.getLong(cursor.getColumnIndex(KEY_DATE)));
                message.setRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
                message.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                message.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
                messages.add(message);
            } while (cursor.moveToNext());
        }
		return messages;
	}
	
}
