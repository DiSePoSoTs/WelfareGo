package it.wego.welfarego.azione.forms;

import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.GenericDocumentoDataModel;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;

/**
 *
 * @author aleph
 */
public abstract class GenericDocumentoFormListener extends AbstractForm {

    public <T extends GenericDocumentoDataModel> T loadData(Class<T> clazz) throws Exception {
        UniqueTasklist task = getTask();
        Pai pai = task.getCodPai();
        PaiIntervento paiItervento = task.getPaiIntervento();
        Utenti utente = pai.getCodUteAs();
        AnagrafeSoc anagrafeSoc = pai.getAnagrafeSoc();
        T res = clazz.newInstance();
        res.setAssistSoc(utente.getCognome() + " " + utente.getNome());
        res.setCognomeUt(anagrafeSoc.getCognome());
        res.setNomeUtenteVerDati(anagrafeSoc.getNome());
        res.setDocumento(task.getCodTmpl().getDesTmpl());
        res.setDescrizione(task.getCodTmpl().getDesTmpl()); // TODO (?)
        if (paiItervento != null) {
            res.setIntervento(paiItervento.getTipologiaIntervento().getDesTipint());
        } else {
            res.setIntervento("nessun intervento associato");
        }
        res.setDataApertPai(pai.getDtApePai());
        res.setnPai(pai.getCodPai().toString());

        return res;
    }
}
