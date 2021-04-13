Ext.define('wp.view.parametri.ParamsTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_paramstab',
    layout: 'border',

    items: [{
        xtype: 'wp_paramstree',
        width: 370,
        region: 'west'
    }, {
        xtype: 'wp_paramsform',
        region: 'center'
    }]
//,
//
//    afterRender: function(me) {
//        
//        Ext.getCmp('wp_paramstree').store.load();
//        
//    }
    
    
});

function wp_parametri_toggle_lock(stato) 
{   
    Ext.getCmp('wp_paramsform').form.findField("tip_param").setReadOnly(stato);
    Ext.getCmp('wp_paramsform').form.findField("cod_param").setReadOnly(stato);
}