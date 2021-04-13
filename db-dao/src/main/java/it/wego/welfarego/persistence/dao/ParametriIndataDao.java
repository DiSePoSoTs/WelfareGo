package it.wego.welfarego.persistence.dao;

import com.google.common.base.Strings;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaParametri;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author giuseppe
 */
public class ParametriIndataDao extends PersistenceAdapter {

    public ParametriIndataDao(EntityManager em) {
        super(em);
    }

    public List<ParametriIndata> findAllByTipParamAttivo(String tipParam) {
        return find(ParametriIndata.class, "SELECT pi FROM ParametriIndata pi",
                ConditionBuilder.equals("pi.idParam.tipParam.tipParam", tipParam),
                ConditionBuilder.equals("pi.idParam.flgAttivo", Parametri.FLAG_ATTIVO_S));
    }

    public List<ParametriIndata> findAll_WEGO_ByTipParamAttivo(String tipParam) {
        return find(ParametriIndata.class, "SELECT pi FROM ParametriIndata pi",
                ConditionBuilder.equals("pi.idParam.tipParam.tipParam", tipParam),
                ConditionBuilder.equals("pi.idParam.flgAttivo", Parametri.FLAG_ATTIVO_S),
                ConditionBuilder.equals("pi.piattaforma", "WEGO"));
    }

    public List<ParametriIndata> findByTipParam(String tipParam) {
        TipologiaParametri tipoParam = getEntityManager().getReference(TipologiaParametri.class, tipParam);
        Query query = getEntityManager().createQuery("SELECT a "
                + " FROM ParametriIndata a JOIN a.idParam b "
                + " WHERE "
                + " b.tipParam = :tipParam "
                + " and a.piattaforma = 'WEGO' "
                + " AND a.dtIniVal = (SELECT MAX(c.dtIniVal) "
                                        + "FROM ParametriIndata c "
                                        + "WHERE c.idParam.idParam = a.idParam.idParam "
                                        + "AND c.dtIniVal <= :today) "
                + " ORDER BY a.idParam.codParam "
        );
        query.setParameter("tipParam", tipoParam);
        query.setParameter("today", new Date());

        return query.getResultList();
    }

    public List<ParametriIndata> findByTipParamAttivo(String tipParam) {
        TipologiaParametri tipoParam = getEntityManager().getReference(TipologiaParametri.class, tipParam);
        Query query = getEntityManager().createQuery("SELECT a "
                + " FROM ParametriIndata a JOIN a.idParam b "
                + " WHERE b.tipParam = :tipParam "
                + " AND a.piattaforma = 'WEGO' "
                + " AND a.idParam.flgAttivo = 'S' "
                + " AND a.dtIniVal = (SELECT MAX(c.dtIniVal) "
                + " FROM ParametriIndata c "
                + " WHERE c.idParam.idParam = a.idParam.idParam "
                + " AND c.dtIniVal <= :today) "
                + " ORDER BY a.idParam.codParam ");
        query.setParameter("tipParam", tipoParam);
        query.setParameter("today", new Date());

        return query.getResultList();
    }

    public ParametriIndata findByIdParam(int idParam) {

        TypedQuery<ParametriIndata> query = getEntityManager().createQuery("SELECT a "
                + "FROM ParametriIndata a JOIN a.idParam b "
                + "WHERE b.idParam = :idParam "
                + "AND a.dtIniVal = (SELECT MAX(c.dtIniVal) "
                + "FROM ParametriIndata c "
                + "WHERE c.idParam.idParam = a.idParam.idParam "
                + "AND c.dtIniVal <= :today) "
                + "ORDER BY a.idParam.codParam ", ParametriIndata.class);

        query.setParameter("idParam", idParam);
        query.setParameter("today", new Date());

        return getSingleResult(query);
    }

