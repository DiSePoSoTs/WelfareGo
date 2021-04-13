Ext.define('wcs.view.referenti.ReferentiRicercaPopup', {
    extend: 'Ext.window.Window',
    alias : 'widget.wcs_ReferentiRicercaPopup',
    title : 'Ricerca',
    modal: true,
    autoShow: true,
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
                    if (nome != ""  || codiceFiscale != ""  ||
                        cognome != ""  || telefono != "" ) {
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
                                        tipoRicerca: tipoRicerca,
                                        exclude: codAnag
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
                                id: 'wcs_risultatiReferentiRicerca',
                                store: ricercaStore,
                                autoScroll: true,
                                height: 300,
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
                                    sortable: false
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
                                    items: [
                                    '-',{
                                        text: 'Aggiungi',
                                        scope: this,
                                        handler: function(a, b) {
                                            var selectedRow = Ext.getCmp('wcs_risultatiReferentiRicerca').getSelectionModel().selected.items[0];
                                            var codAnagFamiliare = selectedRow.data.codAnag;
                                            var tipoAnagrafe = selectedRow.get('tipoAnagrafe');
                                            var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                                            Ext.getCmp('wcs_referentiForm').getForm().reset();
                                            if (tipoAnagrafe == 'AU'){
                                                Ext.getCmp('wcs_referentiForm').getForm().load({
                                                    url: '/CartellaSociale/referenti',
                                                    params: {
                                                        codAnag: codAnag,
                                                        codAnagFamiliare: codAnagFamiliare,
                                                        action: 'readForm'
                                                    },
                                                    waitMsg: 'Caricamento...',
                                                    failure: function(form, action) {
                                                        Ext.Msg.alert("Errore nel caricamento", action.result.errorMessage);
                                                    }
                                                });
                                            } else if (tipoAnagrafe == 'AN') {
                                                var codAnagComunale = selectedRow.data.codAnagComunale;
                                                Ext.getCmp('wcs_referentiForm').getForm().load({
                                                    url: '/CartellaSociale/referenti',
                                                    params: {
                                                        codAnag: codAnag,
                                                        codAnagComunale: codAnagComunale,
                                                        action: 'readForm'
                                                    },
                                                    waitMsg: 'Caricamento...',
                                                    failure: function(form, action) {
                                                        Ext.Msg.alert("Errore nel caricamento", action.result.errorMessage);
                                                    }
                                                });
                                            }
                                            Ext.getCmp('wcs_risultatiRicercaReferentiWindow').close();
                                        }
                                    }
                                    ]
                                }]
                            });
                        }
                        if(this.gridWindow==null){
                            this.gridWindow = Ext.create('Ext.window.Window', {
                                id: 'wcs_risultatiRicercaReferentiWindow',
                                title: 'Risultati',
                                width: 700,
                                modal: true,
                                layout: 'fit',
                                autoScroll: true,
                                items: this.gridResult
                            })
                        }
                        this.gridWindow.show();
                        Ext.getCmp('wcs_risultatiReferentiRicerca').store.load();
                    } else {
                        Ext.Msg.alert('Attenzione!', 'Specificare almeno un criterio di ricerca.', function(){
                            Ext.widget('wcs_ReferentiRicercaPopup').close();
                            Ext.widget('wcs_ReferentiRicercaPopup').show();
                        });
                    }
                }
            }]
        }];

        this.callParent(arguments);
    }
});