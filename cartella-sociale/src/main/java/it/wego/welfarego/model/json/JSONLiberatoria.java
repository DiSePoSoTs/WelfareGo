/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.LiberatoriaBean;
import java.util.List;

/**
 *
 * @author DOTCOM
 */
public class JSONLiberatoria
{
	private Boolean success;
	private Integer total;
	private List<LiberatoriaBean> data;

	public Boolean getSuccess()
	{
		return success;
	}

	public void setSuccess(Boolean success)
	{
		this.success = success;
	}

	public Integer getTotal()
	{
		return total;
	}

	public void setTotal(Integer total)
	{
		this.total = total;
	}

	public List<LiberatoriaBean> getData()
	{
		return data;
	}

	public void setData(List<LiberatoriaBean> data)
	{
		this.data = data;
	}
	
}//JSONLiberatoria
