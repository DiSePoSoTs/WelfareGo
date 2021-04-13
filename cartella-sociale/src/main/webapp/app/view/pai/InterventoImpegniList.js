// may be useless
Ext.define('wcs.view.pai.InterventoImpegniList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventoimpegnilist',
    title: 'Impegni',
    store: 'InterventiImpegniStore',
    height: 250,
    bbar: {
        xtype: 'wcs_interventoimpegnibar'
    },
    initComponent: function() {

        var wcs_rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToMoveEditor: 1,
            saveText: "Salva",
            cancelText: "Annulla",
            autoCancel: false
        });

        wcs_rowEditing.on({
            scope: this,
            afteredit: function(roweditor, changes, record, rowIndex) {
                var form = this.up('wcs_interventiform');
                var values = form.getValues();
                var quantita = values.quantita;
                var durataMesi = values.durataMesi;
                var impStdCosto = values.impStdCosto;
                var items = this.store.data.items;
                var totale = quantita * durataMesi * impStdCosto;
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    totale = totale - item.data.aCarico;
                }
                this.down('wcs_interventoimpegnibar').items.get('totaleField').setValue(totale.toFixed(2));
            }
        });

        this.columns = [{
                header: 'Anno',
                dataIndex: 'anno',
                sortable: true,
                width: 70
            },
            {
                header: 'Capitolo',
                dataIndex: 'capitolo',
                sortable: true,
                width: 80
            },
            {
                header: 'Impegno',
                dataIndex: 'impegno',
                sortable: true,
                width: 80
            },
            {
                header: 'Budget disponibile',
                dataIndex: 'importoDisponibile',
                renderer: Euro,
                sortable: false,
                flex: 1
            },
            {
                header: 'Unita\' disponibili',
                dataIndex: 'unitaDisponibili',
                sortable: false,
                flex: 1
            },
            {
                header: 'Costo previsto',
                dataIndex: 'aCarico',
                sortable: false,
                flex: 1,
                renderer: Euro,
                field: {
                    xtype: 'weuronumberfield',
                    blankText: 'Questo campo è obbligatorio',
                    allowBlank: false,
                    minValue: 0,
                    maxValue: 1000000,
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false
//                    decimalSeparator: ','
                }
            },
            {
                header: 'Budget',
                dataIndex: 'importoComplessivo',
                renderer: Euro,
                sortable: false,
                flex: 1
            },
            {
                header: 'Centro el. di costo',
                dataIndex: 'centroElementareDiCosto',
                sortable: false,
                flex: 1
            },
            {
                header: 'UOT',
                dataIndex: 'uot',
                sortable: false,
                width: 50,
                flex: 1
            } ];

        this.plugins = [wcs_rowEditing],
                this.callParent(arguments);
    }

});