package it.wego.welfarego.listeattesa;

import it.trieste.comune.ssc.json.GsonObject;
import it.trieste.comune.ssc.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import java.util.List;

/*
 * @author Mess
 */
public class ListeAttesaForm extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Proceedable {

	@Override
	public Object load() throws Exception {
		return new ListeAttesaStoreListener(this.getParameters()).load();
	}

	@Override
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		ListeAttesaProceedDataModel data = getDataParameter(ListeAttesaProceedDataModel.class);
		initTransaction();
		for (ListeAttesaProceedRecordDataModel record : data.getInterventi()) {
			PaiIntervento paiIntervento = getEntityManager().find(PaiIntervento.class,
					  new PaiInterventoPK(Integer.parseInt(record.getCodPai()), record.getCodTipInt(), Integer.parseInt(record.getCntTipInt())));
			switch (record.getEsito()) {
				case approvato:
					paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_APERTO);
					PaiEvento evento = insertEvento(paiIntervento, PaiEvento.PAI_APPROVAZIONE_LISTA_ATTESA);
					evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_SI);
					break;
				case respinto:
					Pai pai = paiIntervento.getPai();
					paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
					insertEvento(paiIntervento, PaiEvento.PAI_RIFIUTO_LISTA_ATTESA);
					break;
			}
		}
		commitTransaction();
		return new JsonMessage("operazione completata");
	}

	public static class ListeAttesaProceedRecordDataModel extends GsonObject {

		private String codPai, codTipInt, cntTipInt;
		private Esito esito;

		public static enum Esito {

			approvato, respinto
		}

		public String getCntTipInt() {
			return cntTipInt;
		}

		public void setCntTipInt(String cntTipInt) {
			this.cntTipInt = cntTipInt;
		}

		public String getCodPai() {
			return codPai;
		}

		public void setCodPai(String codPai) {
			this.codPai = codPai;
		}

		public String getCodTipInt() {
			return codTipInt;
		}

		public void setCodTipInt(String codTipInt) {
			this.codTipInt = codTipInt;
		}

		public Esito getEsito() {
			return esito;
		}

		public void setEsito(Esito esito) {
			this.esito = esito;
		}
	}

	public static class ListeAttesaProceedDataModel extends GsonObject {

		private String tipIntervento, listaAttesa;
		private List<ListeAttesaProceedRecordDataModel> interventi;

		public List<ListeAttesaProceedRecordDataModel> getInterventi() {
			return interventi;
		}

		public void setInterventi(List<ListeAttesaProceedRecordDataModel> interventi) {
			this.interventi = interventi;
		}

		public String getListaAttesa() {
			return listaAttesa;
		}

		public void setListaAttesa(String listaAttesa) {
			this.listaAttesa = listaAttesa;
		}

		public String getTipIntervento() {
			return tipIntervento;
		}

		public void setTipIntervento(String tipIntervento) {
			this.tipIntervento = tipIntervento;
		}
	}
}
