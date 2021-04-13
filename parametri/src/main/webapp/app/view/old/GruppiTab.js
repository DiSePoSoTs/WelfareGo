Ext.define('wp.view.parametri.GruppiTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_gruppitab',
    layout: 'column',

    items: [{
        xtype: 'panel',
        width: 300,
        items: [{
            xtype: 'wp_gruppigrid',
            bodyPadding: 10
        }]
    }, {
        xtype: 'wp_gruppiform',
        columnWidth: 1.0
    }],

    afterRender: function(me) {
        
        Ext.getCmp('wp_gruppigrid').store.load();
        
    }
    
    
});

function wp_gruppi_toggle_lock(stato) 
{   
    Ext.getCmp('wp_gruppiform').form.findField("cod_grp_tipint").setReadOnly(stato);
}