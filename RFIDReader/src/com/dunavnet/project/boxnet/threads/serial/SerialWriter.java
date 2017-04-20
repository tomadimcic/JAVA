package com.dunavnet.project.boxnet.threads.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.dunavnet.project.boxnet.Constants;

public class SerialWriter implements Runnable {

	OutputStream out;
	int mode;
	String message;
	ArrayList<String> myList = new ArrayList<String>();
	

	public SerialWriter(OutputStream out, int mode, String message) {
		this.out = out;
		this.mode = mode;
		this.message = message;
	}

	public void init() {
		myList.add("sys reset");

		myList.add("radio set mod lora");
		myList.add("radio set pwr 14");
		myList.add("radio set sync 34");
		myList.add("radio set cr 4/5");
		myList.add("mac set adr on");
		myList.add("mac set devaddr 00000003");
		myList.add("mac set appeui 0011223344556677");
		myList.add("mac set deveui 8811223344556677");
		myList.add("mac set nwkskey 2b7e151628aed2a6abf7158809cf4f3c");
		myList.add("mac set appskey 2b7e151628aed2a6abf7158809cf4f3c");
		myList.add("mac set appkey 2b7e151628aed2a6abf7158809cf4f3c");
		myList.add("radio set freq 868300000");
		myList.add("radio set crc on");
		myList.add("mac set ch freq 0 868100000");
	    myList.add("mac set ch freq 1 868300000");
	    myList.add("mac set ch freq 2 868500000");
	    myList.add("mac set ch freq 3 869100000");
	    myList.add("mac set ch freq 4 868300000");
	    myList.add("mac set ch freq 5 869500000");
	    myList.add("mac set ch freq 6 869700000");
	    myList.add("mac set ch freq 7 869900000");
	    myList.add("mac set ch dcycle 0 9");
	    myList.add("mac set ch dcycle 1 9");
	    myList.add("mac set ch dcycle 2 9");
	    myList.add("mac set ch dcycle 3 9");
	    myList.add("mac set ch dcycle 4 0");
	    myList.add("mac set ch dcycle 5 9");
	    myList.add("mac set ch dcycle 6 9");
	    myList.add("mac set ch dcycle 7 9");
	    myList.add("mac set ch drrange 0 0 6");
	    myList.add("mac set ch drrange 1 0 6");
	    myList.add("mac set ch drrange 2 0 6");
	    myList.add("mac set ch drrange 3 0 6");
	    myList.add("mac set ch drrange 4 0 6");
	    myList.add("mac set ch drrange 5 0 6");
	    myList.add("mac set ch drrange 6 0 6");
	    myList.add("mac set ch drrange 7 0 6");
	    
	    for (int i = 3; i < 8; i++){
	        myList.add("mac set ch status " +i+" on");
	    }
	    
	    myList.add("mac set retx 15");
	    myList.add("mac save");

		for (String iterable_element : myList) {
			try {
				Thread.sleep(1000);
				System.out.println(iterable_element);
				send(iterable_element);
				//this.out.write("\r".getBytes());
				//this.out.write("\n".getBytes());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	public void  sysreset()
	{
	    myList.clear();
		myList.add("sys reset");
	}
	
	public void join()
	{
		myList.add("mac join otaa");
	}

	public void run() {
		
		switch (mode) {
		case Constants.initSending:
			init();
			break;
		case Constants.restartSending:
			send("sys reset");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send("mac join otaa");
			break;
		case Constants.joinSending:
			send("mac join otaa");
			break;
		case Constants.normalSending:
			String mess = toHex(this.message);
			send("mac tx cnf 1 " + mess);
			Constants.println("mac tx cnf 1 " + mess);
			break;

		}
		
	}
	
	public String toHex(String arg) {
		try {
			return String.format("%040x", new BigInteger(1, arg.getBytes("UTF8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void send(String message){
		if (message != null) {	
			try {
				this.out.write(message.getBytes());
				this.out.write("\r".getBytes());
				this.out.write("\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}