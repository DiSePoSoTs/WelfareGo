Ext.define('wcs.view.pai.InterventiRicercaBeneficiari', {
    extend: 'Ext.window.Window',
    alias : 'widget.wcs_interventiRicercaBeneficiariPopup',
    title : 'Ricerca',
    layout: 'fit',
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
                        id: 'wcs_ricercaCognome',
                        fieldLabel: 'Cognome',
                        name: 'cognome',
                        anchor: '95%'
                    }, {
                        xtype:'textfield',
                        id: 'wcs_ricercaCodiceFiscale',
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
                        id: 'wcs_ricercaTelefono',
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
                    inputValue: '2',
                    disabled: true
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
                    button.up('window').close();
                    if (nome != ""  || codiceFiscale != ""  ||
                        cognome != ""  || telefono != "" ) {
                        if(this.gridResult==null){
                            var beneficiariStore = Ext.create('Ext.data.Store', {
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
                                id: 'wcs_risultatiInterventiRicercaBeneficiari',
                                store: beneficiariStore,
                                height: 300,
                                autoScroll: true,
                                columns: [{
                                    header: 'Codice anagrafica',
                                    dataIndex: 'codAnag',
                                    sortable: false,
                                    hidden:true
                                },{
                                header: 'iban',
                                dataIndex: 'iban',
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
                                    store: beneficiariStore,
                                    dock: 'bottom',
                                    displayMsg: 'Visualizzo i risultati da {0} a {1} di {2}',
                                    emptyMsg: 'Nessuna persona',
                                    displayInfo: true,
                                    items: [
                                    '-',{
                                        text: 'Aggiungi',
                                        scope: this,
                                        handler: function() {
                                            Ext.getCmp('wcs_InterventiFormBeneficiarioCod').setValue("");
                                            Ext.getCmp('wcs_InterventiFormBeneficiarioDes').setValue("");
                                            Ext.getCmp('wcs_InterventiFormInsertNewDelegato').setValue('false');
                                            var selectedRow = Ext.getCmp('wcs_risultatiInterventiRicercaBeneficiari').getSelectionModel().selected.items[0];
                                            nome = selectedRow.data.cognome + " " + selectedRow.data.nome;
                                            var cod = selectedRow.data.codAnag;
                                            var iban = selectedRow.data.iban
                                            Ext.getCmp('wcs_InterventiFormBeneficiarioCod').setValue(cod);
                                            Ext.getCmp('wcs_InterventiFormBeneficiarioDes').setValue(nome);
                                            Ext.getCmp('iban_delegato').setValue(iban);
                                            Ext.getCmp('wcs_risultatiRicercaBeneficiariWindow').close();
                                        }
                                    }
                                    ]
                                }]
                            });
                        }
                        if(this.gridWindow==null){
                            this.gridWindow = Ext.create('Ext.window.Window', {
                                id: 'wcs_risultatiRicercaBeneficiariWindow',
                                title: 'Risultati',
                                width: 700,
                                modal: true,
                                layout: 'fit',
                                autoScroll: true,
                                items: this.gridResult
                            })
                        }
                        this.gridWindow.show();
                        Ext.getCmp('wcs_risultatiInterventiRicercaBeneficiari').store.load();
                    }else {
                        Ext.Msg.alert('Attenzione!', 'Specificare almeno un criterio di ricerca.', function(){
                            Ext.widget('wcs_FamigliaSocialeRicercaPopup').close();
                            Ext.widget('wcs_FamigliaSocialeRicercaPopup').show();
                        });
                    }
                }
            }]
        }];

        this.callParent(arguments);
    }
});