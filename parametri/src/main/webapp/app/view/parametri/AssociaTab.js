Ext.define('wp.view.parametri.AssociaTab',{

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_associatab',
    layout: 'border',

    items: [
        
        {xtype: 'wp_associatree', width: 370, region: 'west'},
        {xtype: 'panel', region: 'center', layout: 'border', items:[
            { xtype: 'wp_associadispgrid', region:'north', height: 300 },
            { xtype: 'wp_associarelgrid', region:'center' }
        ]}
        
    ],

    afterRender: function(me) {
        
    }

});