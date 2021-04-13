package it.wego.welfarego.servlet.cartella;

import java.util.LinkedHashMap;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.AnagraficaBean;
import it.wego.welfarego.model.json.JSONAnagrafica;
import it.wego.welfarego.model.json.JSONCondizione;
import it.wego.welfarego.model.json.JSONDiario;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.MapDatiSpecificiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.serializer.CondizioneSerializer;
import it.wego.welfarego.serializer.InterventoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giuseppe
 */
public class PopolaDatiServlet extends JsonServlet {
    private final static Logger logger = LoggerFactory.getLogger(PopolaDatiServlet.class);

	private static final long serialVersionUID = 1L;
	
	@Override
    public Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            String action = Strings.emptyToNull(request.getParameter("action"));
            Preconditions.checkNotNull(action, "missing parameter azione");

            if (Objects.equal(action, "anagrafica")) {
                String codiceAnagraficaWellfareGoAsString = request.getParameter("codAnag");
                return actionAnagrafica(codiceAnagraficaWellfareGoAsString, em);


            } else if (Objects.equal(action, "anagraficaComunale")) {

                String codAnagComunale = request.getParameter("codAnag");
                VistaAnagrafe vistaAnagrafe = new VistaAnagrafeDao(em).findByNumeroIndividuale(Integer.valueOf(codAnagComunale));
                AnagrafeSoc anaSoc = new AnagrafeSocDao(em).findByCodFiscCodAnaCom(vistaAnagrafe.getCodiceFiscale(), codAnagComunale);
                AnagraficaBean bean = null;
              
                if(anaSoc!=null){
                	
                	 PaiDao paiDao = new PaiDao(em);
                     Pai pai = paiDao.findLastPai(anaSoc.getCodAna());
                     if (pai == null) {
                         pai = paiDao.findLastClosedPai(anaSoc.getCodAna());
                     }
                
					bean=new AnagrafeSocSerializer(em).anagrafeSocToAnagraficaBean(anaSoc,pai);
			
				   if (pai!=null && pai.getCartellaSociale() != null) {
	                        bean.setAnagraficaDataAperturaCartella(StringConversion.dateToItString(pai.getCartellaSociale().getDtApCs()));
	                    }
                }
                else {
                	bean = new AnagrafeSocSerializer(em).vistaAnagraficaToAnagraficaBean(vistaAnagrafe);
                }

                JSONAnagrafica json = new JSONAnagrafica();
                json.setAnagrafica(bean);
                json.setSuccess(true);
                return json;

            } else if (Objects.equal(action, "condizione")) {
                int codAnag = Integer.valueOf(request.getParameter("codAnag"));
                AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                AnagrafeSoc soc = anagrafeDao.findByCodAna(codAnag);
                CondizioneSerializer serializer = new CondizioneSerializer();
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findLastPai(codAnag);
                Object bean = serializer.serialize(soc, pai);
                JSONCondizione json = new JSONCondizione();
                json.setData(bean);
                json.setSuccess(true);
                return json;
            }else if (Objects.equal(action, "diario")) {
                int codAnag = Integer.valueOf(request.getParameter("codAnag"));
                AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                AnagrafeSoc soc = anagrafeDao.findByCodAna(codAnag);
                LinkedHashMap<Object, Object> res = Maps.newLinkedHashMap();
                res.put("diario", soc.getDiario());
                JSONDiario json = new JSONDiario();         
                json.setData(res);
                json.setSuccess(true);
                return json;
            }  
            else if (Objects.equal(action, "interventi")) {
                PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
                try {
                    paiInterventoDao.initTransaction();
                    PaiIntervento paiIntervento = paiInterventoDao.findByKey(Integer.parseInt(request.getParameter("codPai")), request.getParameter("tipo"), request.getParameter("cntTipint"));
                    new MapDatiSpecificiInterventoDao(em).fillDatiSpecificiForPaiIntervento(paiIntervento);
                    paiInterventoDao.commitTransaction();
                    return JsonBuilder.newInstance().withData(paiIntervento).withTransformer(InterventoSerializer.getinterventoSerializer()).buildResponse();
                } 
                finally {
                    paiInterventoDao.rollbackTransaction();
                }
            } else if (Objects.equal(action, "pai")) {
                String codPaiString = Strings.emptyToNull(request.getParameter("codPai"));
                Preconditions.checkNotNull(codPaiString);
                int codPai = Integer.valueOf(request.getParameter("codPai"));
                PaiDao paiDao = new PaiDao(em);
                Pai pai = paiDao.findPaiByCodPai(codPai);
                AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
                AnagrafeSoc anagrafe = pai.getCodAna().getAnagrafeSoc();
                AnagraficaBean bean = serializer.anagrafeSocToAnagraficaBean(anagrafe, pai);
                CartellaSociale cartella = pai.getCodAna();
                if (cartella != null) {
                    bean.setAnagraficaDataAperturaCartella(StringConversion.dateToItString(cartella.getDtApCs()));
                }
                JSONAnagrafica json = new JSONAnagrafica();
                json.setAnagrafica(bean);
                json.setSuccess(true);
                return json;
            }
            else if(Objects.equal(action, "salvaInSessione")){
            	HttpSession session = request.getSession(true);
            	String codPaiString = Strings.emptyToNull(request.getParameter("codPai"));
                session.setAttribute("codPai", codPaiString);
                JSONAnagrafica json = new JSONAnagrafica();
                json.setSuccess(true);
                
                return json;
            	
            }  else if(Objects.equal(action, "prendiDaSessione")){
            	HttpSession session = request.getSession();
            	String codPaiString = (String) session.getAttribute("codPai");
            	if (codPaiString==null){
            		return null;
            	}
            	else {
            		  int codPai = Integer.valueOf(codPaiString);
                      PaiDao paiDao = new PaiDao(em);
                      Pai pai = paiDao.findPaiByCodPai(codPai);
                      AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
                      AnagrafeSoc anagrafe = pai.getCodAna().getAnagrafeSoc();
                      AnagraficaBean bean = serializer.anagrafeSocToAnagraficaBean(anagrafe, pai);
                      CartellaSociale cartella = pai.getCodAna();
                      if (cartella != null) {
                          bean.setAnagraficaDataAperturaCartella(StringConversion.dateToItString(cartella.getDtApCs()));
                      }
                      JSONAnagrafica json = new JSONAnagrafica();
                      json.setAnagrafica(bean);
                      json.setSuccess(true);
                      session.removeAttribute("codPai");
                      return json;
            	}
                
              
            	
            }
            		else {
                Preconditions.checkArgument(false, "unknown action code = %s", action);
                return null; //unreacheable
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    Object actionAnagrafica(String codiceAnagraficaWellfareGoAsString, EntityManager em) {
        JSONAnagrafica json = new JSONAnagrafica();

        AnagraficaBean bean = null;

        if (codiceAnagraficaWellfareGoAsString != null && codiceAnagraficaWellfareGoAsString.trim().length()>0) {
            AnagrafeSoc soc = null;
            CartellaSociale cartellaSociale = null;

            int codiceAnagraficoWelfarego = Integer.valueOf(codiceAnagraficaWellfareGoAsString);
            PaiDao paiDao = new PaiDao(em);
            Pai pai = paiDao.findLastPai(codiceAnagraficoWelfarego);

            if (pai == null) {
                pai = paiDao.findLastClosedPai(codiceAnagraficoWelfarego);
            }

            if (pai != null) {
                cartellaSociale = pai.getCartellaSociale();
                soc = cartellaSociale.getAnagrafeSoc();
            } else {
                AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                soc = anagrafeDao.findByCodAna(codiceAnagraficoWelfarego);
            }


            bean = new AnagrafeSocSerializer(em).anagrafeSocToAnagraficaBean(soc, pai);
            if (cartellaSociale != null) {
                bean.setAnagraficaDataAperturaCartella(StringConversion.dateToItString(cartellaSociale.getDtApCs()));
            }
            json.setAnagrafica(bean);
        }
        json.setSuccess(true);
        return json;
    }
}
