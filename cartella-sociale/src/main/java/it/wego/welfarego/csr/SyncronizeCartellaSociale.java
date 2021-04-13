/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.csr;


import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.CodaCsrDao;
import it.wego.welfarego.persistence.entities.CodaCsr;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author piergiorgio
 */
public class SyncronizeCartellaSociale {

    private EntityManager em;
    
   
    public SyncronizeCartellaSociale(EntityManager em) {
        this.em = em;
    }

    public void sincronizzaCartellaSociale() {


        CodaCsrDao csrd = new CodaCsrDao(em);

        List<CodaCsr> coda = csrd.findCodaDaEvadere();

        for (CodaCsr csr : coda) {
            try {
            	switch (csr.getAzione()) {
				case CodaCsr.SINCRONIZZAZIONE_INSERISCI_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withAnagrafeSoc(csr.getCodAna().toString()).inserimentoCartellaSociale();
					break;
				case CodaCsr.SINCRONIZZAZIONE_MODIFICA_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withAnagrafeSoc(csr.getCodAna().toString()).modificaCartella();
					break;
				
				case CodaCsr.SINCRONIZZAZIONE_INSERISCI_INTERVENTO:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPaiIntervento(csr.getCodPai().toString(), csr.getCodTipint(), csr.getCntTipint().toString()).inserimentoIntervento();
					break;

				case CodaCsr.SINCRONIZZAZIONE_MODIFICA_INTERVENTO:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPaiIntervento(csr.getCodPai().toString(), csr.getCodTipint(), csr.getCntTipint().toString()).modificaIntervento();
					break;
					
				case CodaCsr.SINCRONIZZAZIONE_CHIUDI_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withAnagrafeSoc(csr.getCodAna().toString()).chiudiCartellaSociale();
					break;
					
				case CodaCsr.SINCRONIZZAZIONE_RIATTIVA_CARTELLA:
					CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withAnagrafeSoc(csr.getCodAna().toString()).riattivaCartellaSociale();
					break;

				default :
					
					break;
				}

                em.getTransaction().begin();
                csr.setDtCallcsr(new Timestamp(System.currentTimeMillis()));
                csr.setDtInscsr(new Timestamp(System.currentTimeMillis()));
                csr.setTestoErrore("OK");
                em.merge(csr);
            } catch (Exception ex) {
                if (!em.getTransaction().isActive()) {
                    em.getTransaction().begin();
                }
                csr.setDtCallcsr(new Timestamp(System.currentTimeMillis()));
                csr.setNumeroTentativi(csr.getNumeroTentativi()+1);
                String messaggio = "";
                if (ex.getMessage() != null) {
                    messaggio = ex.getMessage().substring(0, (ex.getMessage().length() <= 3900 ? ex.getMessage().length() : 3900));
                }
                csr.setTestoErrore(messaggio);
                em.merge(csr);
            } finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().commit();
                }
            }
        }
        em.close();
    }
}
