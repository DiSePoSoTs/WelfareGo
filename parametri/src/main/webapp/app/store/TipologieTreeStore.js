Ext.define('wp.store.TipologieTreeStore',{

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

            Ext.getCmp('wp_tipologietree').doLayout();
            Ext.getCmp('wp_tipologietab').doLayout();

        }
    }
});