Ext.define('wcs.store.ComuneStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.ComuneModel',
    pageSize: 10,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/comune',
        reader: {
            type: 'json',
            rootProperty: 'comune',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});