package com.khanhtq.phonesecurity.utils;

public class T2_Singleton {
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
}
