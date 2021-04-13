Ext.define('wf.view.lettere.LettereContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.wf_lettere_container',
    layout: 'anchor',
  //  fullscreen: true,
   height:'auto',
    items:[{
        itemId:'wcs_lettereList',
        id:'wcs_lettereList',
        xtype:'wf_lettere_grid'
       
        
    }
    ]
 
});
