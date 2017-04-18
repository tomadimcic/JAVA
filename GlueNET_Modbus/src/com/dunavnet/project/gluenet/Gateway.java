package com.dunavnet.project.gluenet;

import java.util.ArrayList;

import com.dunavnet.project.gluenet.model.Unit;
import com.dunavnet.project.gluenet.threads.Modbus;

public class Gateway {
	
	public static void main(String[] args) {
		Modbus modbus = new Modbus();
		
		ArrayList<Unit> unitList = Settings.getUnits();
		
		modbus.initSerial();
		modbus.setUnitList(unitList);
		modbus.setRunning(true);
        Thread t = new Thread(modbus);
        t.start();
        
        try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        modbus.setRunning(false);

	}

}
