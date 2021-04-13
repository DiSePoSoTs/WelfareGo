Ext.define('wcs.store.FamigliaSocialeStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.FamigliaModel',
    pageSize: 20,
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/famiglia',
        extraParams: {
            action: 'read',
            type: 'sociale'
        },
        reader: {
            type: 'json',
            rootProperty: 'famiglia',
            successProperty: 'success'
        }
    }
});