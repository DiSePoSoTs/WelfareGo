package it.wego.welfarego.services.interventi;

import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.joda.time.LocalDate;

import java.util.Date;

public class DeterminaAnnoAvvioFine {

    int annoAvvio = 0;
    int annoFine = 0;

    public DeterminaAnnoAvvioFine() {}
    
    public DeterminaAnnoAvvioFine(InterventoDto nuovoIntervento) {
        Date dtAvvioNuovoIntervento = nuovoIntervento.getDtAvvio();
        Date dtFineNuovoIntervento = nuovoIntervento.getDtFine();
        annoAvvio = (new LocalDate(dtAvvioNuovoIntervento)).getYear();
        annoFine = (new LocalDate(dtFineNuovoIntervento)).getYear();
    }


    public void setIntervento(PaiIntervento intervento) {
    	setIntervento(new InterventoDto(intervento));
    }
    
    public void setIntervento(InterventoDto intervento) {
        Date dtAvvioNuovoIntervento = intervento.getDtAvvio();
        Date dtFineNuovoIntervento = intervento.getDtFine();
        annoAvvio = (new LocalDate(dtAvvioNuovoIntervento)).getYear();
        annoFine = (new LocalDate(dtFineNuovoIntervento)).getYear();    	
    }
    
    
    public int getAnnoAvvio() {
        return annoAvvio;
    }

    public int getAnnoFine() {
        return annoFine;
    }
}
