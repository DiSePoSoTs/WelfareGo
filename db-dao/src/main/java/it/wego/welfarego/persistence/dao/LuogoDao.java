/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

/**
 *
 * @author aleph
 */
public class LuogoDao extends PersistenceAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public LuogoDao(EntityManager em) {
        super(em);
    }

    public Luogo newLuogo(@Nullable Object stato, @Nullable String statoStr, 
            @Nullable Object prov,@Nullable String provStr,
            @Nullable Object com,@Nullable String comStr,
            @Nullable Object via, @Nullable String viaStr, 
            @Nullable Object civ,  @Nullable String civStr, 
            @Nullable String cap) {
        return newLuogo(
                MoreObjects.firstNonNull(stato, Strings.nullToEmpty(statoStr)).toString(),
                MoreObjects.firstNonNull(prov, Strings.nullToEmpty(provStr)).toString(),
                MoreObjects.firstNonNull(com, Strings.nullToEmpty(comStr)).toString(),
                MoreObjects.firstNonNull(via, Strings.nullToEmpty(viaStr)).toString(),
                MoreObjects.firstNonNull(civ, Strings.nullToEmpty(civStr)).toString(),
                cap);
    }
    
    public Luogo newLuogo(@Nullable String stato, @Nullable String prov, @Nullable String com, @Nullable String via, @Nullable String civ, @Nullable String cap) {
        logger.debug(String.format("stato:%s, prov:%s, com:%s, via:%s, civ:%s, cap:%s", stato, prov, com, via, civ, cap));

        Luogo luogo = new Luogo();
        if (!Strings.isNullOrEmpty(stato)) {
            Stato nttStato = new StatoDao(getEntityManager()).findByCodStato(stato);
            luogo.setStato(nttStato);

            if (!Strings.isNullOrEmpty(prov) && luogo.getStato() != null) {
                Provincia provincia = luogo.getStato().getProvincia(prov);
                luogo.setProvincia(provincia);

                if (!Strings.isNullOrEmpty(com) && luogo.getProvincia() != null) {
                    Comune comune = luogo.getProvincia().getComune(com);
                    luogo.setComune(comune);

                    if (!Strings.isNullOrEmpty(via) && luogo.getComune() != null) {
                        Toponomastica toponomastica = luogo.getComune().getToponomastica(via);
                        luogo.setVia(toponomastica);
                        if (!Strings.isNullOrEmpty(civ) && luogo.getVia() != null) {
                            ToponomasticaCivici toponomasticaCivici = luogo.getVia().getToponomasticaCivici(civ);
                            luogo.setCivico(toponomasticaCivici);
                        }
                    }
                }
            }
        }
        if (luogo.getProvincia() == null) {
            luogo.setProvinciaStr(Strings.emptyToNull(prov));
            }
        
        if (luogo.getComune() == null) {
            luogo.setComuneStr(Strings.emptyToNull(com));
   }     
        
        if (luogo.getVia() == null) {
            luogo.setViaStr(Strings.emptyToNull(via));
           }
        if (luogo.getCivico() == null) {
            luogo.setCivicoStr(Strings.emptyToNull(civ));
        } 
 
        luogo.setCap(Strings.emptyToNull(cap));
        return luogo;
    }

      public void attachReferences(Luogo luogo) {
        luogo.setCivico(getReference(luogo.getCivico()));
        luogo.setVia(getReference(luogo.getVia()));
        luogo.setComune(getReference(luogo.getComune()));
        luogo.setProvincia(getReference(luogo.getProvincia()));
        luogo.setStato(getReference(luogo.getStato()));
    }
}
