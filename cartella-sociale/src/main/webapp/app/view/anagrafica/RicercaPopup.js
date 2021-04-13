Ext.define('wcs.view.anagrafica.RicercaPopup', {
    extend: 'Ext.window.Window',
    alias: 'widget.wcs_ricercaPopup',
    title: 'Ricerca',
    layout: 'fit',
    modal: true,
    width: 700,
    autoHeight:true,
    autoShow: true,
    initComponent: function() {

        var getFormRicerca = function(values) {

            var store = Ext.create('Ext.data.Store', {
                model: 'wcs.model.RisultatiRicercaModel',
                pageSize: 10,
                remoteSort: true,
                autoLoad: true,
                proxy: {
                    type: 'ajax',
                    url: '/CartellaSociale/ricerca',
                    extraParams: values,
                    reader: {
                        type: 'json',
                        rootProperty: 'risultati',
                        totalProperty: 'totalCount',
                        successProperty: 'success'
                    },
                    listeners: {
                        exception: function(proxy, response) {
                            var message = Ext.JSON.decode(response.responseText);
                            if (message) {
                                message = message.message;
                            } else {
                                message = "errore generico";
                            }
                            var config = {
                                title: "Errore",
                                msg: "Si è verificato un errore durante il caricamento dei dati:<br/>" + message,
                                buttons: Ext.Msg.OK
                            };
                            Ext.Msg.show(config);
                        }
                    }
                },
                listeners: {
                    load: function() {
                        if (values.openDirectly && store.data.length == 1) {
                            selezionaRecord();
                        }
                    }
                }
            });

            var selezionaRecord = function() {
                var grid = Ext.getCmp('wcs_ricercaResultList');
                var selectedRow = grid.getSelectionModel().selected.items[0] || store.data.first();
                var codAnag = selectedRow.get('codAnag');
                var tipoAnagrafe = selectedRow.get('tipoAnagrafe');
                if (tipoAnagrafe == 'AU') {
                    ricercaAnagrafeSoc(codAnag);
                }
                if (tipoAnagrafe == 'AN') {
                    ricercaAnagrafeCom(selectedRow);
                }
				
				/*le due funzioni sopra ricercaAnagrafeSoc e ricercaAnagrafeCom
				chiudono la form di ricerca, dov'è presente il componente
				grigliaFamigliaAnagrafica
				Patch DOTCOM 2017/10/24 verifica l'esistenza del componente
				prima di svuotare lo store*/
				if (Ext.getCmp('grigliaFamigliaAnagrafica') !== undefined)
				{
					Ext.getCmp('grigliaFamigliaAnagrafica').store.removeAll();
				}
            }

            var form = Ext.create('Ext.form.Panel', {
                id: 'wcs_risultatiRicercaPanel',
                frame: true,
                fieldDefaults: {
                    labelAlign: 'left'
                },
                items: [{
                        xtype: 'grid',
                        loadMask: {
                            msg: "Caricamento..."
                        },
                        defaults: {
                            labelWidth: 75
                        },
                        remoteSort: true,
                        store: store,
                        columns: [
                            {
                                header: 'AN',
                                dataIndex: 'tipoAnagrafe',
                                sortable: true,
                                width: 50
                            },
                            {
                                header: 'PA',
                                dataIndex: 'posizioneAnagrafica',
                                sortable: true,
                                width: 65
                            },
                            {
                                header: 'Cognome',
                                dataIndex: 'cognome',
                                sortable: true,
                                flex: 1
                            },
                            {
                                header: 'Nome',
                                dataIndex: 'nome',
                                sortable: true,
                                flex: 1
                            },
                            {
                                header: 'Nato il',
                                dataIndex: 'dataNascita',
                                sortable: true,
                                xtype: 'datecolumn',
                                format: 'd/m/Y',
                                width: 90
                            },
                            {
                                header: 'Morto il',
                                dataIndex: 'dataMorte',
                                sortable: true,
                                xtype: 'datecolumn',
                                format: 'd/m/Y',
                                width: 90
                            },
                            {
                                header: 'Indirizzo',
                                dataIndex: 'desViaResidenza',
                                sortable: true,
                                flex: 1
                            },
                            {
                                header: 'Codice fiscale',
                                dataIndex: 'codiceFiscale',
                                sortable: true,
                                width: 120
                            },
                            {
                                header: 'Coniuge',
                                dataIndex: 'nomeConiuge',
                                sortable: true,
                                flex: 1
                            },
                            {
                                header: 'UOT',
                                dataIndex: 'uot',
                                sortable: true,
                                width: 50
                            },
                            {
                                header: 'Comune nascita',
                                dataIndex: 'comuneNascita',
                                sortable: true,
                                flex: 1
                            }],
                        bbar: {
                            xtype: 'pagingtoolbar',
                            store: store,
                            items: ['-', {
                                    xtype: 'label',
                                    text: 'ricerca:  '
                                }, Ext.create('Ext.form.field.Text', {
                                    enableKeyEvents: true,
                                    value: values.ricercaRapida,
                                    listeners: {
                                        specialkey: function(field, e) {
                                            if (e.getKey() == e.ENTER) {
                                                store.proxy.extraParams.ricercaRapida = field.getValue();
                                                store.load();
                                            }
                                        }
                                    }
                                })]
                        },

                     	height:258,

	                    id: 'wcs_ricercaResultList',
	                    listeners: {
	                        itemdblclick: function() {
	                            selezionaRecord();
	                        },
	                        selectionchange: function(model, records) {
	                            if (records[0]) {
	                                this.up('form').getForm().loadRecord(records[0]);
	                                var store = Ext.getCmp('grigliaFamigliaAnagrafica').store;
	                                store.proxy.extraParams ={
	                                		action:'read',
	                                		codAnaCom:records[0].data.codAnagComunale,
	                                		 type:'anagraficaRicerca'
	                                };
	                                store.load();
	                            }
	                        }
	                    }
                    }, {
                        xtype: 'fieldset',
                        title: 'Dettagli',
                        defaults: {
                            labelWidth: 50
                        },
                        items: [{
                                xtype: 'container',
                                anchor: '100%',
                                layout: 'column',
                                items: [{
                                        xtype: 'container',
                                        columnWidth: .3,
                                        layout: 'anchor',
                                        defaultType: 'textfield',
                                        items: [{
                                                xtype: 'combo',
                                                queryMode: 'local',
                                                displayField: 'name',
                                                valueField: 'value',
                                                store: wcs_sessoStore,
                                                name: 'sesso',
                                                fieldLabel: 'Sesso'
                                            }, {
                                                fieldLabel: 'Luogo nascita',
                                                name: 'comuneNascita'
                                            }]
                                    }, {
                                        xtype: 'container',
                                        columnWidth: .3,
                                        layout: 'anchor',
                                        defaultType: 'textfield',
                                        items: [{
                                                xtype: 'datefield',
                                                format: 'd/m/Y',
                                                fieldLabel: 'Data decesso',
                                                name: 'dataMorte'
                                            }, {
                                                xtype: 'combo',
                                                displayField: 'name',
                                                valueField: 'value',
                                                store: wcs_anagraficaCittadinanzaStore,
                                                fieldLabel: 'Cittadinanza',
                                                name: 'cittadinanza'
                                            }, {
                                                fieldLabel: 'CAP',
                                                name: 'capResidenza'
                                            }]
                                    }, {
                                        xtype: 'container',
                                        columnWidth: .3,
                                        layout: 'anchor',
                                        defaultType: 'textfield',
                                        items: [{
                                                fieldLabel: 'Indirizzo',
                                                name: 'desViaResidenza'
                                            }, {
                                                fieldLabel: 'Cognome e nome coniuge',
                                                name: 'nomeConiuge'
                                            }, {
                                                fieldLabel: 'Codice fiscale',
                                                name: 'codiceFiscale'
                                            }]
                                    }
                                    	
                                  ]
                                  
                            }]
                    },
                    
                    {
                    	
                        xtype: 'grid',
                        id:'grigliaFamigliaAnagrafica',
                        
                        defaults: {
                            labelWidth: 75
                        },
                        remoteSort: true,
                        autoHeight:true,
                        store: 'FamigliaAnagraficaStore',
                        title:'Nucleo familiare anagrafe comunale',
                      
                        columns: [{
                            header: 'Cognome',
                            dataIndex: 'cognome',
                            sortable: true,
                            flex: 1
                        },
                        {
                            header: 'Nome',
                            dataIndex: 'nome',
                            sortable: true,
                            flex: 1
                        },
                        {
                            header: 'Stato di nascita',
                            dataIndex: 'statoNascita',
                            sortable: false,
                            hidden: true,
                            flex: 1
                        },
                        {
                            header: 'Codice fiscale',
                            dataIndex: 'codiceFiscale',
                            sortable: true,
                            width: 120
                        },
                        {
                            header: 'Comune di nascita',
                            dataIndex: 'comuneNascitaDes',
                            sortable: false,
                            flex: 1
                        },
                        {
                            header: 'Sesso',
                            dataIndex: 'sesso',
                            hidden: false,
                            sortable: true,
                            width: 50
                        },
                        {
                            header: 'Data di nascita',
                            dataIndex: 'dataNascita',
                            hidden: false,
                            sortable: true,
                            width: 80
                        }]}
                    
                    
                    ],
                buttons: [{
                        text: 'Nuova ricerca',
                        handler: function(btn) {
                        	Ext.getCmp('grigliaFamigliaAnagrafica').store.removeAll();
                        	btn.up('window').close();
                           
                            Ext.widget('wcs_ricercaPopup').close();
                            Ext.widget('wcs_ricercaPopup').show();
                        }
                    }, {
                        text: 'Seleziona',
                        handler: function() {
                        	Ext.getCmp('grigliaFamigliaAnagrafica').store.removeAll();
                            selezionaRecord();
                        }
                    }]
            });



            return form;
        };

        var doSearch = function(values) {

            var gridForm = getFormRicerca(values);

            var gridWindow = Ext.create('Ext.window.Window', {
                id: 'wcs_risultatiRicercaWindow',
                title: 'Risultati',
                layout: 'fit',

                width: 900,
                modal: true,
                items: gridForm
            })

            gridWindow.show();
        };

        this.items = [{
                xtype: 'form',
                frame: true,
                id: 'wcs_ricercaForm',
                items: [{
                        xtype: 'container',
                        anchor: '100%',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        fieldLabel: 'Cognome',
                                        tabIndex: 1000,
                                        name: 'cognome',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: 'Codice fiscale',
                                        tabIndex: 1003,
                                        name: 'codiceFiscale',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: 'Comune di residenza',
                                        id: 'wcs_ricercaComuneResidenza',
                                        name: 'comune',
                                        tabIndex: 1005,
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: 'Data di nascita',
                                        id: 'wcs_ricercaDataNascita',
                                        name: 'dataNascita',
                                        tabIndex: 1007,
                                        regex: /[0-9][0-9][/][0-9][0-9][/][0-9][0-9][0-9][0-9]/,
                                        allowBlank: true,
                                        regexText: 'formato data non valido (gg/mm/aaaa)',
                                        anchor: '95%'
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        fieldLabel: 'Nome',
                                        tabIndex: 1002,
                                        name: 'nome',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: 'Telefono',
                                        tabIndex: 1004,
                                        name: 'telefono',
                                        id: 'wcs_ricercaTelefonoInPopup',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        fieldLabel: 'Via di residenza',
                                        id: 'wcs_ricercaViaResidenza',
                                        tabIndex: 1006,
                                        name: 'via',
                                        anchor: '95%'
                                    }]
                            }]
                    }, {
                        id: 'wcs_ricercaForm_ricercaRapida',
                        xtype: 'textfield',
                        fieldLabel: 'Ricerca rapida',
                        tabIndex: 999,
                        name: 'ricercaRapida',
                        anchor: '97%'
                    }, {
                        xtype: 'radiogroup',
                        fieldLabel: 'Ricerca in',
                        columns: 1,
                        id: 'wcs_tipoRicerca',
                        vertical: true,
                        items: [{
                                boxLabel: 'Archivio Welfarego',
                                name: 'tipoRicerca',
                                inputValue: '1',
                                checked: true,
                                tabIndex: 1008,
                                handler: function(component, checked) {
                                    if (checked) {
                                        Ext.getCmp('wcs_ricercaComuneResidenza').setDisabled(false);
                                        Ext.getCmp('wcs_ricercaViaResidenza').setDisabled(false);
                                        Ext.getCmp('wcs_ricercaForm_ricercaRapida').setDisabled(false);
                                        Ext.getCmp('wcs_ricercaTelefonoInPopup').setDisabled(false);
                                    }
                                }
                            }, {
                                boxLabel: 'Anagrafe comunale',
                                name: 'tipoRicerca',
                                inputValue: '2',
                                tabIndex: 1009,
                                handler: function(component, checked) {
                                    if (checked) {
                                        Ext.getCmp('wcs_ricercaComuneResidenza').setDisabled(true);
                                        Ext.getCmp('wcs_ricercaComuneResidenza').setValue('');
                                        Ext.getCmp('wcs_ricercaViaResidenza').setDisabled(true);
                                        Ext.getCmp('wcs_ricercaViaResidenza').setValue('');
                                        Ext.getCmp('wcs_ricercaForm_ricercaRapida').setDisabled(true);
                                        Ext.getCmp('wcs_ricercaForm_ricercaRapida').setValue('');
                                        Ext.getCmp('wcs_ricercaTelefonoInPopup').setDisabled(true);
                                        Ext.getCmp('wcs_ricercaTelefonoInPopup').setValue('');
                                    }
                                }
                            }]
                    }],
                listeners: {
                    afterrender: function(thisForm, options) {
                        var form = thisForm;

                        this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                            enter: function(event) {
                                doSearch(form.getValues());
                                form.up('window').close();
                            }
                        });

                    }
                },
                buttons: [
                    Ext.create('Ext.ux.VirtualKeyboardButton', {
                        id: 'wcs_keyboardRicercaAnagrafica'
                    }), {
                        text: 'Cerca e Apri',
                        id: 'wcs_ricercaOpenDirectly',
                        gridForm: null,
                        gridWindow: null,
                        handler: function(button) {
                            var values = button.up('form').getValues();
                            values.openDirectly = true;
                            doSearch(values);
                            button.up('window').close();
                        }
                    }, {
                        text: 'Cerca',
                        id: 'wcs_ricercaOpenGrid',
                        gridForm: null,
                        gridWindow: null,
                        handler: function(button) {
                            var values = button.up('form').getValues();
                            doSearch(values);
                            button.up('window').close();
                        }
                    }]
            }];

        this.listeners = {
            afterrender: function() {
                Ext.getCmp('wcs_keyboardRicercaAnagrafica').fireEvent('afterrender');

                Ext.getCmp('wcs_ricercaForm_ricercaRapida').focus();
            }
        }
        this.callParent(arguments);
    }
});