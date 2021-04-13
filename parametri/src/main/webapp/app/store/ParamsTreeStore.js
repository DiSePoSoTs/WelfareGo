Ext.define('wp.store.ParamsTreeStore',{

    extend: 'Ext.data.TreeStore',
    pageSize: 20,
    remoteSort: true,

    fields: [ 
        'pk',
        'text',
        'leaf',
        'tip_param',
        'qtip'
    ],

    proxy: {
        type: 'ajax',   
        url: '/Parametri/ParametriServlet?action=LOAD'
    }
    
});