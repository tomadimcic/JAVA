package com.dunavnet.project.boxnet.threads.serial;

import java.io.IOException;
import java.io.InputStream;

import com.dunavnet.project.boxnet.Constants;

public class SerialReader implements Runnable {

	InputStream in;

	public SerialReader(InputStream in) {
		this.in = in;
	}

	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;

		String response = "";
		try {
			while ((len = this.in.read(buffer)) > -1) {
				String log = new String(buffer, 0, len);
				
				response += log;
				Constants.println("SerialReader-log: "+ response);
		
				if (response.contains("accepted")) {
					Constants.println("\n\nLoRa network ACCEPTED\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.accepted);
					response = "";
				}
				if (response.contains("mac_tx_ok")) {
					Constants.println("\n\nLoRa network mac_tx_ok\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.mac_tx_ok);
					response = "";
				}
				if (response.contains("invalid_data_len")) {
					Constants.println("\n\nLoRa network invalid_data_len\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.invalid_data_len);
					response = "";
				}				
				if (response.contains("busy")) {
					Constants.println("\n\nLoRa network busy\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.busy);
					Thread.sleep(3000);
					response = "";
				}
				if (response.contains("mac_err")) {
					Constants.println("\n\nLoRa network mac_err\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.mac_err);
					response = "";
				}
				if (response.contains("not_joined")) {
					Constants.println("\n\nLoRa network not_joined\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.not_joined);
					response = "";
				}
				if (response.contains("no_free_ch")) {
					Constants.println("\n\nLoRa network no_free_ch\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.no_free_ch);
					response = "";
				}
				if (response.contains("denied")) {
					Constants.println("\n\nLoRa network denied\n\n");
					TwoWaySerialComm.setLoraResponse(Constants.denied);
					response = "";
				}
				if(response.length() > 1000)
					response = response.substring(985);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}