    /**
     *
     * Prendo l'ultimo parametro indata inserito in tabella in base all id_param
     * passato a questa funzione
     *
     * @param idParam
     * @return
     */
    public ParametriIndata getLastParamIndata(int idParam) {

        TypedQuery<ParametriIndata> query = getEntityManager().createQuery("SELECT a "
                + "FROM ParametriIndata a JOIN a.idParam b "
                + "WHERE b.idParam = :idParam "
                + "AND a.dtIniVal = (SELECT MAX(c.dtIniVal) "
                + "FROM ParametriIndata c "
                + "WHERE c.idParam.idParam = a.idParam.idParam "
                + "AND c.dtIniVal <= :today) "
                + "ORDER BY a.dtIniVal DESC", ParametriIndata.class);

        query.setParameter("idParam", idParam);
        query.setParameter("today", new Date());

        return getSingleResult(query);
    }

    public @Nullable
    ParametriIndata findByIdParamIndata(String idParamIndata) {
        return Strings.isNullOrEmpty(idParamIndata) ? null : findByIdParamIndata(Integer.parseInt(idParamIndata));
    }

    public ParametriIndata findByIdParamIndata(int idParamIndata) {
        TypedQuery<ParametriIndata> query = getEntityManager().createNamedQuery("ParametriIndata.findByIdParamIndata", ParametriIndata.class);
        query.setParameter("idParamIndata", idParamIndata);

        return getSingleResult(query);
    }

    public @Nullable
    ParametriIndata getReverse(ParametriIndata parametriIndata) {
        String reverseCode = parametriIndata.getTxt1Param(); //reverse param code
        if (!Strings.isNullOrEmpty(reverseCode)) {
            Parametri parametri = new ParametriDao(getEntityManager()).findOneByCodParamTipParam(reverseCode, parametriIndata.getIdParam().getTipParam().getTipParam());
            if (parametri != null && !parametri.getParametriIndataList().isEmpty()) {
                return parametri.getParametriIndataList().iterator().next(); // TODO fix this ;)
            }
            getLogger().warn("reverse param not found for param = {} , reverse = {}", parametriIndata, reverseCode);
        } else {
            getLogger().debug("reverse param not found for param = {} , reverse = {}", parametriIndata, reverseCode);
        }
        return parametriIndata;
    }

    public List<ParametriIndata> findAll() {
        return find(ParametriIndata.class, "SELECT p FROM ParametriIndata p");
    }

    public ParametriIndata getReference(Integer idParamIndata) {
        return getEntityManager().getReference(ParametriIndata.class, idParamIndata);
    }

    public ParametriIndata getReference(String idParamIndata) {
        return getReference(Integer.valueOf(idParamIndata));
    }

    public @Nullable
    ParametriIndata findByTipParamDesParam(String tipParam, @Nullable String des) {
        return Strings.isNullOrEmpty(des) ? null : findOne(ParametriIndata.class, "SELECT p FROM ParametriIndata p",
                ConditionBuilder.equals("p.idParam.tipParam.tipParam", tipParam), ConditionBuilder.ilike("p.desParam", des));
    }
    
    public @Nullable
    ParametriIndata findNazionalita(String des){
    	ParametriIndata nazionalita = null;
        nazionalita=Strings.isNullOrEmpty(des)?null: findOne(ParametriIndata.class, "SELECT p FROM ParametriIndata p", ConditionBuilder.equals("p.idParam.tipParam.tipParam", "nz"),ConditionBuilder.eilike("p.desParam", des));
        return nazionalita;
    }
    
    public @Nullable
    ParametriIndata findUot(Integer uot){
    	ParametriIndata uotResult = null;
        uotResult=uot==null?null: findOne(ParametriIndata.class, "SELECT p FROM ParametriIndata p", ConditionBuilder.equals("p.idParam.tipParam.tipParam", "uo"),ConditionBuilder.eilike("p.desParam", "UOT "+uot));
        return uotResult;
    }


    public @Nullable
    ParametriIndata findOneByTipParamCodParam(String tipParam, String codParam) {
        return findOne(ParametriIndata.class, "SELECT p FROM ParametriIndata p",
                ConditionBuilder.equals("p.idParam.tipParam.tipParam", tipParam),
                ConditionBuilder.equals("p.idParam.codParam", codParam));
    }
}
