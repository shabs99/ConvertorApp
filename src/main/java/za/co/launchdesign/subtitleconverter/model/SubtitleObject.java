package za.co.launchdesign.subtitleconverter.model;

import lombok.Data;

import java.util.List;

@Data
public class SubtitleObject {

    private String title;
    private String seasonNumber;
    private String episodeNumber;
    private List<Caption> captions;

    //to know if a parsing method has been applied
    private boolean built = false;

    //to store non fatal errors produced during parsing
    private String warnings;
}
