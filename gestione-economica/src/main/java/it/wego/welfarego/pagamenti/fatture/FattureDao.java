package it.wego.welfarego.pagamenti.fatture;

import com.google.common.base.MoreObjects;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class FattureDao {

    public void fill_paiInterventoMese_per_SalvaAcquisizione(PaiInterventoMese mese) {
        mese.setBdgConsQta(MoreObjects.firstNonNull(mese.getBdgConsQta(), mese.getBdgPrevQta()));
        mese.setBdgConsQtaBenef(MoreObjects.firstNonNull(mese.getBdgConsQtaBenef(), MoreObjects.firstNonNull(mese.getBdgConsQta(), mese.getBdgPrevQta())));
        mese.setBdgConsEur(MoreObjects.firstNonNull(mese.getBdgConsEur(), mese.getBdgPrevEur()));
        mese.setGgAssenza(MoreObjects.firstNonNull(mese.getGgAssenza(), BigDecimal.ZERO));
        mese.setGenerato(1);

        if (mese.getPaiIntervento().getTipologiaIntervento().getFlgFatt() == 'S' || mese.getPaiIntervento().getTipologiaIntervento().getFlgPagam() == 'S') {
            if (mese.getPaiCdg() == null) {
                //se non esistono righe in controllo di gestione, le creo
                PaiCdg cdg = new PaiCdg();
                PaiCdgPK cdgPK = new PaiCdgPK();
                cdgPK.setCntTipint(mese.getPaiInterventoMesePK().getCntTipint());
                cdgPK.setCodPai(mese.getPaiInterventoMesePK().getCodPai());
                cdgPK.setCodTipint(mese.getPaiInterventoMesePK().getCodTipint());
                cdgPK.setCodAnno(mese.getPaiInterventoMesePK().getAnno());
                cdgPK.setAnnoEff(mese.getPaiInterventoMesePK().getAnnoEff());
                cdgPK.setMeseEff(mese.getPaiInterventoMesePK().getMeseEff());
                cdgPK.setCodImpe(mese.getPaiInterventoMesePK().getCodImp());
                cdg.setPaiCdgPK(cdgPK);
                cdg.setPaiInterventoMese(mese);
                cdg.setCodAna(mese.getPaiIntervento().getPai().getAnagrafeSoc());
                cdg.setCodCap(mese.getBudgetTipIntervento().getCodCap());
                cdg.setCcele(mese.getPaiIntervento().getTipologiaIntervento().getCcele());
                cdg.setImpStd(mese.getPaiIntervento().getImportoStandard());
                cdg.setImpVar(mese.getBdgConsVar());
                cdg.setQtaPrev(mese.getBdgPrevQta());
                mese.setPaiCdg(cdg);
            }//if
            mese.getPaiCdg().setImpComplUsingFascia(mese.getBdgConsEur());
            mese.getPaiCdg().setQtaErog(mese.getBdgConsQta());
        }//if
    }


    public List<PaiInterventoMese> getPaiInterventoMeseFromId(String id) throws JSONException {
        EntityManager entityManager = Connection.getEntityManager();
        JSONObject jsoIdPaiIntervento = new JSONObject(id);
        JSONObject jsoPeriodoRicerca = new JSONObject((String) jsoIdPaiIntervento.get("periodoRicerca"));

        // QUERY

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery c = cb.createQuery();
        Root r = c.from(PaiInterventoMese.class);
        Join pi = r.join("paiIntervento", JoinType.INNER);
        Join ti = pi.join("tipologiaIntervento", JoinType.INNER);
        Join fad = r.join("idFattDettaglio", JoinType.LEFT);
        Join fa = fad.join("idFatt", JoinType.LEFT);
        Join mad = r.join("idManDettaglio", JoinType.LEFT);
        Join ma = mad.join("idMan", JoinType.LEFT);
        Join pinfs = r.join("idParamFascia", JoinType.LEFT);
        Predicate p = cb.equal(fad.get("idFattDettaglio"), cb.nullLiteral(FatturaDettaglio.class));
        p = cb.and(p, cb.equal(mad.get("idManDettaglio"), cb.nullLiteral(MandatoDettaglio.class)));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codPai"), jsoIdPaiIntervento.get("codPai")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("codTipint"), jsoIdPaiIntervento.get("codTipint")));
        p = cb.and(p, cb.equal(r.get("paiInterventoMesePK").get("cntTipint"), jsoIdPaiIntervento.get("cntTipint")));
        if (jsoPeriodoRicerca.has("meseEff")) {
            Predicate mese = cb.equal(r.get("paiInterventoMesePK").get("meseEff"), jsoPeriodoRicerca.get("meseEff"));
            Predicate anno = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), jsoPeriodoRicerca.get("annoEff"));
            p = cb.and(p, mese, anno);
        }//if
        else {
            //se non era specificato il mese sicuramete deve essere specificato il periodo (verificare il metodo precedente)
            Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffDal"));
            Predicate annoDal = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            Predicate annoDal2 = cb.greaterThan(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffAl"));
            Predicate annoAl = cb.equal(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));
            Predicate annoAl2 = cb.lessThan(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));
          /*  Predicate meseDal = cb.ge(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffDal"));
            Predicate annoDal = cb.ge(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffDal"));
            Predicate meseAl = cb.le(r.get("paiInterventoMesePK").get("meseEff"), (Integer) jsoPeriodoRicerca.get("meseEffAl"));
            Predicate annoAl = cb.le(r.get("paiInterventoMesePK").get("annoEff"), (Integer) jsoPeriodoRicerca.get("annoEffAl"));*/
            p = cb.and(p, cb.or(annoDal2, cb.and(meseDal, annoDal)), cb.or(annoAl2, cb.and(meseAl, annoAl)));
        }//else

        c.where(p);
        ArrayList<Order> order = new ArrayList<Order>();
        order.add(cb.asc(r.get("paiInterventoMesePK").get("annoEff")));
        order.add(cb.asc(r.get("paiInterventoMesePK").get("meseEff")));
        order.add(cb.asc(r.get("paiInterventoMesePK").get("codImp")));
        c.orderBy(order);


        List<PaiInterventoMese> paiInterventoMeseList = entityManager.createQuery(c).getResultList();
        return paiInterventoMeseList;
    }
}
