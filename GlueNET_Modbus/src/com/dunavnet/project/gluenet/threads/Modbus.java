package com.dunavnet.project.gluenet.threads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.dunavnet.project.gluenet.Constants;
import com.dunavnet.project.gluenet.Settings;
import com.dunavnet.project.gluenet.communication.WiFiCommunication;
import com.dunavnet.project.gluenet.model.Unit;
import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class Modbus implements Runnable{
	
	private boolean isRunning = false;
	private ArrayList<Unit> unitList;
	private HashMap<Integer, Unit> unitHM;
	

	/* The important instances of the classes mentioned before */
	SerialConnection con = null; //the connection
	ModbusSerialTransaction trans = null; //the transaction
	ReadMultipleRegistersRequest mrreq = null;
	ReadMultipleRegistersResponse mrres = null;
	SerialParameters params = null;

	int repeat = 1; //a loop for repeating the transaction
	
	public HashMap<Integer, Unit> getUnitHM() {
    	if (this.unitHM == null)
    	{
    		this.unitHM = new HashMap<Integer, Unit>();
    		return this.unitHM;
    	}
		return unitHM;
	}
	
	public void initSerial(){
		
		ModbusCoupler.getReference().setUnitID(1);
		
		params = new SerialParameters();
		params.setPortName("/dev/ttyUSB0");
		params.setBaudRate(Settings.getBaudrate());
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding(com.ghgande.j2mod.modbus.Modbus.SERIAL_ENCODING_RTU);
		params.setEcho(false);
		
		con = new SerialConnection(params);
		
	}
	
	@Override
	public void run() {
		try {
			con.open();
			
			mrreq = new ReadMultipleRegistersRequest(Constants.ref, Constants.count);
			
			trans = new ModbusSerialTransaction(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		long patience = 5000;
		long generalStartTime = System.currentTimeMillis();
		long startTime = System.currentTimeMillis();
		while(isRunning){
			
			readLoop();
			
			if (((System.currentTimeMillis() - startTime) > patience)) {
                //threadMessage("Sending data ...");
                Constants.println("Sending data ...Time: "+ System.currentTimeMillis()/1000 +" hm size: "+ getUnitHM().size());
                if(Constants.DEBUG)
                	printHM();
                Thread t;
                t = new Thread(new WiFiCommunication(cloneUnits(getUnitHM().values()), this, i));
                t.start();
                clearHMData();
                getUnitHM().clear();
                startTime = System.currentTimeMillis();
                i++;

            }
		}
		
		// Close the connection
		con.close();
		
		Constants.println(Long.toString((System.currentTimeMillis() - generalStartTime)));
		
		Constants.println(Integer.toString(i));
		
		Constants.println(Long.toString((System.currentTimeMillis() - generalStartTime)/(i*3)));
		
	}
	
	public void readLoop(){
		
		for (Unit unit : unitList) {
			read(unit);
		}
	}
	
	public void read(Unit unit){
		 
		try {
			//System.setProperty("com.ghgande.modbus.debug", "true");
			
			// Prepare a request
			mrreq.setUnitID(unit.getUnitID());
			mrreq.setHeadless();

			// Prepare a transaction
			trans.setRequest(mrreq);
			trans.setRetries(0);
			trans.setTransDelayMS(100);
			
			// Execute the transaction repeat times
			int k = 0;
			do {
			  trans.execute();
			  mrres = (ReadMultipleRegistersResponse) trans.getResponse();
			  //Constants.println("Word 5 =" + mrres.getRegisterValue(5));
			  unit.addData(mrres.getRegisterValue(5));
			  getUnitHM().put(unit.getUnitID(), unit);
			  //for (int n = 0; n < mrres.getWordCount(); n++) {
			    //Constants.println("Word " + n + "=" + mrres.getRegisterValue(n));
			  //}
			  k++;
			} while (k < repeat);
			
  
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	public void setRunning(boolean running) {
		isRunning = running;
	}
	
	public ArrayList<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(ArrayList<Unit> unitList) {
		this.unitList = unitList;
	}
	
	public ArrayList<Unit> cloneUnits(Collection<Unit>  unitsToClone) {
    	ArrayList<Unit> retVal = new ArrayList<Unit>();
    	for (Unit unit : unitsToClone)
    	{
    		Unit newUnit = new Unit(unit.getType(), unit.getUnitID(), unit.getDataList());
    		retVal.add(newUnit);
    	}
  	
        return retVal;
    }
	
	public void clearHMData(){
		for (Unit unit: getUnitHM().values())
		{
			unit.getDataList().clear();
		}
	}
	
	public void printHM()
	{
		System.out.println("PrintHM, size and time: "+ getUnitHM().size()+ "  " + System.currentTimeMillis()/1000);
		for (Unit unit: getUnitHM().values())
		{
			System.out.println(unit.getUnitID() +"  "+ unit.getType());
			
			for (Integer data: unit.getDataList())
			{
				System.out.println(data);
			}
		}
	}

}
