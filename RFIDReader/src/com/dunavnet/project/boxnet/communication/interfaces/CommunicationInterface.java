/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dunavnet.project.boxnet.communication.interfaces;

import java.util.ArrayList;

import org.json.JSONException;

import com.dunavnet.project.boxnet.model.Message;
import com.dunavnet.project.boxnet.model.TagData;

/**
 *
 * @author t420
 */
public abstract class CommunicationInterface {
	
	public static final String TAGIDKEY = "tagID"; 
	public static final String READERKEY = "readerID";
	
	
	//isLoRaOrWiFi=0 - wifi
	//isLoRaOrWiFi=1 - lora
	public ArrayList<String> createPayload(ArrayList<TagData> tagList, Message payload) throws JSONException {
		ArrayList<String> message = new ArrayList<String>();
			message = payload.createPayload(tagList);
			//message = loraPayload(tagList);
		
		return message;
	}
	
	public void threadMessage(String message) {
        String threadName
                = Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }
	
	public abstract int send(String payload);
	
    
}
