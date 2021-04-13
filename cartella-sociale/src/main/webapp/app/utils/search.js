//Richiamato da popup di ricerca se utilizzato l'anagrafe comunale
function ricercaAnagrafeCom(selectedRow) {
    var codAnagComunale = selectedRow.get('codAnagComunale');
    
    disabilitaPulsantiCartellaSociale();
    Ext.getCmp('wcs_anagraficaTab').getForm().reset();
    log('loading ricercaAnagrafeCom codAnag = ', codAnagComunale);
    ricaricaAnagrafica(codAnagComunale,'anagraficaComunale');
    disabilitaTabCartellaSociale();
    Ext.getCmp('wcs_risultatiRicercaWindow').close();
    wcs_isModified = '';
}

//Richiamato da popup di ricerca se utilizzato l'anagrafe di welfarego
function ricercaAnagrafeSoc(codAnag) {
    log('loading ricercaAnagrafeSoc codAnag = ', codAnag);
    disabilitaPulsantiCartellaSociale();
    Ext.getCmp('wcs_anagraficaTab').getForm().reset();
    ricaricaAnagrafica(codAnag,'anagrafica');
    ricaricaAppuntamenti(codAnag);
    Ext.getCmp('wcs_risultatiRicercaWindow').close();
    wcs_isModified = '';
}
function ricaricaAnagraficaNoAction(codAnag) {
    log('loading anagrafe for codAna = ', codAnag, ' senza action ');
    ricaricaAnagrafica(codAnag,'anagrafica');
}
function ricaricaAnagrafica(codAnag, action) {
    log('loading anagrafe for codAna = ', codAnag, ' con action = ' + action);
    Ext.getCmp('wcs_anagraficaTab').getForm().reset();
    Ext.getCmp('wcs_anagraficaTab').getForm().load({
        url: '/CartellaSociale/popola',
        params: {
            codAnag: codAnag,
            action: action
        },
        waitMsg: 'Caricamento...',
        failure: function(form, action) {
            Ext.Msg.alert("Errore nel caricamento", action.response.statusText);
        },
        success: function(form, bb) {
            log('ricarica anagrafica form, bb.result.data:', form, bb.result.data);
            abilitaTabCartellaSociale();
            abilitaPulsantiCartellaSociale();
            setTimeout(function() { /// FIX ORRIBILE
                popolaAnagrafica(bb.result.data);
            }, 500);
        }
    });
}

//carico il pai che è stato messo in sessione da welfarego forms 
function ricaricaAnagraficaDaSessione(){
	 Ext.getCmp('wcs_anagraficaTab').getForm().reset();
	 Ext.getCmp('wcs_anagraficaTab').getForm().load({
	        url: '/CartellaSociale/popola',
	        params: {
	            action: 'prendiDaSessione'
	        },
	        waitMsg: 'Caricamento...',
	        failure: function(form, action) {
	            Ext.Msg.alert("Errore nel caricamento", action.response.statusText);
	        },
	        success: function() {
	            setTimeout(function() { /// FIX ORRIBILE
	                popolaAnagrafica();
	            }, 500);
	            abilitaTabCartellaSociale();
	            var tabAnagrafica = Ext.getCmp('wcs_anagraficaTab');
	            Ext.getCmp('wcs_cartellaTabPanel').setActiveTab(tabAnagrafica);
	           
	        }
	    });
}


function ricaricaAnagraficaDaPai(codPai) {
    Ext.getCmp('wcs_anagraficaTab').getForm().reset();
    Ext.getCmp('wcs_anagraficaTab').getForm().load({
        url: '/CartellaSociale/popola',
        params: {
            codPai: codPai,
            action: 'pai'
        },
        waitMsg: 'Caricamento...',
        failure: function(form, action) {
            Ext.Msg.alert("Errore nel caricamento", action.response.statusText);
        },
        success: function() {
            setTimeout(function() { /// FIX ORRIBILE
                popolaAnagrafica();
            }, 500);
            abilitaTabCartellaSociale();
            var tabAnagrafica = Ext.getCmp('wcs_anagraficaTab');
            Ext.getCmp('wcs_cartellaTabPanel').setActiveTab(tabAnagrafica);
        }
    });
}

