Ext.define('wp.view.parametri.AssociaTree',{

    extend: 'Ext.tree.Panel',
    alias: 'widget.wp_associatree',
    id: 'wp_associatree',
    
    rootVisible: false,
    autoScroll: true,
    height: 400,
    store: 'AssociaTreeStore',

    listeners: {
        
        selectionchange: function(view, selection, options) {
            
            if (selection[0]) 
            {    

                // ho selezionato una foglia
                if(selection[0].get('pk'))
                {
                    
                    // carico le due grid con i dati specifici in contesto
                    // se ha una primary pk è un intervento e non una tipologia                    
                    Ext.getCmp('wp_associadispgrid').store.load({
                        params: {
                            codTipint: selection[0].get('pk')
                        }
                    });
                    // se ha una primary pk è un intervento e non una tipologia                    
                    Ext.getCmp('wp_associarelgrid').store.load({
                        params: {
                            codTipint: selection[0].get('pk')
                        }
                    });
                    
                } else {

                    // rimuovo tutte le righe dalle due grid se non ho selezioni
                    Ext.getCmp('wp_associadispgrid').store.removeAll();
                    Ext.getCmp('wp_associarelgrid').store.removeAll();                    

                }
            }
        }
        
        
    }
    

    
});