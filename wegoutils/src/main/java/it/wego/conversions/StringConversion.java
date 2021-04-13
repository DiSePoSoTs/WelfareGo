package it.wego.conversions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author giuseppe
 */
public class StringConversion {

    public static String timestampToString(Date data) {
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getTimeStampPattern());
            return sdf.format(data);
        } else {
            return "";
        }
    }

    public static String dateToItDayWeekString(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getItDateWeekPattern());
        return sdf.format(data);
    }

    public static Date itStringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getItDatePattern());
        try {
            return sdf.parse(dateString);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String dateToItString(Date data) {
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getItDatePattern());
            return sdf.format(data);
        } else {
            return "";
        }
    }

    public static String dateToHourString(Date data) {
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getHourPattern());
            return sdf.format(data);
        } else {
            return "";
        }
    }

    public static Date hourStringToDate(String data) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getHourPattern());
        return sdf.parse(data);
    }

    public static Date gsonStringToDate(String dateString) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(ConversionProperties.getInstance().getGsonDatePattern(), Locale.US);
        return sdf.parse(dateString);
    }

    public static String escapeHtmlString(String stringToEscape) {
        if (stringToEscape == null) {
            return "";
        }
        stringToEscape = replace(stringToEscape, "&", "\\&");
        stringToEscape = replace(stringToEscape, "\'", "\\'");
        stringToEscape = replace(stringToEscape, "\"", "\\");
        stringToEscape = replace(stringToEscape, "<", "&#60");
        stringToEscape = replace(stringToEscape, ">", "&#62");
        return stringToEscape;
    }

    @Deprecated
    private static String replace(String s, String one, String another) {
        if (s.equals("")) {
            return "";
        }
        String res = "";
        int i = s.indexOf(one, 0);
        int lastpos = 0;
        while (i != -1) {
            res += s.substring(lastpos, i) + another;
            lastpos = i + one.length();
            i = s.indexOf(one, lastpos);
        }
        res += s.substring(lastpos);
        return res;
    }

    /**
     * Converte una stringa in data
     *
     * @param stringToConvert Stringa da convertire
     * @param pattern Pattern per la conversione
     * @return Un oggetto Date che rappresenta la data indicata nella stringa in
     * input. Se la stringa è nulla, ritorna la data odierna
     * @throws ParseException
     */
    public static Date stringToDate(String stringToConvert, String pattern) throws ParseException {
        if (stringToConvert != null) {
            DateFormat formatter = new SimpleDateFormat(pattern);
            Date date = formatter.parse(stringToConvert);
            return date;
        } else {
            return new Date();
        }
    }

    /**
     * Converte una data in stringa
     *
     * @param date La data da convertire
     * @param pattern Il pattern da utilizzare
     * @return Una stringa rappresentativa della data
     * @throws ParseException
     */
    public static String dateToString(Date date, String pattern) throws ParseException {
        if (date != null) {
            DateFormat formatter = new SimpleDateFormat(pattern);
            String dateToConvert = formatter.format(date);
            return dateToConvert;
        } else {
            return "";
        }
    }

    public static String objectToString(Object obj) {
        if (obj != null) {
            return String.valueOf(obj);
        } else {
            return "";
        }
    }

    public static String toCamelCase(String string) {
        Matcher m = Pattern.compile("_.").matcher(string);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group().replaceAll("_", "").toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
