Ext.define('wp.view.parametri.ParamsTree',{
    extend: 'Ext.tree.Panel',
    alias: 'widget.wp_paramstree',
    id: 'wp_paramstree',    
    rootVisible: false,
    autoScroll: true,
    store: 'ParamsTreeStore',
    listeners: {
        selectionchange: function(view, selection, options) {            
            if (selection[0]) {    
                if(selection[0].get('pk')){
                    // se ha una primary pk è un intervento e non una tipologia
                    Ext.getCmp('wp_paramsform').setActiveRecord(selection[0]);
                    wp_parametri_toggle_lock(true); // blocco i campi
                } else {
                    // se è un parametro non ha figli e resetto il form
                    Ext.getCmp('wp_paramsform').setActiveRecord(null);
                    wp_parametri_toggle_lock(false); // sblocco i campi
                }
            }
        }
    },
    bbar: [{
        xtype: 'button', 
        text: 'refresh', 
        handler:function(){
            Ext.getStore('ParamsTreeStore').load();
        }
    }]
});