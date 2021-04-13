/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.DatoSpecifico;
import it.wego.welfarego.model.json.JSONDatiSpecifici;
import it.wego.welfarego.persistence.dao.MapDatiSpecTipIntDao;
import it.wego.welfarego.persistence.dao.MapDatiSpecificiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.DatiSpecifici;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class ComponentiServlet extends JsonServlet {

	@Override
	public Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {

		EntityManager em = Connection.getEntityManager();
		try {
			String tipoIntervento = Strings.emptyToNull(getParameter("tipo"));
			String cntTipint = Strings.emptyToNull(getParameter("cnt"));
			String codPai = Strings.emptyToNull(getParameter("codPai"));
			String tipOld = Strings.emptyToNull(getParameter("tipOld"));
			String cntOld = Strings.emptyToNull(getParameter("cntOld"));
			List<DatoSpecifico> datiSpecifici = Lists.newArrayList();
			if (cntTipint != null && codPai != null) {
				PaiIntervento paiIntervento = null;
				// se il tipo dell'intervento è uguale a quello hidden quindi non sono statie
				// apportate modifiche... procediamo come prima
				if (tipoIntervento.equals(tipOld) && cntTipint.equals(cntOld)) {
					paiIntervento = new PaiInterventoDao(em).findByKey(Integer.parseInt(codPai), tipoIntervento,
							cntTipint);
					Preconditions.checkNotNull(paiIntervento);

					for (MapDatiSpecificiIntervento datiSpecificiIntervento : new MapDatiSpecificiInterventoDao(em)
							.findByPaiIntervento(paiIntervento)) {
						datiSpecifici.add(serialize(datiSpecificiIntervento.getDatiSpecifici(),
								MoreObjects.firstNonNull(Strings.emptyToNull(datiSpecificiIntervento.getCodValCampo()),
										datiSpecificiIntervento.getValCampo())));
					}

				} else {
					paiIntervento = new PaiInterventoDao(em).findByKey(Integer.parseInt(codPai), tipOld, cntOld);
					Preconditions.checkNotNull(paiIntervento);
					List<MapDatiSpecificiIntervento> datiInterventoVecchio = new MapDatiSpecificiInterventoDao(em)
							.findByPaiIntervento(paiIntervento);

					for (MapDatiSpecTipint datiSpecTipint : new MapDatiSpecTipIntDao(em)
							.findByCodTipInt(tipoIntervento)) {
						DatoSpecifico dato = serialize(datiSpecTipint.getDatiSpecifici());
						dato.setCol(String.valueOf(datiSpecTipint.getColCampo()));
						dato.setRow(String.valueOf(datiSpecTipint.getRowCampo()));
						for (MapDatiSpecificiIntervento m : datiInterventoVecchio) {
							if (m.getCodCampo().equals(dato.getCodCampo())) {
								dato.setValDef(
										MoreObjects.firstNonNull(Strings.emptyToNull(m.getCodValCampo()), m.getValCampo()));

							}
						}
						datiSpecifici.add(dato);

					}
				}
			} else {

				for (MapDatiSpecTipint datiSpecTipint : new MapDatiSpecTipIntDao(em).findByCodTipInt(tipoIntervento)) {
					DatoSpecifico dato = serialize(datiSpecTipint.getDatiSpecifici());
					dato.setCol(String.valueOf(datiSpecTipint.getColCampo()));
					dato.setRow(String.valueOf(datiSpecTipint.getRowCampo()));
					datiSpecifici.add(dato);
				}
			}
			JSONDatiSpecifici json = new JSONDatiSpecifici();
			json.setComponents(datiSpecifici);
			json.setSuccess(true);
			return json;

		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}

	}

	private DatoSpecifico serialize(DatiSpecifici datiSpecifici) {
		DatoSpecifico datoSpecificoBean = new DatoSpecifico();
		datoSpecificoBean.setCodCampo(datiSpecifici.getCodCampoDsNormalized());
		datoSpecificoBean.setCodCampoCsr(datiSpecifici.getCodCampoCsr());
		if (datiSpecifici.getDecimali() != null) {
			datoSpecificoBean.setDecimali(String.valueOf(datiSpecifici.getDecimali()));
		}
		boolean obb = Objects.equal("S", String.valueOf(datiSpecifici.getFlgObb()));
		datoSpecificoBean.setDesCampo(datiSpecifici.getDesCampo() + (obb ? "*" : ""));
		datoSpecificoBean.setFlgEdit(String.valueOf(datiSpecifici.getFlgEdit()));
		datoSpecificoBean.setFlgObb(String.valueOf(datiSpecifici.getFlgObb()));
		datoSpecificoBean.setFlgVis(String.valueOf(datiSpecifici.getFlgVis()));
		datoSpecificoBean.setLunghezza(String.valueOf(datiSpecifici.getLunghezza()));
		datoSpecificoBean.setMsgErrore(datiSpecifici.getMsgErrore());
		datoSpecificoBean.setRegExpr(datiSpecifici.getRegExpr());
		datoSpecificoBean.setTipoCampo(String.valueOf(datiSpecifici.getTipoCampo()));
		datoSpecificoBean.setValAmm(datiSpecifici.getValAmm());
		datoSpecificoBean.setValDef(datiSpecifici.getValDef());
		return datoSpecificoBean;
	}

	private DatoSpecifico serialize(DatiSpecifici ds, String value) {
		DatoSpecifico d = serialize(ds);
		d.setValDef(value);
		return d;
	}
}
