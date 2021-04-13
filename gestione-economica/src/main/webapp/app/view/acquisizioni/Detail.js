
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce il dettaglio di un risultato della ricerca per la
 * sezione Acquisizione dati
 */

Ext.define('Wp.view.acquisizioni.Detail', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Wp.library.form.field.acquisizione_dati.BudgetConsuntivoEditor',
        'Wp.library.form.field.FloatNumber',
        'Ext.Date',
        'Wp.library.grid.column.FloatNumber',
        'Wp.library.grid.column.Month',
        'Wp.store.MotivazioneVariazioneSpesaStore'
    ],
    alias: 'widget.wpacquisizionidetail',
    initComponent: function() {
        this.bodyPadding = '10 0 0 0';
        this.items = [{
                xtype: 'form',
                layout: 'hbox',
                margin: '0 0 10 0',
                padding: '0 10 0 10',
                border: 0,
                defaultType: 'textfield',
                fieldDefaults: {
                    margin: '0 15 0 0'
                },
                items: [{
                        name: 'cognome',
                        readOnly: true
                    }, {
                        name: 'nome',
                        readOnly: true
                    }, {
                        name: 'tipo_intervento',
                        fieldLabel: 'Tipo intervento',
                        width: 505,
                        readOnly: true
                    }]
            }, {
                xtype: 'gridpanel',
                store: 'acquisizioni.DettaglioAcquisizioni',
                border: 0,
                height: 300,
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1,
                        listeners: {
                        	click:{
                        		  element: 'el', //bind to the underlying el property on the panel
                                  fn: function(){ console.log('click el'); }
                        	},
                            edit: function(editor, e) {
                            	
                            	if(e.field == 'codImp'){
                            		  value = e.value;
                            		  var store =  Ext.getCmp('codice_imp').getEditor().getStore();
                            		  var impegno = store.findRecord('codiceImpegno',value);
                            		  anno = impegno.data.anno;
                            		  if(e.record.get('anno')!=anno){
                            			  e.record.set('anno',anno);
                            		  }
                            		  
                            		 
                            	}
                            	
                             /*   // valida i valori di "Qt. erogata" e "Qt. beneficiata"
                                if (e.field == 'qt_erogata' && e.value < e.record.get('qt_beneficiata')) {
                                    Ext.Msg.alert('Attenzione!', 'La quantità erogata deve essere maggiore o uguale alla quantità beneficiata.');
                                    e.record.set(e.field, e.originalValue);
                                }
                                else if (e.field == 'qt_beneficiata' && e.value > e.record.get('qt_erogata')) {
                                    Ext.Msg.alert('Attenzione!', 'La quantità beneficiata deve essere minore o uguale alla quantità erogata.');
                                    e.record.set(e.field, e.originalValue);
                                }*/
                            }
                        }
                    })
                ],
                columns: [{
                	   text: 'tipo intervento',
                        id:'codice',
                        dataIndex: 'cod_tip_int',
                        width: 250,
                       hidden:true
                    },{
                        text: 'Tipo servizio',
                        dataIndex: 'tipo_servizio',
                        width: 250
                    }, {
                        xtype: 'wpmonthcolumn',
                        text: 'Mese',
                        dataIndex: 'mese_eff',
                        width: 60
                    }, {
                        text: 'Impegno',
                        columns: [{
                                text: 'codice',
                                id:'codice_imp',
                                dataIndex: 'codImp',
                                width: 60,
                                editor:{xtype:'combo',
                                	store: Ext.create('Wp.store.Impegni', {
                                		autoLoad:false,
                                		 extend: 'Ext.data.Store',
                                		 requires: ['Wp.model.ComboBoxImpegno'],
                               		      model: 'Wp.model.ComboBoxImpegno',
                                		 proxy: {
                                		        type: 'ajax',
                                		        url: wp_url_servizi.ListaImpegni,
                                		        reader: {
                                		            type: 'json',
                                		            root: 'data',
                                		            successProperty: 'success'
                                		        },
                                	            extraParams:{
                                	            	codTipInt:Ext.getCmp('tipo_intervento_acquisizioni').getValue()
                                	            }
                                		    }
                                	}),
                                	/*store:new Ext.data.ArrayStore({
                                        fields: ['abbr', 'action'],
                                        data : [                                         
                                                ['suspend', 'Suspend'],
                                                ['activate', 'Activate'],
                                                ['update', 'Update'],
                                                ['delete', 'Delete']
                                               ]
                                         }),*/
                                        displayField:'codiceImpegno',
                                        valueField: 'codiceImpegno'
                                      //  mode: 'local',
                                	
                                }
                            }, {
                                text: 'anno',
                                dataIndex: 'anno',
                                width: 40
                            }]
                    }, {
                        text: 'Costo (E)',
                        columns: [{
                                xtype: 'wpfloatnumbercolumn',
                                text: 'preventivo',
                                dataIndex: 'bdgPrevEur',
                                width: 65
                            }, {
                                xtype: 'wpfloatnumbercolumn',
                                text: 'consuntivo',
                                dataIndex: 'bdgConsEur',
                                width: 65,
                                editor: 'acqDatiBudgetConsuntivoEditor'
                            }]
                    },
                    {
                        text: 'Quantità',
                        id: 'wpAqDetQuantitaHeader',
                        columns: [{
                                id: 'wpAqDetUnitaDiMisura',
                                text: ' ',
                                dataIndex: 'unita_di_misura',
                                width: 60,
                                hidden: true
                            }, /*{
                                id: 'wpAqDetQuantitaPrevista',
                                xtype: 'wpfloatnumbercolumn',
                                text: 'Determinato',
                                dataIndex: 'qt_prevista',
                                width: 60,
                                hidden: true
                            },*/{
                                id: 'wpAqDetQuantitaBeneficiata',
                                xtype: 'wpfloatnumbercolumn',
                                text: 'Programmato',
                                dataIndex: 'qt_beneficiata',
                                width: 60,
                                editor: 'wpfloatnumberfield',
                               
                                hidden: true
                            },
                            {
                                id: 'wpAqDetQuantitaErogata',
                                xtype: 'wpfloatnumbercolumn',
                                text: 'Beneficiato(da pagare)',
                                dataIndex: 'qt_erogata',
                                width: 60,
                                editor: 'wpfloatnumberfield',
                                hidden: true
                            } ]
                    }, {
                        xtype: 'numbercolumn',
                        text: 'Assenze',
                        dataIndex: 'assenze',
                        id: 'assenze',
                        width: 60,
                        editor: {xtype: 'numberfield', allowDecimals: false},
                        format: '0'
                    },{
                        xtype: 'numbercolumn',
                        text: 'Assenze totali',
                        dataIndex: 'assenze_totali',
                        width: 60,
                        format: '0'
                    }, {
                        text: 'Liquidazione',
                        dataIndex: 'liquidazione',
                        width: 50
                                //                flex: 1
                    }, {
                        text: 'Fatturazione',
                        dataIndex: 'fatturazione',
                        width: 50
                                //                flex: 1
                    }, {
                        xtype: 'wpfloatnumbercolumn',
                        text: 'Variazione straordinaria',
                        dataIndex: 'variazione_straordinaria',
                        flex: 1,
                        editor: {
                            xtype: 'wpfloatnumberfield',
                            allowBlank: true,
                            allowNegative: true
                        }
                    }, {
                        text: 'Causale',
                        dataIndex: 'causale',
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            maxLength: 1000,
                            maxLengthText: 'Puoi inserire al massimo 1000 caratteri'
                        }
                    }, {
                        text: 'Motivazione variazione spesa',
                        dataIndex: 'motivazioneVariazioneSpesa',
                        flex: 1,
                        editor: {
                            xtype: 'combo',
                            displayField: 'label',
                            valueField: 'id',
                            forceSelection: true,
                            store: 'MotivazioneVariazioneSpesaStore'
                        },
                        renderer: function(value, metadata) {
                            var str = (value && value != '') ? Ext.getStore('MotivazioneVariazioneSpesaStore').getById(parseInt(value)).get('label') : ''
                            //                    metadata.attr = 'ext:qtip="' + str + '"';
                            return str;
                        }
                    }, {
                        text: 'Note',
                        dataIndex: 'note',
                        flex: 1,
                        editor: {
                            xtype: 'textarea',
                            maxLength: 1000,
                            maxLengthText: 'Puoi inserire al massimo 1000 caratteri'
                        }
                    }]
            }];


        this.bbar = {
            xtype: 'pagingtoolbar',
            store: 'acquisizioni.DettaglioAcquisizioni',
            displayInfo: true
                    //            displayMsg: 'visualizzati {0} - {1} di {2}'
        };

        this.buttons = [{
                text: 'Salva',
                action: 'save'
            }, {
                text: 'Salva senza acquisire',
                action: 'saveNoProgress'
            }, {
                text: 'Indietro',
                action: 'back'
            }];

        this.callParent(arguments);
    }
});


