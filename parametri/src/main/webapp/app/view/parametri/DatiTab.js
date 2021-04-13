Ext.define('wp.view.parametri.DatiTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_datitab',
    layout: 'border',

    items: [{
        xtype: 'wp_datigrid',
        region: 'north',
        height: 365
    }, {
        xtype: 'wp_datiform',
        region: 'center'
    }],

    afterRender: function(me) {
        
        Ext.getCmp('wp_datigrid').store.load();
        
    }
});

function wp_dati_toggle_lock(stato) 
{   
    Ext.getCmp('wp_datiform').form.findField('cod_campo').setReadOnly(stato);
}