package com.khanhtq.phonesecurity.models;

/**
 * Entity class used to hold message
 * 
 * @author Khanh Tran
 * 
 */
public class Message {
	public static final int TYPE_INBOX = 1;
	public static final int TYPE_SENT = 2;
	public static final int TYPE_FAILED = 3;
	public static final int READ = 2;
	public static final int UNREAD = 0;
	private int _id;
	/**
	 * The buddy number
	 */
	private String from;
	public Message(int _id, String from, String address, long date, int type,
			int read, int status, String body) {
		super();
		this._id = _id;
		this.from = from;
		this.address = address;
		this.date = date;
		this.type = type;
		this.read = read;
		this.status = status;
		this.body = body;
	}

	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private long date;
	// in box or sent
	private int type;
	// is read or not 1 = read, 0 = unread
	private int read;
	// status
	private int status;
	// Body
	private String body;

	public Message() {
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
