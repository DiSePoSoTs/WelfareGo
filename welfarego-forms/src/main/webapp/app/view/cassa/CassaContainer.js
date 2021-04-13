Ext.define('wf.view.cassa.CassaContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.wf_cassa_container',
    layout: 'anchor',
    fullscreen: true,
    items:[{
        itemId:'wcs_daLiquidareList',
        id:'wcs_daLiquidareList',
        xtype:'wf_liquidare_grid',
        height:300
        
    },{
        itemId:'wcs_logCassa',
        id:'wcs_logCassa',
        xtype:'wf_logCassa_grid',
        height:300
    }
    ]
 
});
