Ext.define('wcs.store.NoteListStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.NoteModel',
    pageSize: 10,
    remoteSort: true,
    sorters: [ {
        property: 'dataApertura',
        direction: 'DESC'
    } ],

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/anagrafica',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    }
});