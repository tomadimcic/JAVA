/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dunavnet.project.boxnet.communication;

import com.dunavnet.project.boxnet.Constants;
import com.dunavnet.project.boxnet.communication.interfaces.CommunicationInterface;
import com.dunavnet.project.boxnet.listener.SendingListener;
import com.dunavnet.project.boxnet.model.LoRaMessage;
import com.dunavnet.project.boxnet.model.TagData;
import com.dunavnet.project.boxnet.model.WiFiMessage;
import com.dunavnet.project.boxnet.model.WiFiSplitMessage;
import com.dunavnet.project.boxnet.threads.ReadLoop;
import com.dunavnet.project.boxnet.threads.serial.TwoWaySerialComm;

import java.util.ArrayList;

import org.json.JSONException;


/**
 *
 * @author Toma
 */
public class LoRaCommunication extends CommunicationInterface implements Runnable{
    
    SendingListener readLoop;
    ArrayList<TagData> myTagsList;
    
    
    public LoRaCommunication(ArrayList<TagData> myTagsList, ReadLoop readLoop){
        this.readLoop = readLoop;
        this.myTagsList = myTagsList;
    }

    @Override
    public void run() {
    	ArrayList<String> jsonData = new ArrayList<>();
    	try {
    		readLoop.setSending(true);
//    		LoRaMessage payload = new LoRaMessage();
    		WiFiSplitMessage payload = new WiFiSplitMessage();
			jsonData = createPayload(myTagsList, payload);
			for (int i = 0; i<jsonData.size(); i++) {
				//threadMessage("Data to send: " + jsonData.get(i));
				send("1="+jsonData.get(i));
				Constants.println("Data to send: "+"1="+jsonData.get(i));
				if(i < jsonData.size()){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			readLoop.setSending(false);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	@Override
	public int send(String payload) {
		TwoWaySerialComm.sendData(payload, 0);
		return 0;
	}

    
}
