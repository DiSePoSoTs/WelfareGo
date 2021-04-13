
Ext.define('wp.store.StruttureTreeStore',{

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
            
           Ext.getCmp('wp_struttree').doLayout();
           Ext.getCmp('wp_struttab').doLayout();
            
        }
        
    }

});