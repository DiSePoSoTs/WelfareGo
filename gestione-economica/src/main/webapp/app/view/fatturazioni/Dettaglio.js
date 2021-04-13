/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la schermata, nella sezione Fatturazioni,
 * per la visualizzaione di una fattura.
 */

Ext.define('Wp.view.fatturazioni.Dettaglio', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.wpfatturazionidettaglio',
    items: [
        {
            xtype: 'gridpanel',
            itemId: 'vociFatturazioni',
            store: 'fatturazioni.dettaglio.VociFatturazioni',
            height: 120,
            selType: 'cellmodel',
            columns: [
                {
                    text: 'Tipo servizio',
                    dataIndex: 'tipo_servizio',
                    flex: 1
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
                    flex: 1
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
                    flex: 1
                },
                {
                    xtype: 'wpfloatnumbercolumn',
                    text: 'Aumento',
                    dataIndex: 'aumento',
                    flex: 1
                },
                {
                    xtype: 'wpfloatnumbercolumn',
                    text: 'Variazione straordinaria',
                    dataIndex: 'variazione_straordinaria',
                    flex: 1
                },
                {                    
                    text: 'Fascia',
                    dataIndex: 'descrizioneRiduzione',
                    flex: 1
                },
                {
                    xtype: 'wpmonthcolumn',
                    text: 'Mese',
                    dataIndex: 'mese',
                    flex: 1
                }
            ]
        },
        {
            xtype: 'form',
            itemId: 'totaliFatturazioni',
            layout: 'column',
            items: [
                {
                    xtype: 'panel',
                    title: 'TOTALE FATTURA',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'wpfloatnumberfield',
                            name: 'totale_fattura',
                            anchor: '100%',
                            margin: 0,
                            readOnly: true
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: 'CONTRIBUTO',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'wpfloatnumberfield',
                            name: 'contributo',
                            anchor: '100%',
                            margin: 0,
                            readOnly: true
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: 'IVA',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'combobox',
                            name: 'iva',
                            anchor: '100%',
                            margin: 0,
                            store: 'fatturazioni.ValoriIva',
                            displayField: 'label',
                            valueField: 'id',
                            forceSelection: true,
                            allowBlank: false,
                            readOnly: true
                        }
                    ]
                },
                {
                    xtype: 'hiddenfield',
                    name: 'imponibile'
                },
                {
                    xtype: 'hiddenfield',
                    name: 'totaleFatturaConIva'
                },
                {
                    xtype: 'hiddenfield',
                    name: 'totaleFatturaSenzaIva'
                },
                {
                    xtype: 'hiddenfield',
                    name: 'importo_iva'
                },
                {
                    xtype: 'panel',
                    title: 'BOLLO',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'wpfloatnumberfield',
                            name: 'bollo',
                            anchor: '100%',
                            margin: 0,
                            readOnly: true
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: 'TOTALE DEL PERIODO',
                    layout: 'anchor',
                    border: 0,
                    columnWidth: 0.2,
                    items: [
                        {
                            xtype: 'wpfloatnumberfield',
                            name: 'totale_periodo',
                            anchor: '100%',
                            margin: 0,
                            readOnly: true
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            layout: 'column',
            border: 0,
            items: [
                {
                    xtype: 'gridpanel',
                    itemId: 'mesiPrecedenti',
                    store: 'fatturazioni.dettaglio.MesiPrecedenti',
                    height: 120,
                    title: 'MESI PRECEDENTI',
                    columnWidth: .5,
                    selType: 'cellmodel',
                    columns: [
                        {
                            xtype: 'wpmonthcolumn',
                            text: 'Mese',
                            dataIndex: 'mese',
                            flex: 1
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Importo',
                            dataIndex: 'importo',
                            flex: 1
                        },
                        {
                            text: 'Causale',
                            dataIndex: 'causale',
                            flex: 1
                        },
                        {
                            text: 'Inserimento',
                            dataIndex: 'inserimento',
                            flex: 1,
                            editor: {
                                xtype: 'combobox',
                                queryMode: 'local',
                                store: [
                                    'Si',
                                    'No'
                                ],
                                forceSelection: true,
                                allowBlank: false,
                                readOnly: true
                            }
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    itemId: 'quoteObbligati',
                    store: 'fatturazioni.dettaglio.QuoteObbligati',
                    height: 120,
                    title: 'QUOTE OBBLIGATI',
                    columnWidth: .5,
                    selType: 'cellmodel',
                    columns: [
                        {
                            text: 'Codice Fiscale',
                            dataIndex: 'codice_fiscale',
                            flex: 1
                        },
                        {
                            xtype: 'wpfloatnumbercolumn',
                            text: 'Importo',
                            dataIndex: 'importo',
                            flex: 1
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'form',
            itemId: 'datiFatturazioni',
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
                            xtype: 'textfield',
                            fieldLabel: 'Causale',
                            name: 'causale',
                            anchor: '100%'
                        },
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
                            fieldLabel: 'Da pagare',
                            name: 'da_pagare',
                            readOnly: true
                        },
                        {
                            xtype: 'datefield',
                            fieldLabel: 'Scadenza',
                            name: 'scadenza',
                            allowBlank: false,
                            format: 'd/m/Y'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: 'Codice fiscale',
                            name: 'codice_fiscale',
                            readOnly: true
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: 'Numero Fattura',
                            allowBlank: false,
                            name: 'numeroFattura',
                            readOnly: true
                        }
//            ,
//            {
//                xtype: 'combobox',
//                name: 'modalita_pagamento',
//                store: 'fatturazioni.ModalitaPagamento',
//                fieldLabel: 'Modalità di pagamento',
//                displayField: 'label',
//                valueField: 'id',
//                forceSelection: true,
//                allowBlank: false,
//                readOnly: true, 
//                hidden:true
//            }
                    ]
                }
            ]

        }
    ],
    buttons: [
        {
            text: 'Indietro',
            action: 'back'
        }
    ]
});
