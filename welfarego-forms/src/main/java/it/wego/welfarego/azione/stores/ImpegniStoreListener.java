package it.wego.welfarego.azione.stores;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.extjs.json.JsonStoreResponse;
import it.wego.welfarego.abstracts.AbstractStoreListener;
import it.wego.extjs.beans.Order;
import it.wego.extjs.json.JsonBuilder;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.stores.transformer.ImpegniJsonMapTransformer;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author aleph
 */
public class ImpegniStoreListener extends AbstractStoreListener {

    private final Logger logger = LogManager.getLogger(ImpegniStoreListener.class);

    public ImpegniStoreListener(AbstractForm parent) {
        super(parent);
    }

    @Deprecated
    public ImpegniStoreListener(Map<String, String> parameters) {
        super(parameters);
    }


    /*
     * fields: [{ 'anno', 'capitolo', 'impegno', 'imp_disp', 'a_carico'],
     */
    @Override
    public Collection load(int start, int limit, int page, List<Order> orderList) throws Exception {
        logger.debug("handling load request");
        EntityManager entityManager = getEntityManager();

        UniqueTasklist task = getTask();
        PaiIntervento paiIntervento = null;

        if (task != null) {
            paiIntervento = task.getPaiIntervento();
        } else if (!Strings.isNullOrEmpty(getParameter("idEvento"))) {
//            paiIntervento = Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(getParameter("idEvento"))).getPaiIntervento();
            paiIntervento = entityManager.find(PaiEvento.class, Integer.valueOf(getParameter("idEvento"))).getPaiIntervento();
        }


        final String codTipInt = extract_cod_Tipo_Int(paiIntervento);
        getLogger().debug(String.format("codTipInt: %s, ", codTipInt));

        BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(entityManager);

        String anni = getParameter("filtroAnni");
        List<BudgetTipIntervento> byCodTipint;
		if (anni == null || anni.isEmpty())
		{
			byCodTipint = budgetTipoInterventoDao.findByCodTipint(codTipInt, limit, start);
		}
		else
		{
			byCodTipint = budgetTipoInterventoDao.findByCodTipInt_And_Cod_Anno(codTipInt, extract_anni(anni), limit, start);
		}
        ImpegniJsonMapTransformer impegniJsonMapTransformer = new ImpegniJsonMapTransformer(entityManager, paiIntervento, codTipInt);


        JsonBuilder jsonBuilder = JsonBuilder.newInstance();
        jsonBuilder = jsonBuilder.withData(byCodTipint);
        jsonBuilder = jsonBuilder.withTransformer(impegniJsonMapTransformer);
        JsonStoreResponse jsonStoreResponse = jsonBuilder.buildStoreResponse();

        Collection out = jsonStoreResponse.getData();

//        getLogger().error("DOTCOM  output" + out);
        return out;
    }

    private List extract_anni(String filtroAnni) {
        filtroAnni= filtroAnni.replaceAll(", ", ",");
        String[] anniAsString = filtroAnni.split(",");
        List anni = Arrays.asList(anniAsString);
        return anni;
    }

    private String extract_cod_Tipo_Int(PaiIntervento paiIntervento) {
        String codTipInt = getParameter("codTipInt");
        if (Strings.isNullOrEmpty(codTipInt) || codTipInt.trim().length() == 0) {
            codTipInt = paiIntervento.getPaiInterventoPK().getCodTipint();
        }

        return codTipInt;
    }

