Ext.define('wcs.store.FamigliaSocialeStoreCombo',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.FamigliaModel',
   
    //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/famiglia',
      
        reader: {
            type: 'json',
            rootProperty: 'famiglia',
            successProperty: 'success'
        }
    }
});