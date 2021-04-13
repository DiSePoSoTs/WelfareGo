package it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.tramite.strategie;


import it.wego.welfarego.dto.CostiInterventoMultiAnno;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.scheduler.rinnovi.helper.RinnovoInterventoLogBuilder;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoMultiannoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.wego.welfarego.scheduler.dto.BudgetEAnniDto;
import it.wego.welfarego.scheduler.rinnovi.rinnovo.automatico.RinnovoAutomaticoteUtils;

import java.math.BigDecimal;

public class CreaProposteTramiteStrategie {
    private final Logger logger = LoggerFactory.getLogger(CreaProposteTramiteStrategie.class);


    private RinnovoAutomaticoteUtils rinnovoAutomaticoteUtils = null;
    private DeterminaBudgetAvvioFine determinaBudgetAvvioFine = null;
    private CalcolaCostoInterventoService calcolaCostoInterventoService = null;
    private CreaPropostaAnnoSingolo creaPropostaAnnoSingolo = null;
    private CreaPropostaAnniMultipli creaPropostaAnniMultipli = null;
    private CalcolaCostoInterventoMultiannoService calcolaCostoInterventoMultiannoService  = null;
    
    public CreaProposteTramiteStrategie() {
    	rinnovoAutomaticoteUtils = new RinnovoAutomaticoteUtils();
    	determinaBudgetAvvioFine = new DeterminaBudgetAvvioFine(rinnovoAutomaticoteUtils);
    	calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        creaPropostaAnnoSingolo = new CreaPropostaAnnoSingolo();
        creaPropostaAnniMultipli = new CreaPropostaAnniMultipli();
        calcolaCostoInterventoMultiannoService = new CalcolaCostoInterventoMultiannoService();
    }
    
    public CreaProposteTramiteStrategie(
    		RinnovoAutomaticoteUtils rinnovoAutomaticoteUtils
    		, CalcolaCostoInterventoService calcolaCostoInterventoService
    		, DeterminaBudgetAvvioFine determinaBudgetAvvioFine
    		, CreaPropostaAnnoSingolo creaPropostaAnnoSingolo
    		, CreaPropostaAnniMultipli creaPropostaAnniMultipli
    		, CalcolaCostoInterventoMultiannoService calcolaCostoInterventoMultiannoService) {
    	this.rinnovoAutomaticoteUtils = rinnovoAutomaticoteUtils;
    	this.determinaBudgetAvvioFine = determinaBudgetAvvioFine;
    	this.calcolaCostoInterventoService = calcolaCostoInterventoService;
    	this.creaPropostaAnnoSingolo = creaPropostaAnnoSingolo;
    	this.creaPropostaAnniMultipli = creaPropostaAnniMultipli;
    	this.calcolaCostoInterventoMultiannoService = calcolaCostoInterventoMultiannoService;
    }
    
    public void crea_proposte(PaiIntervento interventoPadre, PaiIntervento nuovoIntervento
            , PaiInterventoMeseDao paiInterventoMeseDao, BudgetTipoInterventoDao budgetTipoInterventoDao, RinnovoInterventoLogBuilder rinnovoInterventoLogBuilder) throws Exception {

        /*preparo i dati di cui ho bisogno - INIZIO*/
        BudgetTipInterventoPK budgetPadrePK = rinnovoAutomaticoteUtils.getBudgetPadre(interventoPadre, paiInterventoMeseDao);        
        BigDecimal importoStandard = rinnovoAutomaticoteUtils.calcolaImportoStandard(nuovoIntervento);
        ParametriIndata idParamFasciaNuovoIntervento = nuovoIntervento.getPai().getIdParamFascia();

        BudgetEAnniDto  budgetEAnniDto =  determinaBudgetAvvioFine.determina_anni_e_budget_avvio_fine(interventoPadre, nuovoIntervento, budgetTipoInterventoDao);
        BudgetTipIntervento budgetAnnoAvvio = budgetEAnniDto.getBudgetAnnoAvvio();
        BudgetTipIntervento budgetAnnoFine = budgetEAnniDto.getBudgetAnnoFine();
        int annoAvvio = budgetEAnniDto.getAnnoAvvio();
        int annoFine = budgetEAnniDto.getAnnoFine();
        /*preparo i dati di cui ho bisogno - FINE*/



        /*LOGICA APPLICATIVA - INIZIO*/
        boolean isAnnoSingolo = is_anno_singolo(annoAvvio, annoFine);

        rinnovoInterventoLogBuilder.isAnnoSingolo(isAnnoSingolo);

        if (isAnnoSingolo) {
            BigDecimal bdgPrevEur = calcolaCostoInterventoService.calcolaBdgPrevEur(nuovoIntervento);
            BigDecimal bdgPrevQta = rinnovoAutomaticoteUtils.calcolaBdgPrevQta(bdgPrevEur, importoStandard);
            nuovoIntervento.setCostoPrev(bdgPrevEur);

            rinnovoInterventoLogBuilder.setBudgetPrevEur(bdgPrevEur);
            rinnovoInterventoLogBuilder.setBudgetPrevQta(bdgPrevQta);

            creaPropostaAnnoSingolo.crea_proposta(nuovoIntervento, budgetAnnoAvvio, budgetPadrePK
    	            , bdgPrevEur, idParamFasciaNuovoIntervento, importoStandard, bdgPrevQta
    	            , paiInterventoMeseDao, rinnovoInterventoLogBuilder);

        } else {
        	        	
            CostiInterventoMultiAnno costiInterventoMultiAnno = calcolaCostoInterventoMultiannoService.invoke(nuovoIntervento);

            BigDecimal costoPrimoAnno = costiInterventoMultiAnno.getCostoPrimoAnno();
            BigDecimal costoSecondoAnno = costiInterventoMultiAnno.getCostoSecondoAnno();
            
            BigDecimal costoPrevisto = costoPrimoAnno.add(costoSecondoAnno);            
            
            PaiInterventoPK nuovoInterventoPK = nuovoIntervento.getPaiInterventoPK();

            creaPropostaAnniMultipli.creaProposta(interventoPadre, paiInterventoMeseDao, idParamFasciaNuovoIntervento
                    , nuovoInterventoPK, importoStandard
                    , annoAvvio, annoFine, budgetAnnoAvvio, budgetAnnoFine, costoPrimoAnno, costoSecondoAnno, costoPrevisto, rinnovoInterventoLogBuilder);

            nuovoIntervento.setCostoPrev(costoPrevisto);
        }
        /*LOGICA APPLICATIVA - FINE*/

    }

    boolean is_anno_singolo(int annoAvvio, int annoFine) {
        return annoAvvio == annoFine;
    }


}