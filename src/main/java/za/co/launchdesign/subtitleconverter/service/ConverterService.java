package za.co.launchdesign.subtitleconverter.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.co.launchdesign.subtitleconverter.model.Caption;
import za.co.launchdesign.subtitleconverter.model.SubtitleObject;
import za.co.launchdesign.subtitleconverter.model.Time;
import za.co.launchdesign.subtitleconverter.util.WriteToXls;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {

    public XSSFWorkbook convertFile(MultipartFile file, String title, String season, String episode){

        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = getWorkBook(inputStream, title, season, episode);
            return workbook;

        }catch(IllegalStateException | IOException | NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public XSSFWorkbook getWorkBook(InputStream inputStream, String title, String season, String episode){
        SubtitleObject subtitleObject = new SubtitleObject();
        subtitleObject.setBuilt(false);

        WriteToXls writeToXls = new WriteToXls();

        try  {
            subtitleObject = parseFile(inputStream,Charset.defaultCharset(), title, season, episode);
            XSSFWorkbook workbook = writeToXls.writeToXls(subtitleObject);

            return workbook;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*private SubtitleObject parseFile( InputStream is,  String title, String season, String episode) throws IOException {
        return parseFile(is, Charset.defaultCharset(), title, season, episode);
    }*/

    private SubtitleObject parseFile(InputStream is, Charset isCharset,  String title, String season, String episode) throws IOException {

        SubtitleObject tto = new SubtitleObject();
        tto.setEpisodeNumber(episode);
        tto.setTitle(title);
        tto.setSeasonNumber(season);
        Caption caption = new Caption();
        List<Caption> captionList = new ArrayList<>();
        int captionNumber = 1;
        boolean allGood;

        //first lets load the file
        InputStreamReader in= new InputStreamReader(is, isCharset);
        BufferedReader br = new BufferedReader(in);

        String line = br.readLine();
        line = line.replace("\uFEFF", ""); //remove BOM character
        int lineCounter = 0;
        try {
            while(line!=null){
                line = line.trim();
                lineCounter++;
                //if its a blank line, ignore it, otherwise...
                if (!line.isEmpty()){
                    allGood = false;
                    //the first thing should be an increasing number
                    try {
                        int num = Integer.parseInt(line);
                        if (num != captionNumber)
                            throw new Exception();
                        else {
                            caption.setSequence(captionNumber);
                            captionNumber++;
                            allGood = true;
                        }
                    } catch (Exception e) {
                        tto.setWarnings(captionNumber + " expected at line " + lineCounter);
                        tto.setWarnings("\n skipping to next line\n\n");
                    }
                    if (allGood){
                        //we go to next line, here the begin and end time should be found
                        try {
                            lineCounter++;
                            line = br.readLine().trim();
                            String start = line.substring(0, 12);
                            String end = line.substring(line.length()-12, line.length());
                            Time time = new Time("hh:mm:ss,ms",start);
                            caption.setStart(time);
                            time = new Time("hh:mm:ss,ms",end);
                            caption.setEnd(time);
                        } catch (Exception e){
                            tto.setWarnings("incorrect time format at line "+lineCounter);
                            allGood = false;
                        }
                    }
                    if (allGood){
                        //we go to next line where the caption text starts
                        lineCounter++;
                        line = br.readLine().trim();
                        String text = "";
                        while (!line.isEmpty()){
                            text+=line+" ";
                            line = br.readLine().trim();
                            lineCounter++;
                        }
                        caption.setContent(text);
                        //we add the caption.
                        captionList.add(caption);

                    }
                    //we go to next blank
                    while (!line.isEmpty()) {
                        line = br.readLine().trim();
                        lineCounter++;
                    }
                    caption = new Caption();
                }
                line = br.readLine();
            }

            tto.setCaptions(captionList);

        }  catch (NullPointerException e){
            tto.setWarnings("unexpected end of file, maybe last caption is not complete.\n\n");
        } finally{
            //we close the reader
            is.close();
        }

        tto.setBuilt(true);
        return tto;
    }


}
