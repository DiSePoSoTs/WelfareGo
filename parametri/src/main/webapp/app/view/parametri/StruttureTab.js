Ext.define('wp.view.parametri.StruttureTab',{
    
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_struttab',
    layout: 'border',
    id: 'wp_struttab',
    
    items: [{
        xtype: 'wp_struttree', 
        width: 370, 
        region: 'west'
    },{
        xtype: 'panel', 
        region: 'center', 
        layout: 'border', 
        items:[{
            xtype: 'panel', 
            region: 'north', 
            layout: 'column', 
            height: 260, 
            items: [{
                xtype: 'wp_struttgrid', 
                columnWidth: 1, 
                height: 260
            }]
        },{
            xtype: 'wp_struttform', 
            region: 'center'
        }]
    }]    
    
});