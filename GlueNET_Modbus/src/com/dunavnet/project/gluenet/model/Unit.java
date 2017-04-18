package com.dunavnet.project.gluenet.model;

import java.util.ArrayList;

public class Unit {
	
	private String type;
	private int unitID;
	private ArrayList<Integer> dataList;
	
	public Unit(String type, int unitID) {
		super();
		this.type = type;
		this.unitID = unitID;
		this.dataList = new ArrayList<>();
	}
	
	public Unit(String type, int unitID, ArrayList<Integer> dataList) {
		this.type = type;
		this.unitID = unitID;
		this.dataList = dataList;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUnitID() {
		return unitID;
	}
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	public ArrayList<Integer> getDataList(){
		return this.dataList;
	}
	public void addData(int data){
		dataList.add(data);
	}

}
