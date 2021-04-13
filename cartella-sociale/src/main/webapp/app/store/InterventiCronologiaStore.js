Ext.define('wcs.store.InterventiCronologiaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiCronologiaModel',
    pageSize: 20,
    remoteSort: false,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/cronologia',
        reader: {
            type: 'json',
            rootProperty: 'cronologia',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});