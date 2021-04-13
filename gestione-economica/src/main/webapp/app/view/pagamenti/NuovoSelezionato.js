/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la schermata, nella sezione Pagamenti, per la
 * creazione di un nuovo pagamento a partire da una entry selezionata dalla lista
 * di pagamenti "Da generare"
 */

Ext.define('Wp.view.pagamenti.NuovoSelezionato' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wppagamentinuovoselezionato',
    requires: [
        'Wp.library.form.field.acquisizione_dati.BudgetConsuntivoEditor',
        'Wp.store.pagamenti.ModalitaErogazione',
        'Wp.view.common.PagamentiGenericGrid'],

    items: [
    {
        xtype: 'form',
        itemId: 'decretoImpegno',
        layout: 'column',
        items: [
        {
            xtype: 'textfield',
            fieldLabel: 'Impegno',
            name: 'decreto_impegno',
            readOnly: true
        }
        ]
    },
    {
        xtype: 'wppagamentigenericgrid',
        itemId: 'vociPagamenti',
        store: 'pagamenti.nuovoselezionato.VociPagamenti',
        bbar:{
            xtype: 'pagingtoolbar',
            store: 'pagamenti.nuovoselezionato.VociPagamenti',
            displayInfo: true
        }
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
//                xtype: 'wpfloatnumberfield',
                xtype: 'acqDatiBudgetConsuntivoEditor',
                fieldLabel: 'Da liquidare',
                name: 'da_liquidare'
                //readOnly: true
//              editor: 'wpfloatnumberfield'
//                editor: 'acqDatiBudgetConsuntivoEditor'
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
                name: 'cf_beneficiario'
                //readOnly: true
            },
            {
                xtype: 'textfield',
                fieldLabel: 'Nome delegato',
                name: 'nome_delegante'
                //readOnly: true
            },
            {
                xtype: 'textfield',
                fieldLabel: 'C.F. Delegato',
                name: 'cf_delegante'
                //readOnly: true
            },
            {
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
            }
            
            ]
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
});

