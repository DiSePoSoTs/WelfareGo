/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.conversions;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class DateConversion {

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
}
