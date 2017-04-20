package com.dunavnet.project.boxnet.threads.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dunavnet.project.boxnet.Constants;

public class TwoWaySerialComm {
	
	static OutputStream out;
	static int loraResponse = -1;
	static long startSendingTime = 0;
	
	public static void setLoraResponse(int loraResponse) {
		TwoWaySerialComm.loraResponse = loraResponse;
	}

	public static int getLoraResponse() {
		return loraResponse;
	}

	public void connect(String portName) {
		Constants.println("Usao");
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);
		
		Constants.println("Prosao");
		if (portIdentifier.isCurrentlyOwned()) {
			Constants.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					timeout);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				//SerialWriter sw = new SerialWriter(out);
				//sw.init();

				(new Thread(new SerialReader(in))).start();
				sendData("", 1);

			} else {
				Constants
						.println("Error: Only serial ports are handled by this example.");
			}
		}
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendData(String message, int init){
		if(init == 0)
		{
			(new Thread(new SerialWriter(out, Constants.normalSending, message))).start();
		}
		else
		{
			(new Thread(new SerialWriter(out, Constants.restartSending, ""))).start();
		}
		startSendingTime = System.currentTimeMillis();
		boolean running = true;
		boolean restartInitiated = false;
		while (running) {
			Constants.println("sendData-while-- "+ message);
			
			if (getLoraResponse() != -1)
			{
				switch (getLoraResponse()) {
				case Constants.accepted:
				{
					if(restartInitiated)
					{
						restartInitiated = false;
						(new Thread(new SerialWriter(out, Constants.normalSending, message))).start();						
					}
					else
					{
						running = false;
					}
					break;
				}
				case Constants.mac_tx_ok:
				{
					running = false;
					break;
				}
				case Constants.busy:
				{
					break;
				}
				case Constants.denied:
				{
					Constants.println("LoRa mreza nije dostupna! -denied");
					(new Thread(new SerialWriter(out, Constants.joinSending, ""))).start();
					restartInitiated = true;
					break;
				}
				case Constants.not_joined:
				{
					Constants.println("LoRa mreza nije dostupna!- not_joined");
					(new Thread(new SerialWriter(out, Constants.joinSending, ""))).start();
					restartInitiated = true;
					break;
				}
				case Constants.no_free_ch:
				{
					Constants.println("LoRa mreza nije dostupna!- no_free_ch");
					(new Thread(new SerialWriter(out, Constants.restartSending, ""))).start();
					restartInitiated = true;
					break;
				}
				case Constants.invalid_data_len:
				{
					Constants.println("LoRa mreza nije dostupna!- invalid_data_len");
					(new Thread(new SerialWriter(out, Constants.restartSending, ""))).start();
					restartInitiated = true;
					break;
				}
				case Constants.mac_err:
				{
					Constants.println("LoRa mreza nije dostupna!- mac_err");
					(new Thread(new SerialWriter(out, Constants.normalSending, message))).start();
					break;
				}

				}
				
				setLoraResponse(-1);
			}
			
			
			
			if((System.currentTimeMillis() - startSendingTime) > 20000)
			{
				Constants.println("LoRa mreza nije dostupna, proslo vremenski period!");
				(new Thread(new SerialWriter(out, Constants.restartSending, ""))).start();
				startSendingTime = System.currentTimeMillis();
			}
			
			
			try {
				if(running)
					Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		


	}
	
}
