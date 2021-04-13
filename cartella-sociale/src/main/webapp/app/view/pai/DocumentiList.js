Ext.define('wcs.view.pai.DocumentiList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_documentilist',
    title: 'Documenti',
    loadMask: true,
    store: 'DocumentiStore',
    bbar: {
        xtype: 'wcs_paibar',
        store: 'DocumentiStore'
    },
    minHeight: 150,

    initComponent: function() {

        this.columns = [
        {
            header: 'Codice documento',
            dataIndex: 'idDocumento',
            sortable: false,
            hidden:true,
            flex: 1
        },
        {
            header: 'Data',
            dataIndex: 'dtDoc',
            sortable: true,
            width: 100
        },
        {
            header: 'Documento',
            dataIndex: 'nomeFile',
            sortable: true,
            flex: 1
        },
        {
            header: 'Intervento',
            dataIndex: 'paiIntervento',
            sortable: false,
            flex: 1
        },
        {
            header: 'Tipologia',
            dataIndex: 'tipologia',
            sortable: false,
            flex: 1
        },
        {
            header: 'Autore',
            dataIndex: 'codUteAut',
            sortable: false,
            flex: 1
        },
        {
            header: 'Versione',
            dataIndex: 'ver',
            sortable: true,
            width: 70
        },{
            header: 'Download documento',
            xtype:'actioncolumn',
            width:120,
            align: 'center',
            items: [
            {
                iconCls: 'wcs_azione_icon_aa',
                tooltip: 'Download documento',
                handler: function(grid, rowIndex, colIndex) {
                                            var idDocumento = grid.getStore().getAt(rowIndex).data.idDocumento;
                                            var downloadUrl = document.location.origin + '/CartellaSociale/downloadDocumento?idDocumento=' + idDocumento;
                                            window.open(downloadUrl, '_blank');
                }
            }]
        }
        ];

        this.callParent(arguments);
    }

});