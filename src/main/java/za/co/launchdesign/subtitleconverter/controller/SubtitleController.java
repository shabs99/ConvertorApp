package za.co.launchdesign.subtitleconverter.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import za.co.launchdesign.subtitleconverter.service.ConverterService;

import java.io.*;

@RestController
@Slf4j
public class SubtitleController {

    @Autowired
    private ConverterService converterService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertFIle(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
                                              @RequestParam("season") String season,@RequestParam("episode") String episode ) {
        try {
            XSSFWorkbook convertedFile = converterService.convertFile(file, title, season, episode);
            if (convertedFile != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                convertedFile.write(baos);
                return new ResponseEntity<>(baos.toByteArray(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (NullPointerException | IOException e){
            e.printStackTrace();
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
}
