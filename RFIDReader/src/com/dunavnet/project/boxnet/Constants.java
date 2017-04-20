package com.dunavnet.project.boxnet;

public class Constants {
	//lorra response
	public static final int accepted = 1;
	public static final int mac_tx_ok = 0;	
	public static final int invalid_data_len = 2;
	public static final int busy = 3;	
	public static final int mac_err = 4;
	public static final int not_joined = 5;	
	public static final int no_free_ch = 6;
	public static final int denied = 7;
	
	//Serial reader commands
	public static final int normalSending = 0;
	public static final int initSending = 1;
	public static final int restartSending = 2;
	public static final int joinSending = 3;
	
	//Lora number of tags to send
	public static final int numberOfTags = Settings.getLoraNumberOfTags();
	
	
	//Debug
	public static final boolean DEBUG = Settings.getDebugMode();
	
	public static void println(String message)
	{
		if(DEBUG)
		{
			System.out.println(message);
		}
	}
}
