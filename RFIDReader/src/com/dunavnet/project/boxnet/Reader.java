/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dunavnet.project.boxnet;

import com.caen.RFIDLibrary.CAENRFIDException;
import com.caen.RFIDLibrary.CAENRFIDLogicalSource;
import com.caen.RFIDLibrary.CAENRFIDLogicalSourceConstants;
import com.caen.RFIDLibrary.CAENRFIDPort;
import com.caen.RFIDLibrary.CAENRFIDReader;
import com.caen.RFIDLibrary.CAENRFIDReaderInfo;
import com.caen.RFIDLibrary.CAENRFIDTag;
import com.dunavnet.project.boxnet.communication.PingCommunication;
import com.dunavnet.project.boxnet.threads.ReadLoop;
import com.dunavnet.project.boxnet.threads.serial.TwoWaySerialComm;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Toma
 */
public class Reader {

    /**
     * @param args the command line arguments
     */
    boolean running = true;
    String loraPing = "2=";
    
    public void start(){
        CAENRFIDReader myReader = new CAENRFIDReader(); //Create myObject
        ReadLoop readThread = null;
        double Gain = 8.0;
        double Loss = 0.5;
        double ERPPower = 2000.0;
        int OutPower = (int) (ERPPower / Math.pow(10, ((Gain - Loss - 2.14) / 10)));
        this.loraPing += Settings.getLoraPing();

        CAENRFIDLogicalSource mySource = null;

        try {
            myReader.Connect(CAENRFIDPort.CAENRFID_RS232, "/dev/ttyUSB0");

            myReader.SetPower(Integer.parseInt(Settings.setPower()));
            
            System.out.println("Output power: " + myReader.GetPower());

            CAENRFIDReaderInfo Info = myReader.GetReaderInfo(); // Create Object for reader info

            String Model = Info.GetModel(); //Get info about model
            String SerialNumber = Info.GetSerialNumber(); // Get info about serialNumber
            String FWRelease = myReader.GetFirmwareRelease();   // Get info about FW

            System.out.println("Model: " + Model + "; Serial Number: " + SerialNumber + "; FWRelease: " + FWRelease);
            mySource = myReader.GetSource("Source_0"); // Choose Source 0-->RFID tags 1-->Barcode

            mySource.AddReadPoint("Ant0");
            mySource.AddReadPoint("Ant1");
            //if(Settings.numOfReaders().equals("2")){
            	mySource.AddReadPoint("Ant2");
            	mySource.AddReadPoint("Ant3");
            //}
            

            mySource.SetQ_EPC_C1G2(Integer.parseInt(Settings.setQ1())); // set Q Value
            mySource.SetSession_EPC_C1G2(CAENRFIDLogicalSourceConstants.EPC_C1G2_SESSION_S1);

            mySource.SetReadCycle(0);

            long patience = 1000 * 60 * 60;
            
            threadMessage("Starting SerialLoop thread");
            
            if(Settings.getMode().equals("1"))
            	( new TwoWaySerialComm() ).connect("/dev/ttyS80");
            
            threadMessage("SerialLoop thread started");
              

            threadMessage("Starting ReadLoop thread");
            long startTime = System.currentTimeMillis();
            readThread = new ReadLoop(mySource, myReader);
            readThread.setRunning(true);
            Thread t = new Thread(readThread);
            t.start();

            threadMessage("Sending data on every 5 seconds...");
            // loop until ReadLoop
            // thread exits
            while (t.isAlive()) {
                
                // Interrupt ReadLoop every 5 seconds
                t.join(600000);
//                t.join(30000);
                
                threadMessage("I am alive!");
                TwoWaySerialComm.sendData(this.loraPing, 0);
//                Thread t1 = new Thread(new PingCommunication());
//        		t1.start();
        		
        		
        		
        		
                /*if (((System.currentTimeMillis() - startTime) > patience)
                        && t.isAlive()) {
                    threadMessage("Tired of waiting!");
                    t.interrupt();
                    // Shouldn't be long now
                    // -- wait indefinitely
                    t.join();
                }*/
            }
            threadMessage("Finally!");

            //Reader reader = new Reader();
            //reader.read(mySource);

            
            myReader.Disconnect();
            readThread.setRunning(false);
        } catch (Exception e) {

            try {
                
                myReader.Disconnect();
                readThread.setRunning(false);
            } catch (CAENRFIDException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
    }
    
    private void read(CAENRFIDLogicalSource MySource) throws Exception, CAENRFIDException {
        while (running) {

            CAENRFIDTag[] MyTags = MySource.InventoryTag();
            if (MyTags == null) {
                MyTags = new CAENRFIDTag[0];
            }

            //System.out.println("eeee " + MyTags.length);
            for (int i = 0; i < MyTags.length; i++) {
                String s = DatatypeConverter.printHexBinary(MyTags[i].GetId());

                System.out.println("Antenna: " + MyTags[i].GetAntenna() + " Tag: " + s);
            }
        }
    }

    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName
                = Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    public static void main(String[] args) throws InterruptedException {

        Reader reader = new Reader();
        reader.start();

    }

    

}
