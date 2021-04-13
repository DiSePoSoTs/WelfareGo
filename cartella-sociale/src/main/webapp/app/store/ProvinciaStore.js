Ext.define('wcs.store.ProvinciaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.ProvinciaModel',
    pageSize: 10,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/provincia',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});