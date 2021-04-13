/*
 * Gestisce i dati relazionati con le tipologie
 */
Ext.define('wp.store.AssociaRelStore',{
    
    extend: 'Ext.data.Store',
    model: 'wp.model.DatiModel',
    
    // pageSize: 20,
    successProperty: 'success',
    
    proxy: {
        type: 'ajax',
        url : '/Parametri/DatiServlet?action=LOADREL',
        reader: {
            type: 'json',
            root: 'dati'
            // totalProperty: 'total'
        }

    }
    
});