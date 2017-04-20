package com.dunavnet.project.boxnet.model;

import java.util.ArrayList;

import org.json.JSONException;

import com.dunavnet.project.boxnet.Settings;

public class LoRaMessage implements Message{
	
	public static final String TAGIDDELIMITER = "="; 
	public static final String READERDELIMITER = "*";
	public static final int MAXNUMBER = 8;

	@Override
	public ArrayList<String> createPayload(ArrayList<TagData> tagList)
			throws JSONException {
		String data1 = "";
		String data2 = "";
		//String[] stringArray = new String[2];
		ArrayList<String> stringArray = new ArrayList<>();
		int numOfTags = 0;
		int numOfTags1 = 0;
		
		if(Settings.numOfReaders().equals("1")){
    	
	    	
	    	for (TagData tagID : tagList) {
	    		if(numOfTags == 0)
	    			data1 += Settings.getReaderID1() + READERDELIMITER;
	    		String value = tagID.GetId();
	    		data1 += value + TAGIDDELIMITER;
	    		numOfTags++;
	    		if(numOfTags > MAXNUMBER){
	    			data1 = data1.substring(0, data1.length()-1);
	    			stringArray.add(data1);
	    			data1 = "";
	    			numOfTags=0;
	    		}
			}
	    	if(!data1.equals("")){
	    		data1 = data1.substring(0, data1.length()-1);
	    		stringArray.add(data1);
	    	}
	    	
		}
		else{
			data1 += Settings.getReaderID1() + READERDELIMITER;
			data2 += Settings.getReaderID2() + READERDELIMITER;
			for (TagData tagID : tagList) {
	    		String value = tagID.GetId();
	    		if(tagID.GetAntenna().equals("Ant0") || tagID.GetAntenna().equals("Ant1")){
	    			if(numOfTags == 0)
		    			data1 += Settings.getReaderID1() + READERDELIMITER;
	    			data1 += value + TAGIDDELIMITER;
		    		numOfTags++;
		    		if(numOfTags > MAXNUMBER){
		    			data1 = data1.substring(0, data1.length()-1);
		    			stringArray.add(data1);
		    			data1 = "";
		    			numOfTags=0;
		    			data1 += Settings.getReaderID1() + READERDELIMITER;
		    		}
	    		}
	    		else{
	    			if(numOfTags1 == 0)
		    			data2 += Settings.getReaderID2() + READERDELIMITER;
	    			data2 += value + TAGIDDELIMITER;
		    		numOfTags1++;
		    		if(numOfTags1 > MAXNUMBER){
		    			data2 = data2.substring(0, data2.length()-1);
		    			stringArray.add(data2);
		    			data2 = "";
		    			numOfTags1 = 0;
		    			data2 += Settings.getReaderID2() + READERDELIMITER;
		    		}
	    		}
	    		
			}
			
			if(!data1.equals("")){
	    		data1 = data1.substring(0, data1.length()-1);
	    		stringArray.add(data1);
			}
			if(!data2.equals("")){
	    		data2 = data2.substring(0, data2.length()-1);
	    		stringArray.add(data2);
			}
			
		}
		tagList.clear();
		return stringArray;
	}

}
