package it.wego.welfarego.tasklist.model;

import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class OrarioBean {

	private String id;
	private String desc;
	private String note;
	private Long idImpegno;
	private Integer tipo, codAs;
	private String ora;
	private int giorno;

	public Integer getCodAs() {
		return codAs;
	}

	public void setCodAs(Integer codAs) {
		this.codAs = codAs;
	}

	public OrarioBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public Long getIdImpegno() {
		return idImpegno;
	}

	public void setIdImpegno(Long idImpegno) {
		this.idImpegno = idImpegno;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
