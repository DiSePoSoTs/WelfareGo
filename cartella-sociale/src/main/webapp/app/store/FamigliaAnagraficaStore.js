Ext.define('wcs.store.FamigliaAnagraficaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.FamigliaModel',
    pageSize: 10,
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/famiglia',
        extraParams: {
            action: 'read',
            type: 'anagrafica'
        },
        reader: {
            type: 'json',
            rootProperty: 'famiglia',
            successProperty: 'success'
        }
    }
});