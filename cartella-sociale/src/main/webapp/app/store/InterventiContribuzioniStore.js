Ext.define('wcs.store.InterventiContribuzioniStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiContribuzioneModel',
    pageSize: 20,
    storeId: 'InterventiContribuzioniStore',
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/interventi',
        extraParams:{
            action:'contribuzioni'  
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        }
    }
});