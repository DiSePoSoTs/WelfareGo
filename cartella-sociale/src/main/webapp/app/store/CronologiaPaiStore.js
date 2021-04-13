Ext.define('wcs.store.CronologiaPaiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.CronologiaPaiModel',
    pageSize: 10,
    remoteSort: true,

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