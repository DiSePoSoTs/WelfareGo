Ext.define('wf.store.combo.StatoInterventoStore',{
	extend: 'Ext.data.Store',
	model: 'wf.model.ComboModel',
	data : [{
		value: 'AR', 
		name: 'Esecutività'
	},{
		value: 'E', 
		name: 'Variazione'
	},{
		value: 'C', 
		name: 'Chiusura'
	}]
});