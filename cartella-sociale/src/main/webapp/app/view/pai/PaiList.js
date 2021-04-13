Ext.define('wcs.view.pai.PaiList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_pailist',
    store: 'PaiStore',
    autoScroll: true,
    loadMask: true,

    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })
    ],
    initComponent: function() {
        this.columns = [
            {
                header: 'Codice PAI',
                dataIndex: 'codPai',
                sortable: true,
                width: 70,
                field: {
                    xtype: 'textfield',
                    readOnly: true
                }
            },
            {
                header: 'Stato PAI',
                dataIndex: 'statoPai',
                sortable: true,
                width: 70
            },
            {
                header: 'Cognome',
                dataIndex: 'cognome',
                sortable: true,
                flex: 1
            }, {
                header: 'Nome',
                dataIndex: 'nome',
                sortable: true,
                flex: 1
            }, {
                header: 'Data apertura',
                dataIndex: 'dtApePai',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                sortable: true,
                width: 90
            }, {
                header: 'Data chiusura',
                dataIndex: 'dtChiusPai',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                sortable: true,
                width: 90
            }
        ];

        var singoloButtonConfig = {
            xtype: 'button',
            text: 'Singolo',
            enableToggle: true,
            listeners: {
                click: function(button) {
                    var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                    
                    var paiGridStore = Ext.getStore("PaiStore");
                    if (this.getText() != 'Singolo') {
                        this.setText('Singolo');
                        paiGridStore.proxy.extraParams = {
                            codAnag: codAnag,
                            type: 'singolo',
                            action: 'read'
                        };
                    } else {
                        this.setText('Famiglia');
                        paiGridStore.proxy.extraParams = {
                            codAnag: codAnag,
                            type: 'famiglia',
                            action: 'read'
                        };
                    }
                    paiGridStore.load();
                }
            }
        };

        this.dockedItems = [{
                xtype: 'toolbar',
                items: [singoloButtonConfig]
            }];

        this.bbar = {
            xtype: 'wcs_paibar',
            store: 'PaiStore'
   
        };

        this.callParent(arguments);
    }
});