function popolaAnagrafica(data) {

    // aggiorno i campi dei civici, utilizzo i dati che arrivano dal BE
    function aggiorna_civici(data){

        var cmpCivicoDomicilio = Ext.getCmp('wcs_anagraficaCivicoDomicilio');
        var civicoNoTsDomicilio = data.anagraficaCivicoNoTsDomicilio;
        var viaNoTsDomicilio = data.anagraficaViaNoTsDomicilio;
        var civicoDomicilio = data.anagraficaCivicoDomicilio;
        var viaDomicilio = data.anagraficaViaDomicilio;

        var codCivicoDomicilio;
        var codViaDomicilio;

        if(civicoNoTsDomicilio && civicoDomicilio){
            codCivicoDomicilio = civicoDomicilio;
        } else if (civicoNoTsDomicilio) {
            codCivicoDomicilio = civicoNoTsDomicilio;
        } else {
            codCivicoDomicilio = civicoDomicilio;
        }

        if(viaNoTsDomicilio && viaDomicilio){
            codViaDomicilio = viaNoTsDomicilio;
        } else if(viaNoTsDomicilio){
            codViaDomicilio = viaNoTsDomicilio;
        } else {
            codViaDomicilio = viaDomicilio;
        }

        log('codCivicoDomicilio, codViaDomicilio:', codCivicoDomicilio, codViaDomicilio);
        cmpCivicoDomicilio.loadValue(codCivicoDomicilio, codViaDomicilio);

        var cmpCivicoResidenza = Ext.getCmp('wcs_anagraficaCivicoResidenza');
        var civicoNoTsResidenza = data.anagraficaCivicoNoTsResidenza;
        var viaNoTsResidenza = data.anagraficaViaNoTsResidenza;
        var civicoResidenza = data.anagraficaCivicoResidenza;
        var viaResidenza = data.anagraficaViaResidenza;


        var codCivicoResidenza;
        var codViaResidenza;

        if(civicoNoTsResidenza && civicoResidenza){
            codCivicoResidenza = civicoResidenza;
        } else if (civicoNoTsResidenza) {
            codCivicoResidenza = civicoNoTsResidenza;
        } else {
            codCivicoResidenza = civicoResidenza;
        }

        if(viaNoTsResidenza && viaResidenza){
            codViaResidenza = viaNoTsResidenza;
        } else if(viaNoTsResidenza){
            codViaResidenza = viaNoTsResidenza;
        } else {
            codViaResidenza = viaResidenza;
        }

        log('codCivicoResidenza, codViaResidenza:', codCivicoResidenza, codViaResidenza);
        cmpCivicoResidenza.loadValue(codCivicoResidenza, codViaResidenza);

    }

    aggiorna_civici(data);

    Ext.Array.forEach([
        ['wcs_anagraficaProvinciaNascita', 'wcs_anagraficaProvinciaEsteraNascita'],
        ['wcs_anagraficaComuneDiNascita', 'wcs_anagraficaComuneEsteroDiNascita'],
        ['wcs_anagraficaProvinciaResidenza', 'wcs_anagraficaProvinciaEsteraResidenza'],
        ['wcs_anagraficaComuneResidenza', 'wcs_anagraficaComuneEsteroResidenza'],
        ['wcs_anagraficaViaResidenza', 'wcs_anagraficaViaNoTsResidenza'],
        ['wcs_anagraficaCivicoResidenza', 'wcs_anagraficaCivicoNoTsResidenza'],
        ['wcs_anagraficaProvinciaDomicilio', 'wcs_anagraficaProvinciaEsteraDomicilio'],
        ['wcs_anagraficaComuneDomicilio', 'wcs_anagraficaComuneEsteroDomicilio'],
        ['wcs_anagraficaViaDomicilio', 'wcs_anagraficaViaNoTsDomicilio'],
        ['wcs_anagraficaCivicoDomicilio', 'wcs_anagraficaCivicoNoTsDomicilio']
    ], function(field) {
        var cmp = Ext.getCmp(field[0]);
        if (cmp.getValue() == null || cmp.getValue() == "") {
            cmp.setRawValue(Ext.getCmp(field[1]).getValue());
            cmp.clearInvalid();
        }
    });



    var assSocCombo = Ext.getCmp('wcs_anagraficaAssistenteSociale');
    var codUot = Ext.getCmp('wcs_anagraficaUot').getValue();
    assSocCombo.store.getProxy().extraParams.codUot = codUot;
    assSocCombo.store.load();
    abilitaPulsantiCartellaSociale();
    wcs_isModified = '';

    var values = Ext.getCmp('wcs_anagraficaTab').getForm().getValues();
    var nameDisplay = Ext.getCmp('wcs_currentUserName');
	var isLiberatoria =  Ext.getCmp('wcs_liberatoria');
  	if(isLiberatoria.checked){
		isLiberatoria.readOnly=true;
   	}
   	else{
		isLiberatoria.readOnly=false;
    }
   
    //cambio schermata
    
    nameDisplay.setText(values.anagraficaCognome + ' ' + values.anagraficaNome);
    nameDisplay.show();
}


