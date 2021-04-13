Ext.define('wcs.view.agenda.Container',{
    extend: 'Ext.Panel',
    alias: 'widget.wcs_agenda_container',
    autoHeight:true,
    layout: 'border',
    initComponent: function() {
        this.items =  [
            {
            title: 'Calendario',
            region:'west',
            xtype: 'wcs_agenda_date_picker',
            margins: '5 0 0 5',
            width: 200,
            collapsible: true,
            layout: 'fit',
            dataViewId:'wcs_agenda_data_view'
        }
        ,{
            title: 'Settimana',
            items:{
                xtype:'wcs_agenda_data_view',
                id:'wcs_agenda_data_view'
            },
            region: 'center',
            xtype: 'panel',
            layout: 'fit',
            margins: '5 5 0 0'
        },{
            region: 'south',
            xtype: 'wcs_agenda_detail',
            id: 'wcs_agenda_detail',
            margins: '0 5 5 5',
            dataViewId:'wcs_agenda_data_view'
        }
    ];

        this.callParent(arguments);
    }
});