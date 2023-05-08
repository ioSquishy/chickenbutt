package chicken.butt.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {
    private static final String fileName = "excel.xlsx";
    private TreeMap<Long, BRData> data;

    public Excel(TreeMap<Long, BRData> data) {
        this.data = data;
    }

    //epoch id, username, sign out, sign in, total time
    public InputStream generateExcel() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        
        //Title columns
        Row titles = sheet.createRow(0);
        titles.createCell(0).setCellValue("Epoch ID");
        titles.createCell(1).setCellValue("Username");
        titles.createCell(2).setCellValue("Sign Out Time");
        titles.createCell(3).setCellValue("Sign In Time");
        titles.createCell(4).setCellValue("Total Time Taken (Seconds)");

        BRData[] dataArray = (BRData[]) data.values().toArray();
        //data
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            Row entry = sheet.createRow(rowIndex);
            entry.createCell(0).setCellValue(dataArray[rowIndex].getEpochID()); //epochid
            entry.createCell(1).setCellValue(Cache.getUsername(dataArray[rowIndex].getUserID())); //username
            entry.createCell(2).setCellValue(Data.formatTime(dataArray[rowIndex].getSignOutTime())); //signout
            entry.createCell(3).setCellValue(Data.formatTime(dataArray[rowIndex].getSignInTime())); //signin
            entry.createCell(4).setCellValue(dataArray[rowIndex].getBreakLength()); //break length
        }

        //write to file
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch(Exception e) {}

        return null;
    }
}
