package com.khanhtq.phonesecurity.utils;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.khanhtq.phonesecurity.models.Message;

/**
 * 
 * @author Khanh Tran
 * 
 */
public class T2_SQLiteUtility {

	private static final String KEY_ROWID = "_id";
	private static final String KEY_FROM = "_from";
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
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " IF NOT EXIST (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_FROM
					+ " TEXT NOT NULL, " + KEY_ADDRESS + " TEXT NOT NULL, "
					+ KEY_DATE + " LONG NOT NULL, " + KEY_TYPE
					+ " INTEGER NOT NULL, " + KEY_READ + " INTEGER NOT NULL, "
					+ KEY_STATUS + " INTEGER NOT NULL," + KEY_BODY
					+ " TEXT NOT NULL" + ")");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	/**
	 * @param context
	 *            Constructor
	 */
	static public T2_SQLiteUtility instance = null;

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

	/**
	 * Add individual message
	 * 
	 * @param message
	 */
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

	/**
	 * Add a list of message
	 * 
	 * @param message
	 */
	public void addMessage(ArrayList<Message> listMsg) {
		if (listMsg == null || listMsg.size() == 0)
			return;
		int size = listMsg.size();
		for (int i = 0; i < size; i++) {
			addMessage(listMsg.get(i));
		}
	}

	public void deleteMessageById(int id) {
		sqliteDB.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
				new String[] { String.valueOf(id) });
	}

	/**
	 * Set is read
	 * 
	 * @param type
	 * @return
	 */
	public void setMessageIsRead(int id) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_READ, Message.READ);
		sqliteDB.update(DATABASE_TABLE, cv, KEY_ROWID + " = " + id, null);
	}

	// type = 0: get all
	// type = 1: inbox
	// type = 2: sent
	public ArrayList<Message> getMessagesByType(int type) {
		ArrayList<Message> messages = new ArrayList<Message>();
		String query = "SELECT * FROM " + DATABASE_TABLE;
		if (type != 0) {
			query += " WHERE " + KEY_TYPE + " = " + type;
		}
		Cursor cursor = sqliteDB.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			Message message = null;
			do {
				message = new Message();
				message.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
				message.setFrom(cursor.getString(cursor
						.getColumnIndex(KEY_FROM)));
				message.setAddress(cursor.getString(cursor
						.getColumnIndex(KEY_ADDRESS)));
				message.setBody(cursor.getString(cursor
						.getColumnIndex(KEY_BODY)));
				message.setDate(cursor.getLong(cursor.getColumnIndex(KEY_DATE)));
				message.setRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
				message.setStatus(cursor.getInt(cursor
						.getColumnIndex(KEY_STATUS)));
				message.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				messages.add(message);
			} while (cursor.moveToNext());
		}
		Message m = new Message();
		m.setFrom(T2_ContactUtils.getContactName(context, "+84987345656"));
		m.setAddress("0987345656");
		m.setBody("Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.Chieu nay di lam ve thi mua mi tom nhe, o nha het mi roi, con moi sua Fami thoi.");
		m.setRead(Message.READ);
		messages.add(m);
		return messages;
	}

//	/**
//	 * Get conversation list
//	 * 
//	 * @return
//	 */
//	public ArrayList<Message> getMessagesByConversation() {
//		ArrayList<Message> messages = new ArrayList<Message>();
//		String query = "SELECT  DISTINCT " + KEY_ADDRESS + " FROM  ("
//				+ "SELECT " + KEY_ADDRESS + " FROM " + DATABASE_TABLE
//				+ " ORDER BY " + KEY_DATE + " DESC " + ") AS TMP";
//		Cursor cursor = sqliteDB.rawQuery(query, null);
//		if (cursor.moveToFirst()) {
//			do {
//				String tmp = cursor.getString(cursor
//						.getColumnIndex(KEY_ADDRESS));
//				String t2 = T2_ContactUtils.getContactName(context, tmp);
//				Message m = new Message();
//				m.setFrom(t2);
//				m.setAddress(tmp);
//				messages.add(m);
//			} while (cursor.moveToNext());
//		}
//		// data for testing
//		Message m = new Message();
//		m.setFrom(T2_ContactUtils.getContactName(context, "0987345656"));
//		m.setAddress("0987345656");
//		messages.add(m);
//		// end data for testing
//		return messages;
//	}

	/**
	 * Get messages list of a conversation
	 * 
	 * @param from
	 * @return
	 */
	public ArrayList<Message> getMessagesOfConversation(String address) {
		address = T2_ContactUtils.getOriginalAddress(address);
		ArrayList<Message> messages = new ArrayList<Message>();
		String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE "
				+ KEY_ADDRESS + " = '0" + address + "' OR "
				+ KEY_ADDRESS + " = '+84" + address + "'";
		Cursor cursor = sqliteDB.rawQuery(query, null);
		if(cursor != null)if (cursor.moveToFirst()) {
			Message message = null;
			do {
				message = new Message();
				message.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
				message.setFrom(cursor.getString(cursor
						.getColumnIndex(KEY_FROM)));
				message.setAddress(cursor.getString(cursor
						.getColumnIndex(KEY_ADDRESS)));
				message.setBody(cursor.getString(cursor
						.getColumnIndex(KEY_BODY)));
				message.setDate(cursor.getLong(cursor.getColumnIndex(KEY_DATE)));
				message.setRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
				message.setStatus(cursor.getInt(cursor
						.getColumnIndex(KEY_STATUS)));
				message.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				messages.add(message);
			} while (cursor.moveToNext());
		}
		// data for testing
		Message m = new Message();
		m.setFrom(T2_ContactUtils.getContactName(context, "0987345656"));
		m.setAddress("0987345656");
		m.setBody("Kute");
		m.setDate(new Date().getTime() + 3333333);
		messages.add(m);
		// end data for testing
		return messages;
	}

}
