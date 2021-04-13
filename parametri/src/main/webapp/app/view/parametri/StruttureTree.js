Ext.define('wp.view.parametri.StruttureTree',{

    extend: 'Ext.tree.Panel',
    alias: 'widget.wp_struttree',
    id: 'wp_struttree',
    
    rootVisible: false,
    autoScroll: true,
    height: 400,
    store: 'StruttureTreeStore',

    listeners: {
        selectionchange: function(view, selection, options) {
            
            if (selection[0]) 
            {
               
                
                if(selection[0].get('pk'))
                {
                   // se ha una primary pk è un intervento e non una tipologia                    
                    Ext.getCmp('wp_struttgrid').store.load({
                        params: {
                            codTipint: selection[0].get('pk')
                        }
                    });
                    
                    Ext.getCmp('wp_struttform').setActiveRecord(null);
                    
                    // imposto già qui il codTipint sul form altrimenti bombe
                    Ext.getCmp('wp_struttform').getForm().findField("cod_tipint").setValue( selection[0].get('pk') );
                    
                    Ext.getCmp('wp_add_struttura_btn').enable();
                    Ext.getCmp('wp_strutt_form_salva').enable();
                    
                } else {
                    // se è un parametro non ha figli e resetto il form
                    Ext.getCmp('wp_struttgrid').store.removeAll();
                    
                    Ext.getCmp('wp_add_strutt_btn').disable();
                 
                    Ext.getCmp('wp_strutt_form_salva').disable();
                    
                }
            }
        }
    }
    
});