Ext.define('wf.store.combo.CassaSearchStore',{
    extend: 'Ext.data.Store',
    model: 'wf.model.ComboModel',
	  data : [
            {value: 'UTENTE', name: 'Utente'},
            {value: 'DATA_OPERAZIONE', name: 'Data operazione'}
            
        ]
});
