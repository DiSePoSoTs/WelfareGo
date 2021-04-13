Ext.define('wf.view.determine.DetermineContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.wf_determine_container',
    layout: 'card',
    activeItem: 0,
    fullscreen: true,
    items:[{
        xtype:'wf_determine_grid'
    }]
});