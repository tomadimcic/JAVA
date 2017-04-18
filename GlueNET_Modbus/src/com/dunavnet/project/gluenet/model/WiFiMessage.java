package com.dunavnet.project.gluenet.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.dunavnet.project.gluenet.Settings;

public class WiFiMessage implements Message{
	
	public static final String node = "IdWasp";
	public static final String frame = "FrameNumber";
	public static final String timestamp = "Timestamp";
	public static final String sensors = "Sensors";

	@Override
	public String createPayload(ArrayList<Unit> unitList, int frameNumber) throws JSONException {
		//String payload1 = "{\"IdWasp\":\"PLC1\",\"IdSecret\":\"90988592\",\"FrameType\":\"1\",\"FrameNumber\":\"74560\",\"Timestamp\":\"2017-04-10T07:11:38Z\",\"Sensors\":{\"LC1\":\" 43\",\"LC2\":\" 129\",\"TEMP1\":\"-110\",\"TEMP2\":\"-373\",\"BTLCNTR1\":\" 9598\",\"BTLCNTR2\":\" 8692\",\"BTLTMP\":\" 178\",\"CAPIN\":\"0\",\"CAPOUT\":\"0\",\"RHTM1\":\" 604\",\"RHTM2\":\" 251\"}}";

		String nodeID = Settings.getIdNode();
		
		JSONObject json = new JSONObject();
		
		json.put(node, nodeID);
		json.put(frame, Integer.toString(frameNumber));
		json.put(timestamp, getCurrentTimeStamp());
		json.put(sensors, calculateRawData(unitList));
		
		return json.toString();
	}
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	public JSONObject calculateRawData(ArrayList<Unit> unitList){
		
		JSONObject jo = new JSONObject();
		
		return jo;
		
	}

}
