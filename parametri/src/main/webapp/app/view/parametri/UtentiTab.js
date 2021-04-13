Ext.define('wp.view.parametri.UtentiTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_utentitab',
    layout: 'border',

    items: [{
        xtype: 'wp_utentigrid',
        region: 'north',
        height: 365
    }, {
        xtype: 'wp_utentiform',
        region: 'center',
        autoheight:true
    }],

    afterRender: function(me) {
        
        Ext.getCmp('wp_utentigrid').store.load();
        
    }


});