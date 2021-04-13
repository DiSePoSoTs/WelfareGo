Ext.define('wp.view.parametri.BudgetTab',{
    
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_budgettab',
    layout: 'border',
    id: 'wp_budgettab',
    
    items: [{
        xtype: 'wp_budgettree', 
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
                xtype: 'wp_budgetgrid', 
                columnWidth: 0.6, 
                height: 260
            },{
                xtype: 'wp_budgetgriduot', 
                columnWidth: 0.4, 
                height: 260
            }]
        },{
            xtype: 'wp_budgetform', 
            region: 'center'
        }]
    }]    
    
});