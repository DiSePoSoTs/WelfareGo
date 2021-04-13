Ext.define('wcs.store.InterventiSocialCrtListStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.InterventiSocialCrtListModel',
    pageSize: 10,
    remoteSort: true,
    sorters: [ {
        property: 'dataApertura',
        direction: 'DESC'
    } ],


    listeners : {
            load: function(store, records, success, operation) {
                console.log("records.length:");
                console.log(records.length);
                console.log("store");
                console.log(store);
                console.log(records);
                console.log("success", success);
                console.log(operation);

                if(records.length == 0){
                    console.log("non ho ricevuto dati dal server, pulisco lo store.");
                    records.removeAll();
                }
            }
    },


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