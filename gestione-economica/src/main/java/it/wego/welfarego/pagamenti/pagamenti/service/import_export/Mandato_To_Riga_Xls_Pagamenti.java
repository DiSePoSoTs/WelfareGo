package it.wego.welfarego.pagamenti.pagamenti.service.import_export;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Mandato_To_Riga_Xls_Pagamenti implements Function<Mandato, Iterable> {

    public Iterable apply(Mandato mandato) {
//            return mandatoToCsv(mandato);

//        MandatoDettaglio mandatoDettaglio = mandato.getMandatoDettaglioList().isEmpty() ? null : mandato.getMandatoDettaglioList().iterator().next();
//        PaiInterventoMese paiInterventoMese = (mandatoDettaglio == null || mandatoDettaglio.getPaiInterventoMeseList().isEmpty()) ? null
//                : mandatoDettaglio.getPaiInterventoMeseList().iterator().next();
//        Luogo luogo = mandato.getPaiIntervento().getPai().getAnagrafeSoc().getLuogoResidenza();
        Preconditions.checkNotNull(mandato.getModalitaErogazione(), "ATTENZIONE  la modalità di erogazione per " + mandato.getCognomeBeneficiario() + " è nullo");

        return Arrays.asList(
                get_Cod_Nominativo_Sicraweb(),
                get_PartitaIva(),
                get_CodiceFiscale(mandato),
                getNome(mandato),
                getCognome(mandato),
                get_Ragione_Sociale(),
                get_Imponibile(mandato),
                get_Iban(mandato),
                get_Note(mandato),
                get_Note_2_Lingua(),
                get_Note_3_Lingua(),
                get_Nome_Delegato(mandato),
                get_Cognome_Delegato(mandato),
                get_Cf_Delegato(mandato),
                get_Piva_Delegato(mandato),
                get_RagioneSociale_Delegato(mandato),
                get_Impegno(mandato),
                get_Metodo_Pagamento(mandato)
        );
    }

    private String get_Metodo_Pagamento(Mandato mandato) {
        return mandato.getModalitaErogazione().equals("PER_CASSA") ? "CONTA" : "ACBAN";
    }

    private Object get_Impegno(Mandato mandato) {
        List<MandatoDettaglio> mandatoDettaglioList = mandato.getMandatoDettaglioList();
        MandatoDettaglio mandatoDettaglio = mandatoDettaglioList.get(0);
        PaiIntervento paiIntervento = mandatoDettaglio.getPaiIntervento();
        List<PaiInterventoMese> paiInterventoMeseList = paiIntervento.getPaiInterventoMeseList();
        PaiInterventoMese paiInterventoMese = paiInterventoMeseList.get(0);
        return paiInterventoMese.getPaiInterventoMesePK().getCodImp();
    }

    private String get_CodiceFiscale(Mandato mandato) {
        return mandato.getCfBeneficiario();
    }

    private String getNome(Mandato mandato) {
        return mandato.getNomeBeneficiario();
    }

    private String getCognome(Mandato mandato) {
        return mandato.getCognomeBeneficiario();
    }

    private Object get_Ragione_Sociale() {
        // da lasciare vuoto
        return null;
    }

    private BigDecimal get_Imponibile(Mandato mandato) {
        return mandato.getImporto();
    }

    private String get_Iban(Mandato mandato) {

        return mandato.getModalitaErogazione().equals("PER_CASSA") ? "" : mandato.getIban();
    }

    private String get_Note(Mandato mandato) {
        return String.valueOf(mandato.getIdMan());
    }

    private Object get_Note_2_Lingua() {
        // da lasciare vuoto
        return null;
    }

    private Object get_Note_3_Lingua() {
        // da lasciare vuoto
        return null;
    }


    private Object get_PartitaIva() {
        // da lasciare vuoto
        return null;
    }

    private Object get_Cod_Nominativo_Sicraweb() {
        // da lasciare vuoto
        return null;
    }

    private String get_Nome_Delegato(Mandato mandato) {
        String result = "";

        if (non_e_azienda_e_non_e_il_beneficiario(mandato)) {
            result = mandato.getNomeDelegante();
        }

        return result;
    }

    private String get_Cognome_Delegato(Mandato mandato) {
        String result = "";

        if (non_e_azienda_e_non_e_il_beneficiario(mandato)) {
            result = mandato.getCognomeDelegante();
        }

        return result;
    }

    private String get_RagioneSociale_Delegato(Mandato mandato) {
        String result = "";

        if (is_Azienda(mandato)) {
            result = mandato.getCognomeDelegante();
        }

        return result;
    }

    private String get_Cf_Delegato(Mandato mandato) {

        AnagrafeSoc delegante = mandato.getCodAnaDelegante();

        String result = "";
        if (delegante != null) {
            result = delegante.getCodFisc();
        }

        if (is_stessa_persona(mandato)) {
            result = "";
        }

        return result;
    }

    private String get_Piva_Delegato(Mandato mandato) {
        String result = "";

        if (is_Azienda(mandato)) {
            AnagrafeSoc delegante = mandato.getCodAnaDelegante();
            if (delegante != null) {
                result = delegante.getPartIva();
            }
        }

        return result;
    }

    private boolean non_e_azienda_e_non_e_il_beneficiario(Mandato mandato) {
        return !is_Azienda(mandato) && !is_stessa_persona(mandato);
    }

    private boolean is_stessa_persona(Mandato mandato) {
        boolean delegato_is_beneficiario = true;

        AnagrafeSoc anagrafeDelegante = mandato.getCodAnaDelegante();
        AnagrafeSoc anagrafeBeneficiario = mandato.getCodAnaBeneficiario();

        if (anagrafeDelegante != null) {
            Integer codAnaDelegante = anagrafeDelegante.getCodAna();
            Integer codAnaBeneficiario = anagrafeBeneficiario.getCodAna();
            delegato_is_beneficiario = codAnaDelegante.equals(codAnaBeneficiario);
        }

        return delegato_is_beneficiario;
    }

    private boolean is_Azienda(Mandato mandato) {
        boolean isAzienda = false;

        AnagrafeSoc codAnaDelegante = mandato.getCodAnaDelegante();
        if (codAnaDelegante != null && AnagrafeSoc.PERSONA_FISICA_G.equalsIgnoreCase(codAnaDelegante.getFlgPersFg())) {
            isAzienda = true;
        }

        return isAzienda;
    }
}