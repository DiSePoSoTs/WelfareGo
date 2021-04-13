/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la schermata, nella sezione Pagamenti, per la
 * visualizzazione di un pagamento già presente nella base di dati.
 */

Ext.define('Wp.view.pagamenti.Modifica' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wppagamentimodifica',
    requires: ['Wp.store.pagamenti.ModalitaErogazione'],

    items: [{
        xtype: 'form',
        itemId: 'decretoImpegno',
        layout: 'column',
        items: [{
            margin: 5,
            xtype: 'textfield',
            fieldLabel: 'Decreto d\'impegno',
            name: 'decreto_impegno',
            readOnly: true
        }]
    },{
        xtype: 'gridpanel',
        itemId: 'vociPagamenti',
        store: 'pagamenti.dettaglio.VociPagamenti',
        selType: 'cellmodel',
        plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
        columns: [{
            text: 'Causale',
            dataIndex: 'causale',
            flex: 1
        },{
            text: 'Unità di misura',
            dataIndex: 'unita_di_misura',
            flex: 1
        },{
            xtype: 'wpfloatnumbercolumn',
            text: 'Quantità',
            dataIndex: 'quantita',
            flex: 1
        },{
            xtype: 'numbercolumn',
            text: 'Assenze totali',
            dataIndex: 'assenze_totali',
            flex: 1,
            format: '0'
        },{
            xtype: 'numbercolumn',
            text: 'Assenze mensili',
            dataIndex: 'assenze_mensili',
            flex: 1,
            format: '0'
        },{
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo unitario',
            dataIndex: 'importo_unitario',
            flex: 1
        },{
            xtype: 'wpfloatnumbercolumn',
            text: 'Importo dovuto',
            dataIndex: 'importo_dovuto',
            flex: 1
        },{
            xtype: 'wpfloatnumbercolumn',
            text: 'Riduzione',
            dataIndex: 'riduzione',
            flex: 1,
            editor: 'wpfloatnumberfield'
        },{
            xtype: 'wpfloatnumbercolumn',
            text: 'Aumento',
            dataIndex: 'aumento',
            flex: 1,
            editor: 'wpfloatnumberfield'
        },{
            xtype: 'wpmonthcolumn',
            text: 'Mese',
            dataIndex: 'mese',
            flex: 1
        }],
        bbar:{
            xtype: 'pagingtoolbar',
            store: 'pagamenti.dettaglio.VociPagamenti',
            displayInfo: true
        }
    },{
        xtype: 'form',
        itemId: 'datiPagamenti',
        bodyPadding: 10,
        layout: 'column',
        items: [{
            xtype: 'panel',
            border: 0,
            bodyPadding: '0 5 0 0',
            columnWidth: .5,
            layout: 'anchor',
            items: [{
                xtype: 'textareafield',
                fieldLabel: 'Note',
                name: 'note',
                anchor: '100%'
            }]
        },{
            xtype: 'panel',
            border: 0,
            bodyPadding: '0 0 0 5',
            columnWidth: .5,
            layout: {
                type: 'table',
                columns: 2,
                tableAttrs: {
                    style: {
                        width: '100%'
                    }
                }
            },
            items: [{
                xtype: 'acqDatiBudgetConsuntivoEditor',
                fieldLabel: 'Da liquidare',
                name: 'da_liquidare',
                readOnly: true
            },{
                xtype: 'combobox',
                queryMode: 'local',
                fieldLabel: 'Modalità di erogazione*',
                name: 'modalita_erogazione',
                itemId: 'modalita_erogazione',
                allowBlank: false,
                store: 'pagamenti.ModalitaErogazione',
                displayField: 'label',
                valueField: 'id',
                forceSelection: true
            },{
                xtype: 'textfield',
                fieldLabel: 'C.F. Beneficiario',
                name: 'cf_beneficiario'
                //readOnly: true
            },{
                xtype: 'textfield',
                fieldLabel: 'Nome delegato',
                name: 'nome_delegante'
                //readOnly: true
            },{
                xtype: 'textfield',
                fieldLabel: 'C.F. delegato',
                name: 'cf_delegante'
                //readOnly: true
            },{
                xtype: 'textfield',
                fieldLabel: 'Cogome delegato',
                name: 'cognome_delegante'
                //readOnly: true
            },
            {
                xtype: 'textfield',
                fieldLabel: 'Iban su cui accreditare',
                name: 'iban_beneficiario'
                //readOnly: true
            }]
        }]

    }],

    buttons: [{
        text: 'Salva',
        action: 'save'
    },{
        text: 'Indietro',
        action: 'back'
    }]
});

