/**
 * 
 */
package it.wego.welfarego.xsd.pratica;




import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabio Bonaccorso 
 * classe 
 *
 */
@XmlRootElement(name = "famiglia_anagrafica")
public class FamigliaAnagrafica {
	 protected FamigliaAnagrafica.AnagraficaUtente anagraficaUtente;
	  @XmlAccessorType(XmlAccessType.FIELD)
	    @XmlType(name = "", propOrder = {
	        "nome",
	        "cognome",
	        "codFisc",
	        "codAnaCom",
	        "codAnaComFam",
	        "dataNascita",
	        "flgSex",
	        "cittadinanza",
	        "codPosAna",
	        "infoResid",
	        "indirResid",
	        "famiglia"
	    })
	  public static class AnagraficaUtente {
		    @XmlElement(required = true,nillable=true)
	        protected String nome;
	        @XmlElement(required = true,nillable=true)
	        protected String cognome;
	        @XmlElement(required = true,nillable=true)
	        protected String codFisc;
	        @XmlElement(required = true,nillable=true)
	        protected String codAnaCom;
	        @XmlElement(required = true,nillable=true)
	        protected String codAnaComFam;
	        @XmlElement(name = "data_nascita", required = true,nillable=true)
	        protected String dataNascita;
	        @XmlElement(required = true,nillable=true)
	        protected String flgSex;
	        @XmlElement(required = true,nillable=true)
	        protected String cittadinanza;
	        @XmlElement(required = true, nillable = true)
	        protected DecodParamType codPosAna;
	        @XmlElement(required = true)
	        protected InfoTerrType infoResid;
	        @XmlElement(required = true, nillable = true)
	        protected InfoIndirizzoType indirResid;
	    
	        @XmlElement(required = true)
	        private Famiglia famiglia;
	        
	        public Famiglia getFamiglia() {
	            if (famiglia == null) {
	                famiglia = new Famiglia();
	            }
	            return famiglia;
	        }
	        
	        public void setFamiglia(Famiglia famiglia) {
	            this.famiglia = famiglia;
	        }
	        

	        @XmlAccessorType(XmlAccessType.FIELD)
	        @XmlType(name = "", propOrder = {
	            "familiare"
	        })
	        
	        public static class Famiglia {
	        	  @XmlElement(required = true)
	              protected List<Familiare> familiare;
	        	    public List<Familiare> getFamiliare() {
	                    if (familiare == null) {
	                        familiare = new ArrayList<Familiare>();
	                    }
	                    return this.familiare;
	                }
	        	     @XmlAccessorType(XmlAccessType.FIELD)
	                 @XmlType(name = "", propOrder = {
	                     "nome",
	                     "cognome",
	                     "luogoNascita",
	                     "dataNascita"
	                     })
	        	     
	        	     public static class Familiare {
	        	    	   private String nome, cognome, luogoNascita, dataNascita ;

						public String getNome() {
							return nome;
						}

						public void setNome(String nome) {
							this.nome = nome;
						}

						public String getCognome() {
							return cognome;
						}

						public void setCognome(String cognome) {
							this.cognome = cognome;
						}

						public String getLuogoNascita() {
							return luogoNascita;
						}

						public void setLuogoNascita(String luogoNascita) {
							this.luogoNascita = luogoNascita;
						}

						public String getDataNascita() {
							return dataNascita;
						}

						public void setDataNascita(String dataNascita) {
							this.dataNascita = dataNascita;
						}
	        	    	   
	        	     }
	        }


			public String getNome() {
				return nome;
			}

			public void setNome(String nome) {
				this.nome = nome;
			}

			public String getCognome() {
				return cognome;
			}

			public void setCognome(String cognome) {
				this.cognome = cognome;
			}

			public String getCodFisc() {
				return codFisc;
			}

			public void setCodFisc(String codFisc) {
				this.codFisc = codFisc;
			}

			public String getCodAnaCom() {
				return codAnaCom;
			}

			public void setCodAnaCom(String codAnaCom) {
				this.codAnaCom = codAnaCom;
			}

			public String getCodAnaComFam() {
				return codAnaComFam;
			}

			public void setCodAnaComFam(String codAnaComFam) {
				this.codAnaComFam = codAnaComFam;
			}

			public String getDataNascita() {
				return dataNascita;
			}

			public void setDataNascita(String dataNascita) {
				this.dataNascita = dataNascita;
			}

			public String getFlgSex() {
				return flgSex;
			}

			public void setFlgSex(String flgSex) {
				this.flgSex = flgSex;
			}

			public String getCittadinanza() {
				return cittadinanza;
			}

			public void setCittadinanza(String cittadinanza) {
				this.cittadinanza = cittadinanza;
			}

			
			public DecodParamType getCodPosAna() {
				return codPosAna;
			}

			public void setCodPosAna(DecodParamType codPosAna) {
				this.codPosAna = codPosAna;
			}

			public InfoTerrType getInfoResid() {
				return infoResid;
			}

			public void setInfoResid(InfoTerrType infoResid) {
				this.infoResid = infoResid;
			}

			public InfoIndirizzoType getIndirResid() {
				return indirResid;
			}

			public void setIndirResid(InfoIndirizzoType indirResid) {
				this.indirResid = indirResid;
			}
	        
	        
	        
	        


	  }
	public FamigliaAnagrafica.AnagraficaUtente getAnagraficaUtente() {
		return anagraficaUtente;
	}
	public void setAnagraficaUtente(
			FamigliaAnagrafica.AnagraficaUtente anagraficaUtente) {
		this.anagraficaUtente = anagraficaUtente;
	}
	
}
