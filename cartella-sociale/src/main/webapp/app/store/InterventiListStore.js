Ext.define('wcs.store.InterventiListStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiListModel',
    pageSize: 10,
    remoteSort: false,
    sorters: [ {
        property: 'dtApe',
        direction: 'DESC'
    } ],

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/interventi',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});