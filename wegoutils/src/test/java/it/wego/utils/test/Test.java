/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.utils.test;

import it.wego.conversions.StringConversion;
import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String data = StringConversion.timestampToString(new Date());
        System.out.println("Now: " + data);
    }

}
