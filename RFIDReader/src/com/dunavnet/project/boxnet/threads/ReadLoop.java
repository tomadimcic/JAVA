/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dunavnet.project.boxnet.threads;

import com.caen.RFIDLibrary.CAENRFIDException;
import com.caen.RFIDLibrary.CAENRFIDLogicalSource;
import com.caen.RFIDLibrary.CAENRFIDReader;
import com.caen.RFIDLibrary.CAENRFIDTag;
import com.dunavnet.project.boxnet.Constants;
import com.dunavnet.project.boxnet.Settings;
import com.dunavnet.project.boxnet.communication.LoRaCommunication;
import com.dunavnet.project.boxnet.communication.WiFiCommunication;
import com.dunavnet.project.boxnet.listener.SendingListener;
import com.dunavnet.project.boxnet.model.TagData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Toma
 */
public class ReadLoop implements Runnable, SendingListener {

    private boolean running;
    private boolean isSending = false;
    CAENRFIDLogicalSource mySource;
    CAENRFIDReader myReader;
    private ArrayList<CAENRFIDTag> myTagsList;
    private HashMap<String, TagData> myTagsHM;

    public HashMap<String, TagData> getMyTagsHM() {
    	if (this.myTagsHM == null)
    	{
    		this.myTagsHM = new HashMap<String, TagData>();
    		return this.myTagsHM;
    	}
		return myTagsHM;
	}

	public void setMyTagsHM(HashMap<String, TagData> myTagsHM) {
		this.myTagsHM = myTagsHM;
	}

	public ReadLoop(CAENRFIDLogicalSource mySource, CAENRFIDReader myReader) {
        this.mySource = mySource;
        this.myReader = myReader;
    }

    public void run() {
        
        setMyTagsList(new ArrayList<CAENRFIDTag>());
        String mode = Settings.getMode();
        
        try {

            long patience = 5000;
            long startTime = System.currentTimeMillis();
            while (isRunning()) {

                CAENRFIDTag[] myTags = mySource.InventoryTag();
                if (myTags == null) {
                    myTags = new CAENRFIDTag[0];
                }

                for (int i = 0; i < myTags.length; i++) {
                    String tagId = DatatypeConverter.printHexBinary(myTags[i].GetId());
                    String tagAntenna = myTags[i].GetAntenna();
                    TagData td = new TagData(tagId, tagAntenna);
                    getMyTagsHM().put(tagId, td);
                    //threadMessage("Antenna: " + myTags[i].GetAntenna() + " Tag: " + s);
                    
//                    boolean flag = false;
//                    
//                    if(getMyTagsList().size() == 0){
//                    	getMyTagsList().add(myTags[i]);
//                    	//threadMessage("Antenna: " + myTags[i].GetAntenna() + " Tag: " + s);
//                    }
//                    else{
//	                    for (CAENRFIDTag tagID : getMyTagsList()) {
//	                		String str = DatatypeConverter.printHexBinary(tagID.GetId());
//	                		if(s.equals(str)){
//	                			flag = true;
//	                		}
//	            		}
//	                    if(!flag){
//	                    	getMyTagsList().add(myTags[i]);
//	                    	//threadMessage("Antenna: " + myTags[i].GetAntenna() + " Tag: " + s);
//	                    }
//                    }
                }
                Thread.sleep(10);
                
                if (((System.currentTimeMillis() - startTime) > patience)
                        && !getMyTagsHM().isEmpty()) {
                    //threadMessage("Sending data ...");
                    Constants.println("Sending data ...Time: "+ System.currentTimeMillis()/1000 +" hm size: "+ getMyTagsHM().size());
                    if(Constants.DEBUG)
                    	printHM();
                    Thread t;
                    if(mode.equals("1")){
                    	if(!isSending){
                    		t = new Thread(new LoRaCommunication(cloneMyTags(getMyTagsHM().values()), this));
                    		t.start();
                    		Thread.sleep(20);
                    		getMyTagsHM().clear();
                    		}
                    }
                    else{
                    	t = new Thread(new WiFiCommunication(cloneMyTags(getMyTagsHM().values()), this));
                    	t.start();
                    	Thread.sleep(20);
                    	getMyTagsHM().clear();
                    }
                    startTime = System.currentTimeMillis();

                }
            
            }

        } catch (CAENRFIDException ex) {
            threadMessage("Disconnecting reader!");
                setRunning(false);
                
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return the myTagsList
     */
    public ArrayList<CAENRFIDTag> getMyTagsListq() {
        return myTagsList;
    }
    
    public ArrayList<TagData> cloneMyTags(Collection<TagData>  tagsToClone) {
    	ArrayList<TagData> retVal = new ArrayList<TagData>();
    	for (TagData tg : tagsToClone)
    	{
    		TagData newTg = new TagData(tg.GetId(), tg.GetAntenna());
    		retVal.add(newTg);
    	}
  	
        return retVal;
    }

    /**
     * @param myTagsList the myTagsList to set
     */
    public void setMyTagsList(ArrayList<CAENRFIDTag> myTagsList) {
        this.myTagsList = myTagsList;
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

	@Override
	public boolean setSending(boolean isSending) {
		this.isSending = isSending;
		return this.isSending;
	}
	
	public void printHM()
	{
		System.out.println("PrintHM, size and time: "+ getMyTagsHM().size()+ "  " + System.currentTimeMillis()/1000);
		for (TagData tg: getMyTagsHM().values())
		{
			System.out.println(tg.GetId() +"  "+ tg.GetAntenna());
		}
	}

}
