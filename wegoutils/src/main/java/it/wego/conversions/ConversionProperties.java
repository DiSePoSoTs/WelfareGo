package it.wego.conversions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author giuseppe
 */
public class ConversionProperties {

    private static ConversionProperties instance;
    private Properties props;

    private ConversionProperties() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("it/wego/conversions/resources/conversion.properties");
        props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ConversionProperties getInstance() {
        if (instance == null) {
            instance = new ConversionProperties();
        }
        return instance;
    }

    public String getTimeStampPattern() {
        String pattern = props.getProperty("timestamp.pattern");
        return pattern;
    }

    public String getItDatePattern() {
        String pattern = props.getProperty("it.date.pattern");
        return pattern;
    }

    public String getItDateWeekPattern() {
        String pattern = props.getProperty("it.date.week.pattern");
        return pattern;
    }
    public String getGsonDatePattern() {
        String pattern = props.getProperty("gson.date.pattern");
        return pattern;
    }
    public String getHourPattern() {
        String pattern = props.getProperty("hour.pattern");
        return pattern;
    }
}