function ricaricaTab(idTab, codAnag) {
    if (idTab == "wcs_famigliaTab") {
        ricaricaFamiglia(codAnag);
    }
    if (idTab == "wcs_referentiTab") {
        ricaricaReferenti(codAnag);
    }
    if (idTab == "wcs_paiTab") {
        ricaricaPai(codAnag);
    }
    if (idTab == "wcs_appuntamentiTab") {
        ricaricaAppuntamenti(codAnag);
    }
    if (idTab == "wcs_condizioneTab") {
        ricaricaCondizione(codAnag);
    }
    if (idTab == "wcs_diarioTab") {
        ricaricaDiario(codAnag);
        ricaricaNote(codAnag);
    }
    if (idTab == "wcs_anagraficaTab") {
        if (codAnag != null && codAnag != "") {
            ricaricaAnagraficaNoAction(codAnag);
        }
    }
}

function ricaricaAppuntamenti(codAnag) {
    var appuntamentiGrid = Ext.getCmp('wcs_appuntamentiTab').store;
    appuntamentiGrid.proxy.extraParams = {
        codAnag: codAnag
    };
    appuntamentiGrid.load();
}

function ricaricaPai(codAnag) {
    var grid = Ext.getCmp("wcs_paiTab").items.get('wcs_paiList');
    var selection = grid.getSelectionModel().getSelection(), selectedCodPai = selection[0] ? selection[0].get('codPai') : null;
    var paiGridStore = grid.store;
    paiGridStore.proxy.extraParams = {
        codAnag: codAnag,
        type: 'singolo',
        action: 'read'
    };
    paiGridStore.on('load', function() {
        var grid = Ext.getCmp("wcs_paiTab").items.get('wcs_paiList');
        if (grid.store.totalCount > 0) {
            var record = selectedCodPai ? paiGridStore.find('codPai', selectedCodPai) : 0;
            if (record < 0 || record == -1) {
                record = 0;
            }
            log('selecting record : ', record);
            grid.getSelectionModel().select(record, false);
        } else {
            var interventiFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('interventiFieldset');
            var paiInterventiStore = interventiFieldSet.items.get('wcs_paiInterventiList').store;
            paiInterventiStore.proxy.extraParams = {
                action: 'read',
                codPai: ''
            }
            paiInterventiStore.load();

            var paiCronologiaStore = interventiFieldSet.items.get('wcs_paiCronologiaList').store;
            paiCronologiaStore.proxy.extraParams = {
                action: 'read',
                type: 'pai',
                codPai: ''
            }
            paiCronologiaStore.load();

            var paiDocumentiStore = interventiFieldSet.items.get('wcs_paiDocumentiList').store;
            paiDocumentiStore.proxy.extraParams = {
                action: 'read',
                codPai: ''
            }
            paiDocumentiStore.load();
            //Diabilito i pulsanti
            Ext.getCmp('wcs_paiFormNuovoButton').setDisabled(true);
            Ext.getCmp('wcs_nuovoPaiButton').setDisabled(false);
            Ext.getCmp('wcs_paiFormSalvaButton').setDisabled(true);
            Ext.getCmp('wcs_paiChiudiPaiButton').setDisabled(true);
            Ext.getCmp('wcs_paiFormNuovoInterventoButton').setDisabled(true);
            wcs_isModified = '';
        }
    });
    paiGridStore.load();

}

function ricaricaReferenti(codAnag) {
    var referentiGridStore = Ext.getCmp('wcs_referentiTab').items.get('wcs_cartellaReferentiGrid').store;
    referentiGridStore.proxy.extraParams = {
        codAnag: codAnag,
        action: 'read'
    };
    referentiGridStore.load();
}

function ricaricaNote(codAnag){
	 var noteGridStore = Ext.getCmp('wcs_diarioTab').items.get('noteFieldset').items.get('wcs_noteCondiviseList').store;
	    noteGridStore.proxy.extraParams = {
	        codAna: codAnag,
	        action: 'listNote'
	    };
	   noteGridStore.load();
}

