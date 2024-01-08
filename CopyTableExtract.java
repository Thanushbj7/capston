	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
	import java.util.zip.ZipInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
	import java.util.zip.ZipEntry;
	import java.util.zip.ZipInputStream;


public class CopyTableExtract {



	
	
	 public static void main(String[] args) {
	        // Specify the folder containing the zip files
	        String folderPath = "C:/Users/Windows/Downloads/zipfiles";
	        // Specify the path for the Excel file
	        String excelFilePath = "C:/Excel/file.xlsx";

	        // Call the method to unzip files, read logs, and create the Excel file
	        unzipAndReadLogsAndCreateExcel(folderPath, excelFilePath);
	    }

	 private static void unzipAndReadLogsAndCreateExcel(String folderPath, String excelFilePath) {
	        File folder = new File(folderPath);
	        File[] zipFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));

	        if (zipFiles != null) {
	            try (Workbook workbook = new XSSFWorkbook()) {
	                // Create a new sheet
	                Sheet sheet = workbook.createSheet("Sheet1");

	                // Create headers
	                Row headerRow = sheet.createRow(0);
	                String[] headers = {"RUN_DATE", "RUN_CYCLE", "TABLE_NAME", "WEEKLY_DAILY", "RECORD_COUNT", "START_DATE", "END_DATE"};
	                for (int i = 0; i < headers.length; i++) {
	                    Cell cell = headerRow.createCell(i);
	                    cell.setCellValue(headers[i]);
	                }
Map<String,Integer> zipFileNameCount;
	                int rowNum = 1; // Start writing data from the second row

	                for (File zipFile : zipFiles) {
	                	zipFileNameCount=new HashMap<>();
	                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
	                    //tring ZipFileName=ZipFile.getName();
	                   //if(!zNameCount.containsKey(zipFileName))
	                        byte[] buffer = new byte[1024];
	                        ZipEntry zipEntry = zis.getNextEntry();
	                        
String zipFileName=zipFile.getName();
if(zipFileName != null){
if(!zipFileNameCount.containsKey(zipFileName)){
zipFileNameCount.put(zipFileName,1);
}else{
	int runCycleCount=zipFileNameCount.get(zipFileName);
	zipFileNameCount.put(zipFileName,runCycleCount+1);
}
int runCycleCount=zipFileNameCount.get(zipFileName);

	                        while (zipEntry != null) {
	                            String entryName = zipEntry.getName();
	                            File newFile = new File(folder, entryName);

	                            // Create directories if they do not exist
	                            if (zipEntry.isDirectory()) {
	                                newFile.mkdirs();
	                            } else {
	                                // Create parent directories for non-directory entries
	                                new File(newFile.getParent()).mkdirs();

	                                // Extract the file
	                                try (FileOutputStream fos = new FileOutputStream(newFile)) {
	                                    int len;
	                                    while ((len = zis.read(buffer)) > 0) {
	                                        fos.write(buffer, 0, len);
	                                    }
	                                }

	                                // Check if the entry is a log file and does not contain "Truncate" in its name
	                                if (entryName.toLowerCase().endsWith(".log") && !entryName.toLowerCase().contains("truncate")) {
	                                    // Process the log file and extract date and time
	                                    Date runDate = extractDateFromLogFile(newFile);

	                                    // Map table name based on file name
	                                    String tableName = mapTableName(newFile.getName());

	                                    // Extract start date from the line containing "Decrypting..."
	                                    Date startDate = extractStartDate(newFile);
                                         Date endDate=extractEndDate(newFile,sheet,rowNum);
                                         String weeklyDaily = mapWeeklyDaily(newFile.getName());
                                         Row row=sheet.getRow(rowNum);
                                         if(runCycleCount != 0){
                                         Cell runCycleCell=row.createCell(1);
                                         if(runCycleCell != null){
                                         runCycleCell.setCellValue(runCycleCount);
                                         }
                                         }
	                                    // Add log data to Excel file
	                                    addLogDataToExcel(sheet, rowNum++, runDate, tableName, startDate,endDate,weeklyDaily);
	                                }
	                                }
	                            }

	                            // Move to the next entry
	                            zipEntry = zis.getNextEntry();
	                        }

	                        System.out.println("Unzipped and processed logs: " + zipFile.getName());

	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                
	                }

	                // Write the workbook to a file
	                try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
import java.io.*;
import java.nio.file.*;

public class LogProcessor {

    private static final String OUTPUT_FILE_PATH = "output.txt"; // Specify the path for the output file

    public static void main(String[] args) {
        File logFile = new File("your_log_file_path.txt"); // Replace with the actual path to your log file
        processLogFile(logFile);
    }

    private static void processLogFile(File logFile) {
        try {
            PrintStream console = System.out; // Store the reference to the console
            FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE_PATH);
            PrintStream filePrintStream = new PrintStream(fileOutputStream);

            // Redirect System.out to the file
            System.setOut(filePrintStream);

            readLogFile(logFile);

            // Restore the original System.out
            System.setOut(console);

            System.out.println("Log data has been written to " + OUTPUT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (rest of your code remains unchanged)
}
















				import java.io.*;

public class LogProcessor {

    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        File logFile = new File("your_log_file_path.txt"); // Replace with the actual path to your log file
        processLogFile(logFile);
    }

    private static void processLogFile(File logFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE_PATH);
            PrintStream filePrintStream = new PrintStream(fileOutputStream);

            // Redirect System.out to the file
            System.setOut(filePrintStream);

            readLogFile(logFile);

            // Close the file stream to ensure the data is written
            filePrintStream.close();

            System.out.println("Log data has been written to " + OUTPUT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (rest of your code remains unchanged)
}
	    
	

