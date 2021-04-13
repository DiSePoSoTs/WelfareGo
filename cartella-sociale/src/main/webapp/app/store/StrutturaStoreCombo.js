Ext.define('wcs.store.StrutturaStoreCombo',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.StrutturaModel',
    remoteSort : true,
    sortOnLoad: true,
    sorters: [{
        property: 'nome',
        direction: 'ASC'
    }],
    proxy: {
        type: 'ajax',

        url: '/CartellaSociale/struttura',

        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        }
    }
});