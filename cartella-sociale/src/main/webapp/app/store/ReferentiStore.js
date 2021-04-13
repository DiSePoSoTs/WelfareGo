Ext.define('wcs.store.ReferentiStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.ReferentiModel',
    pageSize: 10,
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/referenti',
        extraParams: {
            action: 'read'
        },
        reader: {
            type: 'json',
            rootProperty: 'referenti',
            successProperty: 'success'
        }
    }
});