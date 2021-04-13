Ext.define('wf.store.combo.TaskSearchStore',{
    extend: 'Ext.data.Store',
    model: 'wf.model.ComboModel',
	  data : [
            {value: 'AUTO', name: 'Ricerca rapida'},
            {value: 'COGNOME_UTENTE', name: 'Cognome utente'},
            {value: 'PROTOCOLLO', name: 'Protocollo'},
            {value: 'NUMERO_PAI', name: 'Numero pai'},
            {value: 'DATA_APERTURA', name: 'Data apertura'}/*,
            {value: 'UOT', name: 'UOT'}*/
        ]
});
