package it.wego.welfarego.scheduler;

import it.wego.welfarego.persistence.entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InterventiFactoryTest {

    private InterventiFactoryTest() {
    }

    public static PaiIntervento getFakeIntervento(int index) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        PaiIntervento paiIntervento = new PaiIntervento();
        PaiInterventoPK pk = new PaiInterventoPK();
        pk.setCntTipint(index);
        pk.setCodPai(123);
        pk.setCodTipint("MI005");
        paiIntervento.setPaiInterventoPK(pk);


        try {
            paiIntervento.setDtFine(sdf.parse("01/08/2018"));
        } catch (ParseException e) {
            throw new RuntimeException("", e);
        }
        paiIntervento.setPaiInterventoMeseList(new ArrayList<PaiInterventoMese>());
        paiIntervento.setPai(getPai());
        paiIntervento.setTipologiaIntervento(getTipologiaIntervento(paiIntervento, index));


        return paiIntervento;
    }

    private static TipologiaIntervento getTipologiaIntervento(PaiIntervento paiIntervento, int index) {
        TipologiaIntervento tipologiaIntervento = new TipologiaIntervento();
        switch (index) {
            case 1:
                tipologiaIntervento.setFlgRinnovo(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_DETERMINA);
                break;
            case 2:
                tipologiaIntervento.setFlgRinnovo(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_PROROGA);
                paiIntervento.setDurMesiProroga(1);
                break;
            case 3:
                tipologiaIntervento.setFlgRinnovo(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_S);
                break;
            default:
                tipologiaIntervento.setFlgRinnovo("dsfafd");
        }


        return tipologiaIntervento;
    }

    private static Pai getPai() {
        Pai pai = new Pai();
        pai.setFlgStatoPai('X');
        return pai;
    }
}
