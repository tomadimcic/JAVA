package com.dunavnet.project.gluenet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.dunavnet.project.gluenet.model.Unit;

public class Settings {

	static FileReader config;

	public static boolean getDebugMode() {
		String text[] = openFile().split("\n");
		String debugMode = text[0].split(";")[1];
		if (debugMode.contains("true")) {
			return true;
		} else {
			return false;
		}
	}

	// unit addresses and ID in the modbus
	public static ArrayList<Unit> getUnits() {
		String text[] = openFile().split("\n");
		String addresses[] = text[1].split(";");
		String type[] = text[2].split(";");
		ArrayList<Unit> unitList = new ArrayList<>();

		for (int i = 1; i < addresses.length; i++) {
			Unit unit = new Unit(type[i], Integer.parseInt(addresses[i]));
			unitList.add(unit);
		}

		return unitList;
	}

	// baudrate
	public static int getBaudrate() {
		String text[] = openFile().split("\n");
		String baudrate = text[3].split(";")[1];
		return Integer.parseInt(baudrate);
	}
	
	
	// IoT Hub
	public static String getAzureService(){
		String text[] = openFile().split("\n");
		String service = text[4].split(";")[1];
		
		return service;
	}
	
	public static String getAuth(){
		String text[] = openFile().split("\n");
		String auth = text[5].split(";")[1];
		
		return auth;
	}
	
	//IdWasp
	public static String getIdNode(){
		String text[] = openFile().split("\n");
		String idNode = text[6].split(";")[1];
		
		return idNode;
	}

	private static String openFile() {
		String file = "";
		try {
			config = new FileReader("/boot/gluenetConfig.txt");
			char[] text = new char[800];
			config.read(text);
			for (char c : text) {
				file += c;
			}
			config.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;

	}

}
