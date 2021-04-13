Ext.define('wcs.store.InterventiImpegniStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiImpegnoModel',
    pageSize: 20,
    storeId: 'InterventiImpegniStore',
    autoload: false,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/impegni',
        extraParams: {
            action: 'read'
        },
        reader: {
            type: 'json',
            rootProperty: 'impegni',
            successProperty: 'success'
        }
    }
});