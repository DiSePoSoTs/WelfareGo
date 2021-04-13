Ext.define('wp.view.parametri.ListeTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_listetab',
    layout: 'border',

    items: [
    {
        xtype: 'wp_listegrid',
        width: 300,
        region: 'west'
    },
    {
        xtype: 'wp_listeform',
        region: 'center'
    }],

    afterRender: function(me) {
        
        Ext.getCmp('wp_listegrid').store.load();
        
    }
    
});

function wp_liste_toggle_lock(stato) 
{   
    Ext.getCmp('wp_tipologieform').form.findField("cod_lista_att").setReadOnly(stato);
}