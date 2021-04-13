/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.xsd;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class Utils {

    public static String dateToItString(Date data) {
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(data);
        } else {
            return "";
        }
    }

    public static String dateToString(Date date, String pattern) throws ParseException {
        if (date != null) {
            DateFormat formatter = new SimpleDateFormat(pattern);
            String dateToConvert = formatter.format(date);
            return dateToConvert;
        } else {
            return "";
        }
    }

    public static int calcolaEta(Date dataNascita, Date dataRiferimento) {
        Calendar dr = Calendar.getInstance();
        Calendar dn = Calendar.getInstance();
        dr.setTime(dataRiferimento);
        dn.setTime(dataNascita);
        if (dn.after(dr)) {
            throw new IllegalArgumentException("Non può essere nato nel futuro");
        }
        int eta = dr.get(Calendar.YEAR) - dn.get(Calendar.YEAR);
        if (dr.get(Calendar.DAY_OF_YEAR) <= dn.get(Calendar.DAY_OF_YEAR)) {
            eta--;
        }
        return eta;
    }
    /**
     * Pulisce le stringhe da char che sono illegali nell'xml.
     * @param illegal
     * @return
     */
    public static String cleanStringForXml(String illegal){
    	String xml11pattern = "[^"
                + "\u0001-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]+";
    	return illegal.replaceAll(xml11pattern, "");
    }
}
