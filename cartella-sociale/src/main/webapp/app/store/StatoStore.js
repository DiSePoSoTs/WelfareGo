Ext.define('wcs.store.StatoStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.StatoModel',
    pageSize: 10,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/stato',
        reader: {
            type: 'json',
            rootProperty: 'stato',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }

});