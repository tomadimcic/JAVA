package com.dunavnet.project.boxnet.model;

import java.util.ArrayList;

import org.json.JSONException;

public interface Message {
	
	public ArrayList<String> createPayload(ArrayList<TagData> tagList) throws JSONException;

}
