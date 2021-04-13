package it.wego.welfarego.determine.model;

import it.wego.conversions.StringConversion;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class PaiEventoBean {

    private String id,cognome, nome, data, isee, stato,uot,costoTotale,quantitaTotale,budget;
    private String dataFineIntervento;

    public PaiEventoBean() {
    }

    public static PaiEventoBean fromPaiEvento(PaiEvento paiEvento) {
        PaiEventoBean paiEventoBean = new PaiEventoBean();

        paiEventoBean.setId(String.valueOf(paiEvento.getIdEvento()));
        paiEventoBean.setCognome(paiEvento.getPaiIntervento().getPai().getCodAna().getAnagrafeSoc().getCognome());
        paiEventoBean.setNome(paiEvento.getPaiIntervento().getPai().getCodAna().getAnagrafeSoc().getNome());
        paiEventoBean.setData(StringConversion.dateToItString(paiEvento.getTsEvePai()));
        paiEventoBean.setDataFineIntervento(StringConversion.dateToItString(paiEvento.getPaiIntervento().calculateDtFine()));
        paiEventoBean.setIsee("");
        if (paiEvento.getPaiIntervento().getPai().getIsee() != null) {
            paiEventoBean.setIsee(paiEvento.getPaiIntervento().getPai().getIsee().toString());
        }
        paiEventoBean.setStato(paiEvento.getPaiIntervento().getStatoInt() + "");
        paiEventoBean.setUot(paiEvento.getPaiIntervento().getPai().getIdParamUot().getDesParam());
        List<PaiInterventoMese> listaPim = paiEvento.getPaiIntervento().getPaiInterventoMeseList();
        BigDecimal costoTotale = BigDecimal.ZERO;
        BigDecimal quantitaTotale = BigDecimal.ZERO;
        String budget = "";
        Iterator<PaiInterventoMese> i = listaPim.iterator();
        while (i.hasNext()) {
            PaiInterventoMese p = i.next();
            costoTotale = costoTotale.add(p.getBdgPrevEur());
            quantitaTotale = quantitaTotale.add(p.getBdgPrevQta());
            if (p.getBdgPrevEur().compareTo(BigDecimal.ZERO) > 0) {
                if (budget.equals("") == false) {
                    budget = budget + "-";
                }
                budget = budget + (p.getBudgetTipIntervento().getBudgetTipInterventoPK().getCodImpe());
            }

        }
        paiEventoBean.setBudget(budget);
        paiEventoBean.setCostoTotale(paiEvento.getPaiIntervento().getCostoPrev().toString());
        paiEventoBean.setQuantitaTotale(quantitaTotale.toString());
        return paiEventoBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIsee() {
        return isee;
    }

    public void setIsee(String isee) {
        this.isee = isee;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getUot() {
		return uot;
	}

	public void setUot(String uot) {
		this.uot = uot;
	}

	public String getCostoTotale() {
		return costoTotale;
	}

	public void setCostoTotale(String costoTotale) {
		this.costoTotale = costoTotale;
	}

	public String getQuantitaTotale() {
		return quantitaTotale;
	}

	public void setQuantitaTotale(String quantitaTotale) {
		this.quantitaTotale = quantitaTotale;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

    public String getDataFineIntervento() {
        return dataFineIntervento;
    }

    public void setDataFineIntervento(String dataFineIntervento) {
        this.dataFineIntervento = dataFineIntervento;
    }
}
