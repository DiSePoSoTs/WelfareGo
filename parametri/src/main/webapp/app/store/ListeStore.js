
Ext.define('wp.store.ListeStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.ListeModel',
    pageSize: 20,
    successProperty: 'success',
    autoLoad: true,
    

    proxy: {
        type: 'ajax',
        url : '/Parametri/ListeServlet?action=LOAD',
        reader: {
            type: 'json',
            root: 'data'
        }

    }

});