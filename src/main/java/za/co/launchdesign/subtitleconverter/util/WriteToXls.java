package za.co.launchdesign.subtitleconverter.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import za.co.launchdesign.subtitleconverter.model.Caption;
import za.co.launchdesign.subtitleconverter.model.SubtitleObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class WriteToXls {

    public XSSFWorkbook writeToXls(SubtitleObject subtitleObject) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = subtitleObject.getTitle();

        XSSFSheet sheet = workbook.createSheet("Subtitles");

        XSSFRow row;
        int rowId = 0;


        row= sheet.createRow(rowId++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Title");
        cell = row.createCell(1);
        cell.setCellValue(subtitleObject.getTitle());

        row= sheet.createRow(rowId++);
        cell = row.createCell(0);
        cell.setCellValue("Season");
        cell = row.createCell(1);
        cell.setCellValue(subtitleObject.getSeasonNumber());

        row= sheet.createRow(rowId++);
        cell = row.createCell(0);
        cell.setCellValue("Episode");
        cell = row.createCell(1);
        cell.setCellValue(subtitleObject.getEpisodeNumber());

        row= sheet.createRow(rowId++);
        cell = row.createCell(0);
        cell.setCellValue("Sequence No");
        cell = row.createCell(1);
        cell.setCellValue("Start Time");
        cell = row.createCell(2);
        cell.setCellValue("End Time");
        cell = row.createCell(3);
        cell.setCellValue("Dialog");

        for(Caption caption: subtitleObject.getCaptions()){
            int cellId = 0;

            row= sheet.createRow(rowId++);
            cell = row.createCell(cellId++);
            cell.setCellValue(caption.getSequence());
            cell = row.createCell(cellId++);
            cell.setCellValue(caption.getStart().getTime("hh:mm:ss,ms"));
            cell = row.createCell(cellId++);
            cell.setCellValue(caption.getEnd().getTime("hh:mm:ss,ms"));
            cell = row.createCell(cellId++);
            cell.setCellValue(caption.getContent());
        }

        // need to format destinatoion to give it a name

        //FileOutputStream out = new FileOutputStream(new File(destination, outputFileName));
        //workbook.write(out);
        //out.close();
        System.out.println("Number of sheets" +workbook.getNumberOfSheets());
        return workbook;



    }
}
