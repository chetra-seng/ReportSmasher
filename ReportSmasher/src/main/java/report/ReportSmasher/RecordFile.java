package report.ReportSmasher;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
public class RecordFile {
	private final File fileCSV;
	private ArrayList<String> newFileData;
	private File fileToWrite;
	
	public RecordFile(File fileCSV) {
		this.fileCSV = fileCSV;
	}
	
	public void writeFile() {
		fileToWrite = new File("temp.csv");
		readFile();
		
		// Remove the 7 line fillers from the array list
		// CSV header start from line 8
		newFileData.subList(0, 7).clear();
		
		String[] header = newFileData.get(0).split(","); // Getting all the header separately
		newFileData.remove(0); // Remove the header
		
		try {
			FileWriter w = new FileWriter(fileToWrite);
			BufferedWriter bWriter = new BufferedWriter(w);
			@Deprecated
			CSVPrinter writer = new CSVPrinter(bWriter, CSVFormat.DEFAULT.withHeader(header[0], header[1], header[2], header[3], header[4],
					header[5], header[6], header[7], header[8], header[9], header[10], header[11], header[12], header[13], header[14],
					header[15], header[16], header[17], header[18], header[19]));
			for(String line:newFileData) {
				String[] data = line.split(",");
				writer.printRecord(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
						data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18], data[19]);
				writer.flush();
			}
			writer.close();
			newFileData.clear();
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void readFile() {
		String line;
		newFileData = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileCSV));
			while((line = reader.readLine()) != null) {
				newFileData.add(line);
			}
			reader.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public long[] getData() {
		final String waitTime = "WAITING DURATION";
		long[] result = new long[5];
		for(int i=0; i<5; i++) {
			result[i] = 0;
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileToWrite));
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
			for(CSVRecord record : parser) {
				if(!record.get("AGENT").isBlank()) {
    				try {
    					if(record.get("QUEUE NAME").contains("Line VIP")) {
        					
        					result[0] = result[0] + Integer.parseInt(record.get(waitTime));
        				}
        				else if(record.get("QUEUE NAME").contains("Line_1777_DCS_20210421") ||
        						record.get("QUEUE NAME").contains("Line 17711_TS (New)") ||
        						record.get("QUEUE NAME").contains("Line 0979097097_DCS") ||
        						record.get("QUEUE NAME").contains("Line 0979097097_TS")) {
        					result[1] = result[1] + Integer.parseInt(record.get(waitTime));
        				}
        				else if(record.get("QUEUE NAME").contains("Line 0976 097097 - ENGLISH") ||
        						record.get("QUEUE NAME").contains("Line 0976 097097 - KHMER")) {
        					result[2] = result[2] + Integer.parseInt(record.get(waitTime));
        				}
        				else if(record.get("QUEUE NAME").contains("Line eMoney 868")) {
        					result[3] = result[3] + Integer.parseInt(record.get(waitTime));
        				}
        				else if(record.get("QUEUE NAME").contains("Queue video")) {
        					result[4] = result[4] + Integer.parseInt(record.get(waitTime));
        				}
    				}
    				catch(NumberFormatException ex) {
						System.out.println("Skipped");
					}
    			}
			}
			parser.close();
			boolean deleted = fileToWrite.delete();
			if(deleted) {
				System.out.print("temp.csv was deleted");
			}
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
