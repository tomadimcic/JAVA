package com.dunavnet.project.boxnet.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import com.caen.RFIDLibrary.CAENRFIDTag;
import com.dunavnet.project.boxnet.Settings;
import com.dunavnet.project.boxnet.communication.interfaces.CommunicationInterface;
import com.dunavnet.project.boxnet.model.WiFiMessage;
import com.dunavnet.project.boxnet.threads.ReadLoop;

public class PingCommunication extends CommunicationInterface implements Runnable{


    @Override
    public void run() {
    	try {
			send("");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	@Override
	public int send(String payload) {
		URL object;
		try {
			String url = Settings.getDNETPingService();
			System.out.println(url);
			object = new URL(url);
		

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			//if(Settings.getIsAzure().equals("1"))
				//con.setRequestProperty("Authorization", Settings.getAuth());
			con.setRequestMethod("GET");
			

			//OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			//wr.write(payload);
			//wr.flush();
			
			StringBuilder sb = new StringBuilder();  
			int HttpResult = con.getResponseCode(); 
			System.out.println(HttpResult);
			if (HttpResult == HttpURLConnection.HTTP_OK) {
			    BufferedReader br = new BufferedReader(
			            new InputStreamReader(con.getInputStream(), "utf-8"));
			    String line = null;  
			    while ((line = br.readLine()) != null) {  
			        sb.append(line + "\n");  
			    }
			    br.close();
			    System.out.println("" + sb.toString());  
			} else {
			    System.out.println(con.getResponseMessage());  
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