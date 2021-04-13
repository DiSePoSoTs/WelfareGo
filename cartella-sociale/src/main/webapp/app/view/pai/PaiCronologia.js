Ext.define('wcs.view.pai.PaiCronologia',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_paicronologia',
    title: 'Cronologia PAI',
    store: 'CronologiaPaiStore',
    loadMask: true,
    invalidateScrollerOnRefresh:true, 
    	bbar: {
        xtype: 'wcs_paibar',
        store: 'CronologiaPaiStore'
    },
   minHeight: 150,

    initComponent: function() {
        this.columns = [
            {
            header: 'Evento',
            dataIndex: 'desEvento',
            sortable: true,
            flex: 1
        },
        {
            header: 'Data intervento',
            dataIndex: 'tsEvePai',
            sortable: true,
            flex: 1
        },
        {
            header: 'Intervento',
            dataIndex: 'intervento',
            sortable: false,
            flex: 1
        },
        {
            header: 'Operatore',
            dataIndex: 'operatore',
            sortable: false,
            flex: 1
        }
        ];

        this.callParent(arguments);
    }
});