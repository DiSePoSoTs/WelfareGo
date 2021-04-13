Ext.define('wp.store.UtentiStore',{
    
    extend: 'Ext.data.Store',
    model: 'wp.model.UtentiModel',
    
    pageSize: 16,
    successProperty: 'success',
    
    proxy: {
        type: 'ajax',
        url : '/Parametri/Utenti?action=LOAD',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }

    },
    remoteSort :true
});