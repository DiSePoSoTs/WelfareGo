Ext.define('wp.store.GruppiStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.GruppiModel',
    pageSize: 20,
    successProperty: 'success',
	
    autoLoad: true,
    autoSync: true,

    proxy: {
        type: 'ajax',
        url: '/Parametri/TipologieServlet?action=LOADGRUPPI',

        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data',
            messageProperty: 'message'
        }
    }
});