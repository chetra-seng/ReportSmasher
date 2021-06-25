package report.ReportSmasher;

import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class SocialFile {
	File fileSocial;
	long result;
	
	public SocialFile(File fileSocial) {
		this.fileSocial = fileSocial;
	}
	
	public long[] readSocialFile() {
		long[] result = new long[4];
		int numRow = 23;
		int numCol = 4;
		
		try {
			FileInputStream is = new FileInputStream(fileSocial);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			// 1. Getting total comment in day at E24
			Row row = sheet.getRow(numRow);
			Cell cell = row.getCell(numCol);
			// Round the value before casting
			result[3] = Math.round(cell.getNumericCellValue());
			
			// 2. Getting total inbox in day at F24
			cell = row.getCell(numCol + 1); // Same row but col + 1
			result[1] = Math.round(cell.getNumericCellValue());
			
			// 3. Getting comment waiting time at C34
			numRow = 33;
			numCol = 2;
			row = sheet.getRow(numRow);
			cell = row.getCell(numCol);
			result[2] = Math.round(cell.getNumericCellValue());
			
			// Getting inbox waiting time
			row = sheet.getRow(numRow + 1); // Move down vertically
			cell = row.getCell(numCol); // Same column
			result[0] = Math.round(cell.getNumericCellValue());
			workbook.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
