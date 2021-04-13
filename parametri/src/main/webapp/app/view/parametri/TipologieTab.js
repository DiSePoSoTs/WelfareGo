Ext.define('wp.view.parametri.TipologieTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_tipologietab', 
    id: 'wp_tipologietab',
    layout: 'border',

    items: [{
        xtype: 'wp_tipologietree',            
        width: 370,
        region: 'west'
    }, {
        xtype: 'wp_tipologieform',
        region: 'center'
    }]

});

function wp_tipologie_toggle_lock(stato) 
{   
    Ext.getCmp('wp_tipologieform').form.findField("cod_tipint").setReadOnly(stato);
}