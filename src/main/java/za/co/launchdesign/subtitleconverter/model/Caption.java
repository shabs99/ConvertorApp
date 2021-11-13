package za.co.launchdesign.subtitleconverter.model;

import lombok.Data;

@Data
public class Caption {

    private int sequence;
    private String content;
    private Time start;
    private Time end;

    @Override
    public String toString() {
        return "Caption{" +
                "sequence=" + sequence +
                ", content='" + content + '\'' +
                ", start=" + start.getTime("hh:mm:ss,ms") +
                ", end=" + end.getTime("hh:mm:ss,ms") +
                '}';
    }
}
