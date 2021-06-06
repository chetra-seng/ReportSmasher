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
		JPanel background = new JPanel();
		background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Creating a main panel using box layout
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		background.add(mainPanel); // Adding main panel to background
		
		// Create a label and button Panel
		JPanel buttonAndLabel = new JPanel(new BorderLayout());
		buttonAndLabel.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 20));
		mainPanel.add(buttonAndLabel); // Adding it to main panel
		
		// Create a button panel and add buttons to it
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		fileButton = new JButton[4];
		for(int i = 0; i<4; i++) {
			fileButton[i] = new JButton("Select");
			buttonPanel.add(fileButton[i]);
			if(i<3) buttonPanel.add(Box.createRigidArea(new Dimension(0, 7))); // Adding empty space
		}
		buttonAndLabel.add(BorderLayout.WEST, buttonPanel);
		
		// Create a label panel and add file label to it
		JPanel fileLabelPanel = new JPanel();
		fileLabelPanel.setLayout(new BoxLayout(fileLabelPanel, BoxLayout.Y_AXIS));
		fileLabelPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 0, 0));
		fileLabel = new JLabel[4];
		for(int i = 0; i<4; i++) {
			fileLabel[i] = new JLabel("");
			fileLabelPanel.add(fileLabel[i]);
			if(i<3) fileLabelPanel.add(Box.createRigidArea(new Dimension(0, 17)));
		}
		buttonAndLabel.add(BorderLayout.CENTER, fileLabelPanel);
		
		setAllFileLabel(); // Setting Label
		
		
		// Create a date picker and add it to main panel
		JPanel datePanel = new JPanel(new BorderLayout());
		date = new JDateChooser();
		JLabel dateLabel = new JLabel("Select a date: ");
		datePanel.add(BorderLayout.WEST, dateLabel);
		datePanel.add(BorderLayout.CENTER, date);
		mainPanel.add(datePanel);
		
		// Creating the result text box and add it to main panel
		result = new JTextArea(7, 40);
		result.setEditable(false);
		JScrollPane scroller = new JScrollPane(result);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scroller);
		
		// Create a start button panel and add it to background
		JPanel startPanel = new JPanel();
		startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.X_AXIS));
		JButton startButton = new JButton("Start");
		JButton resetButton = new JButton("Reset"); // Add a reset button
		startPanel.add(startButton);
		startPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Create an empty space
		startPanel.add(resetButton);
		background.add(startPanel);
		
		// Adding action listener
		fileButton[0].addActionListener(new RecordFileListener());
		fileButton[1].addActionListener(new LineFileListener());
		fileButton[2].addActionListener(new SocialFileListener());
		fileButton[3].addActionListener(new ReportFileListener());
		startButton.addActionListener(new StartButtonListener());
		
		frame.getContentPane().add(background);
		frame.setBounds(100, 100, 400, 360);
		frame.setVisible(true);
	}
	
	private void setAllFileLabel() {
		fileLabel[0].setText("System Export File");
		fileLabel[1].setText("Report All Lines File");
		fileLabel[2].setText("Report Social File");
		fileLabel[3].setText("Final Report File");
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
	
	private void displayWaitTimeOrTotalLine(long list[]) {
		result.setText(""); // Clear the old result first
		result.append("Total Line VIP: " + list[0]);
		result.append("\nTotal line mass: " + list[1]);
		result.append("\nTotal line AP: " + list[2]);
		result.append("\nTotal line eMoney: " + list[3]);
		result.append("\nTotal line Video call: " + list[4]);
	}
	
	private void displaySocial(long list[]) {
		result.setText("");
		result.append("Inbox total waiting time: " + list[0]);
		result.append("\nTotal inbox: " + list[1]);
		result.append("\nComment total waiting time: " + list[2]);
		result.append("\nTotal comment: " + list[3]);
	}
	class RecordFileListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("CSV File", "csv");
			fileLabel[0].setText(f.getName());
			fileCSV = new RecordFile(f);
			fileCSV.writeFile();
			waitTime = fileCSV.getData();
			displayWaitTimeOrTotalLine(waitTime);
			String fileName = f.getName();
			if(f.delete()) result.append("\n" + fileName + " was deleted");
		}
	}
	
	class LineFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("Report Percentage File", "xlsx");
			fileLabel[1].setText(f.getName());
			fileLine = new LineFile(f);
			line = fileLine.readLineFile();
			displayWaitTimeOrTotalLine(line);
			String fileName = f.getName();
			if(f.delete()) result.append("\n" + fileName + " was deleted");
		}
	}
	
	class SocialFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			File f = showDialog("Report Social File", "xls");
			fileSocial = new SocialFile(f);
			fileLabel[2].setText(f.getName());
			social = fileSocial.readSocialFile();
			displaySocial(social);
			String fileName = f.getName();
			if(f.delete()) result.append("\n" + fileName + " was deleted");
		}
	}
	
	class ReportFileListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			reportfile = showDialog("Report To Update File", "xlsx");
			// ff = new ReportFile(f); can't do it here
			fileLabel[3].setText(reportfile.getName());
		}
	}
	
	class StartButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			Date todayDate = getCorrectDateFormat().getTime(); // Get a date with 00:00:00 time
			ff = new ReportFile(reportfile, todayDate);
			ff.updateReportFile();
			result.setText("Update Successful...");
		}
	}
	
	class ResetButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			for(int i=0; i<5; i++) {
				line[i] = 0;
				waitTime[i] = 0;
			}
			for(int i=0; i<4; i++) {
				social[i] = 0;
			}
			setAllFileLabel();
			result.setText("");
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
