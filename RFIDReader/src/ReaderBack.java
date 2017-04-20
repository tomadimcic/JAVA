import java.nio.charset.StandardCharsets;

import com.caen.RFIDLibrary.CAENRFIDException;
import com.caen.RFIDLibrary.CAENRFIDLogicalSource;
import com.caen.RFIDLibrary.CAENRFIDPort;
import com.caen.RFIDLibrary.CAENRFIDReader;
import com.caen.RFIDLibrary.CAENRFIDReaderInfo;
import com.caen.RFIDLibrary.CAENRFIDTag;
import javax.xml.bind.DatatypeConverter;


public class ReaderBack {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		
		CAENRFIDReader MyReader = new CAENRFIDReader(); //Create myObject
		double Gain = 8.0;
		double Loss = 1.5;
		double ERPPower = 2000.0;
		int OutPower = (int)(ERPPower/Math.pow(10, ((Gain-Loss-2.14)/10)));
		
		
		
		try {
	        
				MyReader.Connect(CAENRFIDPort.CAENRFID_RS232, "/dev/ttyUSB0");
			
	    
				MyReader.SetPower(OutPower);
	        CAENRFIDReaderInfo Info = MyReader.GetReaderInfo(); // Create Object for reader info
	    
	            String Model = Info.GetModel(); //Get info about model
	            String SerialNumber=Info.GetSerialNumber(); // Get info about serialNumber
	            String FWRelease = MyReader.GetFirmwareRelease();   // Get info about FW
	    
	            System.out.println("aaaa " + Model);
	        
	        CAENRFIDLogicalSource MySource = MyReader.GetSource("Source_0"); // Choose Source 0-->RFID tags 1-->Barcode
                MySource.AddReadPoint("Ant0");
                MySource.AddReadPoint("Ant1");

                String[] str = MyReader.GetReadPoints();
            
                for (int i = 0; i < str.length; i++) {
                
                    System.out.println(str[i]);
                    System.out.println(MyReader.GetReadPointStatus(str[i]));
                }


	        MySource.SetQ_EPC_C1G2(3); // set Q Value
	        
	        System.out.println("bbbbb");
	    
	        CAENRFIDTag[] MyTags = MySource.InventoryTag(); 
	        if(MyTags == null){
	        	MyTags = new CAENRFIDTag[0];
	        }
	        
		        
	        System.out.println("ccc");
	    
	        System.out.println("eeee " + MyTags.length);
	        //final char[] hexArray = "0123456789ABCDEF".toCharArray();
	        //if (MyTags.length > 0) { 
	        for (int i = 0; i < MyTags.length; i++) {
	        	String s = DatatypeConverter.printHexBinary(MyTags[i].GetId());

			System.out.println("Antenna: " + MyTags[i].GetAntenna() + " Tag: " + s);
	        }
	        
    
	        //}
		} catch (Exception e) {
			
			try {
				MyReader.Disconnect();
			} catch (CAENRFIDException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			
		} // Open a connection
		
		try {
			MyReader.Disconnect();
		} catch (CAENRFIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		
    
          

	}

}
