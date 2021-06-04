package report.ReportSmasher;

import java.io.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class LineFile {
	File fileLine;
	
	public LineFile(File fileLine) {
		this.fileLine = fileLine;
	}
	
	public long[] readLineFile() {
		long[] result = new long[5];
		Row row;
		Cell cell;
		
		try {
			FileInputStream is = new FileInputStream(fileLine);
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheet("TOTAL");
			
			// Getting line1777 data at I145
			row = sheet.getRow(144);
			cell = row.getCell(8); // I = 9 ---> 8 index
			result[1] = Math.round(cell.getNumericCellValue()); // Round the number before casting
			
			// Getting lineAp data at I166
			row = sheet.getRow(165);
			cell = row.getCell(8);
			result[2] = Math.round(cell.getNumericCellValue());
			
			// Getting Vip data at I195
			row = sheet.getRow(194);
			cell = row.getCell(8);
			result[0] = Math.round(cell.getNumericCellValue());
			
			// Getting eMoney data at U166
			row = sheet.getRow(165);
			cell = row.getCell(20); // U = 20 --> index = 20
			result[3] = Math.round(cell.getNumericCellValue());
			
			// Getting video call data at U210
			row = sheet.getRow(209);
			cell = row.getCell(20);
			result[4] = Math.round(cell.getNumericCellValue());
			workbook.close();
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
