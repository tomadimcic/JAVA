/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dunavnet.project.boxnet.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import com.caen.RFIDLibrary.CAENRFIDTag;
import com.dunavnet.project.boxnet.Settings;
import com.dunavnet.project.boxnet.communication.interfaces.CommunicationInterface;
import com.dunavnet.project.boxnet.exception.RFIDReaderLog;
import com.dunavnet.project.boxnet.model.TagData;
import com.dunavnet.project.boxnet.model.WiFiMessage;
import com.dunavnet.project.boxnet.threads.ReadLoop;

/**
 *
 * @author Toma
 */
public class WiFiCommunication extends CommunicationInterface implements Runnable{

	ReadLoop readLoop;
    ArrayList<TagData> myTagsList;
    
    
    public WiFiCommunication(ArrayList<TagData> myTagsList, ReadLoop readLoop){
        this.readLoop = readLoop;
        this.myTagsList = myTagsList;
    }

    @Override
    public void run() {
    	ArrayList<String> jsonData = new ArrayList<>();
    	try {
    		WiFiMessage payload = new WiFiMessage();
			jsonData = createPayload(myTagsList, payload);
			for (String json : jsonData) {
				if(json != null){
					threadMessage("Data to send: " + json);
					send(json);
				}
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
			String url = "";
			if(Settings.getIsAzure().equals("0"))
				url = Settings.getDNETService();
			else
				url = Settings.getAzureService();
			System.out.println(url);
			object = new URL(url);
		

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			if(Settings.getIsAzure().equals("1"))
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
			RFIDReaderLog.logData(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			RFIDReaderLog.logData(e.getMessage());
		}
		return 0;
	}

	
    
    
}
