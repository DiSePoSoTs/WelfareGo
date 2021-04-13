Ext.define('wcs.view.condizione.CondizioneForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.wcs_condizioneform',
	title : 'Condizione',
	bodyStyle : 'padding:5px 5px 0',
	frame : true,
	defaults : {
		labelWidth : 150
	},
	initComponent : function() {
		this.reader = Ext.create('Ext.data.reader.Reader', {
					model : 'wcs.model.CondizioneModel',
					type : 'json',
					rootProperty : 'data',
					successProperty : 'success'
				});
		this.fieldDefaults = {
			labelWidth : 150
		};
		this.items = [{
			xtype : 'container',
			anchor : '100%',
			layout : 'column',
			items : [{
						xtype : 'container',
						columnWidth : .45,
						layout : 'anchor',
						items : [{
									xtype : 'textfield',
									id : 'wcs_condizioneCognome',
									readOnly : true,
									fieldLabel : 'Cognome',
									tabIndex : 1,
									name : 'condizioneCognome',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 3,
									valueField : 'value',
									store : wcs_condizioneStatoCivileStore,
									id : 'wcs_condizioneStatoCivile',
									readOnly : wcs_condizioneStatoCivileRO,
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									fieldLabel : 'Stato civile*',
									name : 'condizioneStatoCivile',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 5,
									valueField : 'value',
									store : wcs_condizioneCondizioneProfessionaleStore,
									id : 'wcs_condizioneCondizioneProfessionale',
									readOnly : wcs_condizioneProfessionaleRO,
									hidden : wcs_condizioneProfessionaleHidden,
									fieldLabel : 'Condizione professionale*',
									name : 'condizioneCondizioneProfessionale',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 7,
									valueField : 'value',
									store : wcs_condizioneAbitazioneStore,
									id : 'wcs_condizioneAbitazione',
									readOnly : wcs_condizioneAbitazioneRO,
									hidden : wcs_condizioneAbitazioneHidden,
									fieldLabel : 'Abitazione',
									name : 'condizioneAbitazione',
									anchor : '97%'
								}, {
									xtype : 'datefield',
									format : 'd/m/Y',
									readOnly : true,
									fieldLabel : 'Aggiornamento abitazione',
									name : 'condizioneAggiornamentoAbitazione',
									anchor : '95%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 10,
									valueField : 'value',
									store : wcs_condizioneRedditoStore,
									id : 'wcs_condizioneFormaReddito',
									fieldLabel : 'Forma reddito',
									readOnly : wcs_condizioneFormaRedditoRO,
									hidden : wcs_condizioneFormaRedditoHidden,
									name : 'condizioneFormaReddito',
									anchor : '97%'
								}, {
									xtype : 'textarea',
									id : 'wcs_condizioneNote',
									fieldLabel : 'Note',
									tabIndex : 12,
									name : 'condizioneNote',
									anchor : '97%'
								}]
					}, {
						xtype : 'container',
						columnWidth : .45,
						readOnly : true,
						layout : 'anchor',
						items : [{
									xtype : 'textfield',
									id : 'wcs_condizioneNome',
									readOnly : true,
									tabIndex : 2,
									fieldLabel : 'Nome',
									name : 'condizioneNome',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 4,
									valueField : 'value',
									store : wcs_condizioneTitoloDiStudioStore,
									id : 'wcs_condizioneTitoloStudio',
									fieldLabel : 'Titolo di studio*',
									name : 'condizioneTitoloStudio',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 6,
									valueField : 'value',
									store : wcs_condizioneCondizioneFamigliareStore,
									id : 'wcs_condizioneCondizioneFamiliare',
									fieldLabel : 'Condizione familiare',
									readOnly : wcs_condizioneFamigliareRO,
									hidden : wcs_condizioneFamigliareHidden,
									name : 'condizioneCondizioneFamiliare',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									tabIndex : 8,
									valueField : 'value',
									store : wcs_condizioneStatoFisicoStore,
									id : 'wcs_condizioneStatoFisico',
									fieldLabel : 'Stato fisico',
									readOnly : wcs_condizioneStatoFisicoRO,
									hidden : wcs_condizioneStatoFisicoHidden,
									name : 'condizioneStatoFisico',
									anchor : '97%'
								}, {
									xtype : 'weuronumberfield',
									hideTrigger : true,
									keyNavEnabled : false,
									mouseWheelEnabled : false,
									tabIndex : 9,
									allowDecimals : true,
									minValue : 0,
									maxLength : 9,
									//                    decimalSeparator:',',
									id : 'wcs_condizioneRedditoMensile',
									fieldLabel : 'Reddito mensile',
									readOnly : wcs_condizioneFormaRedditoRO,
									hidden : wcs_condizioneFormaRedditoHidden,
									name : 'condizioneRedditoMensile',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									valueField : 'value',
									store : wcs_condizioneAccompagnamentoStore,
									id : 'wcs_condizioneAccompagnamento',
									readOnly : wcs_condizioneAccompagnamentoRO,
									hidden : wcs_condizioneAccompagnamentoHidden,
									fieldLabel : 'Accompagnamento',
									tabIndex : 11,
									name : 'condizioneAccompagnamento',
									anchor : '97%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									valueField : 'value',
									store : wcs_invaliditaCivileCombo,
									id : 'wcs_condizioneInvaliditaCivile',
									fieldLabel : 'Invalidità civile (%)',
									name : 'condizioneInvaliditaCivile',
									anchor : '97%'
								}, {
									xtype : 'container',
									layout : 'hbox',
									items : [{
										xtype : 'combo',
										store : [['N', 'No'], ['S', 'Si']],
										fieldLabel : "Richiesta di assegno di accompagnamento",
										name : 'condizioneRichiestaAssegnoAccompagnamento',
										forceSelection : true,
										tabIndex : 14,
										width : 220,
										margin : '0 5 0 0'
									}, {
										flex : 1,
										xtype : 'datefield',
										format : 'd/m/Y',
										fieldLabel : "data richiesta",
										tabIndex : 15,
										labelWidth : 100,
										name : 'condizioneDataRichiestaAssegnoAccompagnamento'
									}],
									anchor : '97%'
								}]
					}]
		}, {
			xtype : 'fieldset',
			title : 'Coordinate bancarie',
			collapsible : true,
			defaultType : 'textfield',
			defaults : {
				anchor : '100%'
			},
			layout : 'anchor',
			items : [{
				xtype : 'container',
				anchor : '98%',
				layout : 'column',
				items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
										xtype : 'textfield',
										fieldLabel : 'Cod. Nazione',
										readOnly : wcs_condizioneCodNazioneRO,
										id : 'wcs_condizioneCodNazione',
										minLength : 2,
										tabIndex : 16,
										minLengthText : 'La lunghezza del campo è 2',
										maxLength : 2,
										name : 'condizioneCodNazione',
										anchor : '97%'
									}, {
										xtype : 'textfield',
										id : 'wcs_condizioneCIN',
										tabIndex : 18,
										readOnly : wcs_condizioneCINRO,
										fieldLabel : 'CIN',
										minLength : 1,
										minLengthText : 'La lunghezza del campo è 1',
										maxLength : 1,
										name : 'condizioneCIN',
										anchor : '97%'
									}, {
										xtype : 'textfield',
										tabIndex : 20,
										id : 'wcs_condizioneCAB',
										readOnly : wcs_condizioneCABRO,
										fieldLabel : 'CAB',
										minLength : 5,
										minLengthText : 'La lunghezza del campo è 5',
										maxLength : 5,
										name : 'condizioneCAB',
										vtype : 'FiveNums',
										anchor : '97%'
									}, {
										xtype : 'textfield',
										id : 'wcs_condizioneIBAN',
										tabIndex : 22,
										fieldLabel : 'IBAN',
										//minLength: 27,
										//minLengthText: 'La lunghezza minima del campo è 27',
										//maxLength: 27,
										readOnly : wcs_condizioneIBANRO,
										name : 'condizioneIBAN',
										anchor : '97%'
									}]
						}, {
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
										xtype : 'textfield',
										id : 'wcs_condizioneContr',
										fieldLabel : 'Codice controllo',
										tabIndex : 17,
										minLength : 2,
										minLengthText : 'La lunghezza del campo è 2',
										maxLength : 2,
										readOnly : wcs_condizioneContrRO,
										name : 'condizioneContr',
										vtype : 'TwoNums',
										anchor : '97%'
									}, {
										xtype : 'textfield',
										id : 'wcs_condizioneABI',
										readOnly : wcs_condizioneABIRO,
										fieldLabel : 'ABI',
										tabIndex : 19,
										minLength : 5,
										minLengthText : 'La lunghezza del campo è 5',
										maxLength : 5,
										name : 'condizioneABI',
										vtype : 'FiveNums',
										anchor : '97%'
									}, {
										xtype : 'numberfield',
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										tabIndex : 21,
										id : 'wcs_condizioneContoCorrente',
										maxLength : 12,
										maxLengthText : 'La lunghezza massima del campo è 12',
										readOnly : wcs_condizioneCCorrenteRO,
										fieldLabel : 'Conto corrente',
										name : 'condizioneContoCorrente',
										anchor : '97%'
									}, {
										xtype : 'button',
										text : 'Calcola IBAN',
										tabIndex : 22,
										disabled : wcs_condizioneIBANButtonHidden,
										hidden : wcs_condizioneIBANButtonHidden,
										handler : this.calculateIBAN
									}]
						}]
			}]
		}];

		this.buttons = [Ext.create('Ext.ux.VirtualKeyboardButton', {
							tabIndex : 23,
							id : 'wcs_keyboardCondizioneForm'
						}), {
					itemId : 'wcs_condizioneClean',
					text : 'Pulisci',
					tabIndex : 24,
					scope : this,
					handler : this.onReset
				}, {
					id : 'wcs_condizioneSalva',
					text : 'Salva',
					tabIndex : 25,
					formBind : true,
					handler : function() {
						var condizioneTab = Ext.getCmp('wcs_condizioneTab');
						var famigliaTab = Ext.getCmp('wcs_famigliaTab');
						var referentiTab = Ext.getCmp('wcs_referentiTab');
						var paiTab = Ext.getCmp('wcs_paiTab');
						var appuntamentiTab = Ext.getCmp('wcs_appuntamentiTab');
						var form = this.up('form').getForm();
						if (form.isValid()) {
							var codAnag = Ext.getCmp('wcs_anagraficaCodAna')
									.getValue();
							form.submit({
								url : '/CartellaSociale/condizione',
								waitTitle : 'Salvataggio',
								waitMsg : 'Sto salvando i dati...',
								params : {
									codAnag : codAnag
								},
								success : function(form, action) {
									var json = Ext.JSON
											.decode(action.response.responseText);
									if (json.success) {
										condizioneTab.setDisabled(false);
										famigliaTab.setDisabled(false);
										referentiTab.setDisabled(false);
										paiTab.setDisabled(false);
										appuntamentiTab.setDisabled(false);
										wcs_isModified = '';
										Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : json.message,
													buttons : Ext.MessageBox.OK
												});
									} else {
										wcs_isModified = '';
										Ext.MessageBox.show({
													title : 'Errore',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													icon : Ext.window.MessageBox.ERROR
												});
									}
								},
								failure : function(form, action) {
									var json = Ext.JSON
											.decode(action.response.responseText);
									wcs_isModified = '';
									Ext.MessageBox.show({
												title : 'Esito operazione',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												icon : Ext.window.MessageBox.ERROR
											});
								}
							});
						} else {
							Ext.MessageBox
									.alert('Errore',
											'Verifica che tutti i campi obbligatori siano compilati.');
						}
					}
				}];

		this.callParent(arguments);
	},

	onReset : function() {
		//        this.getForm().reset();
		var tabAnagrafica = Ext.getCmp('wcs_anagraficaTab');
		//        tabAnagrafica.getForm().reset();
		resetAll();
		Ext.getCmp('wcs_cartellaTabPanel').setActiveTab(tabAnagrafica);
	},

	calculateIBAN : function() {
		var codNazione = Ext.getCmp('wcs_condizioneCodNazione').getValue()
				.toUpperCase();
		var cin = Ext.getCmp('wcs_condizioneCIN').getValue().toUpperCase();
		var cab = Ext.getCmp('wcs_condizioneCAB').getValue();
		var abi = Ext.getCmp('wcs_condizioneABI').getValue();
		var contr = Ext.getCmp('wcs_condizioneContr').getValue();
		var cc = Ext.getCmp('wcs_condizioneContoCorrente').getValue()
				.toString();
		var cclength = cc.length;
		while (cclength < 12) {
			cc = '0' + cc;
			cclength = cc.length;
		}
		var ibanValue = codNazione + contr + cin + abi + cab + cc;
		Ext.getCmp('wcs_condizioneIBAN').setValue(ibanValue);
	}
});