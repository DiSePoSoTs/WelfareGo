/*
 * Gestisce i dati specifici per la schermata di gestione
 */
Ext.define('wp.store.DatiStore',{
    
    extend: 'Ext.data.Store',
    model: 'wp.model.DatiModel',
    
    pageSize: 20,
    successProperty: 'success',
    remoteSort:true,
    
    proxy: {
        type: 'ajax',
        url : '/Parametri/DatiServlet?action=LOAD',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }

    }
    
});