package it.wego.welfarego.azione.stores;

import com.google.common.base.Strings;
import it.trieste.comune.ssc.json.JsonStoreResponse;
import it.wego.welfarego.abstracts.AbstractStoreListener;
import it.trieste.comune.ssc.beans.Order;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoUotDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author aleph
 */
public class ImpegniUotStoreListener extends AbstractStoreListener {

    private final Logger logger = LogManager.getLogger(ImpegniUotStoreListener.class);

    public ImpegniUotStoreListener(AbstractForm parent) {
        super(parent);
    }

    @Deprecated
    public ImpegniUotStoreListener(Map<String, String> parameters) {
        super(parameters);
    }

    /*
     * fields: [{ 'anno', 'capitolo', 'impegno', 'imp_disp', 'a_carico'],
     */
    @Override
    public Collection load(int start, int limit, int page, List<Order> orderList) throws Exception {
        logger.debug("handling load request");
        UniqueTasklist task = getTask();
        final String codTipInt = (!Strings.isNullOrEmpty(getParameter("codTipInt"))?getParameter("codTipInt"):task.getPaiIntervento().getPaiInterventoPK().getCodTipint());
        final PaiIntervento paiIntervento = task.getPaiIntervento();
        final PaiInterventoPK paiInterventoPK = paiIntervento.getPaiInterventoPK();
        final PaiInterventoDao pi = new PaiInterventoDao(getEntityManager());

        ParametriIndata uot = paiIntervento.getPai().getIdParamUot();
		BudgetTipoInterventoUotDao budgetTipoInterventoUotDao = new BudgetTipoInterventoUotDao(getEntityManager());
		uot=transformUot(uot, budgetTipoInterventoUotDao, codTipInt, new ParametriIndataDao(getEntityManager()));
        final String desParam= uot.getDesParam();
		JsonBuilder jsonBuilder = JsonBuilder.newInstance();
		List<BudgetTipInterventoUot> byCodTipIntIdParamUot = budgetTipoInterventoUotDao.findByCodTipIntIdParamUot(codTipInt, uot.getIdParamIndata());
		jsonBuilder = jsonBuilder.withData(byCodTipIntIdParamUot);
		jsonBuilder = jsonBuilder.withTransformer(new JsonMapTransformer<BudgetTipInterventoUot>() {
			int id = 1;
			PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());

			@Override
			public void transformToMap(BudgetTipInterventoUot budgetTipInterventoUot) {
				BudgetTipInterventoUotPK pk = budgetTipInterventoUot.getBudgetTipInterventoUotPK();

				put("id", "" + (id++));
				put("anno", Short.toString(pk.getCodAnno()));
				put("capitolo", Integer.toString(budgetTipInterventoUot.getBudgetTipIntervento().getCodCap()));
				put("budget_iniziale", (budgetTipInterventoUot.getBdgDispEur()).toString());
				put("imp_disp", paiInterventoMeseDao.calculateBdgtDisp(budgetTipInterventoUot).toString());
				put("imp_disp_netto", paiInterventoMeseDao.calculateRealBdgtDisp(budgetTipInterventoUot).toString());
				put("imp_disp_proroghe", paiInterventoMeseDao.calculateRealBdgtDisp(budgetTipInterventoUot).subtract(pi.sumCostoProrghe(budgetTipInterventoUot.getBudgetTipIntervento().getBudgetTipInterventoPK().getCodImpe(), paiIntervento.getPaiInterventoPK().getCodTipint(), paiIntervento.getPai().getIdParamUot().getIdParamIndata())).toString());
				put("totale_prenotato", paiInterventoMeseDao.calculateBudgetPrenotato(budgetTipInterventoUot).toString());
				put("uot", desParam);
				put("impegno", budgetTipInterventoUot.getBudgetTipIntervento().getBudgetTipInterventoPK().getCodImpe());
				put("bdgDispQta", budgetTipInterventoUot.getBdgDispOre().subtract(paiInterventoMeseDao.sumBdgtPrevAndConsQta(budgetTipInterventoUot)).toString());
				put("bdgDispQtaCons", budgetTipInterventoUot.getBdgDispOre().subtract(paiInterventoMeseDao.sumBdgtConsQta(budgetTipInterventoUot)).toString());
			}

		});
		JsonStoreResponse jsonStoreResponse = jsonBuilder.buildStoreResponse();
		Collection data = jsonStoreResponse.getData();
		return data;
      
    }

    @Override
    public Object update(String dataStr) throws Exception {
    	return null;
    }
    
    /**
     * Metodo per capire se la uot è 3 o 4 e se esiste il budget per queste uot in caso trasforma la uot in 1 o 2
     * @param Uot
     * @param bdao
     * @param codTipint
     * @return
     */
    private ParametriIndata transformUot(ParametriIndata uot,BudgetTipoInterventoUotDao bdao , String codTipint,ParametriIndataDao pdao){
    	ParametriIndata result = uot;
    	ParametriIndata uot1= pdao.findUot(1);
    	ParametriIndata uot2 = pdao.findUot(2);
    	if(uot==null){
    		//non fare nulla
    		return result;
    	}
    	//caso uot 3
    	if((uot.getIdParamIndata()==7)){
    	//se è uot 3, non ha budget mentre uot 1 lo ha 
    	if( (bdao.findByCodTipIntIdParamUot(codTipint, uot.getIdParamIndata()).isEmpty()) && (bdao.findByCodTipIntIdParamUot(codTipint, uot1.getIdParamIndata()).isEmpty()==false)){
    		result=uot1;
    	}
    }
    	//caso uot 4
    	if((uot.getIdParamIndata()==8)){
    	//se è uot 4, non ha budget mentre uot 2 lo ha 
    	if( (bdao.findByCodTipIntIdParamUot(codTipint, uot.getIdParamIndata()).isEmpty()) && (bdao.findByCodTipIntIdParamUot(codTipint, uot2.getIdParamIndata()).isEmpty()==false)){
    		result=uot2;
    	}
    }
    	
    	return result;
    }
}
