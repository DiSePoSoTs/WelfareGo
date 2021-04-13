package it.wego.welfarego.services;

import com.google.common.base.Preconditions;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;


public class AnagraficheSyncService {
    public static void allineaWellfargoFromComune(String codiceFiscale, EntityManager entityManager, String codAnagWelfargo, VistaAnagrafeDao vistaAnagrafeDao, String codiceAnagrafico)  throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Preconditions.checkNotNull(codAnagWelfargo, "il parametro codAna e' mancante");
        if(codiceAnagrafico== null && codiceFiscale==null ){
            throw new IllegalArgumentException("Attenzione : Almeno uno fra codice fiscale e codice anagrafico deve essere valorizzato");
        }
        AnagrafeSoc anagrafe = new AnagrafeSocDao(entityManager).findByCodAna(codAnagWelfargo);
        VistaAnagrafe vista = null;
        if(codiceAnagrafico!=null){
            vista = vistaAnagrafeDao.findByNumeroIndividuale(codiceAnagrafico);
        }
        else {
            vista= vistaAnagrafeDao.findByCodiceFiscale(codiceFiscale);
        }
        Preconditions.checkNotNull(vista, "utente non trovato in anagrafe comunale per cod = {}", codiceAnagrafico);
        entityManager.getTransaction().begin();
        vistaAnagrafeDao.allineaAnagrafeWegoDaAnagrafeComune(vista, anagrafe);
        entityManager.getTransaction().commit();
    }
}
