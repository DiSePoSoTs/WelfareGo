Ext.define('wp.view.parametri.TipologieTree',{

    extend: 'Ext.tree.Panel',
    alias: 'widget.wp_tipologietree',
    id: 'wp_tipologietree',
    
    rootVisible: false,
    // autoScroll: true,
    height: 400,
    store: 'TipologieTreeStore',

    listeners: {
        
        selectionchange: function(view, selection, options) {
            
            if (selection[0]) 
            {    
                if(selection[0].get('pk'))
                {
                    // se ha una primary pk è un intervento e non una tipologia
                    Ext.getCmp('wp_tipologieform').setActiveRecord(selection[0]);
                    
                } else {
                    // se è un parametro non ha figli e resetto il form
                    Ext.getCmp('wp_tipologieform').setActiveRecord(null);
                }
            }
        }
        
        
    }
    

    
});