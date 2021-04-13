/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import java.text.SimpleDateFormat;

import it.wego.welfarego.model.PagamentoBean;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import java.math.BigDecimal;

/**
 *
 * @author giuseppe
 */
public class PagamentiSerializer {

    public static PagamentoBean serializePagamentoBean(MandatoDettaglio mandato)  {
        PagamentoBean bean = new PagamentoBean();
        if (mandato.getTimbro() != null){
            bean.setData(new SimpleDateFormat("dd/MM/yyyy").format(mandato.getTimbro()));
        }
        if (mandato.getCostoTotale() != null) {
			BigDecimal totale = mandato.getCostoTotale();
			if (mandato.getAumento() != null)
			{
				totale = totale.add(mandato.getAumento());
			}
			if (mandato.getRiduzione() != null)
			{
				totale = totale.subtract(mandato.getRiduzione());
			}
            bean.setImporto(String.valueOf(totale));
        }
        bean.setMandato(String.valueOf(mandato.getNumeroMandato()));
        if (mandato.getIdMan() != null) {
            bean.setIdMandato(String.valueOf(mandato.getIdMan().getIdMan()));
        }
        bean.setModalitaErogazione(mandato.getIdMan().getModalitaErogazione());
        String mesi="";
        for(PaiInterventoMese p : mandato.getPaiInterventoMeseList()){
        	mesi+=" "+ p.getPaiInterventoMesePK().getMeseEff() +"/" +p.getPaiInterventoMesePK().getAnnoEff();
        }
        //usiamo riscosso dato che non viene mai usato e dato che ci hanno pagato solo 0.5 gg
        bean.setRiscosso(mesi);

        return bean;
    }


}
