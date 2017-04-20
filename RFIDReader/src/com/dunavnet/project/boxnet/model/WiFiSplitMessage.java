package com.dunavnet.project.boxnet.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.dunavnet.project.boxnet.Constants;
import com.dunavnet.project.boxnet.Settings;

public class WiFiSplitMessage implements Message{
	
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
	    	int counter = 0;
	    	for (TagData tagID : tagList) {
	    		String value = tagID.GetId();
	    		if(!value.equals(gateID1)){
	    			if(counter < Constants.numberOfTags)
	    			{
	    				counter += 1;
	    				data1.append(TAGIDKEY, value);
	    				Constants.println(value + "  " + counter);
	    			}
	    			else
	    			{
	    				if(!isGate1Closed && data1.has(TAGIDKEY) && data1.getJSONArray(TAGIDKEY).length() > 0)
	    				{
	    					Constants.println("First: "+data1.toString());
	    					stringArray.add(data1.toString());
		    				data1.remove(TAGIDKEY);
		    				data1.append(TAGIDKEY, value);
		    				counter = 1;
		    				Constants.println(value + "  " + counter);
	    				}
	    				else if(isGate1Closed && data1.has(TAGIDKEY))
	    				{
	    					data1.remove(TAGIDKEY);
	    					counter = 0;
	    				}
	    			}
	    		}
	    		else{
	    			isGate1Closed = true;
	    			Constants.println("Gate is closed");
	    			break;
	    		}
	    		
			}
	    	if(data1.has(TAGIDKEY) && data1.getJSONArray(TAGIDKEY).length() > 0)
	    	{
	    		Constants.println("Last: " + data1.toString());
	    		stringArray.add(data1.toString());
	    	}
		}
		else{
			String gateID2 = Settings.getGateID2();
			data1.put(READERKEY, Settings.getReaderID1());
			data2.put(READERKEY, Settings.getReaderID2());
			int counterReaderOne = 0;
			int counterReaderTwo = 0;
			for (TagData tagID : tagList) {
				
	    		String value = tagID.GetId();
	    		if(tagID.GetAntenna().equalsIgnoreCase("Ant0") || tagID.GetAntenna().equalsIgnoreCase("Ant1")){
	    			
	    			if (tagID.GetAntenna().equalsIgnoreCase("Ant0")) {
						if (!value.equals(gateID1)) {
							if (counterReaderOne < Constants.numberOfTags) {
								counterReaderOne += 1;
								data1.append(TAGIDKEY, value);
							} else {
								if (!isGate1Closed
										&& data1.has(TAGIDKEY)
										&& data1.getJSONArray(TAGIDKEY)
												.length() > 0) {
									stringArray.add(data1.toString());
									data1.remove(TAGIDKEY);
									counterReaderOne = 1;
									data1.append(TAGIDKEY, value);
								} else if (isGate1Closed && data1.has(TAGIDKEY)) {
									data1.remove(TAGIDKEY);
									counterReaderOne = 0;
								}
							}
						} else {
							Constants.println("Gate 1 is closed");
							isGate1Closed = true;
						}
					}

	    		else
	    		{
	    			if (tagID.GetAntenna().equalsIgnoreCase("Ant1")) {
						if (!value.equals(gateID2)) {
							if (counterReaderTwo < Constants.numberOfTags) {
								counterReaderTwo += 1;
								data2.append(TAGIDKEY, value);
							} else {
								if (!isGate2Closed
										&& data2.has(TAGIDKEY)
										&& data2.getJSONArray(TAGIDKEY)
												.length() > 0) {
									stringArray.add(data2.toString());
									data2.remove(TAGIDKEY);
									counterReaderTwo = 1;
									data2.append(TAGIDKEY, value);
								} else if (isGate2Closed && data2.has(TAGIDKEY)) {
									data2.remove(TAGIDKEY);
									counterReaderTwo = 0;
								}
							}
						} else {
							Constants.println("Gate 2 is closed");
							isGate2Closed = true;
						}
					}
	    		}
			}
			}
			
			
	    	if(data1.has(TAGIDKEY) && data1.getJSONArray(TAGIDKEY).length() > 0)
	    	{
	    		stringArray.add(data1.toString());
	    	}
	    	
	    	if(data2.has(TAGIDKEY) && data2.getJSONArray(TAGIDKEY).length() > 0)
	    	{
	    		stringArray.add(data2.toString());
	    	}

		}
		tagList.clear();
		return stringArray;
	}

}
