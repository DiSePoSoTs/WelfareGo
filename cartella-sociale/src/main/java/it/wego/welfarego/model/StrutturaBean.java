/**
 * 
 */
package it.wego.welfarego.model;


import it.wego.welfarego.persistence.entities.Struttura;
/**
*
* @author Fabio Bonaccorso
* Oggetto dto per le strutture
* 
*/

/**
 * @author fabio
 *
 */
public class StrutturaBean {
	
	
	private Integer id ;
	private String cod_tipint;
	private String nome;
	private String indirizzo;
	private String cod_csr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCod_tipint() {
		return cod_tipint;
	}
	public void setCod_tipint(String cod_tipint) {
		this.cod_tipint = cod_tipint;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCod_csr() {
		return cod_csr;
	}
	public void setCod_csr(String cod_csr) {
		this.cod_csr = cod_csr;
	}

	public StrutturaBean serialize(Struttura s){
		
		StrutturaBean bean = new StrutturaBean();
		bean.setId(s.getId());
		bean.setCod_csr(s.getCsrId());
		bean.setIndirizzo(s.getIndirizzo());
		bean.setNome(s.getNome());
		
		return bean;
		
	}
	    
	
}
