Ext.define('wcs.store.InterventiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventoModel',
    pageSize: 20,
    remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/interventi',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});