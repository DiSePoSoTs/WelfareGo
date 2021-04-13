Ext.define('wcs.view.pai.InterventoContribuzioniList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventocontribuzionilist',
    title: 'Fatture',
    store: 'InterventiContribuzioniStore',
    loadMask: true,
    bbar: { xtype: 'wcs_interventocontribuzionibar'},

    initComponent: function() {
        this.columns = [
        {
            header: 'Data',
            dataIndex: 'data',
            xtype: 'datecolumn', 
            format:'d/m/Y',
            sortable: true,
            flex: 1
        },
        {
            header: 'Importo',
            dataIndex: 'importo',
            renderer: Euro,
            sortable: true,
            flex: 1
        },
        {
            header: 'ID Fattura',
            dataIndex: 'idFattura',
            sortable: true,
            flex: 1
        },
        {
            header: 'Fattura',
            dataIndex: 'fattura',
            sortable: true,
            flex: 1
        },
        {
            header: 'Pagato',
            dataIndex: 'pagato',
            renderer: Euro,
            sortable: true,
            flex: 1
        }
        ];

        this.callParent(arguments);
    }
});