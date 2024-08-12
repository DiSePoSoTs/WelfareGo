package it.wego.welfarego.listeattesa;

import it.trieste.comune.ssc.beans.MultipleFieldPojoComparator;
import it.trieste.comune.ssc.beans.Order;
import it.wego.welfarego.abstracts.AbstractStoreListener;
import it.wego.welfarego.determine.model.PaiInterventoBean;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.ListaAttesa;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import java.util.*;

/**
 *
 * @author aleph
 */
public class ListeAttesaStoreListener extends AbstractStoreListener {

	public static final String PARAM_KEY_FILTRO_LISTA = "lista_attesa",
			  PARAM_KEY_FILTRO_TIPOLOGIA_INTERVENTO = "tipologia_intervento",
			  VALUE_ALL = "all";
	public static final Character LIST_STATUS = 'L';

	public ListeAttesaStoreListener(Map<String, String> parameters) {
		super(parameters);
	}

	@Override
	public Collection load(int start, int limit, int page, List<Order> orderList) throws Exception {
		String listaAttesa = getParameter(PARAM_KEY_FILTRO_LISTA),
				  tipologiaIntervento = getParameter(PARAM_KEY_FILTRO_TIPOLOGIA_INTERVENTO);
		PaiInterventoDao paiInterventoDao = new PaiInterventoDao(getEntityManager());
		List<PaiIntervento> interventi = null;

		if (tipologiaIntervento != null && !tipologiaIntervento.equals(VALUE_ALL)) {
			interventi = paiInterventoDao.findByStatusTipint(LIST_STATUS, getEntityManager().getReference(TipologiaIntervento.class, tipologiaIntervento));
		} else if (listaAttesa != null) {
			interventi = paiInterventoDao.findByStatusList(LIST_STATUS, getEntityManager().getReference(ListaAttesa.class, Integer.valueOf(listaAttesa)));
		} else {
			interventi = paiInterventoDao.findByStatus(LIST_STATUS);
		}

		List<PaiInterventoBean> listaDetermineBean = new ArrayList<PaiInterventoBean>();

		for (PaiIntervento paiIntervento : interventi) {
			PaiInterventoBean record=PaiInterventoBean.fromPaiIntervento(paiIntervento);
			listaDetermineBean.add(record);
		}

		// TODO ordinamento a livello di DAO

		if (orderList != null && !orderList.isEmpty()) {
			Collections.sort(listaDetermineBean, new MultipleFieldPojoComparator(orderList.toArray(new Order[orderList.size()])));
		}
		listaDetermineBean = listaDetermineBean.subList(start, Math.min(start + limit, listaDetermineBean.size()));
		return listaDetermineBean;
	}

	@Override
	public Object update(String data) throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
