Ext.define('wcs.store.DocumentiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.DocumentoModel',
    pageSize: 10,
    remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/documenti',
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        }
    }
});