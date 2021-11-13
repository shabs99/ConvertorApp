package za.co.launchdesign.subtitleconverter.model;

import lombok.Data;

@Data
public class Time {

    private int mseconds;


    /**
     * Constructor to create a time object.
     *
     * @param format supported formats: "hh:mm:ss,ms", "h:mm:ss.cs" and "h:m:s:f/fps"
     * @param value  string in the correct format
     */
    public Time(String format, String value) {
        if (format.equalsIgnoreCase("hh:mm:ss,ms")) {
            // this type of format:  01:02:22,501 (used in .SRT)
            int h, m, s, ms;
            h = Integer.parseInt(value.substring(0, 2));
            m = Integer.parseInt(value.substring(3, 5));
            s = Integer.parseInt(value.substring(6, 8));
            ms = Integer.parseInt(value.substring(9, 12));

            mseconds = ms + s * 1000 + m * 60000 + h * 3600000;
        }
    }

    /**
     * Method to return a formatted value of the time stored
     *
     * @param string supported formats: "hh:mm:ss,ms", "h:mm:ss.cs" and "hhmmssff/fps"
     * @return formatted time in a string
     */
    public String getTime(String format) {
        //we use string builder for efficiency
        StringBuilder time = new StringBuilder();
        String aux;
        if (format.equalsIgnoreCase("hh:mm:ss,ms")) {
            // this type of format:  01:02:22,501 (used in .SRT)
            int h, m, s, ms;
            h = mseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (mseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (mseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(',');
            ms = mseconds % 1000;
            aux = String.valueOf(ms);
            if (aux.length() == 1) time.append("00");
            else if (aux.length() == 2) time.append('0');
            time.append(aux);
        }

        return time.toString();
    }

}
