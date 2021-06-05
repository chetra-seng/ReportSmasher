package report.ReportSmasher;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
public class AppLauncher {
	private JTextArea result;
	private JButton[] fileButton;
	private JLabel[] fileLabel;
	private JFrame frame;
	private String defaultLocation = "C:/Users/" + System.getProperty("user.name") + "/Downloads";
	private long[] waitTime;
	private long[] line;
	private long[] social;
	private RecordFile fileCSV;
	private LineFile fileLine;
	private SocialFile fileSocial;
	private ReportFile ff;
	private File reportfile;
	
	private JDateChooser date;
	
	public void start() {
		frame = new JFrame("Report Smasher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		// Creating a grid to hold labels and buttons
		JPanel gridPanel = new JPanel();
		GridLayout grid = new GridLayout(4, 2);
		grid.setVgap(5);
		grid.setHgap(5);
		gridPanel.setLayout(grid);
		fileButton = new JButton[4];
		for(int i = 0; i<4; i++) {
			fileButton[i] = new JButton("Select");
		}
		
		fileLabel = new JLabel[4];
		for(int i = 0; i<4; i++) {
			fileLabel[i] = new JLabel("");
			gridPanel.add(fileButton[i]);
			gridPanel.add(fileLabel[i]);
		}
		
		// Setting Label
		fileLabel[0].setText("System Export File");
		fileLabel[1].setText("Report All Lines File");
		fileLabel[2].setText("Report Social File");
		fileLabel[3].setText("Final Report File");
		
		// Creating the result text box
		result = new JTextArea(7, 40);
		result.setEditable(false);
		JScrollPane scroller = new JScrollPane(result);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Create a date picker
		JPanel datePanel = new JPanel(new BorderLayout());
		date = new JDateChooser();
		JLabel dateLabe = new JLabel("Select a date: ");
		datePanel.add(BorderLayout.WEST, dateLabe);
		datePanel.add(BorderLayout.CENTER, date);
		
		// Creating the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel t = new JPanel(); // Create a new panel to host grid
		t.add(gridPanel); // add the newly created panel
		topPanel.add(t);
		topPanel.add(datePanel);
		JPanel resultPanel = new JPanel();
		resultPanel.add(scroller);
		resultPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		topPanel.add(resultPanel);
		
		// Creating bottom Panel
		JPanel bottomPanel = new JPanel();
		JButton startButton = new JButton("Start");
		bottomPanel.add(startButton);
		
		// Adding action listener
		fileButton[0].addActionListener(new RecordFileListener());
		fileButton[1].addActionListener(new LineFileListener());
		fileButton[2].addActionListener(new SocialFileListener());
		fileButton[3].addActionListener(new ReportFileListener());
		startButton.addActionListener(new StartButtonListener());
		
		mainPanel.add(BorderLayout.NORTH, topPanel);
		mainPanel.add(BorderLayout.CENTER, bottomPanel);
		frame.getContentPane().add(mainPanel);
		frame.setBounds(100, 100, 500, 400);
		frame.setVisible(true);
	}
	
	// File chooser pop up
	private File showDialog(String message, String fileType) {
		JFileChooser chooser = new JFileChooser(defaultLocation);
		chooser.setAcceptAllFileFilterUsed(false); // Don't accept all type of file
		chooser.setDialogTitle("Select a File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(message, fileType);
		chooser.addChoosableFileFilter(filter); // Set file filter
		chooser.showOpenDialog(frame);
		
		return(chooser.getSelectedFile());
	}
	
	private Calendar getCorrectDateFormat() {
		Calendar oldCal = date.getCalendar();
		int dd = oldCal.get(Calendar.DAY_OF_MONTH);
		int mm = oldCal.get(Calendar.MONTH);
		int yy = oldCal.get(Calendar.YEAR);
		Calendar newCal = new GregorianCalendar(yy, mm, dd);
		
		return newCal;
	}
	
	class RecordFileListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("CSV File", "csv");
			fileButton[0].setText("Selected");
			fileLabel[0].setText(f.getName());
			fileCSV = new RecordFile(f);
		}
	}
	
