package it.wego.welfarego.xsd.pratica.dto;

import it.wego.welfarego.persistence.entities.AnagrafeSoc;

public class AnagrafeDto {
    private AnagrafeSoc anagrafeSoc;
    private String relazioneDiParentela;

    public AnagrafeDto(AnagrafeSoc anagrafeSoc, String relazioneDiParentela) {
        this.anagrafeSoc = anagrafeSoc;
        this.relazioneDiParentela = relazioneDiParentela;
    }

    public AnagrafeSoc getAnagrafeSoc() {
        return anagrafeSoc;
    }

    public String getRelazioneDiParentela() {
        return relazioneDiParentela;
    }
}