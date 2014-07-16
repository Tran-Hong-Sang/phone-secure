package com.khanhtq.phonesecurity.utils;

import com.khanhtq.phonesecurity.models.Message;

public class T2_Singleton {
	public static Message currentMessageForConversation = null;
	private static boolean status = false;
	public static void active(){
		status = true;
	}
	public static boolean isActive(){
		return status;
	}
	public static void deactive() {
		status = false;
	}
	/**
	 * boolean for checking lock or unlock mode
	 */
	public static boolean _bool = false;
}
