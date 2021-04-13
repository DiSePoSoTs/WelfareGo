Ext.define('wcs.view.anagrafica.AnagraficaForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_anagraficaform',
    title: 'Anagrafica',
    bodyStyle: 'padding:5px 5px 0',
    frame: true,
    autoScroll: false,
    defaults: {
        labelWidth: 100
    },
    initComponent: function() {
        this.reader = Ext.create('Ext.data.reader.Reader', {
            model: 'wcs.model.AnagraficaModel',
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        });
        Ext.override(Ext.LoadMask, {
            onHide: function() {
                 this.callParent();
            }
       });

        this.items = [{
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                items: [{
                        xtype: 'hiddenfield',
                        id: 'wcs_anagraficaCodAna',
                        name: 'codAna'
                    },
                    {
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        items: [{
                                xtype: 'datefield',
                                format: 'd/m/Y',
                                readOnly: true,
                                id: 'wcs_anagraficaDataAperturaCartella',
                                fieldLabel: 'Data apertura cartella',
                                name: 'anagraficaDataAperturaCartella',
                                maxValue: new Date(),
                                tabIndex: 1,
                                anchor: '95%'
                            },
                            {
                                xtype: 'combo',
                                displayField: 'name',
                                valueField: 'value',
                                store: wcs_anagraficaUOTStore,
                                typeAhead: true,
                                queryMode: 'local',
                                forceSelection: true,
                                id: 'wcs_anagraficaUot',
                                readOnly: wcs_anagraficaUOTRO,
                                fieldLabel: 'UOT',
                                tabIndex: 3,
                                name: 'anagraficaUot',
                                listeners: {
                                    'select': function(combo, record) {
                                        var assCombo = Ext.getCmp('wcs_anagraficaAssistenteSociale');
                                        assCombo.clearValue();
                                        assCombo.store.getProxy().extraParams.codUot = this.getValue();
                                        assCombo.store.load();
                                    }
                                },
                                anchor: '95%'
                            },
                            {
                                xtype: 'textfield',
                                id: 'wcs_anagraficaCodiceAnagrafico',
                                fieldLabel: 'Codice anagrafico',
                                tabIndex: 5,
                                name: 'anagraficaCodiceAnagrafico',
                                anchor: '95%'
                            },{
                                xtype: 'textfield',
                                readOnly: wcs_anagraficaCognomeRO,
                                fieldLabel: '<b>Cognome</b>*',
                                tabIndex: 7,
                                maxLength: 255,
                                maxLengthText: 'Lunghezza massima 255 caratteri',
                                allowBlank: false,
                                blankText: 'Questo campo è obbligatorio',
                                id: 'wcs_anagraficaCognome',
                                name: 'anagraficaCognome',
                                anchor: '95%'
                            },  {
                                xtype: 'combo',
                                displayField: 'name',
                                valueField: 'value',
                                store: wcs_anagraficaCittadinanzaStore,
                                readOnly: wcs_anagraficanazionalitaRO,
                                typeAhead: true,
                                id: 'wcs_anagraficaCittadinanza',
                                fieldLabel: 'Cittadinanza*',
                                forceSelection: true,
                                listeners: {
                                    select: {
                                        fn: function(component, record) {
                                            var stato = record[0].data.name.toUpperCase();
                                            if (stato != 'ITALIA') {
                                                Ext.getCmp('wcs_anagraficaCodiceFiscale').clearInvalid();
                                                Ext.getCmp('wcs_anagraficaForzaCodiceFiscale').setDisabled(false);
                                            } else {
                                                Ext.getCmp('wcs_anagraficaForzaCodiceFiscale').setDisabled(true);
                                                Ext.getCmp('wcs_anagraficaForzaCodiceFiscale').checked = false;
                                            }
                                        }
                                    }
                                },
                                tabIndex: 9,
                                queryMode: 'local',
                                allowBlank: false,
                                blankText: 'Questo campo è obbligatorio',
                                name: 'anagraficaCittadinanza',
                                anchor: '95%'
                            }, {
                                xtype: 'combo',
                                displayField: 'name',
                                valueField: 'value',
                                tabIndex: 11,
                                typeAhead: true,
                                queryMode: 'local',
                                forceSelection: true,
                                store: wcs_anagraficaPosizioneStore,
                                readOnly: wcs_anagraficaPosizioneAnagraficaRO,
                                fieldLabel: 'Posizione anagrafica',
                                id: 'wcs_anagraficaPosizioneAnagrafica',
                                name: 'anagraficaPosizioneAnagrafica',
                                anchor: '95%'
                            }, {
                                xtype: 'textfield',
                                readOnly: wcs_anagraficaCognomeConiugeRO,
                                fieldLabel: 'Cognome coniuge',
                                maxLength: 255,
                                tabIndex: 13,
                                maxLengthText: 'Lunghezza massima 255 caratteri',
                                id: 'wcs_anagraficaCognomeConiuge',
                                name: 'anagraficaCognomeConiuge',
                                anchor: '95%'
                            },{
                                xtype: 'checkbox',
                                name: 'anagraficaLiberatoria',
                                id: 'wcs_liberatoria',
                                tabIndex: 15,
                                fieldLabel: 'Ha firmato liberatoria'
                             
                              } ]
                    },
                    {
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        items: [{
                                xtype: 'combo',
                                readOnly: wcs_anagraficaSegnalatoRO,
                                displayField: 'name',
                                valueField: 'value',
                                typeAhead: true,
                                tabIndex: 2,
                                store: wcs_anagraficaSegnalatoDaStore,
                                fieldLabel: 'Segnalato da',
                                id: 'wcs_anagraficaSegnalatoDa',
                                name: 'anagraficaSegnalatoDa',
                                anchor: '95%'
                            },
                            {
                                xtype: 'combo',
                                displayField: 'name',
                                valueField: 'value',
                                forceSelection: true,
                                tabIndex: 4,
                                store: new Ext.data.Store({
                                    model: 'wcs.model.AssistenteSocialeModel',
                                    listeners: {
                                        load: {
                                            fn: function(a, b, c, d) {
                                                var assSocValue = Ext.getCmp('wcs_anagraficaAssistenteSocialeValue').getValue();
                                               
                                                if (assSocValue != null && assSocValue != '') {
                                                    var assSocCombo = Ext.getCmp('wcs_anagraficaAssistenteSociale');
                                                    assSocCombo.setValue(assSocValue);
                                                    wcs_isModified = '';
                                                }
                                            }
                                        }
                                    }
                                }),
                                readOnly: wcs_anagraficaAssistenteSocialeRO,
                                id: 'wcs_anagraficaAssistenteSociale',
                                fieldLabel: 'Assistente sociale',
                                name: 'anagraficaAssistenteSociale',
                                anchor: '95%'
                            }, {
                                xtype: 'hiddenfield',
                                name: 'anagraficaAssistenteSocialeValue',
                                id: 'wcs_anagraficaAssistenteSocialeValue'
                            },{
                                xtype: 'combo',
                                displayField: 'name',
                                valueField: 'value',
                                forceSelection: true,
                                tabIndex: 8,
                                store: new Ext.data.Store({
                                    model: 'wcs.model.EducatoreModel',
                                    autoLoad:true,
                                    listeners: {
                                        load: {
                                            fn: function(a, b, c, d) {
                                                var educatoreValue = Ext.getCmp('wcs_anagraficaEducatoreValue').getValue();
                                               
                                                if (educatoreValue != null && educatoreValue != '') {
                                                    var educatoreCombo = Ext.getCmp('wcs_anagraficaEducatore');
                                                    educatoreCombo.setValue(educatoreValue);
                                                }
                                            }
                                        }
                                    }
                                }),
                                readOnly: wcs_anagraficaAssistenteSocialeRO,
                                id: 'wcs_anagraficaEducatore',
                                fieldLabel: 'Altro operatore',
                                name: 'anagraficaEducatore',
                                anchor: '95%'
                            }, {
                                xtype: 'hiddenfield',
                                name: 'anagraficaEducatoreValue',
                                id: 'wcs_anagraficaEducatoreValue'
                            },
                            {
                                xtype: 'textfield',
                                fieldLabel: 'Codice nucleo familiare',
                                tabIndex: 6,
                                id: 'wcs_anagraficaCodiceNucleoFamigliare',
                                name: 'anagraficaCodiceNucleoFamiliare',
                                anchor: '95%'
                            }, {
                                xtype: 'textfield',
                                readOnly: wcs_anagraficaNomeRO,
                                id: 'wcs_anagraficaNome',
                                maxLength: 255,
                                tabIndex: 10,
                                maxLengthText: 'Lunghezza massima 255 caratteri',
                                fieldLabel: '<b>Nome</b>*',
                                allowBlank: false,
                                blankText: 'Questo campo è obbligatorio',
                                name: 'anagraficaNome',
                                anchor: '95%'
                            }, {
                                xtype: 'combo',
                                readOnly: wcs_anagraficaSessoRO,
                                queryMode: 'local',
                                displayField: 'name',
                                valueField: 'value',
                                tabIndex: 12,
                                typeAhead: true,
                                store: wcs_sessoStore,
                                fieldLabel: 'Sesso*',
                                id: 'wcs_anagraficaSesso',
                                allowBlank: false,
                                blankText: 'Questo campo è obbligatorio',
                                name: 'anagraficaSesso',
                                anchor: '95%'
                            }, {
                                xtype: 'hiddenfield',
                                id: 'wcs_anagraficaComuneDiNascitaDes',
                                name: 'anagraficaComuneDiNascitaDes'
                            }, {
                                xtype: 'container',
                                layout: 'column',
                                anchor: '95%',
                                items: [{
                                        xtype: 'container',
                                        columnWidth: .6,
                                        layout: 'anchor',
                                        items: [{
                                                xtype: 'textfield',
                                                readOnly: wcs_anagraficaCodiceFiscaleRO,
                                                name:'anagraficaCodiceFiscale',
                                                id: 'wcs_anagraficaCodiceFiscale',
                                                fieldLabel: 'Codice fiscale',
                                                tabIndex: 12,
                                                allowBlank: true,
                                                listeners: {
                                                    change: {
                                                        fn: function(a, b, c, d) {
                                                            var stato = Ext.getCmp('wcs_anagraficaCittadinanza').rawValue.toUpperCase();
                                                            if (stato != 'ITALIA') {
                                                                this.vtype = '';
                                                                this.allowBlank = true;
                                                            } else {
                                                                this.vtype = 'CodiceFiscale';
                                                            }
                                                        }
                                                    }
                                                },
                                                validator: function(value) {
                                                	if(value=="" || value == null){
                                                		return true
                                                	}
                                                    if (/^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][a-zA-Z0-9]{3}[a-zA-Z]$/.test(value)) {
                                                        return true;
                                                    } else {
                                                        return 'Deve essere un codice fiscale corretto';
                                                    }
                                                },
                                                name: 'anagraficaCodiceFiscale'
                                            }]
                                    }, {
                                        xtype: 'container',
                                        columnWidth: .4,
                                        layout: 'anchor',
                                        items: [{
                                                xtype: 'checkbox',
                                                fieldLabel: 'Forzatura',
                                                disabled: true,
                                                listeners: {
                                                    change: function(component) {
                                                        var codiceFiscale = Ext.getCmp('wcs_anagraficaCodiceFiscale');
                                                        if (component.checked) {
                                                            codiceFiscale.allowBlank = true;
                                                        } else {
                                                            codiceFiscale.allowBlank = false;
                                                        }
                                                    }
                                                },
                                                id: 'wcs_anagraficaForzaCodiceFiscale',
                                                name: 'anagraficaForzaCodiceFiscale'
                                            }]
                                    }]
                            }, {
                                xtype: 'datefield',
                                readOnly: wcs_anagraficaDataDecessoRO,
                                format: 'd/m/Y',
                                maxValue: new Date(),
                                fieldLabel: 'Data decesso',
                                validator: function(value) {
                                    var dataNascita = Ext.getCmp('wcs_anagraficaDataDiNascita').getValue();
                                    var dataMorte = Ext.Date.parse(value, "d/m/Y");
                                    if (dataMorte < dataNascita) {
                                        return "La data decesso non può essere antecedente alla data di nascita";
                                    } else {
                                        return true;
                                    }
                                },
                                id: 'wcs_anagraficaDataMorte',
                                tabIndex: 16,
                                name: 'anagraficaDataMorte',
                                anchor: '95%'
                            }]
                    }]
            }, {
                xtype: 'fieldset',
                title: 'Dati nascita',
                collapsible: true,
                collapsed: false,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [{
                        xtype: 'container',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'wcs_statoremotecombo',
                                        hiddenName: 'codStato',
                                        valueField: 'codStato',
                                        tabIndex: 15,
                                        displayField: 'desStato',
                                        readOnly: wcs_anagraficaStatoNascitaRO,
                                        id: 'wcs_anagraficaStatoNascita',
                                        fieldLabel: 'Stato di nascita*',
                                        store: new wcs.store.StatoStore({
                                            id: 'wcs_statoNascitaStore'
                                        }),
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        name: 'anagraficaStatoNascita',
                                        anchor: '95%',
                                        listeners: {
                                            select: function(combo, record, index) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                var forceSelection = true;
                                                Ext.getCmp('wcs_anagraficaProvinciaNascita').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaProvinciaEsteraNascita').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneDiNascita').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneEsteroDiNascita').setRawValue('');
                                                if (record[0].data.desStato.toUpperCase() != 'ITALIA') {
                                                    forceSelection = false;
                                                }
                                                Ext.getCmp('wcs_anagraficaProvinciaNascita').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaComuneDiNascita').forceSelection = forceSelection;
                                            },
                                            beforequery: function(obj, options) {
                                                var value = Ext.getCmp('wcs_anagraficaStatoNascita').getValue();
                                                if (value == null) {
                                                    Ext.getCmp('wcs_anagraficaProvinciaNascita').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaProvinciaEsteraNascita').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneDiNascita').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneEsteroDiNascita').setRawValue('');
                                                }
                                            }
                                        }
                                    }, {
                                        xtype: 'wcs_comuneremotecombo',
                                        hiddenName: 'codComune',
                                        valueField: 'codComune',
                                        tabIndex: 17,
                                        displayField: 'desComune',
                                        readOnly: wcs_anagraficaComuneNascitaRO,
                                        fieldLabel: 'Comune di nascita*',
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        id: 'wcs_anagraficaComuneDiNascita',
                                        name: 'anagraficaComuneDiNascita',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoNascita').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaNascita').getValue()
                                                };
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        name: 'anagraficaComuneEsteroDiNascita',
                                        id: 'wcs_anagraficaComuneEsteroDiNascita'
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaProvinciaEsteraNascita',
                                        name: 'anagraficaProvinciaEsteraNascita'
                                    }
                                    , {
                                        xtype: 'wcs_provinciaremotecombo',
                                        hiddenName: 'codProvincia',
                                        valueField: 'codProvincia',
                                        tabIndex: 16,
                                        displayField: 'desProvincia',
                                        readOnly: wcs_anagraficaStatoNascitaRO,
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        fieldLabel: 'Provincia di nascita*',
                                        id: 'wcs_anagraficaProvinciaNascita',
                                        name: 'anagraficaProvinciaNascita',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoNascita').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaComuneDiNascita').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'datefield',
                                        maxValue: new Date(),
                                        readOnly: wcs_anagraficaDataNascitaRO,
                                        format: 'd/m/Y',
                                        tabIndex: 18,
                                        id: 'wcs_anagraficaDataDiNascita',
                                        fieldLabel: 'Data di nascita*',
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        name: 'anagraficaDataDiNascita',
                                        anchor: '95%'
                                    }]
                            }]
                    }]
            }
            , {
                xtype: 'fieldset',
                title: 'Dati residenza',
                collapsible: true,
                id: 'wcs_anagraficaDatiResidenza',
                collapsed: true,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [{
                        xtype: 'container',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'wcs_statoremotecombo',
                                        hiddenName: 'codStato',
                                        valueField: 'codStato',
                                        tabIndex: 19,
                                        displayField: 'desStato',
                                        store: new wcs.store.StatoStore({
                                            id: 'wcs_statoResidenzaStore'
                                        }),
                                        readOnly: wcs_anagraficaStatoResidenzaRO,
                                        id: 'wcs_anagraficaStatoDiResidenza',
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        fieldLabel: 'Stato di residenza*',
                                        name: 'anagraficaStatoDiResidenza',
                                        listeners: {
                                            select: function(combo, record, index) {
                                                var forceSelection = true;
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                Ext.getCmp('wcs_anagraficaProvinciaResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaProvinciaEsteraResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneEsteroResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                                if (record[0].data.desStato.toUpperCase() != 'ITALIA') {
                                                    forceSelection = false;
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').maxLength = 10;
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').minLength = 0;
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').vtype = '';
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').maxLengthText = 'La lunghezza massima del campo è 10 caratteri';
                                                } else {
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').maxLength = 5;
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').vtype = 'FiveNums';
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').minLength = 5;
                                                }
                                                Ext.getCmp('wcs_anagraficaProvinciaResidenza').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaComuneResidenza').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaViaResidenza').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').forceSelection = forceSelection;//forceSelection;
                                            },
                                            beforequery: function(obj, options) {
                                                var value = Ext.getCmp('wcs_anagraficaStatoDiResidenza').getValue();
                                                if (value == null) {
                                                    Ext.getCmp('wcs_anagraficaProvinciaResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaProvinciaEsteraResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneEsteroResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCapResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaViaResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaViaNoTsResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCivicoResidenza').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                                }
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'wcs_comuneremotecombo',
                                        hiddenName: 'codComune',
                                        valueField: 'codComune',
                                        tabIndex: 21,
                                        displayField: 'desComune',
                                        readOnly: wcs_anagraficaComuneResidenzaRO,
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        fieldLabel: 'Comune di residenza*',
                                        id: 'wcs_anagraficaComuneResidenza',
                                        name: 'anagraficaComuneResidenza',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDiResidenza').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaResidenza').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaComuneEsteroResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                                var forceSelection = true;
                                                if (record[0].data.desComune.toUpperCase() != 'TRIESTE') {
                                                    forceSelection = false;
                                                }
                                                Ext.getCmp('wcs_anagraficaViaResidenza').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').forceSelection = forceSelection;//forceSelection;
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaComuneEsteroResidenza',
                                        name: 'anagraficaComuneEsteroResidenza'
                                    }, {
                                        tabIndex: 23,
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaCapResidenzaRO,
                                        fieldLabel: 'CAP residenza',
                                        id: 'wcs_anagraficaCapResidenza',
                                        name: 'anagraficaCapResidenza',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'combo',
                                        store: 'tipologiaResidenzaStore',
                                        fieldLabel: 'Tipologia residenza*',
                                        name: 'anagraficaTipologiaResidenza',
                                        anchor: '95%',
                                        value: '494',
                                        displayField: 'name',
                                        valueField: 'value',
                                        allowBlank: false
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'wcs_provinciaremotecombo',
                                        hiddenName: 'codProvincia',
                                        valueField: 'codProvincia',
                                        tabIndex: 20,
                                        displayField: 'desProvincia',
                                        readOnly: wcs_anagraficaProvinciaResidenzaRO,
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        fieldLabel: 'Provincia di residenza*',
                                        id: 'wcs_anagraficaProvinciaResidenza',
                                        name: 'anagraficaProvinciaResidenza',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDiResidenza').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaComuneResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneEsteroResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaProvinciaEsteraResidenza',
                                        name: 'anagraficaProvinciaEsteraResidenza'
                                    }, {
                                        xtype: 'wcs_viaremotecombo',
                                        hiddenName: 'codVia',
                                        tabIndex: 22,
                                        valueField: 'codVia',
                                        displayField: 'desVia',
                                        readOnly: wcs_anagraficaViaResidenzaRO,
                                        id: 'wcs_anagraficaViaResidenza',
                                        fieldLabel: 'Via di residenza*',
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        name: 'anagraficaViaResidenza',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 2;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDiResidenza').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaResidenza').getValue(),
                                                    codComune: Ext.getCmp('wcs_anagraficaComuneResidenza').getValue()
                                                };
                                                Ext.getCmp('wcs_anagraficaViaNoTsResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoResidenza').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaViaNoTsResidenza',
                                        name: 'anagraficaViaNoTsResidenza'
                                    }
                                    , {
                                        xtype: 'wcs_civicoremotecombo',
                                        hiddenName: 'codCivico',
                                        tabIndex: 24,
                                        valueField: 'codCivico',
                                        displayField: 'desCivico',
                                        readOnly: wcs_anagraficaCivicoResidenzaRO,
                                        fieldLabel: 'Civico di residenza*',
                                        id: 'wcs_anagraficaCivicoResidenza',
                                        allowBlank: false,
                                        blankText: 'Questo campo è obbligatorio',
                                        name: 'anagraficaCivicoResidenza',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 1;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDiResidenza').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaResidenza').getValue(),
                                                    codComune: Ext.getCmp('wcs_anagraficaComuneResidenza').getValue(),
                                                    codVia: Ext.getCmp('wcs_anagraficaViaResidenza').getValue()
                                                };
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsResidenza').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaCivicoNoTsResidenza',
                                        name: 'anagraficaCivicoNoTsResidenza'
                                    }]
                            }]
                    }]
            }, {
                xtype: 'fieldset',
                title: 'Dati domicilio',
                collapsed: true,
                collapsible: true,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [{
                        xtype: 'container',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [
                                        
                                        {
                                	
                                        xtype: 'wcs_statoremotecombo',
                                        hiddenName: 'codStato',
                                        valueField: 'codStato',
                                        store: new wcs.store.StatoStore({
                                            id: 'wcs_statoDomicilioStore'
                                        }),
                                        displayField: 'desStato',
                                        readOnly: wcs_anagraficaStatoDomicilioRO,
                                        fieldLabel: 'Stato domicilio',
                                        tabIndex: 25,
                                        id: 'wcs_anagraficaStatoDomicilio',
                                        name: 'anagraficaStatoDomicilio',
                                        listeners: {
                                            select: function(combo, record, index) {
                                                var forceSelection = true;
                                                this.minChars = 3;
                                                Ext.getCmp('wcs_anagraficaProvinciaDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaProvinciaEsteraDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneEsteroDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                                if (record[0].data.desStato.toUpperCase() != 'ITALIA') {
                                                    forceSelection = false;
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').maxLength = 10;
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').minLength = 0;
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').vtype = '';
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').maxLengthText = 'La lunghezza massima del campo è 10 caratteri';
                                                } else {
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').maxLength = 5;
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').minLength = 5;
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').vtype = 'FiveNums';
                                                }
                                                Ext.getCmp('wcs_anagraficaProvinciaDomicilio').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaComuneDomicilio').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaViaDomicilio').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaCivicoDomicilio').forceSelection = forceSelection;//forceSelection;
                                            },
                                            beforequery: function(combo, record, index) {
                                                var value = Ext.getCmp('wcs_anagraficaStatoDomicilio').getValue();
                                                if (value == null) {
                                                    Ext.getCmp('wcs_anagraficaProvinciaDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaProvinciaEsteraDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaComuneEsteroDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCapDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaViaDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaViaNoTsDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCivicoDomicilio').setRawValue('');
                                                    Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                                }
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'wcs_comuneremotecombo',
                                        hiddenName: 'codComune',
                                        valueField: 'codComune',
                                        displayField: 'desComune',
                                        tabIndex: 27,
                                        readOnly: wcs_anagraficaComuneDomicilioRO,
                                        fieldLabel: 'Comune domicilio',
                                        id: 'wcs_anagraficaComuneDomicilio',
                                        name: 'anagraficaComuneDomicilio',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDomicilio').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaDomicilio').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaComuneEsteroDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                                var forceSelection = true;
                                                if (record[0].data.desComune.toUpperCase() != 'TRIESTE') {
                                                    forceSelection = false;
                                                }
                                                Ext.getCmp('wcs_anagraficaViaDomicilio').forceSelection = forceSelection;
                                                Ext.getCmp('wcs_anagraficaCivicoDomicilio').forceSelection = forceSelection;//forceSelection;
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaComuneEsteroDomicilio',
                                        name: 'anagraficaComuneEsteroDomicilio'
                                    }, {
                                        tabIndex: 29,
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaCapDomicilioRO,
                                        fieldLabel: 'CAP domicilio',
                                        id: 'wcs_anagraficaCapDomicilio',
                                        name: 'anagraficaCapDomicilio',
                                        anchor: '95%'
                                    },{  xtype: 'textfield',
                                        fieldLabel: 'Presso:',
                                        tabIndex: 6,
                                        id: 'wcs_anagraficaPresso',
                                        name: 'anagraficaPresso',
                                        readOnly: wcs_anagraficaStatoDomicilioRO,
                                        anchor: '95%'}]
                            }, {
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'wcs_provinciaremotecombo',
                                        hiddenName: 'codProvincia',
                                        valueField: 'codProvincia',
                                        tabIndex: 26,
                                        displayField: 'desProvincia',
                                        readOnly: wcs_anagraficaProvinciaDomicilioRO,
                                        fieldLabel: 'Provincia domicilio',
                                        id: 'wcs_anagraficaProvinciaDomicilio',
                                        name: 'anagraficaProvinciaDomicilio',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 3;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDomicilio').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaProvinciaEsteraDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaComuneEsteroDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCapDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaViaNoTsDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoDomicilio').setRawValue('');
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaProvinciaEsteraDomicilio',
                                        name: 'anagraficaProvinciaEsteraDomicilio'
                                    },
                                    {
                                        xtype: 'container',
                                        columnWidth: .5,
                                        layout: 'anchor',
                                        items: [{
                                                xtype: 'wcs_viaremotecombo',
                                                hiddenName: 'codVia',
                                                tabIndex: 28,
                                                valueField: 'codVia',
                                                displayField: 'desVia',
                                                readOnly: wcs_anagraficaViaDomicilioRO,
                                                fieldLabel: 'Via domicilio',
                                                id: 'wcs_anagraficaViaDomicilio',
                                                name: 'anagraficaViaDomicilio',
                                                listeners: {
                                                    beforequery: function(obj, options) {
                                                        this.store.removeAll();
                                                        this.minChars = 2;
                                                        this.store.proxy.extraParams = {
                                                            codStato: Ext.getCmp('wcs_anagraficaStatoDomicilio').getValue(),
                                                            codProv: Ext.getCmp('wcs_anagraficaProvinciaDomicilio').getValue(),
                                                            codComune: Ext.getCmp('wcs_anagraficaComuneDomicilio').getValue()
                                                        };
                                                    },
                                                    select: function(combo, record, index) {
                                                        Ext.getCmp('wcs_anagraficaViaNoTsDomicilio').setRawValue('');
                                                        Ext.getCmp('wcs_anagraficaCivicoDomicilio').setRawValue('');
                                                        Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                                    }
                                                },
                                                anchor: '95%'
                                            }]
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaViaNoTsDomicilio',
                                        name: 'anagraficaViaNoTsDomicilio'
                                    }, {
                                        xtype: 'wcs_civicoremotecombo',
                                        hiddenName: 'codCivico',
                                        valueField: 'codCivico',
                                        displayField: 'desCivico',
                                        tabIndex: 30,
                                        readOnly: wcs_anagraficaCivicoDomicilioRO,
                                        fieldLabel: 'Civico domicilio',
                                        id: 'wcs_anagraficaCivicoDomicilio',
                                        name: 'anagraficaCivicoDomicilio',
                                        listeners: {
                                            beforequery: function(obj, options) {
                                                this.store.removeAll();
                                                this.minChars = 1;
                                                this.store.proxy.extraParams = {
                                                    codStato: Ext.getCmp('wcs_anagraficaStatoDomicilio').getValue(),
                                                    codProv: Ext.getCmp('wcs_anagraficaProvinciaDomicilio').getValue(),
                                                    codComune: Ext.getCmp('wcs_anagraficaComuneDomicilio').getValue(),
                                                    codVia: Ext.getCmp('wcs_anagraficaViaDomicilio').getValue()
                                                };
                                            },
                                            select: function(combo, record, index) {
                                                Ext.getCmp('wcs_anagraficaCivicoNoTsDomicilio').setRawValue('');
                                            }
                                        },
                                        anchor: '95%'
                                    }, {
                                        xtype: 'hiddenfield',
                                        id: 'wcs_anagraficaCivicoNoTsDomicilio',
                                        name: 'anagraficaCivicoNoTsDomicilio'
                                    }]
                            }]
                    }]
            }
            , {
                xtype: 'fieldset',
                title: 'Contatti',
                collapsible: true,
                collapsed: true,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [{
                        xtype: 'container',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaTelefonoDomicilioRO,
                                        fieldLabel: 'Telefono',
                                        maxLength: 20,
                                        tabIndex: 31,
                                        maxLengthText: 'Lunghezza massima 20 caratteri',
                                        di: 'wcs_anagraficaTelefono',
                                        name: 'anagraficaTelefono',
                                        anchor: '95%'

                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaCellulareDomicilioRO,
                                        fieldLabel: 'Cellulare',
                                        tabIndex: 32,
                                        maxLength: 20,
                                        maxLengthText: 'Lunghezza massima 20 caratteri',
                                        id: 'wcs_anagraficaCellulare',
                                        name: 'anagraficaCellulare',
                                        anchor: '95%'
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaEmailDomicilioRO,
                                        fieldLabel: 'Email',
                                        tabIndex: 33,
                                        maxLength: 200,
                                        maxLengthText: 'Lunghezza massima 200 caratteri',
                                        id: 'wcs_anagraficaEmail',
                                        name: 'anagraficaEmail',
                                        vtype: 'email',
                                        vtypeText: 'Il campo deve essere un indirizzo email valido',
                                        anchor: '95%'
                                    }]
                            }]
                    }, {
                        xtype: 'container',
                        anchor: '100%',
                        layout: 'column',
                        items: [{
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'checkbox',
                                        name: 'anagraficaNotificaSMS',
                                        id: 'wcs_anagraficaNotificaSMS',
                                        tabIndex: 34,
                                        fieldLabel: 'Avvisa via SMS',
                                        listeners: {
                                            change: function(component) {
                                                var cellulare = Ext.getCmp('wcs_anagraficaCellulare');
                                                if (component.checked) {
                                                    cellulare.allowBlank = false;
                                                } else {
                                                    cellulare.allowBlank = true;
                                                }
                                            }
                                        }
                                    } ]
                            }, {
                                xtype: 'container',
                                columnWidth: .5,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'checkbox',
                                        name: 'anagraficaNotificaEmail',
                                        tabIndex: 35,
                                        id: 'wcs_anagraficaNotificaEmail',
                                        fieldLabel: 'Avvisa via email',
                                        listeners: {
                                            change: function(component) {
                                                var email = Ext.getCmp('wcs_anagraficaEmail');
                                                if (component.checked) {
                                                    email.allowBlank = false;
                                                }
                                                else {
                                                    email.allowBlank = true;
                                                }
                                            }
                                        }
                                    }]
                            }]
                    }]
            }, {
                xtype: 'fieldset',
                title: 'Altro',
                collapsible: true,
                collapsed: true,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [{
                        xtype: 'container',
                        layout: 'column',
                        anchor: '95%',
                        items: [{
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaEnteGestoreRO,
                                        fieldLabel: 'Ente gestore',
                                        maxLength: 20,
                                        tabIndex: 36,
                                        maxLengthText: 'Lunghezza massima 20 caratteri',
                                        id: 'wcs_anagraficaEnteGestore',
                                        name: 'anagraficaEnteGestore',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaASSRO,
                                        fieldLabel: 'ASS',
                                        tabIndex: 39,
                                        maxLength: 1,
                                        maxLengthText: 'Lunghezza massima 1 carattere',
                                        id: 'wcs_anagraficaAss',
                                        name: 'anagraficaAss',
                                        anchor: '95%'
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaZonaRO,
                                        fieldLabel: 'Zona',
                                        tabIndex: 37,
                                        maxLength: 1,
                                        maxLengthText: 'Lunghezza massima 1 carattere',
                                        id: 'wcs_anagraficaZona',
                                        name: 'anagraficaZona',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaDistrettoASSRO,
                                        fieldLabel: 'Distretto ASS',
                                        maxLength: 1,
                                        tabIndex: 40,
                                        maxLengthText: 'Lunghezza massima 1 carattere',
                                        id: 'wcs_anagraficaDistrettoAss',
                                        name: 'anagraficaDistrettoASS',
                                        anchor: '95%'
                                    }]
                            }, {
                                xtype: 'container',
                                columnWidth: .3,
                                layout: 'anchor',
                                items: [{
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaSottozonaRO,
                                        fieldLabel: 'Sottozona',
                                        tabIndex: 38,
                                        maxLength: 2,
                                        maxLengthText: 'Lunghezza massima 2 caratteri',
                                        id: 'wcs_anagraficaSottozona',
                                        name: 'anagraficaSottozona',
                                        anchor: '95%'
                                    }, {
                                        xtype: 'textfield',
                                        readOnly: wcs_anagraficaMedicoBaseRO,
                                        maxLength: 100,
                                        maxLengthText: 'Lunghezza massima 100 caratteri',
                                        fieldLabel: 'Medico di base',
                                        tabIndex: 41,
                                        id: 'wcs_anagraficaMedicoDiBase',
                                        name: 'anagraficaMedicoDiBase',
                                        anchor: '95%'
                                    }]
                            }]
                    }]
            }, {
                xtype: 'fieldset',
                title: 'Note',
                collapsible: true,
                collapsed: false,
                layout: 'anchor',
                items: [{
                        xtype: 'textarea',
                        anchor: '100%',
                        name: 'anagraficaNote'
                    }]
            }];

        this.buttons = [
            Ext.create('Ext.ux.VirtualKeyboardButton', {
                id: 'wcs_keyboardAnagraficaForm'
            }),
            {
                itemId: 'wcs_anagraficaSearch',
                id: 'wcs_anagraficaSearch',
                name:'wcs_anagraficaSearch',
                text: 'Ricerca',
                scope: this,
                handler: this.onSearch
            }, {
                itemId: 'wcs_anagraficaClean',
                text: 'Pulisci',
                scope: this,
                handler: this.onReset
            }, {
                id: 'wcs_anagraficaVerify',
                text: 'Verifica',
                disabled: true,
                scope: this,
                handler: this.onVerify
            }, {
                id: 'wcs_anagraficaSalva',
                text: 'Salva',
                handler: function() {
                    var dataMorte = Ext.getCmp('wcs_anagraficaDataMorte').getValue();
                    var dataNascita = Ext.getCmp('wcs_anagraficaDataDiNascita').getValue();
                    var form = this.up('wcs_anagraficaform').getForm();
                    if (form.isValid()) {
                        if (dataMorte != null && dataMorte != "") {

                            Ext.MessageBox.show({
                                title: 'Attenzione',
                                msg: 'Stai per valorizzare la data decesso dell\'utente. Vuoi procedere? Se l\'utente ha dei PAI aperti sarà necessario chiuderli anticipatamente.',
                                buttons: Ext.MessageBox.OKCANCEL,
                                fn: function(btn) {
                                    if (btn == 'ok') {
                                        form.submit({
                                            url: '/CartellaSociale/anagrafica',
                                            waitTitle: 'Salvataggio',
                                            waitMsg: 'Sto salvando i dati...',
                                            params: {
                                                action: 'write'
                                            },
                                            waitMsg:'Sto salvando i dati...',
                                                    success: function(form, action) {
                                                var json = Ext.JSON.decode(action.response.responseText);
                                                if (json.success) {
                                                    Ext.getCmp('wcs_anagraficaCodAna').setValue(json.codAna);
                                                    Ext.MessageBox.show({
                                                        title: 'Esito operazione',
                                                        msg: json.message,
                                                        buttons: Ext.MessageBox.OK
                                                    });
                                                    abilitaTabCartellaSociale();
                                                    abilitaPulsantiCartellaSociale();
                                                    wcs_isModified = '';
                                                } else {
                                                    wcs_isModified = '';
                                                    var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                                                    if (json != null) {
                                                        msg = json.message;
                                                    }
                                                    Ext.MessageBox.show({
                                                        title: 'Esito operazione',
                                                        msg: msg,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon: Ext.window.MessageBox.ERROR
                                                    });
                                                }
                                            },
                                            failure: function(form, action) {
                                                wcs_isModified = '';
                                                var json = Ext.JSON.decode(action.response.responseText);
                                                var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                                                if (json != null) {
                                                    msg = json.message;
                                                }
                                                Ext.MessageBox.show({
                                                    title: 'Esito operazione',
                                                    msg: msg,
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.window.MessageBox.ERROR
                                                });
                                            }
                                        });
                                    }
                                    else {
                                        Ext.getCmp('wcs_anagraficaDataMorte').setValue('');
                                    }
                                }
                            });
                        } else {
                            form.submit({
                                url: '/CartellaSociale/anagrafica',
                                waitTitle: 'Salvataggio',
                                waitMsg: 'Sto salvando i dati...',
                                params: {
                                    action: 'write'
                                },
                                success: function(form, action) {
                                    var json = Ext.JSON.decode(action.response.responseText);
                                    if (json.success) {
                                        Ext.getCmp('wcs_anagraficaCodAna').setValue(json.codAna);
                                        Ext.MessageBox.show({
                                            title: 'Esito operazione',
                                            msg: json.message,
                                            buttons: Ext.MessageBox.OK
                                        });
                                        abilitaTabCartellaSociale();
                                        abilitaPulsantiCartellaSociale();
                                        wcs_isModified = '';
                                        var values = form.getValues();
                                        var nameDisplay = Ext.getCmp('wcs_currentUserName');
                                        nameDisplay.setText(values.anagraficaCognome + ' ' + values.anagraficaNome);
                                        nameDisplay.show();
                                    } else {
                                        wcs_isModified = '';
                                        var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                                        if (json != null) {
                                            msg = json.message;
                                        }
                                        Ext.MessageBox.show({
                                            title: 'Esito operazione',
                                            msg: msg,
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.window.MessageBox.ERROR
                                        });
                                    }
                                },
                                failure: function(form, action) {
                                    wcs_isModified = '';
                                    var json = Ext.JSON.decode(action.response.responseText);
                                    var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                                    if (json != null) {
                                        msg = json.message;
                                    }
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: msg,
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.window.MessageBox.ERROR
                                    });
                                }
                            });
                        }
                    }
                    else {
                        if (dataMorte != null && dataMorte != "" && dataMorte < dataNascita) {
                            Ext.MessageBox.alert('Errore', 'La data decesso è antecedente alla data di nascita.');
                        } else {
                            Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati correttamente.');
                        }
                        Ext.getCmp('wcs_anagraficaDatiResidenza').expand();
                    }
                }
            }];

        this.callParent(arguments);
    },
    onSearch: function() {
        Ext.widget('wcs_ricercaPopup').close();
        Ext.widget('wcs_ricercaPopup').show();
    },
    onReset: function() {
        resetAll();
        var nameDisplay = Ext.getCmp('wcs_currentUserName');
        nameDisplay.setText('');
        nameDisplay.hide();

        document.title='Cartella sociale:';
    },
    onVerify: function() {
        var form = this.getForm(), values = form.getValues();

        function sincronizzaDati() {
            Ext.Ajax.request({
                url: '/CartellaSociale/anagrafica',
                params: {
                    codAna: values.codAna,
                    anagraficaCodiceAnagrafico:values.anagraficaCodiceAnagrafico,
                    codiceFiscale : values.anagraficaCodiceFiscale,
                    action: 'sincronizzaDaAnagrafeSoc'
                },
                callback: function(x, success, response) {
                    var json = success ? Ext.JSON.decode(response.responseText) : null;
                    if (json && json.success) {
                        Ext.MessageBox.show({
                            title: 'Esito operazione',
                            msg: json.message,
                            buttons: Ext.MessageBox.OK
                        }); 
                        ricaricaAnagraficaNoAction(values.codAna);
                    } else {
                        Ext.MessageBox.show({
                            title: 'Esito operazione',
                            msg: json && json.message ? json.message : 'si e\' verificato un errore',
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.window.MessageBox.ERROR
                        });
                    }
                }
            });
        }
      
            form.submit({
                url: '/CartellaSociale/anagrafica',
                waitTitle: 'Verifica',
                waitMsg: 'Sto verificando i dati...',
                params: {
                    action: 'verifica'
                },
                success: function(form, action) {
                    var json = Ext.JSON.decode(action.response.responseText);
                    var canUpdate = json.data.canUpdate === 'true';
                    if (json.success) {
                        Ext.MessageBox.show({
                            title: 'Esito operazione',
                            msg: json.message + (canUpdate ? '<br />si desidera sovrascrivere i dati locali con quelli provenienti dall\'anagrafe comunale?' : ''),
                            buttons: canUpdate ? (Ext.MessageBox.YES + Ext.MessageBox.NO) : Ext.MessageBox.OK,
                            fn: function(button) {
                                if (button === 'yes') {
                                    sincronizzaDati();
                                }
                            }
                        });
                        abilitaTabCartellaSociale();
                        abilitaPulsantiCartellaSociale();
                    } else {
                        Ext.MessageBox.show({
                            title: 'Esito operazione',
                            msg: json.message,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.window.MessageBox.ERROR
                        });
                    }
                },
                failure: function(form, action) {
                    var message = 'Si è verificato un errore durante l\'operazione di verifica:\n Assicurarsi che i dati obbligatori (in rosso)  siano compilati (anche con dati fittizi) per procedere con l\' operazione di verifica. ';
                    try {
                        message = Ext.JSON.decode(action.response.responseText).message || message;
                    } catch (e) {
                        log('error: ', e);
                    }
                    Ext.MessageBox.show({
                        title: 'Esito operazione',
                        msg: message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.window.MessageBox.ERROR
                    });
                }
            });
    }
});