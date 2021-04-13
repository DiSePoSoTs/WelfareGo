Ext.define('wp.view.parametri.BudgetTree',{

    extend: 'Ext.tree.Panel',
    alias: 'widget.wp_budgettree',
    id: 'wp_budgettree',
    
    rootVisible: false,
    autoScroll: true,
    height: 400,
    store: 'BudgetTreeStore',

    listeners: {
        selectionchange: function(view, selection, options) {
            
            if (selection[0]) 
            {
                // rimuovo tutto dalla grid out
                Ext.getCmp('wp_budgetgriduot').store.removeAll();
                
                if(selection[0].get('pk'))
                {
                    // se ha una primary pk è un intervento e non una tipologia                    
                    Ext.getCmp('wp_budgetgrid').store.load({
                        params: {
                            codTipint: selection[0].get('pk')
                        }
                    });
                    
                    Ext.getCmp('wp_budgetform').setActiveRecord(null);
                    
                    // imposto già qui il codTipint sul form altrimenti bombe
                    Ext.getCmp('wp_budgetform').getForm().findField("cod_tipint").setValue( selection[0].get('pk') );
                    
                    Ext.getCmp('wp_add_budget_btn').enable();
                    Ext.getCmp('wp_budget_form_salva').enable();
                    
                } else {
                    // se è un parametro non ha figli e resetto il form
                    Ext.getCmp('wp_budgetgrid').store.removeAll();
                    
                    Ext.getCmp('wp_add_budget_btn').disable();
                    Ext.getCmp('wp_budget_grid_uot_add').disable();
                    Ext.getCmp('wp_budget_form_salva').disable();
                    
                }
            }
        }
    }
    
});