    @Override
    public Object update(String dataStr) throws Exception {
        logger.debug("handling update request : " + dataStr);
        initTransaction();

//        Type listType = new TypeToken<List<Mao>>() {
//        }.getType();
//        JsonBuilder.getStringMapType()
        List<Map<String, String>> dataList = getGson().fromJson(dataStr, JsonBuilder.LIST_OF_MAP_OF_STRINGS);
        UniqueTasklist task = getTask();
        PaiIntervento intervento = null;
        if (task != null) {
            intervento = task.getPaiIntervento();
        } else if (!Strings.isNullOrEmpty(getParameter("idEvento"))) {
            intervento = Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(getParameter("idEvento"))).getPaiIntervento();
        }

        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());
        PaiInterventoPK paiInterventoPK = intervento.getPaiInterventoPK();
        final String codTipInt = (extract_cod_Tipo_Int(intervento));
        for (Map<String, String> data : dataList) {
            String quantitaString = Strings.emptyToNull(data.get("bdgPrevQta"));
            BigDecimal costo = new BigDecimal(data.get("a_carico").replace(',', '.')),
                    quantita = quantitaString == null ? BigDecimal.ZERO : new BigDecimal(quantitaString.replace(',', '.'));
            if (paiInterventoPK.getCodTipint().equals(codTipInt)) {

                BudgetTipInterventoPK budgetTipInterventoPK = new BudgetTipInterventoPK(
                        paiInterventoPK.getCodTipint(),
                        Short.parseShort(data.get("anno")),
                        data.get("impegno"));

                PaiInterventoMese prop = paiInterventoMeseDao.findProposta(paiInterventoPK, budgetTipInterventoPK, intervento.getPai().getIdParamFascia());
                if (Objects.equal(prop.getBdgPrevEur(), costo) && !Objects.equal(prop.getBdgPrevQta(), quantita)) {
                    costo = quantita.multiply(getImportoStandard(intervento, getEntityManager()));
                } else if (!Objects.equal(prop.getBdgPrevEur(), costo) && Objects.equal(prop.getBdgPrevQta(), quantita)) {
                    quantita = costo.divide(getImportoStandard(intervento, getEntityManager()), MathContext.DECIMAL32);
                } else {
                    Preconditions.checkArgument(!(!Objects.equal(prop.getBdgPrevEur(), costo) && !Objects.equal(prop.getBdgPrevQta(), quantita)), "impossibile modificare contemporaneamente costo e quantita'");
                }
                prop.setBdgPrevQta(quantita);
                prop.setBdgPrevEur(costo);
            } else {


                TipologiaInterventoDao tdao = new TipologiaInterventoDao(getEntityManager());
                TipologiaIntervento t = tdao.findByCodTipint(codTipInt);
                if (Objects.equal(BigDecimal.ZERO, costo) && !Objects.equal(BigDecimal.ZERO, quantita)) {
                    costo = quantita.multiply(getImportoStandard(intervento, getEntityManager()));
                } else if (!Objects.equal(BigDecimal.ZERO, costo) && Objects.equal(BigDecimal.ZERO, quantita)) {
                    quantita = costo.divide(getImportoStandard(intervento, getEntityManager()), MathContext.DECIMAL32);
                }

            }
            data.put("a_carico", costo.toString());
            //  data.put("unitaDiMisuraDesc", task.getPaiIntervento().getIdParamUniMis().getDesParam());
            data.put("bdgPrevQta", quantita.toString());
        }

        insertEvento(intervento, PaiEvento.PAI_UPDATE_PROPOSTE_IMPEGNI);
        commitTransaction();
        return dataList;
    }
//    private static class ImpegnoDataModel extends GsonObject {
//        // }, 'anno', 'capitolo', 'impegno', 'imp_disp', 'a_carico'],
//
//        private int id;
//        private String anno, capitolo, impegno, impDisp, aCarico;
//
//        public String getACarico() {
//            return aCarico;
//        }
//
//        public void setACarico(String a_carico) {
//            this.aCarico = a_carico;
//        }
//
//        public String getAnno() {
//            return anno;
//        }
//
//        public void setAnno(String anno) {
//            this.anno = anno;
//        }
//
//        public String getCapitolo() {
//            return capitolo;
//        }
//
//        public void setCapitolo(String capitolo) {
//            this.capitolo = capitolo;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getImpDisp() {
//            return impDisp;
//        }
//
//        public void setImpDisp(String impDisp) {
//            this.impDisp = impDisp;
//        }
//
//        public String getImpegno() {
//            return impegno;
//        }
//
//        public void setImpegno(String impegno) {
//            this.impegno = impegno;
//        }
//    }

    /**
     * Ritorna il costo standard di un intervento.
     *
     * @param i
     * @param e
     * @return
     */
    private BigDecimal getImportoStandard(PaiIntervento i, EntityManager e) {
        BigDecimal importoStandard;
        //se ha una struttura
        if (i.getTariffa()!= null) {
            importoStandard = i.getTariffa().getCosto();
        } else {
            importoStandard = i.getTipologiaIntervento().getImpStdCosto();
        }
        return importoStandard;
    }

}
