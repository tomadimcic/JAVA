package com.dunavnet.project.boxnet.model;

public class TagData {
	private String id;
	private String antenna;
	
	public String GetId() {
		return id;
	}
	public String GetAntenna() {
		return antenna;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}
	
	public TagData(String tagId, String tagAntenna)
	{
		this.id = tagId;
		this.antenna = tagAntenna;
	}
	
	
	

}
