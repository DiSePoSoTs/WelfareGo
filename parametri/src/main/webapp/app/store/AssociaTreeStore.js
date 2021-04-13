
Ext.define('wp.store.AssociaTreeStore',{

    extend: 'Ext.data.TreeStore',
    autoLoad: true,
    
    fields: [ 
    'pk',
    'text',
    'leaf',
    'qtip'
    ],

    proxy: {
        type: 'ajax',   
        url: '/Parametri/TipologieServlet?action=LOAD',
        reader: {
            type: 'json'
        }
    },
    
    listeners: {

        load: function() {

            Ext.getCmp('wp_associatree').doLayout();
            Ext.getCmp('wp_associatab').doLayout();

        }
    }

});