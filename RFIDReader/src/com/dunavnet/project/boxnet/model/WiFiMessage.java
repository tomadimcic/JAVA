package com.dunavnet.project.boxnet.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.dunavnet.project.boxnet.Constants;
import com.dunavnet.project.boxnet.Settings;

public class WiFiMessage implements Message{
	
	public static final String TAGIDKEY = "tagID"; 
	public static final String READERKEY = "readerID";

	@Override
	public ArrayList<String> createPayload(ArrayList<TagData> tagList)
			throws JSONException {
		JSONObject data1 = new JSONObject();
		JSONObject data2 = new JSONObject();
		//String[] stringArray = new String[2];
		
		String gateID1 = Settings.getGateID1();
		boolean isGate1Closed = false;
		boolean isGate2Closed = false;
		
		ArrayList<String> stringArray = new ArrayList<>();
		
		if(Settings.numOfReaders().equals("1")){
    	
	    	data1.put(READERKEY, Settings.getReaderID1());
	    	for (TagData tagID : tagList) {
	    		String value = tagID.GetId();
	    		if(!value.equals(gateID1)){
	    			data1.append(TAGIDKEY, value);
	    		}
	    		else{
	    			isGate1Closed = true;
	    			Constants.println("Gate is closed");
	    			break;
	    		}
	    		
			}
	    	stringArray.add(data1.toString());
		}
		else{
			String gateID2 = Settings.getGateID2();
			for (TagData tagID : tagList) {
				
	    		String value = tagID.GetId();
	    		if(tagID.GetAntenna().equals("Ant0") || tagID.GetAntenna().equals("Ant1")){
	    			
	    			if(!value.equals(gateID1)){
	    				data1.append(TAGIDKEY, value);
	    			}
	    			else{
	    				Constants.println("Gate 1 is closed");
	    				isGate1Closed = true;
		    		}
	    		}
	    		else{
	    			if(!value.equals(gateID2)){
	    				data2.append(TAGIDKEY, value);
	    			}
	    			else{
	    				Constants.println("Gate 2 is closed");
	    				isGate2Closed = true;
		    		}
	    		}
	    		
	    		
	    		
			}
			
			if(isGate1Closed)
				data1 = new JSONObject();
			if(isGate2Closed)
				data2 = new JSONObject();
			
			if(data1.length() != 0){
				data1.put(READERKEY, Settings.getReaderID1());
				stringArray.add(data1.toString());
			}
			if(data2.length() != 0){
				data2.put(READERKEY, Settings.getReaderID2());
				stringArray.add(data2.toString());
			}
		}
		tagList.clear();
		return stringArray;
	}

}
