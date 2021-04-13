/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la schermata, nella sezione Pagamenti, per la
 * creazione di un nuovo pagamento accessibile mediante il tasto 'Nuova pagamento'
 */

Ext.define('Wp.view.pagamenti.Nuovo' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wppagamentinuovo',
    requires: ['Wp.store.pagamenti.ModalitaErogazione'],
    border: 0,

    items: [
        {
            xtype: 'form',
            itemId: 'cercaPersona',
            border:0,
            layout: 'column',
            items: [
                {
                    xtype: 'panel',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'combo',
                            itemId: 'autocompletePersone',
                            store: 'pagamenti.nuovo.AutocompletePersone',
                            displayField: 'title',
                            hideLabel: true,
                            hideTrigger:true,
                            anchor: '100%',

                            listConfig:
                            {
                                loadingText: 'Ricerca...',
                                emptyText: 'Nessuna anagrafica trovata',

                                // Custom rendering template for each item
                                getInnerTpl: function() {
                                    return '<span id="{id}">{nome} {cognome} - {codice_fiscale}</span>';
                                }
                            },
                            pageSize: 10
                        },
                        {
                            xtype: 'component',
                            style: 'margin-top:10px',
                            html: 'Scrivi almeno 4 caratteri per avviare la ricerca.'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            itemId: 'panelPagamentoNuovo',
            layout: 'anchor',
            hidden: true,
            border: 0,
            id:'panelPagamentoNuovo',
            columnWidth: 0.2,
            items: [
                {
                    xtype: 'form',
                    itemId: 'decretoImpegno',
                    layout: 'column',
                    items: [
                        {
                            xtype: 'combobox',
                            fieldLabel: 'Impegno*',
                            itemId:'decreto_impegno',
                            id:'decreto_impegno',
                            name:'decreto_impegno',
                            store: 'pagamenti.nuovo.Decreti',
                            queryMode: 'remote',
                            displayField: 'label',
                            valueField: 'id',
                            forceSelection: true,
                            allowBlank: false
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    itemId: 'vociPagamenti',
                    store: 'pagamenti.nuovo.VociPagamenti',
                    id: 'vociPagamenti',
                    height: 180,

                    plugins: [
                        Ext.create('Ext.grid.plugin.CellEditing', {
                            clicksToEdit: 1
                        })
                    ],

                    tbar: [{
                                text: 'Aggiungi',
                                itemId: 'vociPagamenti-bottoneAggiungi',
                                iconCls: 'bottoneGrid'
                            }, {
                                text:'Rimuovi',
                                itemId: 'vociPagamenti-bottoneRimuovi',
                                iconCls: 'employee-remove',
                                disabled: true
                            }
                        ],//tbar

                    listeners: {
                        'selectionchange': function(view, records) {
                            this.down('#vociPagamenti-bottoneRimuovi').setDisabled(!records.length);
                        }
                    },//listeners

                    columns: [
                        {
                            text: 'Tipo servizio',
                            dataIndex: 'tipo_servizio',
                            flex: 1,
                            editor: {
                                xtype: 'combobox',
                                itemId:'tipoServizio',
                                id:'tipoServizioNuovo',
                                name:'tipoServizio',
                                store: 'pagamenti.nuovo.ListaTipologiaInterventiNuovo',
                                queryMode: 'remote',
                                displayField: 'label',
                                valueField: 'id',
                                forceSelection: true,
                                allowBlank: false
                            }//editor
                        },{
                            text: 'tipo_servizio_value',
                            dataIndex: 'tipo_servizio_value',
                            flex: 1,
                            hidden:true
                        },
                        {
                            text: 'Unità di misura',
                            dataIndex: 'unita_di_misura',
                            flex: 1
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Quantità',
                            dataIndex: 'quantita',
                            flex: 1,
                            editor: 'wpfloatnumberfield'
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Importo unitario',
                            dataIndex: 'importo_unitario',
                            flex: 1
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Importo dovuto',
                            dataIndex: 'importo_dovuto',
                            flex: 1
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Riduzione',
                            dataIndex: 'riduzione',
                            flex: 1,
                            editor: 'wpfloatnumberfield'
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Aumento',
                            dataIndex: 'aumento',
                            flex: 1,
                            editor: 'wpfloatnumberfield'
                        },
                        {
                            xtype: 'wpmonthcolumn',
                            text: 'Mese',
                            dataIndex: 'mese',
                            flex: 1,
                            editor: {
                                xtype: 'combobox',
                                queryMode: 'local',
                                store: 'Mese',
                                displayField: 'label',
                                valueField: 'id',
                                forceSelection: true,
                                allowBlank: false
                            }
                        }
                    ]
                },
                {
                    xtype: 'form',
                    itemId: 'datiPagamenti',
                    bodyPadding: 10,
                    layout: 'column',
                    items: [
                        {
                            xtype: 'panel',
                            border: 0,
                            bodyPadding: '0 5 0 0',
                            columnWidth: .5,
                            layout: 'anchor',
                            items: [
                                {
                                    xtype: 'textareafield',
                                    fieldLabel: 'Note',
                                    name: 'note',
                                    anchor: '100%'
                                }
                            ]
                        },
                        {
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
                            items: [
                                {
                                    xtype: 'wpfloatnumberfield',
                                    fieldLabel: 'Da liquidare',
                                    name: 'da_liquidare',
                                    readOnly: true
                                },
                                {
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
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'C.F. Beneficiario',
                                    name: 'cf_beneficiario',
                                    readOnly: true
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'Cognome delegato',
                                    name: 'cognome_delegante',
                                    readOnly: true
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'C.F. Delegato',
                                    name: 'cf_delegante',
                                    readOnly: true
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'Nome delegato',
                                    name: 'nome_delegante',
                                    readOnly: true
                                },
                                {
                                    xtype: 'hiddenfield',
                                    name: 'decreto_impegno'
                                }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            text: 'Salva',
                            action: 'save'
                        },
                        {
                            text: 'Indietro',
                            action: 'back'
                        }
                    ]
                }
            ]
        }
    ]

});

