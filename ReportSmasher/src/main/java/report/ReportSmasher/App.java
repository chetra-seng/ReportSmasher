package report.ReportSmasher;

import org.apache.commons.csv.*;
import java.io.*;
import java.util.ArrayList;
public class App 
{
	ArrayList<String> text;
	File file;
    public static void main( String[] args )
    {
        App test = new App();
        test.readTheFile();
        test.writeTheFile();
        test.smash();
    }
    
    public void readTheFile() {
    	try {
    		File readFile = new File("C:/Users/Chetra/Downloads/call-logs_202105270840.csv");
    		FileReader r = new FileReader(readFile);
    		BufferedReader reader = new BufferedReader(r);
    		String line = null;
    		text = new ArrayList<String>();
    		while((line = reader.readLine()) != null) {
    			text.add(line);
    		}
    		reader.close();
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void writeTheFile() {
    	// Remove 7 lines from array list
    	for(int i=0; i<7; i++) {
    		text.remove(0);
    	}
    	String header[] = text.get(0).split(","); // Get header
    	text.remove(0); // Remove header from array list
    	
    	try {
    		file = new File("E:/temp.csv");
    		FileWriter w = new FileWriter(file);
    		BufferedWriter writer = new BufferedWriter(w);
    		// Add all the headers
    		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header[0], header[1], header[2],
    				header[3], header[4], header[5], header[6], header[7], header[8], header[9], header[10],
    				header[11], header[12], header[13], header[14], header[15], header[16], header[17], header[18],
    				header[19]));
    		for(String line:text) {
    			String part[] = line.split(",");
    			printer.printRecord(part[0], part[1], part[2], part[3], part[4], part[5], part[6], part[7],
    					part[8], part[9], part[10], part[11], part[12], part[13], part[14], part[15], part[16],
    					part[17], part[18], part[19]);
    		}
    		printer.flush();
    		w.close();
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void smash() {
    	try {
    		BufferedReader r = new BufferedReader(new FileReader(file));
    		CSVParser parser = new CSVParser(r, CSVFormat.DEFAULT.withHeader().withTrim());
    		long lineVip = 0;
    		long line1777 = 0;
    		long lineAp = 0;
    		long lineEmoney = 0;
    		String waitTime = "WAITING DURATION";
    		for(CSVRecord record : parser) {
    			if(!record.get("AGENT").isBlank()) {
    				if(record.get("QUEUE NAME").contains("Line VIP")) {
    					lineVip = lineVip + Integer.parseInt(record.get(waitTime));
    				}
    				else if(record.get("QUEUE NAME").contains("Line_1777_DCS_20210421") ||
    						record.get("QUEUE NAME").contains("Line 17711_TS (New)") ||
    						record.get("QUEUE NAME").contains("Line 0979097097_DCS") ||
    						record.get("QUEUE NAME").contains("Line 0979097097_TS")) {
    					line1777 = line1777 + Integer.parseInt(record.get(waitTime));
    				}
    				else if(record.get("QUEUE NAME").contains("Line 0976 097097 - ENGLISH") ||
    						record.get("QUEUE NAME").contains("Line 0976 097097 - KHMER")) {
    					lineAp = lineAp + Integer.parseInt(record.get(waitTime));
    				}
    				else if(record.get("QUEUE NAME").contains("Line eMoney 868")) {
    					lineEmoney = lineEmoney + Integer.parseInt(record.get(waitTime));
    				}
    			}
    		}
    		System.out.println("VIP: " + lineVip);
    		System.out.println("1777: " + line1777);
    		System.out.println("AP: " + lineAp);
    		System.out.println("Emoney: " + lineEmoney);
    		r.close();
    		boolean deleted = file.delete();
    		if(deleted) {
    			System.out.println("File Deleted");
    		}
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
