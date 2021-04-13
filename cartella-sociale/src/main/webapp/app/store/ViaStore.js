Ext.define('wcs.store.ViaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.ViaModel',
    pageSize: 10,
    
    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/via',
        reader: {
            type: 'json',
            rootProperty: 'via',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});