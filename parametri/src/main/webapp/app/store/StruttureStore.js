
Ext.define('wp.store.StruttureStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.StruttureModel',
    pageSize: 20,
    successProperty: 'success',
    autoLoad: false,
    

    proxy: {
        type: 'ajax',
        url : '/Parametri/StruttureServlet?action=LOAD',
        reader: {
            type: 'json',
            root: 'data'
        },
        params: {
            codTipint: null
        }

    },
    sorters: [{
        property : 'nome',
        direction: 'DESC'
    }]

});