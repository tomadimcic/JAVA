package com.dunavnet.project.boxnet.exception;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RFIDReaderLog {
	
	static FileReader config;
	
	public static void logData(String data){
		PrintWriter out;
		
		String file = openFile();
		
		file += System.currentTimeMillis() + " - " + data + "\n";
		
		System.out.println("ovddereee" + file);
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("/boot/readerLog.txt", true)));
			out.print(file);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String openFile() {
		String file = "";
		try {
			config = new FileReader("/boot/readerLog.txt");
			char[] text = new char[5000];
			config.read(text);
			for(char c: text){
				    file += c;
			}
			//config.close();
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
