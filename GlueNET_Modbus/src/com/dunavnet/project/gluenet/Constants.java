package com.dunavnet.project.gluenet;

public class Constants {

	// modbus constants
	public static final int ref = 0; // the reference, where to start reading
										// from
	public static final int count = 8; // the count of IR's to read
	public static final int repeat = 1; // a loop for repeating the transaction

	
	
	// Debug
	public static final boolean DEBUG = Settings.getDebugMode();

	public static void println(String message) {
		if (DEBUG) {
			System.out.println(message);
		}
	}

}