function ricaricaFamiglia(codAnag) {
    var famigliaAnagraficaGridStore = Ext.getCmp('wcs_famigliaTab').items.get('wcs_famigliaAnagraficaGrid').store;
    famigliaAnagraficaGridStore.proxy.extraParams = {
        type: 'anagrafica',
        codAnag: codAnag,
        action: 'read'
    };
    famigliaAnagraficaGridStore.load();

    var famigliaSocialeGridStore = Ext.getCmp('wcs_famigliaTab').items.get('wcs_famigliaSocialeGrid').store;
    famigliaSocialeGridStore.proxy.extraParams = {
        type: 'sociale',
        codAnag: codAnag,
        action: 'read'
    };
    famigliaSocialeGridStore.load();
}

function ricaricaCondizione(codAnag) {
    Ext.getCmp('wcs_condizioneTab').getForm().reset();
    Ext.getCmp('wcs_condizioneTab').getForm().load({
        url: '/CartellaSociale/popola',
        params: {
            codAnag: codAnag,
            action: 'condizione'
        },
        waitMsg: 'Caricamento...',
        failure: function(form, action) {
            Ext.Msg.alert("Errore nel caricamento", action.response.statusText);
        },
        success: function() {
            wcs_isModified = '';
        }
    });
}

function ricaricaDiario(codAnag) {
    Ext.getCmp('wcs_diarioTab').getForm().reset();
    Ext.getCmp('wcs_diarioTab').getForm().load({
        url: '/CartellaSociale/popola',
        params: {
            codAnag: codAnag,
            action: 'diario'
        },
        waitMsg: 'Caricamento...',
        failure: function(form, action) {
            Ext.Msg.alert("Errore nel caricamento", action.response.statusText);
        },
        success: function() {
            wcs_isModified = '';
        }
    });
}

function abilitaTabCartellaSociale() {
    var condizioneTab = Ext.getCmp('wcs_condizioneTab');
    var famigliaTab = Ext.getCmp('wcs_famigliaTab');
    var referentiTab = Ext.getCmp('wcs_referentiTab');
    var paiTab = Ext.getCmp('wcs_paiTab');
    var appuntamentiTab = Ext.getCmp('wcs_appuntamentiTab');
    var diarioTab = Ext.getCmp('wcs_diarioTab');
    condizioneTab.setDisabled(false);
    famigliaTab.setDisabled(false);
    referentiTab.setDisabled(false);
    paiTab.setDisabled(false);
    appuntamentiTab.setDisabled(false);
    diarioTab.setDisabled(wcs_diarioHidden);
}

function disabilitaTabCartellaSociale() {
    var condizioneTab = Ext.getCmp('wcs_condizioneTab');
    var famigliaTab = Ext.getCmp('wcs_famigliaTab');
    var referentiTab = Ext.getCmp('wcs_referentiTab');
    var diarioTab = Ext.getCmp('wcs_diarioTab');
    var paiTab = Ext.getCmp('wcs_paiTab');
    var appuntamentiTab = Ext.getCmp('wcs_appuntamentiTab');
    condizioneTab.setDisabled(true);
    famigliaTab.setDisabled(true);
    referentiTab.setDisabled(true);
    paiTab.setDisabled(true);
    diarioTab.setDisabled(true);
    appuntamentiTab.setDisabled(true);
}

function disabilitaPulsantiCartellaSociale() {
    var cartellaRegistraContattoButton = Ext.getCmp('wcs_cartellaRegistraContattoButton');
    var cartellaFissaAppuntamentoButton = Ext.getCmp('wcs_cartellaFissaAppuntamentoButton');
    var cartellaDecessoButton = Ext.getCmp('wcs_cartellaDecessoButton');
    var cartellaStampaButton = Ext.getCmp('wcs_cartellaStampaButton');
    var cartellaRichiediApprovazioneButton = Ext.getCmp('wcs_cartellaRichiediApprovazioneButton');
    var cartellaProduciDomandaButton = Ext.getCmp('wcs_cartellaProduciDomandaButton');
    cartellaFissaAppuntamentoButton.setDisabled(true);
    cartellaRegistraContattoButton.setDisabled(true);
    cartellaDecessoButton.setDisabled(true);
    cartellaStampaButton.setDisabled(true);
    cartellaRichiediApprovazioneButton.setDisabled(true);
    cartellaProduciDomandaButton.setDisabled(true);
}

