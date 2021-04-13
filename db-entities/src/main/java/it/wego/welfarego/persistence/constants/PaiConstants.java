/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.constants;

import it.wego.welfarego.persistence.entities.Pai;

/**
 *
 * @author aleph
 * @deprecated use constants in entities.Pai
 */
@Deprecated
public class PaiConstants {

    public static final char APERTO = Pai.STATO_APERTO;
    public static final char CHIUSO = Pai.STATO_CHIUSO;
    public static final char SOSPESO = Pai.STATO_SOSPESO;
    public static final char RIFIUTATO = Pai.STATO_RIFIUTATO;
}
