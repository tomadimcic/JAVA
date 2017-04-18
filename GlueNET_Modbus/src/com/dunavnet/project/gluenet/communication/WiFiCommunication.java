package com.dunavnet.project.gluenet.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import com.dunavnet.project.gluenet.Settings;
import com.dunavnet.project.gluenet.communication.CommunicationInterface;
import com.dunavnet.project.gluenet.model.Unit;
import com.dunavnet.project.gluenet.model.WiFiMessage;
import com.dunavnet.project.gluenet.threads.Modbus;

public class WiFiCommunication extends CommunicationInterface implements Runnable{
	
	Modbus modbusLoop;
    ArrayList<Unit> myUnitsList;
    int frameNumber;
    
    
    public WiFiCommunication(ArrayList<Unit> myUnitsList, Modbus modbusLoop, int frameNumber){
        this.modbusLoop = modbusLoop;
        this.myUnitsList = myUnitsList;
        this.frameNumber = frameNumber;
    }

	@Override
	public void run() {
		String jsonData = "";
    	try {
    		WiFiMessage payload = new WiFiMessage();
			jsonData = createPayload(myUnitsList, payload, frameNumber);
			if(jsonData != null){
				threadMessage("Data to send: " + jsonData);
				//send(json);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int send(String payload) {
		URL object;
		try {
			String url =  Settings.getAzureService();
			System.out.println(url);
			object = new URL(url);
		

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			//if(Settings.getIsAzure().equals("1"))
			con.setRequestProperty("Authorization", Settings.getAuth());
			con.setRequestMethod("POST");
			

			//RFIDReaderLog.logData(payload);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(payload);
			wr.flush();
			
			StringBuilder sb = new StringBuilder();  
			int HttpResult = con.getResponseCode(); 
			System.out.println(HttpResult);
			if (HttpResult == HttpURLConnection.HTTP_OK || HttpResult == HttpURLConnection.HTTP_NO_CONTENT) {
			    BufferedReader br = new BufferedReader(
			            new InputStreamReader(con.getInputStream(), "utf-8"));
			    String line = null;  
			    while ((line = br.readLine()) != null) {  
			        sb.append(line + "\n");  
			    }
			    br.close();
			    System.out.println("" + sb.toString());  
			    //RFIDReaderLog.logData(sb.toString());
			} else {
			    System.out.println(con.getResponseMessage());
			    //RFIDReaderLog.logData(con.getResponseMessage());
			}  
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
