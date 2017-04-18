package com.dunavnet.project.gluenet.model;

import java.util.ArrayList;

import org.json.JSONException;

public interface Message {
	
	public String createPayload(ArrayList<Unit> unitList, int frameNumber) throws JSONException;

}
