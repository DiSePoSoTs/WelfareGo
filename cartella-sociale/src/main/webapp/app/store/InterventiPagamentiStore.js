Ext.define('wcs.store.InterventiPagamentiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiPagamentoModel',
    pageSize: 20,
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/interventi?action=pagamenti',
        reader: {
            type: 'json',
            rootProperty: 'pagamenti',
            successProperty: 'success'
        }
    }
});