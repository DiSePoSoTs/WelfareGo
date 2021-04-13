Ext.define('wcs.view.famiglia.FamigliaSocialeRicercaPopup', {
    extend: 'Ext.window.Window',
    alias : 'widget.wcs_FamigliaSocialeRicercaPopup',
    title : 'Ricerca',
    layout: 'fit',
    modal: true,
    autoScroll: true,
    width: 700,
    initComponent: function() {
        this.items= [{
            xtype: 'form',
            frame: true,
            items: [
            {
                xtype: 'container',
                anchor: '100%',
                layout:'column',
                items:[{
                    xtype: 'container',
                    columnWidth:.5,
                    layout: 'anchor',
                    items: [{
                        xtype:'textfield',
                        fieldLabel: 'Cognome',
                        name: 'cognome',
                        anchor: '95%'
                    }, {
                        xtype:'textfield',
                        fieldLabel: 'Codice fiscale',
                        name: 'codiceFiscale',
                        anchor: '95%'
                    }]
                },{
                    xtype: 'container',
                    columnWidth:.5,
                    layout: 'anchor',
                    items: [{
                        xtype:'textfield',
                        fieldLabel: 'Nome',
                        name: 'nome',
                        anchor: '95%'
                    }, {
                        xtype:'textfield',
                        fieldLabel: 'Telefono',
                        name: 'telefono',
                        anchor: '95%'
                    }]
                }]
            },
            {
                xtype: 'radiogroup',
                fieldLabel: 'Ricerca in',
                columns: 1,
                vertical: true,
                items: [
                {
                    boxLabel: 'Archivio utenti',
                    name: 'tipoRicerca',
                    inputValue: '1',
                    checked: true
                },

                {
                    boxLabel: 'Anagrafe comunale',
                    name: 'tipoRicerca',
                    inputValue: '2'
                }
                ]
            }],

            buttons: [{
                text: 'Cerca',
                gridResult:null,
                gridWindow:null,
                handler: function(button) {
                    var form = this.up('form').getForm();
                    var values = form.getValues();
                    var nome = values['nome'];
                    var codiceFiscale = values['codiceFiscale'];
                    var cognome=  values['cognome'];
                    var telefono = values['telefono'];
                    var tipoRicerca = values['tipoRicerca'];
                    var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                    button.up('window').close();
                    if(this.gridResult==null){
                        var ricercaStore = Ext.create('Ext.data.Store', {
                            model: 'wcs.model.RisultatiRicercaModel',
                            remoteSort: true,
                            proxy: {
                                type: 'ajax',
                                url: '/CartellaSociale/ricerca',
                                pageSize: 20,
                                extraParams: {
                                    nome: nome,
                                    codiceFiscale: codiceFiscale,
                                    cognome: cognome,
                                    telefono: telefono,
                                    exclude: codAnag,
                                    tipoRicerca: tipoRicerca
                                },
                                reader: {
                                    type: 'json',
                                    rootProperty: 'risultati',
                                    totalProperty: 'totalCount',
                                    successProperty: 'success'
                                }
                            }
                        });
                        this.gridResult = Ext.create('Ext.grid.Panel', {
                            id: 'wcs_risultatiFamigliaSocialeRicerca',
                            store: ricercaStore,
                            height: 300,
                            autoScroll: true,
                            columns: [{
                                header: 'Codice anagrafica',
                                dataIndex: 'codAnag',
                                sortable: false,
                                hidden:true
                            },
                            {
                                header: 'Codice anagrafica comunale',
                                dataIndex: 'codAnagComunale',
                                sortable: false,
                                hidden:true
                            },
                            {
                                header: 'AN',
                                dataIndex: 'tipoAnagrafe',
                                sortable: false,
                                flex:1
                            },{
                                header: 'Cognome',
                                dataIndex: 'cognome',
                                sortable: true,
                                flex:1
                            },
                            {
                                header: 'Nome',
                                dataIndex: 'nome',
                                sortable: true,
                                flex:1
                            },{
                                header: 'Codice fiscale',
                                dataIndex: 'codiceFiscale',
                                sortable: true,
                                flex:1
                            }],
                            dockedItems: [{
                                xtype: 'pagingtoolbar',
                                store: ricercaStore,
                                dock: 'bottom',
                                displayInfo: true,
                                items: ['-',{
                                    text: 'Aggiungi come',
                                    scope: this,
                                    handler: function(a, b) {
                                        var selectedRow = Ext.getCmp('wcs_risultatiFamigliaSocialeRicerca').getSelectionModel().selected.items[0];
                                        var tipoAnagrafe = selectedRow.get('tipoAnagrafe');
                                        var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                                        var codQualField=Ext.getCmp('wcs_popupNuovoFamiliare_tipoQualifica');
                                        var codQual= codQualField.getValue();
                                        if(!codQualField.isValid()){
//                                            Ext.Msg.alert('Errore',"E' necessario indicare il tipo di relazione");
                                            return;
                                        }
                                        Ext.getCmp('wcs_famigliaSocialeForm').getForm().reset();
                                        if (tipoAnagrafe == 'AU'){
                                            var codAnagFamiliare = selectedRow.data.codAnag;
                                            Ext.getCmp('wcs_famigliaSocialeForm').getForm().load({
                                                url: '/CartellaSociale/famiglia',
                                                params: {
                                                    codAnag: codAnag,
                                                    codAnagFamiliare: codAnagFamiliare,
                                                    action: 'readForm',
                                                    codQual:codQual
                                                },
                                                waitMsg: 'Caricamento...',
                                                failure: function(form, action) {
                                                    Ext.Msg.alert("Errore nel caricamento", action.result.errorMessage);
                                                }
                                            });
                                        } else if (tipoAnagrafe == 'AN') {
                                            var codAnagComunale = selectedRow.data.codAnagComunale;
                                            Ext.getCmp('wcs_famigliaSocialeForm').getForm().load({
                                                url: '/CartellaSociale/famiglia',
                                                params: {
                                                    codAnag: codAnag,
                                                    codAnagComunale: codAnagComunale,
                                                    action: 'readForm',
                                                    codQual:codQual
                                                },
                                                waitMsg: 'Caricamento...',
                                                failure: function(form, action) {
                                                    Ext.Msg.alert("Errore nel caricamento", action.result.errorMessage);
                                                }
                                            });
                                        }
                                        Ext.getCmp('wcs_risultatiRicercaFamigliareWindow').close();
                                    }
                                },{
                                    xtype:'combo',
                                    id:'wcs_popupNuovoFamiliare_tipoQualifica',
                                    store:'famigliaQualificaParentelaComboStore',
                                    displayField: 'name',
                                    valueField: 'value',
                                    allowBlank: false
                                }]
                            }]
                        });
                    }
                    if(this.gridWindow==null){
                        this.gridWindow = Ext.create('Ext.window.Window', {
                            id: 'wcs_risultatiRicercaFamigliareWindow',
                            title: 'Risultati',
                            width: 700,
                            modal: true,
                            layout: 'fit',
                            autoScroll: true,
                            items: this.gridResult
                        })
                    }
                    this.gridWindow.show();
                    Ext.getCmp('wcs_risultatiFamigliaSocialeRicerca').store.load();
                }
            }]
        }];

        this.callParent(arguments);
    }
});