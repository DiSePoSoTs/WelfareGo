Ext.define('wcs.store.AgendaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.ImpegnoModel',
    pageSize: 20,
    remoteSort: true,
    autoLoad:true,
    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/agenda',
//        extraParams: {
//            action: 'load'
//        },
        reader: {
            type: 'json',
            rootProperty: 'impegno',
            successProperty: 'success'
        }
    }
});