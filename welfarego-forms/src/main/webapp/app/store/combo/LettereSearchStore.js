Ext.define('wf.store.combo.LettereSearchStore',{
    extend: 'Ext.data.Store',
    model: 'wf.model.ComboModel',
	  data : [
            {value: 'all', name: 'Tutte'},
            {value: 'Produzione lettera di assegnazione contributo', name: 'Lettera contributo'},
            {value: 'Produzione lettera di comunicazione mandato', name: 'Lettera mandato '},
           
        ]
});
