Ext.require('wcs.view.common.DatiBancariFieldSet');
Ext.require('wcs.view.pai.calcoloImporto');
Ext.require('wcs.view.pai.calcoloImportoEco');
Ext.require('wcs.view.pai.calcoloISEE');
Ext.require('wcs.view.pai.calcoloImportoTrasporti');

(function() {

	var newDelegatoData = null;

	var createInsertDelegatoSocWindow = function(interventiForm) {

		Ext.require('wcs.view.anagrafica.AnagraficaSocForm');
		var window = Ext.create('Ext.window.Window', {
			title : 'Inserimento dati Delegato (persona giuridica)',
			height : 505,
			width : 1000,
			modal : true,
			layout : 'fit',
			items : {
				xtype : 'wcs_anagraficasocform'
			},
			buttons : [{
				text : 'Conferma',
				tabIndex : 22,
				handler : function() {
					var form = window.items.first().getForm();
					var delegatoData = form.getValues();

					if (delegatoData.codiceAnagrafica) {
						Ext.getCmp('wcs_InterventiFormBeneficiarioDes')
								.setValue(delegatoData.ragioneSociale);
						Ext.getCmp('wcs_InterventiFormBeneficiarioCod')
								.setValue(delegatoData.codiceAnagrafica);
						Ext.getCmp('iban_delegato').setValue(delegatoData.IBAN);
						window.close();
					} else {
						Ext.Msg
								.alert('Errore',
										"E' necessario salvare i dati del delegato prima di procedere")
					}
				}
			}, {
				text : 'Annulla',
				tabIndex : 23,
				handler : function() {
					window.close();
				}
			}]
		});
		window.show();
	};

	var createInsertDelegateWindow = function(interventiForm) {

		Ext.require('wcs.store.StatoStore');
		var window = Ext.create('Ext.window.Window', {
			title : 'Inserimento dati Delegato (persona fisica)',
			height : 665,
			width : 1000,
			modal : true,
			layout : 'fit',
			items : {
				frame : true,
				autoScroll : true,
				xtype : 'form',
				bodyStyle : 'padding:5px 5px 0',
				defaults : {
					labelWidth : 100
				},
				items : [{
					xtype : 'container',
					anchor : '100%',
					layout : 'column',
					items : [{
						xtype : 'container',
						columnWidth : .5,
						layout : 'anchor',
						items : [{
									xtype : 'textfield',
									fieldLabel : '<b>Cognome</b>*',
									tabIndex : 7,
									maxLength : 255,
									maxLengthText : 'Lunghezza massima 255 caratteri',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									id : 'wcs_delegatoCognome',
									name : 'anagraficaCognome',
									anchor : '95%'
								}, {
									xtype : 'combo',
									displayField : 'name',
									valueField : 'value',
									store : wcs_anagraficaCittadinanzaStore,
									typeAhead : true,
									id : 'wcs_delegatoCittadinanza',
									fieldLabel : 'Cittadinanza*',
									forceSelection : true,
									listeners : {
										select : {
											fn : function(component, record) {
												var stato = record[0].data.name
														.toUpperCase();
												if (stato != 'ITALIA') {
													Ext
															.getCmp('wcs_delegatoCodiceFiscale')
															.clearInvalid();
													Ext
															.getCmp('wcs_delegatoForzaCodiceFiscale')
															.setDisabled(false);
												} else {
													Ext
															.getCmp('wcs_delegatoForzaCodiceFiscale')
															.setDisabled(true);
													Ext
															.getCmp('wcs_delegatoForzaCodiceFiscale').checked = false;
												}
											}
										}
									},
									tabIndex : 9,
									queryMode : 'local',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									name : 'anagraficaCittadinanza',
									anchor : '95%'
								}]
					}, {
						xtype : 'container',
						columnWidth : .5,
						layout : 'anchor',
						items : [{
									xtype : 'textfield',
									maxLength : 255,
									tabIndex : 8,
									maxLengthText : 'Lunghezza massima 255 caratteri',
									fieldLabel : '<b>Nome</b>*',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									name : 'anagraficaNome',
									anchor : '95%'
								}, {
									xtype : 'combo',
									queryMode : 'local',
									displayField : 'name',
									valueField : 'value',
									tabIndex : 10,
									typeAhead : true,
									store : wcs_sessoStore,
									fieldLabel : 'Sesso*',
									id : 'wcs_delegatoSesso',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									name : 'anagraficaSesso',
									anchor : '95%'
								}, {
									xtype : 'container',
									layout : 'column',
									anchor : '95%',
									items : [{
										xtype : 'container',
										columnWidth : .6,
										layout : 'anchor',
										items : [{
											xtype : 'textfield',
											id : 'wcs_delegatoCodiceFiscale',
											fieldLabel : 'Codice fiscale*',
											tabIndex : 12,
											allowBlank : false,
											listeners : {
												change : {
													fn : function(a, b, c, d) {
														var stato = Ext
																.getCmp('wcs_delegatoCittadinanza').rawValue
																.toUpperCase();
														if (stato != 'ITALIA') {
															this.vtype = '';
															this.allowBlank = true;
														} else {
															this.vtype = 'CodiceFiscale';
															this.allowBlank = false;
														}
													}
												}
											},
											validator : function(value) {
												if (/^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/
														.test(value)) {
													return true;
												} else {
													return 'Deve essere un codice fiscale corretto';
												}
											},
											name : 'anagraficaCodiceFiscale'
										}]
									}, {
										xtype : 'container',
										columnWidth : .4,
										layout : 'anchor',
										items : [{
											xtype : 'checkbox',
											fieldLabel : 'Forzatura',
											disabled : true,
											listeners : {
												change : function(component) {
													var codiceFiscale = Ext
															.getCmp('wcs_delegatoCodiceFiscale');
													if (component.checked) {
														codiceFiscale.allowBlank = true;
													} else {
														codiceFiscale.allowBlank = false;
													}
												}
											},
											id : 'wcs_delegatoForzaCodiceFiscale',
											name : 'anagraficaForzaCodiceFiscale'
										}]
									}]
								}
						]
					}]
				}, {
					xtype : 'fieldset',
					title : 'Dati residenza',
					collapsible : false,
					id : 'wcs_delegatoDatiResidenza',
					defaultType : 'textfield',
					layout : 'anchor',
					items : [{
						xtype : 'container',
						layout : 'column',
						items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
								xtype : 'wcs_statoremotecombo',
								hiddenName : 'codStato',
								valueField : 'codStato',
								tabIndex : 19,
								displayField : 'desStato',
								store : new wcs.store.StatoStore({
											id : 'wcs_statoResidenzaStore'
										}),
								id : 'wcs_delegatoStatoDiResidenza',
								allowBlank : true,
								fieldLabel : 'Stato di residenza',
								name : 'anagraficaStatoDiResidenza',
								listeners : {
									select : function(combo, record, index) {
										var forceSelection = true;
										this.store.removeAll();
										this.minChars = 3;
										Ext
												.getCmp('wcs_delegatoProvinciaResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoProvinciaEsteraResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoComuneResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoComuneEsteroResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoCapResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoViaResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoViaNoTsResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoNoTsResidenza')
												.setRawValue('');
										if (record[0].data.desStato
												.toUpperCase() != 'ITALIA') {
											forceSelection = false;
											Ext
													.getCmp('wcs_delegatoCapResidenza').maxLength = 10;
											Ext
													.getCmp('wcs_delegatoCapResidenza').minLength = 0;
											Ext
													.getCmp('wcs_delegatoCapResidenza').vtype = '';
											Ext
													.getCmp('wcs_delegatoCapResidenza').maxLengthText = 'La lunghezza massima del campo è 10 caratteri';
										} else {
											Ext
													.getCmp('wcs_delegatoCapResidenza').maxLength = 5;
											Ext
													.getCmp('wcs_delegatoCapResidenza').vtype = 'FiveNums';
											Ext
													.getCmp('wcs_delegatoCapResidenza').minLength = 5;
										}
										Ext
												.getCmp('wcs_delegatoProvinciaResidenza').forceSelection = forceSelection;
										Ext
												.getCmp('wcs_delegatoComuneResidenza').forceSelection = forceSelection;
										Ext.getCmp('wcs_delegatoViaResidenza').forceSelection = forceSelection;
										Ext
												.getCmp('wcs_delegatoCivicoResidenza').forceSelection = forceSelection; // forceSelection;
									},
									beforequery : function(obj, options) {
										var value = Ext
												.getCmp('wcs_delegatoStatoDiResidenza')
												.getValue();
										if (value == null) {
											Ext
													.getCmp('wcs_delegatoProvinciaResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoProvinciaEsteraResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoComuneResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoComuneEsteroResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoCapResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoViaResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoViaNoTsResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoCivicoResidenza')
													.setRawValue('');
											Ext
													.getCmp('wcs_delegatoCivicoNoTsResidenza')
													.setRawValue('');
										}
									}
								},
								anchor : '95%'
							}, {
								xtype : 'wcs_comuneremotecombo',
								hiddenName : 'codComune',
								valueField : 'codComune',
								tabIndex : 21,
								displayField : 'desComune',
								allowBlank : true,
								fieldLabel : 'Comune di residenza',
								id : 'wcs_delegatoComuneResidenza',
								name : 'anagraficaComuneResidenza',
								listeners : {
									beforequery : function(obj, options) {
										this.store.removeAll();
										this.minChars = 3;
										this.store.proxy.extraParams = {
											codStato : Ext
													.getCmp('wcs_delegatoStatoDiResidenza')
													.getValue(),
											codProv : Ext
													.getCmp('wcs_delegatoProvinciaResidenza')
													.getValue()
										};
									},
									select : function(combo, record, index) {
										Ext
												.getCmp('wcs_delegatoComuneEsteroResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoCapResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoViaResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoViaNoTsResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoNoTsResidenza')
												.setRawValue('');
										var forceSelection = true;
										if (record[0].data.desComune
												.toUpperCase() != 'TRIESTE') {
											forceSelection = false;
										}
										Ext.getCmp('wcs_delegatoViaResidenza').forceSelection = forceSelection;
										Ext
												.getCmp('wcs_delegatoCivicoResidenza').forceSelection = forceSelection; // forceSelection;
									}
								},
								anchor : '95%'
							}, {
								xtype : 'hiddenfield',
								id : 'wcs_delegatoComuneEsteroResidenza',
								name : 'anagraficaComuneEsteroResidenza'
							}, {
								tabIndex : 23,
								xtype : 'textfield',
								fieldLabel : 'CAP residenza',
								id : 'wcs_delegatoCapResidenza',
								name : 'anagraficaCapResidenza',
								anchor : '95%'
							}]
						}, {
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
								xtype : 'wcs_provinciaremotecombo',
								hiddenName : 'codProvincia',
								valueField : 'codProvincia',
								tabIndex : 20,
								displayField : 'desProvincia',
								allowBlank : true,
								blankText : 'Questo campo è obbligatorio',
								fieldLabel : 'Provincia di residenza',
								id : 'wcs_delegatoProvinciaResidenza',
								name : 'anagraficaProvinciaResidenza',
								listeners : {
									beforequery : function(obj, options) {
										this.store.removeAll();
										this.minChars = 3;
										this.store.proxy.extraParams = {
											codStato : Ext
													.getCmp('wcs_delegatoStatoDiResidenza')
													.getValue()
										};
									},
									select : function(combo, record, index) {
										Ext
												.getCmp('wcs_delegatoComuneResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoComuneEsteroResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoCapResidenza')
												.setRawValue('');
										Ext.getCmp('wcs_delegatoViaResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoViaNoTsResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoNoTsResidenza')
												.setRawValue('');
									}
								},
								anchor : '95%'
							}, {
								xtype : 'hiddenfield',
								id : 'wcs_delegatoProvinciaEsteraResidenza',
								name : 'anagraficaProvinciaEsteraResidenza'
							}, {
								xtype : 'wcs_viaremotecombo',
								hiddenName : 'codVia',
								tabIndex : 22,
								valueField : 'codVia',
								displayField : 'desVia',
								id : 'wcs_delegatoViaResidenza',
								fieldLabel : 'Via di residenza',
								allowBlank : true,
								blankText : 'Questo campo è obbligatorio',
								name : 'anagraficaViaResidenza',
								listeners : {
									beforequery : function(obj, options) {
										this.store.removeAll();
										this.minChars = 2;
										this.store.proxy.extraParams = {
											codStato : Ext
													.getCmp('wcs_delegatoStatoDiResidenza')
													.getValue(),
											codProv : Ext
													.getCmp('wcs_delegatoProvinciaResidenza')
													.getValue(),
											codComune : Ext
													.getCmp('wcs_delegatoComuneResidenza')
													.getValue()
										};
										Ext
												.getCmp('wcs_delegatoViaNoTsResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoResidenza')
												.setRawValue('');
										Ext
												.getCmp('wcs_delegatoCivicoNoTsResidenza')
												.setRawValue('');
									}
								},
								anchor : '95%'
							}, {
								xtype : 'hiddenfield',
								id : 'wcs_delegatoViaNoTsResidenza',
								name : 'anagraficaViaNoTsResidenza'
							}, {
								xtype : 'wcs_civicoremotecombo',
								hiddenName : 'codCivico',
								tabIndex : 24,
								valueField : 'codCivico',
								displayField : 'desCivico',
								fieldLabel : 'Civico di residenza',
								id : 'wcs_delegatoCivicoResidenza',
								allowBlank : true,
								name : 'anagraficaCivicoResidenza',
								listeners : {
									beforequery : function(obj, options) {
										this.store.removeAll();
										this.minChars = 1;
										this.store.proxy.extraParams = {
											codStato : Ext
													.getCmp('wcs_delegatoStatoDiResidenza')
													.getValue(),
											codProv : Ext
													.getCmp('wcs_delegatoProvinciaResidenza')
													.getValue(),
											codComune : Ext
													.getCmp('wcs_delegatoComuneResidenza')
													.getValue(),
											codVia : Ext
													.getCmp('wcs_delegatoViaResidenza')
													.getValue()
										};
										Ext
												.getCmp('wcs_delegatoCivicoNoTsResidenza')
												.setRawValue('');
									}
								},
								anchor : '95%'
							}, {
								xtype : 'hiddenfield',
								id : 'wcs_delegatoCivicoNoTsResidenza',
								name : 'anagraficaCivicoNoTsResidenza'
							}]
						}]
					}]
				}, {
					xtype : 'fieldset',
					title : 'Contatti',
					collapsible : false,
					defaultType : 'textfield',
					layout : 'anchor',
					items : [{
						xtype : 'container',
						layout : 'column',
						items : [{
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
								xtype : 'textfield',
								fieldLabel : 'Telefono ',
								maxLength : 20,
								tabIndex : 31,
								maxLengthText : 'Lunghezza massima 20 caratteri',
								allowBlank : true,
								di : 'wcs_delegatoTelefono',
								name : 'anagraficaTelefono',
								anchor : '95%'

							}]
						}, {
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
								xtype : 'textfield',
								fieldLabel : 'Cellulare',
								tabIndex : 32,
								maxLength : 20,
								maxLengthText : 'Lunghezza massima 20 caratteri',
								id : 'wcs_delegatoCellulare',
								name : 'anagraficaCellulare',
								anchor : '95%'

							}]
						}, {
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
								xtype : 'textfield',
								fieldLabel : 'Email',
								tabIndex : 33,
								maxLength : 200,
								maxLengthText : 'Lunghezza massima 200 caratteri',
								id : 'wcs_delegatoEmail',
								name : 'anagraficaEmail',
								vtype : 'email',
								vtypeText : 'Il campo deve essere un indirizzo email valido',
								anchor : '95%'

							}]
						}]
					}, {
						xtype : 'container',
						anchor : '100%',
						layout : 'column',
						items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
								xtype : 'checkbox',
								name : 'anagraficaNotificaSMS',
								id : 'wcs_delegatoNotificaSMS',
								tabIndex : 34,
								fieldLabel : 'Avvisa via SMS',
								listeners : {
									change : function(component) {
										var cellulare = Ext
												.getCmp('wcs_delegatoCellulare');
										if (component.checked) {
											cellulare.allowBlank = false;
										} else {
											cellulare.allowBlank = true;
										}
									}
								}
							}]
						}, {
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
								xtype : 'checkbox',
								name : 'anagraficaNotificaEmail',
								tabIndex : 35,
								id : 'wcs_delegatoNotificaEmail',
								fieldLabel : 'Avvisa via email',
								listeners : {
									change : function(component) {
										var email = Ext
												.getCmp('wcs_delegatoEmail');
										if (component.checked) {
											email.allowBlank = false;
										} else {
											email.allowBlank = true;
										}
									}
								}
							}]
						}]
					}]
				}],
				buttons : [{
					text : 'Conferma',
					tabIndex : 36,
					handler : function() {
						var form = window.items.first().getForm();
						if (form.isValid()) {
							newDelegatoData = form.getValues();
							log('dati delegato:');
							log(newDelegatoData);

							Ext
									.getCmp('wcs_InterventiFormBeneficiarioDes')
									.setValue(newDelegatoData.anagraficaCognome
													+ ' '
													+ newDelegatoData.anagraficaNome);
							Ext.getCmp('wcs_InterventiFormInsertNewDelegato')
									.setValue('true');
							Ext.getCmp('iban_delegato').setValue("");
							window.close();
						}
					}
				}, {
					text : 'Annulla',
					tabIndex : 37,
					handler : function() {
						window.close();
					}
				}]
			}
		});
		if (Ext.getCmp('wcs_InterventiFormInsertNewDelegato').getValue() == 'true') {
			window.items.first().getForm().setValues(newDelegatoData);
		}
		window.show();
	};

	var openIseeWindow = function(interventiForm) {

		var window;
		var isee = Ext.getCmp('wcs_paiISEE').getValue();
		var isee2 = Ext.getCmp('wcs_paiISEE2').getValue();
		var isee3 = Ext.getCmp('wcs_paiISEE3').getValue();
		window = Ext.create('Ext.window.Window', {
					title : 'Calcolo isee ',
					height : 280,
					width : 640,
					modal : false,
					layout : 'fit',
					items : {
						xtype : 'wcs_calcoloisee'
					},
					buttons : [{
								text : 'Copia isee',
								tabIndex : 21,
								handler : function() {
									copiaIseeRicalcolato(window, interventiForm);
								}
							}]

				});
		log('finestra creata');
		var form = window.items.first().getForm();

		if (isee != '') {
			form.findField('importo_isee').setValue(isee);
		}
		if (isee2 != '') {
			form.findField('importo_isee2').setValue(isee2);
		}
		if (isee3 != '') {
			form.findField('importo_isee3').setValue(isee3);
		}
		window.show();
	}

	var openCalcoloWindow = function(interventiForm) {

		var window;
		var isee = Ext.getCmp('wcs_paiISEE').getValue();
		if (Ext.getCmp('wcs_interventoTipo').getValue() == 'AD013') {
			window = Ext.create('Ext.window.Window', {
						title : 'Calcolo del massimo erogabile ',
						height : 280,
						width : 640,
						modal : false,
						layout : 'fit',
						items : {
							xtype : 'wcs_calcoloimporto'
						},
						buttons : [{
									text : 'Copia massimo erogabile',
									tabIndex : 21,
									handler : function() {
										getMassimoErogabile(window);
									}
								}]

					});
			log('finestra creata');
			var form = window.items.first().getForm();

			if (isee != '') {
				form.findField('importo_isee').setValue(isee);
			}
			window.show();
		} else if (Ext.getCmp('wcs_interventoTipo').getValue() == 'DI019') {
			window = Ext.create('Ext.window.Window', {
						title : 'Calcolo del massimo erogabile ',
						height : 280,
						width : 640,
						modal : false,
						layout : 'fit',
						items : {
							xtype : 'wcs_calcoloimporto_trasporti'
						},
						buttons : [{
									text : 'Copia massimo erogabile',
									tabIndex : 21,
									handler : function() {
										getMassimoErogabile(window);
									}
								}]

					});
			log('finestra creata');
			var form = window.items.first().getForm();

			if (isee != '') {
				form.findField('importo_isee').setValue(isee);
			}

			window.show();
		} else {
			if (isee != '' && Number(isee) > 7500) {
				Ext.MessageBox.show({
					title : 'Errore',
					msg : 'Attenzione l isee è superiore a 7500 euro massimo previsto per gli interventi economici.',
					buttons : Ext.MessageBox.OK,
					icon : Ext.window.MessageBox.ERROR
				});
			} else {
				window = Ext.create('Ext.window.Window', {
							title : 'Calcolo del massimo erogabile ',
							height : 280,
							width : 640,
							modal : false,
							layout : 'fit',
							items : {
								xtype : 'wcs_calcoloimportoeco'
							},
							buttons : [{
										text : 'Copia massimo erogabile',
										tabIndex : 21,
										handler : function() {
											getMassimoErogabile(window);
										}
									}]

						});
				window.show();
			}
		}

		log('finestramostrata');

	};

	function getMassimoErogabile(window) {

		var form = window.items.first().getForm();
		var data = form.getValues();
		massimoErogabile = data.massimo_erogabile;
		Ext.getCmp('wcs_interventoQuantita').setValue(massimoErogabile);
		if (Ext.getCmp('wcs_interventoTipo').getValue() == 'AD013') {
			Ext.getCmp('wcs_interventoQuantita').setReadOnly(true);
		}

		window.close();

	};

	function copiaIseeRicalcolato(window, interventiForm) {

		var form = window.items.first().getForm();
		var data = form.getValues();
		iseeRicalcolato = data.isee_ricalcolato;
		if (interventiForm.form.findField('ds_isee_ricalc') == null) {
			alert("Attenzione questo intervento non prevede questo dato specifico");
		} else {
			interventiForm.form.findField('ds_isee_ricalc')
					.setValue(iseeRicalcolato);
		}
		window.close();

	};

	// ############################################## Main Form
	// ####################################################################

	Ext.define('wcs.view.pai.InterventiForm', {
		extend : 'Ext.form.Panel',
		alias : 'widget.wcs_interventiform',

		frame : true,
		autoScroll : true,
		defaults : {
			labelWidth : 100,
			anchor : '99%'
		},

		initComponent : function() {

			var tariffaCaricata;
			var fieldset_inserimento_in_struttura_aperto = false;

			var tipoStore = wcs_tipologiaInterventoStore;
			tipoStore.load();

			function setTipoStoreFilter(codClasse) {
				if (tipoStore.isFiltered()) {
					tipoStore.clearFilter(true);
				}
				tipoStore.filter('codClasse', codClasse);
			}

			setTipoStoreFilter(wcs_interventoClasseStore.first().get('value'));
			tipoStore.clearFilter(true);

			var setTariffaCaricata = this.setTariffaCaricata = function(
					idTariffa) {
				tariffaCaricata = idTariffa;
				Ext.getCmp('wcs_tariffa').setValue(idTariffa);
			};

			var impostaCalcolaDurataFunc = this.impostaCalcolaDurataFunc = function(
					data) {

				var useDurata = data.flgFineDurata != 'F';
				calcolaDurata = useDurata ? function() {
					log('recupero durata');
					return Ext.getCmp('wcs_paiInterventoDurataMesi').getValue();
				}
						: function() {
							log('calcolo durata come fine-inizio');
							if (Ext.getCmp('wcs_paiInterventoDataAvvio')
									.getValue() != null
									&& Ext.getCmp('wcs_paiInterventoDataAvvio')
											.getValue() != '') {
								log('Sto usando data avvio');
								return (Math
										.round(Ext
												.getCmp('wcs_paiInterventoDataFine')
												.getValue().getTime()
												- Ext
														.getCmp('wcs_paiInterventoDataAvvio')
														.getValue().getTime())
										/ (1000 * 60 * 60 * 24) + 1).toFixed();
							} else {
								log('Sto usando data avvio proposta');
								return (Math
										.round(Ext
												.getCmp('wcs_paiInterventoDataFine')
												.getValue().getTime()
												- Ext
														.getCmp('wcs_paiInterventoDataAvvioProposta')
														.getValue().getTime())
										/ (1000 * 60 * 60 * 24) + 1).toFixed();
							}
						};
			};

			function isQtSettimanaleUnsafe() {

				return Ext.getCmp('wcs_interventoTipo').store.findRecord(
						'value', Ext.getCmp('wcs_interventoTipo').getValue()).data.label
						.match(/sett/i) ? true : false;
			}

			function isPid() {

				var codTipInt = Ext.getCmp('wcs_interventoTipo').getValue();
				if (codTipInt == 'AZ017' || codTipInt == 'AZ018'
						|| codTipInt == 'AZ016') {
					return true;
				} else {
					return false;
				}
			}

			function monthDiff(d1, d2) {

				var months;
				months = (d2.getFullYear() - d1.getFullYear()) * 12;
				months -= d1.getMonth() + 1;
				months += d2.getMonth();
				return months <= 0 ? 0 : months;
			}

			function isQtSettimanale() {

				try {
					return isQtSettimanaleUnsafe();
				} catch (e) {
					log('error in isQtSettimanale = ', e);
					return false;
				}
			}

			function weeks_between(date1, date2) {

				// The number of milliseconds in one week
				var ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
				// Convert both dates to milliseconds
				var date1_ms = date1.getTime();
				var date2_ms = date2.getTime();
				// Calculate the difference in milliseconds
				var difference_ms = Math.abs(date1_ms - date2_ms);
				// Convert back to weeks and return hole weeks
				return Math.floor(difference_ms / ONE_WEEK);
			}

			var onSelectTipoIntervento = function(component, record) {

				log('selected tipInt = ', record);
				if (!record[0]) {
					log('null data, exit');
					return;
				}
				var tipoIntervento = record[0].data.value;
				if (tipoIntervento == 'DI019') {
					Ext.getCmp('calculationButton').show();
				}
				if (tipoIntervento == 'AD013') {
					Ext.getCmp('wcs_interventoQuantita').setReadOnly(true);
				} else {
					Ext.getCmp('wcs_interventoQuantita').setReadOnly(false);
				}
				var impStdCosto = record[0].data.impStdCosto;
				var label = record[0].data.label;
				var maxDurataMesi = record[0].data.maxDurataMesi;
				var codPai = Ext.getCmp('wcs_paiCodPai').getValue();

				Ext.getCmp("wcs_interventoQuantita").el.dom.firstChild.lastChild.nodeValue = label;
				Ext.getCmp('wcs_interventiImpStdCosto').setValue(impStdCosto);

				Ext.getCmp('wcs_paiInterventoDurataMesi')
						.setMaxValue(maxDurataMesi);

				var useDurata = record[0].data.flgFineDurata != 'F';
				log('durata intervento in mesi = ', useDurata);

				Ext.getCmp('wcs_paiInterventoDataFine').setVisible(!useDurata);
				Ext.getCmp('wcs_paiInterventoDataFine').setDisabled(useDurata);
				Ext.getCmp('wcs_paiInterventoDurataMesi').setVisible(useDurata);
				Ext.getCmp('wcs_paiInterventoDurataMesi')
						.setDisabled(!useDurata);
				impostaCalcolaDurataFunc(record[0].data);

				var fieldset = Ext.getCmp('wcs_interventiDatiSpecifici');
				var interventoImpegnoList = Ext
						.getCmp('wcs_interventoImpegnoList');
				interventoImpegnoList.store.proxy.extraParams = {
					codTipInt : tipoIntervento,
					codPai : codPai,
					action : 'read'
				};

				var interventoStrutturaList = Ext.getCmp('struttura');
				interventoStrutturaList.store.clearData();
				interventoStrutturaList.setValue("");
				interventoStrutturaList.store.proxy.extraParams = {
					codTipInt : tipoIntervento,
					action : 'lista'
				};
				interventoStrutturaList.store.load();
				// val=Ext.getCmp('wcs_interventoQuantita').getValue();
				var val = record[0].data.quantita;
				interventoImpegnoList.costoUnitario = val;
				log('should update value for comp ' + interventoImpegnoList
						+ ' with value ' + val);
				interventoImpegnoList.store.load();
				var cnt = '';
				if (Ext.getCmp('wcs_interventiFormCntTipInt') != null) {
					cnt = Ext.getCmp('wcs_interventiFormCntTipInt').getValue();
				}
				var tipOld = '';
				if (Ext.getCmp('wcs_interventiFormCodTipIntHidden') != null) {
					tipOld = Ext.getCmp('wcs_interventiFormCodTipIntHidden')
							.getValue();
				}
				var cntOld = ''
				if (Ext.getCmp('wcs_interventiFormCntTipIntHidden') != null) {
					cntOld = Ext.getCmp('wcs_interventiFormCntTipIntHidden')
							.getValue();
				}
				populateDatiSpecifici(fieldset, tipoIntervento, cnt, codPai,
						tipOld, cntOld);

				Ext.Ajax.request({
							url : '/CartellaSociale/interventi',
							params : {
								action : 'verificaIntervento',
								codTipint : record[0].data.value,
								codPai : Ext.getCmp('wcs_paiCodPai').getValue()
							},
							success : function(response) {
								var json = Ext.JSON
										.decode(response.responseText);
								if (json.message != '') {
									Ext.MessageBox.show({
												title : 'Esito operazione',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												icon : Ext.window.MessageBox.ERROR
											});
								}
							}
						});
			};

			this.reader = Ext.create('Ext.data.reader.Reader', {

						model : 'model.InterventoModel',
						type : 'json',
						rootProperty : 'data',
						successProperty : 'success'
					});

			var interventiForm = this;

			var ricaricaTariffe = function() {

				var struttura = Ext.getCmp('struttura').getValue();
				var tariffaGrid = Ext.getCmp('tariffaGrid');
				tariffaGrid.store.proxy.extraParams = {
					action : 'list',
					idStruttura : struttura
				};
				tariffaGrid.store.load();
			};

			var cambioTariffa = function(record) {

				Ext.getCmp('wcs_interventoQuantita')
						.setValue(record.data.costo);
				Ext.getCmp('wcs_tariffa').setValue(record.data.id);
			};

			var aggiornaCosto = this.aggiornaCosto = function(component) {

				var form = component ? component.up('form') : this;

				log('aggiornamento costo');
				var values = form.getValues();
				var forfait = false;
				// può essere selezionata solo una tariffa.
				var tariffa = Ext.getCmp('tariffaGrid').getSelectionModel()
						.getSelection()[0]
				if (tariffa !== undefined) {
					if (tariffa.get('forfait') === 'Si') {
						forfait = true;
					}
				} else {
					// se la Grid non è selezionata, potrebbe
					// non essere ancora disegnata
					// (caricamento pagina in modifica)
					// verifico la form
					if (values.tariffa !== undefined) {
						var storeItems = Ext.getCmp('tariffaGrid').getStore().data.items;
						for (var z = 0; z < storeItems.length; z++) {
							if (storeItems[z].data.id === parseInt(
									values.tariffa, 10)) {
								forfait = storeItems[z].data.forfait === "Si";
								break;
							}
						}
					}
				}
				var struttura = Ext.getCmp('struttura').getValue();

				var dataPartenza = values.dataAvvio;
				if (dataPartenza == null || dataPartenza == "") {
					dataPartenza = values.dataAvvioProposta;
				}
				var dataFine = values.dataFine;
				var quantita = Number(values.quantita.replace(',', '.'));

				Ext.Ajax.request({
							url : '/CartellaSociale/interventi',
							params : {
								action : 'calcolaCosto',
								quantita : quantita,
								durataMesi : Ext
										.getCmp('wcs_paiInterventoDurataMesi')
										.getValue(),
								tipo : Ext.getCmp('wcs_interventoTipo')
										.getValue(),
								dataAvvio : dataPartenza,
								dataFine : dataFine,
								struttura : struttura,
								codPai : Ext.getCmp('wcs_paiCodPai').getValue(),
								cntTipint : Ext
										.getCmp('wcs_interventiFormCntTipIntHidden')
										.getValue(),
								forfait : forfait

							},
							success : function(conn, response) {

								var totale = 0;

								var result = Ext.JSON.decode(conn.responseText);
								log(result);
								if (result.importoUnitario === undefined) {
									totale = result.importo;

									log('totale dopo' + totale);
									Ext.getCmp('wcs_paiInterventoCostoTotale')
											.setValue(totale);
									valorizzaTotaleImpegni(totale);

								} else {
									costoUnitario = result.importoUnitario;
									Ext.getCmp('wcs_interventoQuantita')
											.setValue(costoUnitario);
								}
							}
						});
			};

			var valorizzaTotaleImpegni = function(totale) {
				var grid = Ext.getCmp('wcs_interventoImpegnoList');
				var items = grid.store.data.items;
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					totale = totale - item.data.aCarico;
				}
				var field = Ext
						.getCmp('wcs_interventoImpegnoListBBTotaleField');
				field.setValue(totale);
				if (totale < 0) {
					field.setFieldStyle('color:red');
				}
			};

			var impegniStore = Ext.create('wcs.store.InterventiImpegniStore', {
						autoLoad : false
					});

			this.items = [{
						xtype : 'hiddenfield',
						name : 'codTipIntHidden',
						id : 'wcs_interventiFormCodTipIntHidden'
					}, {
						xtype : 'hiddenfield',
						name : 'cntTipIntHidden',
						id : 'wcs_interventiFormCntTipIntHidden'
					}, {
						xtype : 'combo',
						displayField : 'name',
						valueField : 'value',
						store : wcs_interventoClasseStore,
						readOnly : wcs_interventoClasseRO,
						id : 'wcs_interventoClasse',
						fieldLabel : 'Classe*',
						editable : false,
						listeners : {
							change : function(combo, codClasse) {
								if (wcs_interventoClasseRO) {
									return;
								}
								log('scelta classe tipInt = ', codClasse);
								// Solo per interventi di tipo
								// economici, mosta nascondi
								// bottone per caocolare
								// l'importo da erogare.
								if (codClasse == 459) {
									Ext.getCmp('calculationButton').show();
									Ext.getCmp('iseeButton').show();

								} else {
									Ext.getCmp('calculationButton').hide();
									Ext.getCmp('iseeButton').hide();
								}
								// solo per interventi di tipo
								// professionale quantifichiamo
								// il tutto ad uno e lasciamo il
								// campo quantità mesi solo read
								// only
								if (codClasse == 457) {
									Ext.getCmp('wcs_paiInterventoDurataMesi')
											.setValue(1);
									Ext.getCmp('wcs_paiInterventoDurataMesi')
											.setReadOnly(true);
								}

								setTipoStoreFilter(codClasse);
								Ext.getCmp('wcs_interventoTipo').reset();
								cleanItems(Ext.getCmp('wcs_interventiDatiSpecifici'));
							}
						},
						allowBlank : false,
						blankText : 'Questo campo è obbligatorio',
						name : 'classe'
					}, {
						xtype : 'combo',
						queryMode : 'local',
						store : tipoStore,
						displayField : 'name',
						valueField : 'value',
						readOnly : wcs_interventoRO,
						id : 'wcs_interventoTipo',
						lastQuery : '',
						listeners : {
							select : onSelectTipoIntervento
							/*,
							afterrender : function(field) {
								setTimeout(function() {
											var event = document.createEvent("HTMLEvents");
											event.initEvent('click', true, true);
											Ext.getCmp('wcs_interventoTipo').getEl().down('input').dom.dispatchEvent(event);
											Ext.getCmp('wcs_interventoTipo').getEl().down('input').dom.dispatchEvent(event);
										}, 100);
							},

							beforequery : function(q) {
								if (q.query) {
									var length = q.query.length;
									q.query = new RegExp(Ext.escapeRe(q.query), 'i');
									q.query.length = length;
								}
							}*/
						},

						fieldLabel : 'Intervento*',
						editable : true,
						allowBlank : false,
						blankText : 'Questo campo è obbligatorio',
						name : 'tipo'
					}, {
						xtype : 'fieldcontainer',
						fieldLabel : 'Delegato',
						layout : {
							type : 'hbox',
							pack : 'start',
							align : 'middle'
						},
						items : [{
									xtype : 'hiddenfield',
									id : 'wcs_InterventiFormBeneficiarioCod',
									name : 'codBeneficiario'
								}, {
									xtype : 'hiddenfield',
									id : 'wcs_InterventiFormInsertNewDelegato',
									name : 'insertNewDelegato',
									value : 'false'
								}, {
									xtype : 'textfield',
									id : 'wcs_InterventiFormBeneficiarioDes',
									name : 'desBeneficiario',
									blankText : 'Questo campo è obbligatorio',
									readOnly : true,
									allowBlank : false,
									flex : 1
								}, {
									xtype : 'button',
									text : 'Ricerca persona fisica',
									margin : '0 5 0 5',
									handler : function() {
										Ext
												.widget('wcs_interventiRicercaBeneficiariPopup')
												.show();
									}
								}, {
									xtype : 'button',
									text : 'Inserisci persona fisica',
									margin : '0 5 0 0',
									handler : function() {
										createInsertDelegateWindow(interventiForm);
									}
								}, {
									xtype : 'button',
									text : 'Ricerca/Inserisci persona giuridica',
									handler : function() {
										createInsertDelegatoSocWindow(interventiForm);
									}
								}]
					}, {
						xtype : 'fieldset',
						title : 'Richiedente(da compilare solo se intervento per minore)',
						collapsed : true,
						collapsible : true,
						layout : 'anchor',
						items : [{
									xtype : 'combo',
									displayField : 'cognomeNome',
									valueField : 'codAnag',
									store : 'FamigliaSocialeStoreCombo',
									name : 'codRichiedente',
									id : 'wcs_interventoRichiedente',
									fieldLabel : 'Richiedente',
									editable : false,
									emptyText : 'Il servizio',
									submitEmptyText : false,
									forceSelection : true,
									anchor : '100%'
								}]
					}, {
						xtype : 'hiddenfield',
						name : 'cntTipint',
						id : 'wcs_interventiFormCntTipInt'
					}, {
						xtype : 'hiddenfield',
						name : 'impStdCosto',
						itemId : 'wcs_interventiImpStdCosto',
						id : 'wcs_interventiImpStdCosto'
					}, {
						title : 'Coordinate Bancarie Delegato <i>(solo se delegato)</i>',
						xtype : 'wcs_datibancarifieldset',
						collapsed : true
					}
					// fieldset Inserimento in struttura
					, {
						xtype : 'fieldset',
						title : 'Inserimento in struttura',
						id : 'fieldset_inserimento_in_struttura',
						collapsible : true,
						collapsed : true,
						items : [{
							xtype : 'container',
							anchor : '100%',
							layout : 'anchor',
							items : [{
								xtype : 'grid',
								title : 'Scegli familiare',
								store : 'FamigliaSocialeStore',
								selType : 'checkboxmodel',
								id : 'famigliaGrid',
								height : 250,
								selModel : {
									checkOnly : true,
									mode : 'MULTI'
								},
								plugins : [Ext.create(
										'Ext.grid.plugin.CellEditing', {
											clicksToEdit : 1
										})],
								columns : [{
											header : 'Cognome',
											dataIndex : 'cognome',
											sortable : true,
											flex : 1
										}, {
											header : 'Nome',
											dataIndex : 'nome',
											sortable : true,
											flex : 1
										}, {
											header : 'Relazione',
											dataIndex : 'desQual',
											sortable : false,
											flex : 1
										}, {
											header : 'Codice fiscale',
											dataIndex : 'codiceFiscale',
											sortable : true,
											width : 120
										}, {
											header : 'Comune di nascita',
											dataIndex : 'comuneNascitaDes',
											sortable : false,
											flex : 1
										}, {
											header : 'Sesso',
											dataIndex : 'sesso',
											hidden : false,
											sortable : true,
											width : 50
										}, {
											header : 'Data di nascita',
											dataIndex : 'dataNascita',
											hidden : false,
											sortable : true,
											width : 80
										}, {
											header : 'Comune di residenza',
											dataIndex : 'comuneResidenzaDes',
											sortable : false,
											flex : 1
										}, {
											header : 'Codice anagrafica famigliare',
											dataIndex : 'codAnaFamigliare',
											hidden : true,
											sortable : false
										}, {
											header : 'Codice anagrafica',
											dataIndex : 'codAnag',
											hidden : true,
											sortable : false
										}, {
											header : 'Retta giornaliera',
											dataIndex : 'costo_famigliare',
											sortable : false,
											renderer : Euro,
											field : {
												xtype : 'wdecimalnumberfield'
											}
										}],
								listeners : {
									deselect : function(grid, record) {
										record.set('selezionato', false);
									},
									select : function(grid, record) {
										record.set('selezionato', true);
									}
								}
							}, {
								xtype : 'combo',
								displayField : 'nome',
								valueField : 'id',
								store : 'StrutturaStoreCombo',
								name : 'struttura',
								id : 'struttura',
								fieldLabel : 'Struttura',
								editable : false,

								forceSelection : true,
								anchor : '100%',
								listeners : {
									change : function(combo, record, index) {
										ricaricaTariffe(this);
									}
								}
							}, {
								xtype : 'numberfield',
								name : 'riduzioni',
								id : 'riduzioni',
								fieldLabel : 'Numero di giorni per settimana/mese',
								listeners : {
									change : function(combo, record, index) {
										aggiornaCosto(this);
									}
								}
							}, {
								xtype : 'grid',
								title : 'Scegli tariffa',
								store : 'TariffaStore',
								selType : 'checkboxmodel',
								id : 'tariffaGrid',
								height : 250,
								selModel : {
									checkOnly : true,
									mode : 'SINGLE',
									allowDeselect : false,
									getHeaderConfig : function() {
										var me = this;
										/*
										 * Cerca
										 * Ext.selection.CheckboxModel.getHeaderConfig
										 * nei sorgenti
										 * di Ext ....
										 */
										return {
											width : me.headerWidth,
											renderer : Ext.Function.bind(
													me.renderer, me)
										};
									}
								},
								columns : [{
											header : 'Anno',
											dataIndex : 'anno',
											sortable : true,
											width : 60
										}, {
											header : 'Descrizione',
											dataIndex : 'descrizione',
											sortable : true,
											width : 650
										}, {
											header : 'Costo',
											dataIndex : 'costo',
											sortable : true,
											width : 100,
											renderer : Euro
										}, {
											header : 'Forfait',
											dataIndex : 'forfait',
											sortable : false,
											width : 50
										}],
								listeners : {
									afterrender : function(field) {
										fieldset_inserimento_in_struttura_aperto = true;
										log(
												'afterrender fieldset_inserimento_in_struttura_aperto',
												fieldset_inserimento_in_struttura_aperto);

										if (tariffaCaricata !== undefined) {
											var storeItems = this.getStore().data.items;
											for (var z = 0; z < storeItems.length; z++) {
												if (storeItems[z].data.id === tariffaCaricata) {
													this
															.getSelectionModel()
															.select(
																	storeItems[z],
																	false);
													break;
												}
											}
										}
									},
									select : function(grid, record) {
										if (tariffaCaricata === undefined) {
											cambioTariffa(record);
										} else {
											// nel caso sia il primo caricamento (selezionato automaticamente da afterrender) non eseguo il
											// cambio tariffa per non sovrascrivere l'eventuale modifica manuale
											tariffaCaricata = undefined;
										}
									}
								}
							}, {
								xtype : 'hiddenfield',
								id : 'wcs_tariffa',
								name : 'tariffa'
							}]
						}]
					},
					// Dati intervento
					{
						xtype : 'fieldset',
						title : 'Dati intervento',
						collapsible : true,
						defaultType : 'textfield',
						layout : 'anchor',
						items : [{
							xtype : 'container',
							layout : 'column',
							anchor : '95%',
							items : [
									// RIGA 1
									{
								xtype : 'container',
								columnWidth : .3,
								layout : 'anchor',
								items : [
										// Quantità*
										{
									xtype : 'wdecimalnumberfield',
									hideTrigger : true,
									id : 'wcs_interventoQuantita',
									keyNavEnabled : false,
									mouseWheelEnabled : false,
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									maxLength : 9,
									fieldLabel : 'Quantità*',
									name : 'quantita',
									listeners : {
										change : function(component) {
											aggiornaCosto(component);
										}
									},
									minValue : 0,
									allowDecimals : true,
									value : 0,
									anchor : '97%'
								},
										// labelQuantita
										{
											xtype : 'hiddenfield',
											name : 'labelQuantita'
										}]
							},
									// Data inizio
									// proposta*
									{
										columnWidth : .3,
										xtype : 'datefield',
										margin : '0 5 0 0',
										format : 'd/m/Y',
										fieldLabel : 'Data inizio proposta*',
										id : 'wcs_paiInterventoDataAvvioProposta',
										name : 'dataAvvioProposta',
										allowBlank : false,
										blankText : 'Questo campo è obbligatorio',
										listeners : {
											change : function(component) {
												aggiornaCosto(component);
											}
										}
									},
									// Durata mesi*
									{
										columnWidth : .3,
										xtype : 'numberfield',
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										maxLength : 9,
										maxValue : 12,
										blankText : 'Questo campo è obbligatorio',
										allowBlank : true,
										fieldLabel : 'Durata mesi*',
										name : 'durataMesi',
										id : 'wcs_paiInterventoDurataMesi',
										minValue : 1,
										allowDecimals : false,
										value : 0,
										listeners : {
											change : function(component) {
												aggiornaCosto(component);
											}
										}
									},
									// Data fine*
									{
										columnWidth : .3,
										xtype : 'datefield',
										format : 'd/m/Y',
										fieldLabel : 'Data fine*',
										name : 'dataFine',
										hidden : true,
										allowBlank : false,
										disabled : true,
										id : 'wcs_paiInterventoDataFine',
										listeners : {
											change : function(component, value) {
												aggiornaCosto(component);
											}
										}
									}]
						},
								// RIGA 2
								{
									xtype : 'container',
									layout : 'column',
									columnWidth : .5,
									anchor : '95%',
									items : [
											// Data inizio
											{
										xtype : 'datefield',
										format : 'd/m/Y',
										fieldLabel : 'Data inizio',
										id : 'wcs_paiInterventoDataAvvio',
										name : 'dataAvvio',
										readOnly : wcs_interventoDataInizioRO,
										listeners : {
											change : function(component) {
												aggiornaCosto(component);
											}
										}
									}

									]
								},
								// Costo totale
								{
									columnWidth : .3,
									xtype : 'wdecimalnumberfield',
									name : "costoTotale",
									fieldLabel : 'Costo totale',
									readOnly : true,
									id : 'wcs_paiInterventoCostoTotale'
								},
								// Urgente
								{
									xtype : 'checkbox',
									name : 'urgente',
									fieldLabel : 'Urgente',
									inputValue : 'S'
								},
								// Validato
								{
									xtype : 'checkbox',
									name : 'approvazioneTecnica',
									hidden : wcs_isAutorizzazioneDisabled,
									disabled : wcs_isAutorizzazioneDisabled,
									fieldLabel : 'Validato',
									inputValue : 'S'
								},
								// Calcola massimo erogabile
								{
									id : 'calculationButton',
									xtype : 'button',
									text : 'Calcola massimo erogabile',
									hidden : true,
									handler : function() {
										log("bottone cliccato")
										openCalcoloWindow(interventiForm);
									}
								},
								// Ricalcolo ISEE
								{
									id : 'iseeButton',
									xtype : 'button',
									text : 'Ricalcolo ISEE',
									hidden : true,
									handler : function() {
										log("bottone cliccato")
										openIseeWindow(interventiForm);
									}
								},
								// Motivazione
								{
									id : 'wcs_paiInterventoMotivazione',
									anchor : '95%',
									xtype : 'textarea',
									fieldLabel : 'Motivazione',
									name : 'motivazione',
									margin : '10 0 10 0',
									hidden : wcs_interventoMotivazioneHidden
								}]
					}, {
						xtype : 'fieldset',
						title : 'Stato',
						collapsible : true,
						collapsed : true,
						defaults : {
							anchor : '95%'
						},
						layout : 'column',
						items : [{
									xtype : 'container',
									columnWidth : .3,
									layout : 'anchor',
									items : [{
												xtype : 'combo',
												displayField : 'name',
												valueField : 'value',
												store : wcs_interventoStatoStore,
												readOnly : true,
												fieldLabel : 'Stato',
												name : 'stato',
												anchor : '95%'
											}, {
												xtype : 'textfield',
												fieldLabel : 'Numero determina',
												name : 'numeroDetermina',
												anchor : '95%'
											}]
								}, {
									xtype : 'container',
									columnWidth : .3,
									layout : 'anchor',
									items : [{
										xtype : 'datefield',
										format : 'd/m/Y',
										readOnly : wcs_interventoDataAperturaRO,
										id : 'wcs_interventoDataApertura',
										fieldLabel : 'Data apertura',
										name : 'dataApertura',
										anchor : '95%'
									}, {
										xtype : 'textfield',
										fieldLabel : 'Protocollo Intervento',
										name : 'protocollo',
										anchor : '95%'
									}]
								}, {
									xtype : 'container',
									columnWidth : .3,
									layout : 'anchor',
									items : [{
										xtype : 'datefield',
										format : 'd/m/Y',
										readOnly : wcs_interventoDataEsecutivitaRO,
										fieldLabel : 'Data esecutività',
										name : 'dataEsecutivita',
										anchor : '95%'
									}]
								}]
					}, {
						xtype : 'fieldset',
						title : 'Dati originali intervento',
						collapsible : true,
						collapsed : true,
						defaults : {
							anchor : '95%'
						},
						layout : 'anchor',
						items : [{
									id : 'wcs_paiInterventoDatiOriginali',
									anchor : '95%',
									xtype : 'textarea',
									fieldLabel : 'Dati originali',
									editable : false,
									readOnly : true,
									name : 'datiOriginali',
									margin : '10 0 10 0'
								}]
					}, {
						xtype : 'fieldset',
						title : 'Dati chiusura',
						collapsible : true,
						collapsed : true,
						defaults : {
							anchor : '95%'
						},
						layout : 'anchor',
						items : [{
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
										xtype : 'combo',
										displayField : 'name',
										editable : false,
										valueField : 'value',
										store : wcs_interventoEsitoStore,
										readOnly : wcs_interventoClasseRO,
										id : 'wcs_interventoEsito',
										fieldLabel : 'Motivazione chiusura anticipata',
										name : 'esito',
										anchor : '95%'
									}]
						}, {
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
										xtype : 'datefield',
										editable : false,
										format : 'd/m/Y',
										readOnly : wcs_interventoDataChiusuraRO,
										fieldLabel : 'Data chiusura',
										name : 'dataChiusura',
										anchor : '95%'
									}]
						}, {
							xtype : 'container',
							columnWidth : .3,
							layout : 'anchor',
							items : [{
								xtype : 'textarea',
								id : 'wcs_interventoNoteChiusura',
								fieldLabel : 'Note chiusura',
								name : 'noteChiusura',
								maxLength : 1000,
								width : 800,
								maxLengthText : 'Lunghezza massima 1000 caratteri',
								anchor : '97%'
							}]
						}]
					}, {
						xtype : 'fieldset',
						title : 'Dati sospensione',
						collapsible : true,
						collapsed : true,
						defaults : {
							anchor : '95%'
						},
						layout : 'anchor',
						items : [{
									xtype : 'container',
									columnWidth : .3,
									layout : 'anchor',
									items : [{
												xtype : 'datefield',
												editable : false,
												format : 'd/m/Y',
												readOnly : true,
												fieldLabel : 'Data sospensione',
												name : 'dataSospensioneRead',
												anchor : '95%'
											}]
								}, {
									xtype : 'container',
									columnWidth : .3,
									layout : 'anchor',
									items : [{
										xtype : 'textarea',
										id : 'wcs_interventoMotivazioneSospensione',
										fieldLabel : 'Motivazione sospensione',
										name : 'noteSospensioneRead',
										maxLength : 1000,
										readOnly : true,
										width : 800,
										maxLengthText : 'Lunghezza massima 1000 caratteri',
										anchor : '97%'
									}]
								}]
					}, {
						xtype : 'fieldset',
						title : 'Dati specifici',
						id : 'wcs_interventiDatiSpecifici',
						readOnly : wcs_interventoDatiSpecificiRO,
						collapsible : true,
						collapsed : true,
						defaults : {
							anchor : '95%'
						},
						layout : 'anchor'
					}, {
						xtype : 'fieldset',
						title : 'Impegni',
						collapsed : true,
						id : 'wcs_impegniFieldset',
						collapsible : true,
						items : [

						{
							id : 'wcs_interventoImpegnoList',
							xtype : 'grid',
							title : 'Impegni',
							store : impegniStore,
							height : 250,
							bbar : Ext.create('Ext.PagingToolbar', {
								store : impegniStore,
								displayMsg : 'Visualizzo gli impegni da {0} a {1} di {2}',
								emptyMsg : 'Nessun impegno',
								items : ['-', {
									xtype : 'weuronumberfield',
									id : 'wcs_interventoImpegnoListBBTotaleField',
									readOnly : true,
									minValue : 0,
									fieldLabel : 'Differenza €',
									itemId : 'totaleField',
									name : 'totale',
									value : 0
								}]
							}),
							columns : [{
										header : 'Anno',
										dataIndex : 'anno',
										sortable : true,
										width : 70
									}, {
										header : 'Capitolo',
										dataIndex : 'capitolo',
										sortable : true,
										width : 80
									}, {
										header : 'Impegno',
										dataIndex : 'impegno',
										sortable : true,
										width : 80
									}, {
										header : 'Budget disponibile',
										dataIndex : 'importoDisponibile',
										xtype : 'numbercolumn',
										format : '0,000.00 €',
										sortable : false,
										flex : 1
									}, {
										header : 'Unita\' disponibili',
										dataIndex : 'unitaDisponibili',
										sortable : false,
										flex : 1
									}, {
										header : 'Costo previsto',
										dataIndex : 'aCarico',
										sortable : false,
										flex : 1,
										xtype : 'numbercolumn',
										format : '0,000.00 €',
										editor : {
											xtype : 'weuronumberfield',
											blankText : 'Questo campo è obbligatorio',
											allowBlank : false
										}
									}, {
										header : 'Budget',
										dataIndex : 'importoComplessivo',
										xtype : 'numbercolumn',
										format : '0,000.00 €',
										sortable : false,
										flex : 1
									}, {
										header : 'Centro el. di costo',
										dataIndex : 'centroElementareDiCosto',
										sortable : false,
										flex : 1
									}, {
										header : 'UOT',
										dataIndex : 'uot',
										sortable : false,
										width : 50,
										flex : 1
									}],
							plugins : [

							Ext.create('Ext.grid.plugin.RowEditing', {
								clicksToMoveEditor : 1,
								saveText : "Salva",
								cancelText : "Annulla",
								autoCancel : false,
								listeners : {
									afteredit : function() {
										aggiornaCosto(Ext
												.getCmp('wcs_interventoImpegnoList'));
									}
								}
							})]
						}]
					}, {
						xtype : 'fieldset',
						title : 'Cronologia',
						collapsible : true,
						collapsed : true,
						items : {
							xtype : 'wcs_interventocronologialist',
							id : 'wcs_interventoCronologiaList'
						}
					}, {
						xtype : 'fieldset',
						title : 'Pagamenti e contribuzioni',
						collapsible : true,
						collapsed : true,
						items : [{
							xtype : 'container',
							anchor : '100%',
							layout : 'column',
							items : [{
										xtype : 'wcs_interventopagamentilist',
										columnWidth : 0.5,
										id : 'wcs_interventoPagamentiList'
									}, {
										xtype : 'wcs_interventocontribuzionilist',
										columnWidth : 0.5,
										id : 'wcs_interventoContribuzioniList'
									}]
						}]
					}];

			this.buttons = [Ext.create('Ext.ux.VirtualKeyboardButton', {
								id : 'wcs_keyboardInterventiForm'
							}), {
						id : 'wcs_interventoFormSalva',
						text : 'Salva',
						handler : function() {

							log('fieldset_inserimento_in_struttura_aperto',
									fieldset_inserimento_in_struttura_aperto);
							var is_struttura_selezionata = Ext
									.getCmp('struttura').getValue();

							var familiare_selezionato = false;
							var is_tariffa_selezionata = false;

							if (fieldset_inserimento_in_struttura_aperto) {
								var num_familiari_selezionati = Ext
										.getCmp('famigliaGrid')
										.getSelectionModel().getSelection().length;
								familiare_selezionato = num_familiari_selezionati > 0;

								var num_tariffe_selezionate = Ext
										.getCmp('tariffaGrid')
										.getSelectionModel().getSelection().length
								is_tariffa_selezionata = num_tariffe_selezionate > 0;

							} else {
								// il_fieldset_interventi_in_struttura_non_è_stato_aperto ...
								function isSelezionato(element) {
									return element.data.selezionato;
								}

								var num_familiari_selezionati = Ext
										.getCmp('famigliaGrid').getStore().data.items
										.filter(isSelezionato).length;
								familiare_selezionato = num_familiari_selezionati > 0;

								is_tariffa_selezionata = false;
								if (tariffaCaricata) {
									is_tariffa_selezionata = true;
								}

							}

							if (familiare_selezionato) {
								if (is_struttura_selezionata
										&& is_tariffa_selezionata) {
									//ok
								} else {
									Ext.MessageBox
											.alert('Errore',
													'Verifica la selezione di struttura e tariffa.');
									return;
								}
							}

							if (is_struttura_selezionata) {
								if (!is_tariffa_selezionata) {
									Ext.MessageBox
											.alert('Errore',
													'Verifica la selezione di struttura e tariffa.');
									return;
								}
							}

							var form = this.up('form').getForm();
							var impegniStore = Ext
									.getCmp('wcs_interventoImpegnoList').store;
							var interventiFieldset = Ext.getCmp('wcs_paiTab').items
									.get('wcs_paiForm').items
									.get('interventiFieldset');
							var win = this.up('window');
							var civilmenteObbligatoStore = null;
							var selectedRowIndexes = [];
							var famigliaItems = Ext.getCmp('famigliaGrid')
									.getStore().data.items;

							for (var i = 0; i < famigliaItems.length; i++) {
								if (famigliaItems[i].get('selezionato')
										&& famigliaItems[i].get('tipo') !== "1"
										&& famigliaItems[i].get('tipo') !== "0") {
									//se è selezionato ed è un figlio
									//gli altri selezionati sono solo in sola lettura
									selectedRowIndexes.push(famigliaItems[i]
											.get('codAnag')
											+ ';'
											+ famigliaItems[i]
													.get('costo_famigliare'));
								}
							}
							if (Ext
									.getCmp('wcs_ricercaCivilmenteObbligatoGrid') != undefined) {
								civilmenteObbligatoStore = Ext
										.getCmp('wcs_ricercaCivilmenteObbligatoGrid').store;
							}
							if (form.isValid()) {
								var valoreImpegno = Ext
										.getCmp('wcs_interventoImpegnoListBBTotaleField')
										.getValue();
								var codPai = Ext.getCmp('wcs_paiCodPai')
										.getValue();
								if (new Number(valoreImpegno) >= 0) {

									form.submit({
										url : '/CartellaSociale/interventi',
										waitTitle : 'Salvataggio',
										waitMsg : 'Sto salvando i dati...',
										params : {
											newDelegatoData : Ext.JSON
													.encode(newDelegatoData),
											codPai : codPai,
											codOpe : wcs_codOperatore,
											impegniStore : createJSONFromImpegniStore(impegniStore),
											importoCivilmentObbligato : createJSONCivilmenteObbligato(civilmenteObbligatoStore),
											familiari : selectedRowIndexes,
											action : 'write'
										},
										success : function(form, action) {
											var json = Ext.JSON
													.decode(action.response.responseText);
											var interventiGrid = interventiFieldset.items
													.get('wcs_paiInterventiList');
											if (json.success) {
												Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													fn : function() {
														interventiGrid.store
																.load();
														interventiFieldset.items
																.get('wcs_paiCronologiaList').store
																.load();
														interventiFieldset.items
																.get('wcs_paiDocumentiList').store
																.load();
														win.close();

														abilitaPulsantiCartellaSociale(); // servira' a qualcosa? chissa' . . .'
													}
												});
											} else {
												Ext.MessageBox.show({
													title : 'Errore',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													icon : Ext.window.MessageBox.ERROR,
													fn : function() {
														interventiGrid.store
																.load();
														interventiFieldset.items
																.get('wcs_paiCronologiaList').store
																.load();
														interventiFieldset.items
																.get('wcs_paiDocumentiList').store
																.load();
													}
												});
											}
										},
										failure : function(form, action) {
											var json = Ext.JSON
													.decode(action.response.responseText);
											var interventiGrid = Ext
													.getCmp('wcs_paiTab').items
													.get('wcs_paiForm').items
													.get('interventiFieldset').items
													.get('wcs_paiInterventiList');
											Ext.MessageBox.show({
												title : 'Esito operazione',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												icon : Ext.window.MessageBox.ERROR,
												fn : function() {
													interventiGrid.store.load();
													interventiFieldset.items
															.get('wcs_paiCronologiaList').store
															.load();
													interventiFieldset.items
															.get('wcs_paiDocumentiList').store
															.load();
												}
											});
										}
									});
								} else {
									Ext.MessageBox
											.alert(
													'Errore',
													'Attenzione! La somma degli importi attribuiti all\'intervento non corrisponde al costo calcolato');
								}
							} else {
								Ext.MessageBox
										.alert('Errore',
												'Verifica che tutti i campi obbligatori siano compilati.');
							}
						}
					}, {
						id : 'wcs_interventiFormChiudiForzatamente',
						text : 'Chiudi forzatamente intervento',
						disabled : true,
						scope : this,
						handler : function() {
							var win = this.up('window');
							var codPai = Ext.getCmp('wcs_paiCodPai').getValue();
							var interventiFieldset = Ext.getCmp('wcs_paiTab').items
									.get('wcs_paiForm').items
									.get('interventiFieldset');
							var form = this.getForm();
							if (form.isValid()) {
								form.submit({
									url : '/CartellaSociale/interventi',
									waitTitle : 'Salvataggio',
									waitMsg : 'Sto salvando i dati...',
									params : {
										codPai : codPai,
										action : 'close'
									},
									success : function(form, action) {
										var json = Ext.JSON
												.decode(action.response.responseText);
										var interventiGrid = interventiFieldset.items
												.get('wcs_paiInterventiList');
										if (json.success) {
											Ext.MessageBox.show({
												title : 'Esito operazione',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												fn : function() {
													win.close();
													interventiGrid.store.load();
													interventiFieldset.items
															.get('wcs_paiCronologiaList').store
															.load();
													interventiFieldset.items
															.get('wcs_paiDocumentiList').store
															.load();
												}
											});
										} else {
											Ext.MessageBox.show({
												title : 'Errore',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												icon : Ext.window.MessageBox.ERROR
											});
										}
									},
									failure : function(form, action, a, b, c, d) {
										Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : action.result.message,
													buttons : Ext.MessageBox.OK,
													icon : Ext.window.MessageBox.ERROR
												});
									}
								});
							} else {
								Ext.MessageBox.show({
									title : 'Esito operazione',
									msg : 'Verifica che i campi siano compilati correttamente',
									buttons : Ext.MessageBox.OK,
									icon : Ext.window.MessageBox.ERROR
								});
							}
						}
					}, {
						id : 'wcs_interventiFormCancella',
						text : 'Cancella intervento',
						disabled : false,
						scope : this,
						handler : function() {

							var famigliaItems = Ext.getCmp('famigliaGrid')
									.getStore().data.items;
							var msg = "";
							for (var i = 0; i < famigliaItems.length; i++) {
								if (famigliaItems[i].get('tipo') === "-1") {
									// individuato un intervento figlio
									msg = "Contemporaneamente verranno cancellati anche tutti gli INTERVENTI collegati ai FAMIGLIARI. ";
								}
							}
							var win = this.up('window');
							var codPai = Ext.getCmp('wcs_paiCodPai').getValue();
							var interventiFieldset = Ext.getCmp('wcs_paiTab').items
									.get('wcs_paiForm').items
									.get('interventiFieldset');
							var form = this.getForm();

							Ext.MessageBox
									.confirm(
											'ATTENZIONE CANCELLAZIONE INTERVENTO ',
											'Attenzione, vuoi davvero cancellare questo intervento ? Ti ricordiamo che la cancellazione intervento è IRREVERSIBILE. '
													+ msg
													+ 'Vuoi DAVVERO procedere? ',
											function(btn) {
												if (btn == 'yes') {
													form.submit({
														url : '/CartellaSociale/interventi',
														waitTitle : 'Cancellazione',
														waitMsg : 'Sto cancellando i dati ..',
														params : {
															codPai : codPai,
															action : 'delete'
														},
														success : function(
																form, action) {
															var json = Ext.JSON
																	.decode(action.response.responseText);
															var interventiGrid = interventiFieldset.items
																	.get('wcs_paiInterventiList');
															if (json.success) {
																Ext.MessageBox
																		.show({
																			title : 'Esito operazione',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			fn : function() {
																				win
																						.close();
																				interventiGrid.store
																						.load();
																				interventiFieldset.items
																						.get('wcs_paiCronologiaList').store
																						.load();
																				interventiFieldset.items
																						.get('wcs_paiDocumentiList').store
																						.load();
																			}
																		});
															} else {
																Ext.MessageBox
																		.show({
																			title : 'Errore',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			icon : Ext.window.MessageBox.ERROR
																		});
															}
														},
														failure : function(
																form, action,
																a, b, c, d) {
															Ext.MessageBox
																	.show({
																		title : 'Esito operazione',
																		msg : action.result.message,
																		buttons : Ext.MessageBox.OK,
																		icon : Ext.window.MessageBox.ERROR
																	});
														}
													});
												}
											});
						}
					}, {
						text : 'Sospendi/riprendi',
						handler : function() {
							var form = this.up('form').getForm();
							var win = this.up('window');

							var values = form.getValues();
							var stato = values['stato'];
							var codPai = Ext.getCmp('wcs_paiCodPai').getValue();
							var interventiFieldset = Ext.getCmp('wcs_paiTab').items
									.get('wcs_paiForm').items
									.get('interventiFieldset');
							//faccio il check del valore dello stato paer capire se l'intervento deve essere sospeso oppure ripreso
							if (stato == "S") {
								form.submit({
									url : '/CartellaSociale/interventi',
									waitTitle : 'Salvataggio',
									waitMsg : 'Sto salvando i dati...',
									params : {
										action : 'sospendi',
										codPai : codPai
									},
									success : function(form, action) {
										var json = Ext.JSON
												.decode(action.response.responseText);
										var interventiGrid = interventiFieldset.items
												.get('wcs_paiInterventiList');
										if (json.success) {
											Ext.MessageBox.show({
												title : 'Esito operazione',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												fn : function() {
													win.close();
													interventiGrid.store.load();
													interventiFieldset.items
															.get('wcs_paiCronologiaList').store
															.load();

												}
											});
										} else {
											Ext.MessageBox.show({
												title : 'Errore',
												msg : json.message,
												buttons : Ext.MessageBox.OK,
												icon : Ext.window.MessageBox.ERROR
											});
										}
									},
									failure : function(form, action, a, b, c, d) {
										Ext.MessageBox.show({
											title : 'Esito operazione',
											msg : 'Operazione non riuscita:controllare che tutti i campi siano compilati',
											buttons : Ext.MessageBox.OK,
											icon : Ext.window.MessageBox.ERROR
										});
									}
								});
							} else if (stato == "E") {
								var codPai = Ext.getCmp('wcs_paiCodPai')
										.getValue();
								//TODO CREA FINESTRA E CHIAMA FUNZIONE DI SOSPENSIONE
								//apriamo la finestra di conferma
								var confirmWindow = Ext.create(
										'Ext.window.Window', {
											layout : 'anchor',
											autoHeight : true,
											modal : true,
											items : [{
														xtype : 'datefield',
														editable : false,
														format : 'd/m/Y',
														value : new Date(),
														fieldLabel : 'Data chiusura',
														id : 'dataSospensioneInserita',
														width : 700
													}, {
														xtype : 'textarea',
														id : 'motivazioneSospensione',
														fieldLabel : 'Motivazione sospensione',
														maxLength : 1000,
														width : 700,
														maxLengthText : 'Lunghezza massima 1000 caratteri'
													}],
											buttons : [{
												text : 'Conferma',
												handler : function() {
													var dataSospensione = Ext
															.getCmp('dataSospensioneInserita')
															.getRawValue();
													var motivazione = Ext
															.getCmp('motivazioneSospensione')
															.getValue();
													form.submit({
														url : '/CartellaSociale/interventi',
														waitTitle : 'Sospensione',
														waitMsg : 'Sto sospendendo...',
														params : {
															action : 'sospendi',
															motivazionesospensione : motivazione,
															datasospensione : dataSospensione,
															codPai : codPai

														},
														success : function(
																form, action) {
															var json = Ext.JSON
																	.decode(action.response.responseText);
															var interventiGrid = interventiFieldset.items
																	.get('wcs_paiInterventiList');
															if (json.success) {
																Ext.MessageBox
																		.show({
																			title : 'Esito operazione',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			fn : function() {
																				confirmWindow
																						.close();
																				win
																						.close();
																				interventiGrid.store
																						.load();
																				interventiFieldset.items
																						.get('wcs_paiCronologiaList').store
																						.load();
																			}
																		});
															} else {
																Ext.MessageBox
																		.show({
																			title : 'Errore',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			icon : Ext.window.MessageBox.ERROR
																		});
															}
														},
														failure : function(
																form, action,
																a, b, c, d) {
															Ext.MessageBox
																	.show({
																		title : 'Esito operazione',
																		msg : 'Operazione non riuscita:controllare che tutti i campi siano compilati',
																		buttons : Ext.MessageBox.OK,
																		icon : Ext.window.MessageBox.ERROR
																	});
														}
													});
												}
											},		//fine bottone conferma
											{
												text : "Annulla",
												handler : function() {
													confirmWindow.close();
												}
											}		//fine bottone annulla
											],
											title : 'Sospensione intervento',
											width : 750,
											id : 'confirmSospensioneWindow'
										}).show();
							} else {
								alert("Attenzione non posso sospendere questo intervento.");
							}
						} //handler del button
					} //fine bottone sospensi riprendi
			]; //fine bottoni

			this.callParent();

		}
	});
})();