Ext.define('wcs.store.LiberatoriaSocialCrtListStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.LibertorieSocialCrtListModel',
    pageSize: 10,
    remoteSort: true,
    sorters: [ {
        property: 'dataFirma',
        direction: 'DESC'
    } ],

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/liberatorie',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});