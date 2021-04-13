/*
 * Gestisce i dati specifici non ancora relazionati
 */
Ext.define('wp.store.AssociaDispStore',{
    
    extend: 'Ext.data.Store',
    model: 'wp.model.DatiModel',
    
    // pageSize: 20,
    successProperty: 'success',
    
    proxy: {
        type: 'ajax',
        url : '/Parametri/DatiServlet?action=LOADDISP',
        reader: {
            type: 'json',
            root: 'dati'
            // totalProperty: 'total'
        }

    }
    
});