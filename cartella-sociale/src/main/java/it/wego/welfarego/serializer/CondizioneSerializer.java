package it.wego.welfarego.serializer;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import it.wego.conversions.StringConversion;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import java.util.Map;

/**
 *
 * @author giuseppe
 */
public class CondizioneSerializer {

    public Map serialize(AnagrafeSoc anag, Pai pai) {
        Map res = Maps.newLinkedHashMap();
        if (anag.getIdParamTipAll() != null) {
            res.put("condizioneAbitazione", (String.valueOf(anag.getIdParamTipAll().getIdParamIndata())));
            res.put("condizioneAggiornamentoAbitazione", StringConversion.dateToItString(anag.getDtAggAb()));
        }
        res.put("condizioneAccompagnamento", String.valueOf(anag.getFlgAccomp()));
        res.put("condizioneCognome", anag.getCognome());
        if (anag.getCondFam() != null) {
            res.put("condizioneCondizioneFamiliare", String.valueOf(anag.getCondFam().getIdParamIndata()));
        }
        if (anag.getIdParamCondProf() != null) {
            res.put("condizioneCondizioneProfessionale", String.valueOf(anag.getIdParamCondProf().getIdParamIndata()));
        }

        if (anag.getIdParamRedd() != null) {
            res.put("condizioneFormaReddito", String.valueOf(anag.getIdParamRedd().getIdParamIndata()));
        }
        res.put("condizioneIBAN", anag.getIbanPagam());

        if (anag.getPercInvCiv() != null) {
            res.put("condizioneInvaliditaCivile", String.valueOf(anag.getPercInvCiv()));
        }
        res.put("condizioneNome", anag.getNome());
        res.put("condizioneNote", anag.getNote());
        if (anag.getReddMens() != null) {
            res.put("condizioneRedditoMensile", String.valueOf(anag.getReddMens()));
        }

        if (anag.getIdParamStatoCiv() != null) {
            res.put("condizioneStatoCivile", String.valueOf(anag.getIdParamStatoCiv().getIdParamIndata()));
        }
        if (anag.getIdParamStatoFis() != null) {
            res.put("condizioneStatoFisico", String.valueOf(anag.getIdParamStatoFis().getIdParamIndata()));
        }
        if (anag.getIdParamTit() != null) {
            res.put("condizioneTitoloStudio", String.valueOf(anag.getIdParamTit().getIdParamIndata()));
        }
        if (anag.getRichiestaAssegnoAccompagnamento() != null) {
            res.put("condizioneRichiestaAssegnoAccompagnamento", anag.getRichiestaAssegnoAccompagnamento());
            if (Objects.equal(anag.getRichiestaAssegnoAccompagnamento(), AnagrafeSoc.RICHIESTA_ASSEGNO_ACCOMPAGNAMENTO_S) && anag.getDataRichiestaAssegnoAccompagnamento() != null) {
                res.put("condizioneDataRichiestaAssegnoAccompagnamento", StringConversion.dateToItString(anag.getDataRichiestaAssegnoAccompagnamento()));
            }
        }
        return res;
    }
}
