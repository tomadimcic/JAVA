package com.dunavnet.project.boxnet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
*
* @author Toma
*/
public class Settings {
	
	static FileReader config;
	
	public static String numOfReaders(){
		String text[] = openFile().split("\n");
		String numOfReaders = text[0].split(";")[1];
		
		return numOfReaders;
	}

	public static String getReaderID1(){
		String text[] = openFile().split("\n");
		String readerID1 = text[1].split(";")[1];
		
		return readerID1;
	}
	
	public static String getReaderID2(){
		String text[] = openFile().split("\n");
		String readerID2 = text[2].split(";")[1];
		
		return readerID2;
	}
	
	public static String getDNETService(){
		String text[] = openFile().split("\n");
		String service = text[3].split(";")[1];
		
		return service;
	}
	
	//azure=0 - dnetservice
	//azure=1 - azureservice
	public static String getIsAzure(){
		String text[] = openFile().split("\n");
		String service = text[4].split(";")[1];
		
		return service;
	}
	
	public static String getAzureService(){
		String text[] = openFile().split("\n");
		String service = text[5].split(";")[1];
		
		return service;
	}
	
	//mode=0 - WiFi
	//mode=1 - LoRa
	public static String getMode(){
		String text[] = openFile().split("\n");
		String service = text[6].split(";")[1];
		
		return service;
	}
	
	public static String getAuth(){
		String text[] = openFile().split("\n");
		String auth = text[7].split(";")[1];
		
		return auth;
	}
	
	public static String getDNETPingService(){
		String text[] = openFile().split("\n");
		String pingURL = text[8].split(";")[1];
		
		return pingURL;
	}
	
	public static String setPower(){
		String text[] = openFile().split("\n");
		String pw = text[9].split(";")[1];
		
		return pw;
	}
	
	public static String setQ1(){
		String text[] = openFile().split("\n");
		String q1 = text[10].split(";")[1];
		
		return q1;
	}
	
	public static String getGateID1(){
		String text[] = openFile().split("\n");
		String gateid = text[11].split(";")[1];
		
		return gateid;
	}
	
	public static String getGateID2(){
		String text[] = openFile().split("\n");
		String gateid = text[12].split(";")[1];
		
		return gateid;
	}
	public static String getLoraPort(){
		String text[] = openFile().split("\n");
		String loraPort = text[13].split(";")[1];
		
		return loraPort;
	}
	public static String getLoraPing(){
		String text[] = openFile().split("\n");
		String loraPing = text[14].split(";")[1];
		
		return loraPing;
	}
	public static boolean getDebugMode(){
		String text[] = openFile().split("\n");
		String debugMode = text[15].split(";")[1];
		if(debugMode.contains("true"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int getLoraNumberOfTags(){
		String text[] = openFile().split("\n");
		String numberOfTags = text[16].split(";")[1];
		
		return Integer.decode(numberOfTags).intValue();
	}

	private static String openFile() {
		String file = "";
		try {
			config = new FileReader("/boot/readerConfig.txt");
			char[] text = new char[800];
			config.read(text);
			for(char c: text){
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
