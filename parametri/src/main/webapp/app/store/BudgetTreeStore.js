
Ext.define('wp.store.BudgetTreeStore',{

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
            
            Ext.getCmp('wp_budgettree').doLayout();
            Ext.getCmp('wp_budgettab').doLayout();
            
        }
        
    }

});