Ext.define('wcs.store.AppuntamentiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.AppuntamentoModel',
    pageSize: 60,
    remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/appuntamenti',
        reader: {
            type: 'json',
            rootProperty: 'appuntamenti',
            successProperty: 'success'
        }
    }
});