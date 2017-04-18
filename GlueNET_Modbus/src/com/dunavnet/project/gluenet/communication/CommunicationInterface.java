package com.dunavnet.project.gluenet.communication;

import java.util.ArrayList;

import org.json.JSONException;

import com.dunavnet.project.gluenet.model.Message;
import com.dunavnet.project.gluenet.model.Unit;

public abstract class CommunicationInterface {
	
	public String createPayload(ArrayList<Unit> tagList, Message payload, int frameNumber) throws JSONException {
		String message = payload.createPayload(tagList, frameNumber);
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
