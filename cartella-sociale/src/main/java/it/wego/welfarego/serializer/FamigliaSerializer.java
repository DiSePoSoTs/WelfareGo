/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public enum FamigliaSerializer implements Function<AnagrafeSoc, Map<String, Object>> {

    INSTANCE;

    public Map<String, Object> serializeAnagrafeSoc(AnagrafeSoc anagrafeSociale, int codAna, @Nullable Integer codAnaFam) {
        codAnaFam = MoreObjects.firstNonNull(codAnaFam, anagrafeSociale.getCodAna());
        Map<String, Object> res = Maps.newLinkedHashMap();
        if (anagrafeSociale.getLuogoResidenza() != null) {
            res.put("civicoResidenza", anagrafeSociale.getLuogoResidenza().getCodCiv());
            res.put("civicoResidenzaDes", anagrafeSociale.getLuogoResidenza().getCivicoText());
        }
        res.put("codAnaFamigliare", String.valueOf(codAnaFam));
        res.put("codAnag", String.valueOf(codAna));
        res.put("codiceFiscale", anagrafeSociale.getCodFisc());
        res.put("cognome", anagrafeSociale.getCognome());
        if (anagrafeSociale.getLuogoNascita().getComune() != null) {
            res.put("comuneNascita", anagrafeSociale.getLuogoNascita().getComune().getDesCom());
            res.put("comuneNascitaDes", anagrafeSociale.getLuogoNascita().getComune().getDesCom());
        }
        if (anagrafeSociale.getLuogoResidenza().getComune() != null) {
            res.put("comuneResidenza", anagrafeSociale.getLuogoResidenza().getComune().getDesCom());
            res.put("comuneResidenzaDes", anagrafeSociale.getLuogoResidenza().getComune().getDesCom());
        }

        res.put("dataMorte", StringConversion.dateToItString(anagrafeSociale.getDtMorte()));
        res.put("dataNascita", StringConversion.dateToItString(anagrafeSociale.getDtNasc()));
        if (anagrafeSociale.getCodStatoNaz() != null) {
            res.put("nazionalita", String.valueOf(anagrafeSociale.getCodStatoNaz().getIdParam().getIdParam()));
        }
        if (anagrafeSociale.getCodStatoCitt() != null) {
            res.put("cittadinanza", String.valueOf(anagrafeSociale.getCodStatoCitt().getCodStato()));
        }

        res.put("nome", anagrafeSociale.getNome());
        res.put("attivitaLavoroStudio", anagrafeSociale.getAttivitaLavoroStudio());
        if (anagrafeSociale.getLuogoResidenza().getProvincia() != null) {
            res.put("provinciaResidenza", anagrafeSociale.getLuogoResidenza().getProvincia().getDesProv());
            res.put("provinciaResidenzaDes", anagrafeSociale.getLuogoResidenza().getProvincia().getDesProv());
        }
        res.put("sesso", String.valueOf(anagrafeSociale.getFlgSex()));
        if (anagrafeSociale.getIdParamStatoCiv() != null) {
            res.put("statoCivile", String.valueOf(anagrafeSociale.getIdParamStatoCiv().getIdParamIndata()));
        }
        if (anagrafeSociale.getLuogoNascita().getStato() != null) {
            res.put("statoNascita", anagrafeSociale.getLuogoNascita().getStato().getDesStato());
        }
        if (anagrafeSociale.getLuogoResidenza().getStato() != null) {
            res.put("statoResidenza", anagrafeSociale.getLuogoResidenza().getStato().getDesStato());
        }

        if (anagrafeSociale.getLuogoResidenza().getVia() != null) {
            res.put("viaResidenza", anagrafeSociale.getLuogoResidenza().getVia().getDesVia());
            res.put("viaResidenzaDes", anagrafeSociale.getLuogoResidenza().getVia().getDesVia());
        }
        if (anagrafeSociale.getReddito() != null) {
            res.put("dataAggiornamentoRedditoFamiliare", StringConversion.dateToItString(anagrafeSociale.getDataUpdateReddito()));
            res.put("redditoFamiliare", anagrafeSociale.getReddito().toPlainString());
        }

        return res;
    }

    public static Function<VistaAnagrafe, Map<String,Object>> getVistaAnagrafeSerializer(final EntityManager entityManager) {
        return new JsonMapTransformer<VistaAnagrafe>() {
            @Override
            public void transformToMap(VistaAnagrafe vistaAnagrafe) {
                put("nome", vistaAnagrafe.getNome());
                put("cognome", vistaAnagrafe.getCognome());
                if (vistaAnagrafe.getSesso().equals("1")) {
                    put("sesso", "M");
                } else {
                    put("sesso", "F");
                }
                put("codiceFiscale", vistaAnagrafe.getCodiceFiscale());
                put("dataNascita", StringConversion.dateToItString(vistaAnagrafe.getDataNascita()));
                put("comuneNascitaDes", vistaAnagrafe.getLuogoNascita());
                put("statoResidenzaDes", MoreObjects.firstNonNull(vistaAnagrafe.getDescrizioneStatoResidenza(), "ITALIA"));
                put("statoNascitaDes", MoreObjects.firstNonNull(vistaAnagrafe.getDescrizioneStatoNascita(), "ITALIA"));
                put("provinciaResidenzaDes", vistaAnagrafe.getDescrizioneProvinciaResidenza());
                put("comuneResidenzaDes", vistaAnagrafe.getDescrizioneComuneResidenza());
                put("viaResidenzaDes", vistaAnagrafe.getDescrizioneViaResidenza());
                put("civicoResidenzaDes", vistaAnagrafe.getDescrizioneCivicoResidenza());
                put("dataMorte", StringConversion.dateToItString(vistaAnagrafe.getDataMorte()));
                put("codAnaComunale", String.valueOf(vistaAnagrafe.getNumeroIndividuale()));
                put("posizioneAnagrafica", vistaAnagrafe.getPosizioneAnagrafica());
                put("cittadinanza", vistaAnagrafe.getCittadinanza());

            }
        };
    }

    public Map<String, Object> apply(AnagrafeSoc input) {
        Preconditions.checkNotNull(input);
        return serializeAnagrafeSoc(input, input.getCodAna(), null);
    }
}
