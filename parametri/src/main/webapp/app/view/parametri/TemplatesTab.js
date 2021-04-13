Ext.define('wp.view.parametri.TemplatesTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_templatestab',
    layout: 'border',

    items: [{
        xtype: 'wp_templatesgrid',
        height: 420,
        region: 'north'
    }, {
        xtype: 'wp_templatesform',
        region: 'center'
    }],

    afterRender: function(me) {
        
        Ext.getCmp('wp_templatesgrid').store.load();
        
    }

});

function wp_templates_toggle_lock(stato) 
{
    
    Ext.getCmp('wp_templatesform').form.findField("cod_tmpl").setReadOnly(stato);
    
}