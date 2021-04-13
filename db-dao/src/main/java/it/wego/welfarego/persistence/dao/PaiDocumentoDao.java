package it.wego.welfarego.persistence.dao;

import com.google.common.collect.Iterables;
import it.wego.extjs.beans.Order;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class PaiDocumentoDao extends PersistenceAdapter {

    public PaiDocumentoDao(EntityManager em) {
        super(em);
    }

    public List<PaiDocumento> findByCodPai(Pai pai) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiDocumento p "
                + "WHERE p.codPai = :codPai "
                + "ORDER BY p.dtDoc DESC");
        query.setParameter("codPai", pai);

        List<PaiDocumento> documenti = query.getResultList();
        return documenti;
    }

    public PaiDocumento findByIdDocumento(Integer idDocumento) {
        Query query = getEntityManager().createNamedQuery("PaiDocumento.findByIdDocumento");
        query.setParameter("idDocumento", idDocumento);

        PaiDocumento doc = (PaiDocumento) getSingleResult(query);
        return doc;
    }

    public int countByCodPai(Pai pai) {
    	Query query=getEntityManager().createNativeQuery("SELECT p.* " +
    			   "from pai_documento p " +
    				"WHERE p.cod_pai = ?", PaiDocumento.class);
        query.setParameter(1, pai.getCodPai());
        int count = 0;
        List<PaiDocumento> documenti = query.getResultList();
        count = documenti.size();
        return count;
    }

    public List<PaiDocumento> findByCodPai(Pai pai, int limit, int offset) {
    	Query rightQuery=getEntityManager().createNativeQuery("SELECT p.* " +
	   "from pai_documento p " +
		"WHERE p.cod_pai = ?", PaiDocumento.class);
            	rightQuery.setParameter(1, pai.getCodPai());
            	rightQuery.setFirstResult(offset);
            	rightQuery.setMaxResults(limit);
     
        List<PaiDocumento> documenti = rightQuery.getResultList();
        return documenti;
    }

    public List<PaiDocumento> findByCodPai(Pai pai, int limit, int offset, Order order) {
        if (order != null) {
        	Query rightQuery=getEntityManager().createNativeQuery("SELECT p.* " +
        			   "from pai_documento p " +
        				"WHERE p.cod_pai = ? "+
			"ORDER BY p."+ order.getProperty(), PaiDocumento.class);
        	rightQuery.setParameter(1, pai.getCodPai());
        	rightQuery.setFirstResult(offset);
        	rightQuery.setMaxResults(limit);
        	
            List<PaiDocumento> documenti = rightQuery.getResultList();
            return documenti;
        } else {
            return findByCodPai(pai, limit, offset);
        }
    }

    public PaiDocumento findLastDoc(PaiInterventoPK paiInterventoPK, String codTipoDoc) {
        getLogger().debug("searching last doc version for paiIntervento, tipoDoc : " + paiInterventoPK + " , '" + codTipoDoc + "'");
        Query q = getEntityManager().createNativeQuery("SELECT x.* FROM" +
    			" (SELECT p.*,RANK() OVER (PARTITION BY p.cod_tipdoc,p.cod_tipint, p.cnt_tipint ORDER BY ver desc) rank " +
    			"from pai_documento p " +
    			"WHERE p.cod_pai = "+paiInterventoPK.getCodPai()+" and p.cod_tipdoc = '"+codTipoDoc+"' and p.cod_tipint = '"+paiInterventoPK.getCodTipint()+"' and p.cnt_tipint = "+paiInterventoPK.getCntTipint()+"  ) x " +
    			"WHERE x.rank = 1",PaiDocumento.class);
                if(q.getResultList().size()>0){
        	   return (PaiDocumento) q.getResultList().get(0);
                }
                else{
                	return null;
                }
    }

    
    //Cambiata con query di FERRACIN in data 04 10 2013
    public @Nullable
    PaiDocumento findLastDoc(Integer codPai, String codTipoDoc) {
    Query q = getEntityManager().createNativeQuery("SELECT x.* FROM" +
			" (SELECT p.*,RANK() OVER (PARTITION BY p.cod_tipdoc,p.cod_tipint, p.cnt_tipint ORDER BY ver desc) rank " +
			"from pai_documento p " +
			"WHERE p.cod_pai = "+codPai+" and p.cod_tipdoc = '"+codTipoDoc+"' ) x " +
			"WHERE x.rank = 1",PaiDocumento.class);
         if(q.getResultList().size()>0){
 	      return (PaiDocumento) q.getResultList().get(0);
         }
         else{
         	return null;
         }

    }

    public PaiDocumento findLastDocByIdDocumento(Integer idDocumento) {
        PaiDocumento paiDocumento = findByIdDocumento(idDocumento);
       // return findLastDoc(paiDocumento.getCodPai().getCodPai(), paiDocumento.getCodTipdoc());WTF?????? perchè diavolo dopo aver beccato il documento giusto by id prendono il last documento di quel tipo.. ? E' ovvio che tornerà sbagliato
        
       return paiDocumento;
    }

    /**
     * crea e persiste un documento
     */
    public PaiDocumento createDoc(UniqueTasklist task, String tipoDoc, String encodedData, String nomeFile) {
        return createDoc(task.getCodPai(), task.getCodPai().getCodUteAs(), task.getPaiIntervento(), tipoDoc, encodedData, nomeFile, null);
    }

    /**
     * crea e persiste un documento
     */
    public PaiDocumento createDoc(PaiEvento evento, String tipoDoc, String encodedData, String nomeFile) {
        return createDoc(evento.getCodPai(), evento.getCodUte(), evento.getPaiIntervento(), tipoDoc, encodedData, nomeFile, null);
    }

    public PaiDocumento createDoc(Pai pai, Utenti codUte, String tipoDoc, String encodedData, String nomeFile) {
        return createDoc(pai, codUte, null, tipoDoc, encodedData, nomeFile, null);
    }

    public PaiDocumento createDoc(Pai pai, Utenti utente, PaiIntervento paiIntervento, String tipoDoc, String encodedData, String nomeFile) {
        return createDoc(pai, utente, paiIntervento, tipoDoc, encodedData, nomeFile, null);
    }

    public PaiDocumento createDoc(PaiEvento evento, String tipoDoc, String reportResultEncoded, String nomeReportFile, String tipoDetermina) {
        return createDoc(evento.getCodPai(), evento.getCodUte(), evento.getPaiIntervento(), tipoDoc, reportResultEncoded, nomeReportFile, tipoDetermina);
    }

    /**
     * crea e persiste un documento
     */
    public PaiDocumento createDoc(Pai pai, Utenti utente, PaiIntervento paiIntervento, String tipoDoc, String encodedData, String nomeFile, String tipoDetermina) {
        PaiDocumento paiDocumento = new PaiDocumento();
        paiDocumento.setVer(BigInteger.ONE);
        paiDocumento.setDtDoc(new Date());
        paiDocumento.setCodPai(pai);
        paiDocumento.setCodUteAut(utente);
        if (paiIntervento != null) {
            paiDocumento.setPaiIntervento(paiIntervento);
        }
        paiDocumento.setCodTipdoc(tipoDoc);
        paiDocumento.setBlobDoc(encodedData);
        paiDocumento.setNomeFile(nomeFile);
        paiDocumento.setTipoDetermina(tipoDetermina);
        insertDoc(paiDocumento);
        return paiDocumento;
    }

    /**
     * persiste un documento, eventualmente incrementando il numero di versione
     */
    public PaiDocumento insertDoc(PaiDocumento documento) {
        PaiDocumento lastDoc = findLastDoc(documento);
        if (lastDoc != null) {
            documento.setVer(lastDoc.getVer().add(BigInteger.ONE));
        } else {
            documento.setVer(BigInteger.ONE);
        }
        getLogger().debug("inserting doc {} with version {}", documento, documento.getVer());
        documento.setIdDocumento(null); //auto-increment
        getEntityManager().persist(documento);
        getEntityManager().flush();
        return documento;
    }


    public PaiDocumento findLastDoc(PaiDocumento documento) {
        return findLastDoc(documento.getCodPai(), documento.getPaiIntervento(), documento.getCodTipdoc());
    }

    public PaiDocumento findLastDoc(Pai pai, PaiIntervento intervento, String tipoDoc) {
        return intervento != null
                ? findLastDoc(intervento.getPaiInterventoPK(), tipoDoc)
                : findLastDoc(pai.getCodPai(), tipoDoc);
    }

    public PaiDocumento findLastDoc(Integer codPai, PaiInterventoPK interventoPK, String tipoDoc) {
        return interventoPK != null
                ? findLastDoc(interventoPK, tipoDoc)
                : findLastDoc(codPai, tipoDoc);
    }
}