	class LineFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("Report Percentage File", "xlsx");
			fileButton[1].setText("Selected");
			fileLabel[1].setText(f.getName());
			fileLine = new LineFile(f);
		}
	}
	
	class SocialFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("Report Social File", "xls");
			fileSocial = new SocialFile(f);
			fileButton[2].setText("Selected");
			fileLabel[2].setText(f.getName());
		}
	}
	
	class ReportFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			reportfile = showDialog("Report To Update File", "xlsx");
			// ff = new ReportFile(f); can't do it here
			fileButton[3].setText("Selected");
			fileLabel[3].setText(reportfile.getName());
		}
	}
	
	class StartButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			Date todayDate = getCorrectDateFormat().getTime(); // Get a date with 00:00:00 time
			
			fileCSV.writeFile();
			waitTime = fileCSV.getData();
			for(int i=0; i<waitTime.length; i++) {
				result.append("Result: " + waitTime[i] + "\n");
			}
			
			line = fileLine.readLineFile();
			for(int i=0; i<line.length; i++) {
				result.append(line[i] + "\n");
			}
			
			social = fileSocial.readSocialFile();
			for(int i=0; i<social.length; i++) {
				result.append(social[i] + "\n");
			}
			
			ff = new ReportFile(reportfile, todayDate);
			ff.updateReportFile();
			
		}
	}
	
	class ReportFile{
		File fileReport;
		Date todayDate;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		
		public ReportFile(File fileReport, Date todayDate) {
			this.fileReport = fileReport;
			this.todayDate = todayDate;
		}
		
		private int findCell() {
			try {
				FileInputStream is = new FileInputStream(fileReport);
				workbook = new XSSFWorkbook(is);
				int num = 11;
				sheet = workbook.getSheetAt(0);
				Row header = sheet.getRow(1); // Header is on row number 2
				while(num < 345) {
					Cell cell = header.getCell(num);
					if(todayDate.compareTo(cell.getDateCellValue()) == 0) {
						return cell.getColumnIndex();
					}
					num++;
				}
				is.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return -1;
		}
		
		private void updateTwoCell(int numRow, int numCol, int index) {
			Row waitTimeRow = sheet.getRow(numRow); // Wait time Row
			Row lineRow = sheet.getRow(numRow+1); // Line Row is below wait time Row
			Cell waitTimeCell = waitTimeRow.getCell(numCol);
			Cell lineCell = lineRow.getCell(numCol);
			
			// Updating both cell
			waitTimeCell.setCellValue((double)waitTime[index]);
			lineCell.setCellValue((double)line[index]);
		}
		
		private void updateWaitTimeAndLine(int numCol) {
			int numRow = 9; // Starting from line number 10
			
			// Update the first 4 values because they are closed together
			for(int i=0; i<4; i++) {
				updateTwoCell(numRow, numCol, i);
				numRow = numRow + 3;
			}
			
			// Updating the last value aka line video
			numRow = 28; // line video is currently at line 29
			updateTwoCell(numRow, numCol, 4);
		}
		
		private void updateOneCell(int numRow, int numCol, int index) {
			Row row = sheet.getRow(numRow);
			Cell cell = row.getCell(numCol);
			cell.setCellValue((double)social[index]);
		}
		
		private void updateSocial(int numCol) {
			int numRow = 22; // Start line at 23
			int index = 0; // social[index] which starts from 0
			
			updateOneCell(numRow, numCol, index); // Updating first value
			
			//Update the second value
			numRow++; index++;
			updateOneCell(numRow, numCol, index); // One line below the first
			
			// Update the third value
			numRow = 25; index++;
			updateOneCell(numRow, numCol, index);
			
			// Update the last value
			numRow++; index++;
			updateOneCell(numRow, numCol, index);
		}
		
		public void updateReportFile() {
			int numCol = findCell();
			updateWaitTimeAndLine(numCol);
			updateSocial(numCol);
			try {
				FileOutputStream os = new FileOutputStream(fileReport);
				workbook.write(os);
				os.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		}
	}
}
