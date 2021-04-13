Ext.define('Wp.view.common.PagamentiGenericGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wppagamentigenericgrid',
    selType: 'cellmodel',
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    columns: [{
            text: 'Causale',
            dataIndex: 'causale',
            flex: 1
        }, {
            xtype: 'wpmonthcolumn',
            text: 'Mese',
            dataIndex: 'mese',
            width: 60
        }, {
            text: 'Impegno',
            columns: [{
                    text: 'codice',
                    dataIndex: 'codImp',
                    width: 60
                }, {
                    text: 'anno',
                    dataIndex: 'anno',
                    width: 40
                }]
        }, {
            text: ' ',
            dataIndex: 'unita_di_misura',
            width: 60,
            hidden: true
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'prevista',
            dataIndex: 'quantita',
            width: 60,
            hidden: true
        },
        {
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo unitario',
            dataIndex: 'importo_unitario',
            flex: 1,
            hidden: true
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo preventivato',
            dataIndex: 'importo_preventivato',
            flex: 1
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo consuntivato',
            dataIndex: 'importo_consuntivato',
            flex: 1
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo dovuto',
            dataIndex: 'importo_dovuto',
            flex: 1
        }, {
            xtype: 'numbercolumn',
            text: 'Assenze totali',
            dataIndex: 'assenze_totali',
            flex: 1,
            format: '0'
        }, {
            xtype: 'numbercolumn',
            text: 'Assenze mensili',
            dataIndex: 'assenze_mensili',
            flex: 1,
            format: '0'
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Riduzione',
            dataIndex: 'riduzione',
            flex: 1
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Aumento',
            dataIndex: 'aumento',
            flex: 1
        }, {
            xtype: 'wpfloatnumbercolumn',
            text: 'Variazione straordinaria',
            dataIndex: 'variazione_straordinaria',
            flex: 1
        },{
            text: 'Note',
            dataIndex: 'note',
            flex: 1
        }, {
            text: 'causale',
            dataIndex: 'causale_variazione_straordinaria',
            flex: 1
        }]
});