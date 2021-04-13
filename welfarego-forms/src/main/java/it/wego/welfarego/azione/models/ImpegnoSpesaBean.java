/**
 * 
 */
package it.wego.welfarego.azione.models;

/**
 * @author fabio Bonaccorso 
 * Impegno spesa bean per creare impegni spesa in casso di interventi trasformati.
 * 
 *
 */
public class ImpegnoSpesaBean {
	    private String carico;
	    private String anno;
	    private int capitolo;
	    private String id;
	    private String impegno;
	    private String quantita;
		
	    
	 
		
		public String getAnno() {
			return anno;
		}
		public void setAnno(String anno) {
			this.anno = anno;
		}
		public int getCapitolo() {
			return capitolo;
		}
		public void setCapitolo(int capitolo) {
			this.capitolo = capitolo;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getImpegno() {
			return impegno;
		}
		public void setImpegno(String impegno) {
			this.impegno = impegno;
		}
		
		public String getQuantita() {
			return quantita;
		}
		public void setQuantita(String quantita) {
			this.quantita = quantita;
		}
		public String getCarico() {
			return carico;
		}
		public void setCarico(String carico) {
			this.carico = carico;
		}
	
}