function abilitaPulsantiCartellaSociale() {
    var cartellaRegistraContattoButton = Ext.getCmp('wcs_cartellaRegistraContattoButton');
    var cartellaDecessoButton = Ext.getCmp('wcs_cartellaDecessoButton');
    var cartellaStampaButton = Ext.getCmp('wcs_cartellaStampaButton');
    var codUot = Ext.getCmp('wcs_anagraficaUot').getValue();
    var assSocValue = Ext.getCmp('wcs_anagraficaAssistenteSocialeValue').getValue();
    cartellaRegistraContattoButton.setDisabled(false);
    var cartellaFissaAppuntamentoButton = Ext.getCmp('wcs_cartellaFissaAppuntamentoButton');
    var cartellaRichiediApprovazioneButton = Ext.getCmp('wcs_cartellaRichiediApprovazioneButton');
    var cartellaProduciDomandaButton = Ext.getCmp('wcs_cartellaProduciDomandaButton');
    if (codUot != null && assSocValue != null) {
        cartellaFissaAppuntamentoButton.setDisabled(false);
        cartellaRichiediApprovazioneButton.setDisabled(false);
        cartellaProduciDomandaButton.setDisabled(false);
        cartellaDecessoButton.setDisabled(false);
    }
    cartellaStampaButton.setDisabled(false);
    var codice = Ext.getCmp('wcs_anagraficaCodiceAnagrafico').getValue();
    var codiceFiscale =  Ext.getCmp('wcs_anagraficaCodiceFiscale').getValue();
    var btn = Ext.getCmp('wcs_anagraficaVerify');
    if ((codiceFiscale!=null && codiceFiscale !="")||(codice != null && codice != "")  ) {
        btn.setDisabled(false);
    } else {
        btn.setDisabled(true);
    }
}

function showResults(button) {
    var nome = Ext.getCmp('wcs_ricercaNome').value;
    var codiceFiscale = Ext.getCmp('wcs_ricercaCodiceFiscale').value;
    var via = Ext.getCmp('wcs_ricercaVia').value;
    var cognome = Ext.getCmp('wcs_ricercaCognome').value;
    var telefono = Ext.getCmp('wcs_ricercaTelefono').value;
    var civico = Ext.getCmp('wcs_ricercaCivico').value;
    var tipoRicerca = Ext.getCmp('wcs_tipoRicerca').getValue().tipoRicerca;
    button.up('window').close();
    if (this.gridForm == null) {
        this.gridForm = Ext.create('Ext.form.Panel', {
            id: 'wcs_risultatiRicercaPanel',
            frame: true,
            layout: 'fit',
            fieldDefaults: {
                labelAlign: 'left'
            },
            items: [{
                    xtype: 'wcs_ricercaresult',
                    height: 200,
                    width: 900,
                    id: 'wcs_ricercaResultList',
                    search_store: Ext.create('Ext.data.Store', {
                        model: 'wcs.model.RisultatiRicercaModel',
                        proxy: {
                            type: 'ajax',
                            url: '/CartellaSociale/ricerca',
                            pageSize: 20,
                            extraParams: {
                                nome: nome,
                                codiceFiscale: codiceFiscale,
                                via: via,
                                cognome: cognome,
                                telefono: telefono,
                                civico: civico,
                                tipoRicerca: tipoRicerca
                            },
                            reader: {
                                type: 'json',
                                rootProperty: 'risultati',
                                successProperty: 'success'
                            }
                        }
                    }),
                    listeners: {
                        selectionchange: function(model, records) {
                            if (records[0]) {
                                this.up('form').getForm().loadRecord(records[0]);
                            }
                        }
                    }
                }, {
                    xtype: 'fieldset',
                    title: 'Dettagli',
                    defaults: {
                        labelWidth: 100
                    },
                    width: 900,
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
                                            readOnly: wcs_anagraficaSessoRO,
                                            queryMode: 'local',
                                            displayField: 'name',
                                            valueField: 'value',
                                            store: wcs_sessoStore,
                                            name: 'sesso',
                                            fieldLabel: 'Sesso'
                                        }, {
                                            fieldLabel: 'Luogo nascita',
                                            name: 'comuneNascita'
                                        }
                                    ]
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
                                        }
                                    ]
                                }]
                        }]
                }],
            buttons: [{
                    text: 'Nuova ricerca',
                    handler: function(b) {
                        b.up('window').close();
                        Ext.widget('wcs_ricercaPopup').close();
                        Ext.widget('wcs_ricercaPopup').show();
                    }
                }]
        });
    }
    if (this.gridWindow == null) {
        this.gridWindow = Ext.create('Ext.window.Window', {
            id: 'wcs_risultatiRicercaWindow',
            title: 'Risultati',
            modal: true,
            items: this.gridForm
        })
    }
    this.gridWindow.show();
    Ext.getCmp('wcs_ricercaResultList').search_store.load();
}