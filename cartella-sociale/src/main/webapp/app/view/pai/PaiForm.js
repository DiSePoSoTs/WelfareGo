Ext.define('wcs.view.pai.PaiForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.wcs_paiform',
	fieldDefaults : {
		anchor : '100%',
		labelAlign : 'right',
		labelWidth : 100
	},
	activeRecord : null,
	frame : true,

	initComponent : function() {
		Ext.override(Ext.LoadMask, {
					onHide : function() {
						this.callParent();
					}
				});

		var salvaPaiForm = function(form, azione) {

			var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
			if (form.isValid()) {
				form.submit({
							url : '/CartellaSociale/pai',
							waitTitle : 'Salvataggio',
							waitMsg : 'Sto salvando i dati...',
							params : {
								codAnag : codAnag,
								action : 'update'
							},
							success : function(form, action) {
								if (azione == 'default') {
									salvataggioPaiDefault(action, codAnag);
								} else if (azione == 'nuovointervento') {
									apriNuovoIntervento(action, codAnag);
								}
							},
							failure : function(form, action) {
								wcs_isModified = '';
								var json = Ext.JSON.decode(action.response.responseText);
								Ext.MessageBox.show({
											title : 'Esito operazione',
											msg : json.message,
											buttons : Ext.MessageBox.OK,
											icon : Ext.window.MessageBox.ERROR
										});
							}
						});
			} else {
				Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
			}
		}

		var salvataggioPaiDefault = function(action, codAnag) {

			var json = Ext.JSON.decode(action.response.responseText);
			wcs_isModified = '';
			if (json.success) {
				Ext.MessageBox.show({
							title : 'Esito operazione',
							msg : json.message,
							buttons : Ext.MessageBox.OK,
							fn : function() {
								ricaricaPai(codAnag);
							}
						});
			} else {
				wcs_isModified = '';
				Ext.MessageBox.show({
							title : 'Errore',
							msg : json.message,
							buttons : Ext.MessageBox.OK,
							icon : Ext.window.MessageBox.ERROR,
							fn : function() {
								ricaricaPai(codAnag);
							}
						});
			}
		}

		var apriNuovoIntervento = function(action, codAnag) {

			var json = Ext.JSON.decode(action.response.responseText);
			wcs_isModified = '';
			if (json.success) {
				log('apertura nuova form intervento');
				var wcs_interventiFormWindow = Ext.create('Ext.window.Window',
															{
																title : 'Intervento',
																width : 900,
																closable : true,
																autoScroll : false,
																items : [{
																			xtype : 'wcs_interventiform',
																			id : 'wcs_interventiForm'
																		}]
															});
				wcs_interventiFormWindow.on({
					show : function() {
						var quantitaField = Ext.getCmp('wcs_interventoQuantita');
						if (quantitaField.el != undefined) {
							quantitaField.el.dom.firstChild.lastChild.nodeValue = 'Quantità';
						}
						var datiSpecifici = Ext.getCmp('wcs_interventiDatiSpecifici');
						cleanItems(datiSpecifici);
						// Pulisco anche i civilmente obbligati
						var impegniFieldSet = Ext.getCmp('wcs_impegniFieldset');
						var civObbComponent = Ext.getCmp('wcs_ricercaCivilmenteObbligatoGrid');
						if (civObbComponent != null) {
							impegniFieldSet.remove(civObbComponent);
						}
					}
				});
				wcs_interventiFormWindow.show();
				wcs_interventiFormWindow.down('wcs_interventiform').getForm()
						.reset();
				wcs_interventiFormWindow.down('wcs_interventiform').getForm()
						.isValid();

				Ext.getCmp('wcs_interventoImpegnoList').store.proxy.extraParams = {
					empty : true
				};
				Ext.getCmp('wcs_interventoImpegnoList').store.load();
				Ext.getCmp('wcs_interventoRichiedente').store.proxy.extraParams = {
					action : 'read',
					type : 'socialeReferenti',
					codAnag : codAnag
				};
				Ext.getCmp('famigliaGrid').store.proxy.extraParams = {
					action : 'read',
					type : 'sociale',
					codAnag : codAnag
				};
				Ext.getCmp('famigliaGrid').store.load();
				Ext.getCmp('wcs_interventoRichiedente').store.load();

				Ext.getCmp('wcs_interventoCronologiaList').store.proxy.extraParams = {
					empty : true
				};
				Ext.getCmp('wcs_interventoCronologiaList').store.load();
				Ext.getCmp('wcs_interventoPagamentiList').store.proxy.extraParams.action = null;
				Ext.getCmp('wcs_interventoPagamentiList').store.load();
				Ext.getCmp('wcs_interventoContribuzioniList').store.proxy.extraParams.action = null;
				Ext.getCmp('wcs_interventoContribuzioniList').store.load();
				Ext.getCmp('wcs_interventoFormSalva').setDisabled(false);
				Ext.getCmp('wcs_interventiFormChiudiForzatamente').setDisabled(true);
				Ext.getCmp('wcs_interventiFormCancella').setDisabled(true);
				Ext.getCmp('wcs_interventiDatiSpecifici').setDisabled(false);
				Ext.getCmp('wcs_interventoClasse').setReadOnly(false);
				Ext.getCmp('wcs_interventoTipo').setReadOnly(false);
				var codAna = Ext.getCmp('wcs_anagraficaCodAna').getValue();
				var nome = Ext.getCmp('wcs_anagraficaNome').getValue();
				var cognome = Ext.getCmp('wcs_anagraficaCognome').getValue();
				Ext.getCmp('wcs_InterventiFormBeneficiarioCod').setValue(codAna);
				Ext.getCmp('wcs_InterventiFormBeneficiarioDes').setValue(cognome + ' ' + nome);

				Ext.getCmp('tariffaGrid').store.removeAll();
				Ext.getCmp('tariffaGrid').store.sync();
			} else {
				wcs_isModified = '';
				Ext.MessageBox.show({
							title : 'Errore',
							msg : json.message,
							buttons : Ext.MessageBox.OK,
							icon : Ext.window.MessageBox.ERROR,
							fn : function() {
								ricaricaPai(codAnag);
							}
						});
			}
		}

		var apriFinestraPaiIntervento = window.wcs_apriFinestraPaiIntervento = function(
				data, originalData) {

			var duplicateInt = originalData ? true : false;
			log('apertura finestra pai intervento = ', data);
			if (duplicateInt) {
				log('duplicate of = ', originalData);
			}

			var waitPopup = Ext.Msg.wait('apertura intervento in corso . . ',
					'Apertura Intervento', {
						modal : true
					});

			var codPai = data.codPai;
			var cntTipint = data.cntTipint;
			var tipo = data.tipo;
			var dataChiusura = data.dataChiusura;
			var codRichiedente = data.richiedente;
			var codStruttura = data.struttura;
			var idTariffa = data.tariffa;
			var codAna = data.codAna;

			var predisponiStores = function() {

				log('predisposizione stores finestra vecchio pai intervento = ', data);

				Ext.getCmp('wcs_interventoCronologiaList').store.proxy.extraParams = {
					codPai : codPai,
					cntTipint : cntTipint,
					tipo : tipo,
					action : 'read'
				};
				Ext.getCmp('wcs_interventoCronologiaList').store.load();

				Ext.getCmp('wcs_interventoPagamentiList').store.proxy.extraParams = {
					codPai : codPai,
					cntTipint : cntTipint,
					tipo : tipo,
					action : 'read'
				};
				Ext.getCmp('wcs_interventoPagamentiList').store.load();
				Ext.getCmp('wcs_interventoRichiedente').store.proxy.extraParams = {
					action : 'read',
					type : 'socialeReferenti',
					codAnag : codAna
				};
				Ext.getCmp('wcs_interventoRichiedente').store.load();

				Ext.getStore('InterventiContribuzioniStore').proxy.extraParams = {
					codPai : codPai,
					cntTipint : cntTipint,
					tipo : tipo,
					action : 'contribuzioni'
				};
				Ext.getStore('InterventiContribuzioniStore').load();
				Ext.getStore('StrutturaStoreCombo').proxy.extraParams = {
					codTipInt : tipo,
					action : 'lista'
				};
				Ext.getStore('StrutturaStoreCombo').load();

				Ext.getCmp('famigliaGrid').store.proxy.extraParams = {
					action : 'read',
					type : 'sociale',
					codAnag : codAna
				};
				Ext.getCmp('famigliaGrid').store.load();

				if (codStruttura !== undefined) {
					Ext.getCmp('tariffaGrid').store.proxy.extraParams = {
						action : 'list',
						idStruttura : codStruttura
					};
					Ext.getCmp('tariffaGrid').store.load();
				}

				// Carico i dati dopo il caricamento per evitare problemi nella
				// visualizzazione.
				/*Ext.getCmp('wcs_interventoImpegnoList').on('afterrender',
						function() {
							Ext.getCmp('wcs_interventoImpegnoList').store.proxy.extraParams = {
								codTipInt : tipo,
								codPai : codPai,
								cntTipint : cntTipint,
								action : 'read'
							};
							Ext.getCmp('wcs_interventoImpegnoList').store.load(
									function() {
										Ext.getCmp('wcs_interventiForm')
												.aggiornaCosto();
									});
						}, this);*/
			};

			var inizializzazioneForm = function(form, famigliari) {

				Ext.getCmp('wcs_interventiForm').impostaCalcolaDurataFunc(data);
				var classeCombo = Ext.getCmp('wcs_interventoClasse');
				var tipoCombo = Ext.getCmp('wcs_interventoTipo');
				var strutturaCombo = Ext.getCmp('struttura');
				var famigliaGrid = Ext.getCmp('famigliaGrid');
				var richiedenteCombo = Ext.getCmp('wcs_interventoRichiedente');
				/*setTimeout(function() { // / FIX ORRIBILE
							log('fixing tipoCombo');
							tipoCombo.setValue(tipoCombo.getValue()); 
							Ext.getCmp('wcs_interventiForm')
									.aggiornaCosto(tipoCombo);
						}, 1500);*/
				if (!duplicateInt) {
					tipoCombo.setReadOnly(true);
					classeCombo.setReadOnly(true);
				}

				predisponiStores();
				setTimeout(function() { // / FIX ORRIBILE
							log('fixing richiedenteCombo');
							richiedenteCombo.setValue(codRichiedente);
						}, 1500);

				setTimeout(function() { // / FIX ORRIBILE
							log('fixing stuttura combo ');
							strutturaCombo.setValue(codStruttura);
						}, 1500);

				var fieldset = Ext.getCmp('wcs_interventiDatiSpecifici');
				populateDatiSpecifici(fieldset, tipo, cntTipint, codPai, tipo,
						cntTipint);

				// Non posso modificare il tipo di un intervento già aperto
				var dataEsecutivita = form.getValues().dataEsecutivita;
				var civilmenteObbligato = Ext
						.getCmp('wcs_ricercaCivilmenteObbligatoGrid');
				var isDataEsecutivitaSet = dataEsecutivita !== undefined
						&& dataEsecutivita !== "";
				if (civilmenteObbligato !== undefined) {
					civilmenteObbligato.setDisabled(isDataEsecutivitaSet);
				}
				if (isDataEsecutivitaSet) {
					Ext.getCmp('wcs_paiInterventoDataAvvio').setReadOnly(true);

				}
				famigliaGrid.getSelectionModel()
						.setLocked(isDataEsecutivitaSet);
				if (isDataEsecutivitaSet) {
					famigliaGrid.on('beforeedit', function() {
								return false;
							});
				}
				Ext.getCmp('wcs_interventoQuantita')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_paiInterventoDurataMesi')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_paiInterventoDataFine')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_interventoClasse')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_interventoTipo')
						.setReadOnly(isDataEsecutivitaSet);
				// commentato perchè adesso la data di avvio è modificabile solo
				// dal tipo di utente
				// Ext.getCmp('wcs_paiInterventoDataAvvio').setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_paiInterventoDataAvvioProposta')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_interventoImpegnoList')
						.setDisabled(isDataEsecutivitaSet);
				Ext.getCmp('wcs_paiInterventoMotivazione')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('wcs_interventoRichiedente')
						.setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('struttura').setReadOnly(isDataEsecutivitaSet);
				Ext.getCmp('tariffaGrid').getSelectionModel()
						.setLocked(isDataEsecutivitaSet);
				var useDurata = data.flgFineDurata != 'F';
				Ext.getCmp('wcs_paiInterventoDataFine').setVisible(!useDurata);
				Ext.getCmp('wcs_paiInterventoDataFine').setDisabled(useDurata);
				Ext.getCmp('wcs_paiInterventoDurataMesi').setVisible(useDurata);
				Ext.getCmp('wcs_paiInterventoDurataMesi')
						.setDisabled(!useDurata);
				var isClosed = dataChiusura != undefined && dataChiusura != "";
				Ext.getCmp('wcs_interventoFormSalva').setDisabled(isClosed);
				Ext.getCmp('wcs_interventiFormChiudiForzatamente')
						.setDisabled(isClosed);
				Ext.getCmp('wcs_interventiDatiSpecifici').setDisabled(isClosed);

				var quantitaLabel = form.getValues().labelQuantita;
				var quantitaField = Ext.getCmp('wcs_interventoQuantita');
				// solo per il fondo di solidarità regionale
				if (tipo == 'AD013') {
					Ext.getCmp('wcs_interventoQuantita').setReadOnly(true);
				}

				interventiFormWindow.show();
				waitPopup.close();
				quantitaField.el.dom.firstChild.lastChild.nodeValue = quantitaLabel;

				var useDataFineIndicativa = quantitaLabel.match(/sett/i)
						? true
						: false;
				log('use dataFineIndicativa = ', useDataFineIndicativa);
				// Ext.getCmp('wcs_paiInterventoDataFineIndicativa').setVisible(useDataFineIndicativa);
				// spike solution da fixare quando ci sarà tempo.
				if (data.tipo == "AZ018" || data.tipo == "AZ016"
						|| data.tipo == "AZ017") {
					Ext.getCmp('wcs_paiInterventoDurataMesi').setMaxValue(3);
				} else {
					Ext.getCmp('wcs_paiInterventoDurataMesi').setMaxValue(12);
				}

				var popolaFamigliaStore = function(store, records, options) {

					var storeItems = store.data.items;
					for (var z = 0; z < storeItems.length; z++) {
						if (famigliari !== undefined) {
							for (var i = 0; i < famigliari.length; i++) {
								if (parseInt(storeItems[z].data.codAnag, 10) === famigliari[i].codAnag) {
									storeItems[z].set('costo_famigliare',
											famigliari[i].importoPrevisto);
									storeItems[z].set('selezionato', true);
									storeItems[z].set('tipo',
											famigliari[i].tipo);
									break;
								} else {
									storeItems[z].set('costo_famigliare', "");
									storeItems[z].set('selezionato', false);
								}
							}
						} else {
							storeItems[z].set('costo_famigliare', "");
							storeItems[z].set('selezionato', false);
						}
					}
				};

				famigliaGrid.getStore().on('load', popolaFamigliaStore);

				var selezionaFamigliariSalvati = function() {

					var storeItems = famigliaGrid.getStore().data.items;
					var locked = famigliaGrid.getSelectionModel().isLocked();
					famigliaGrid.getSelectionModel().setLocked(false);
					for (var z = 0; z < storeItems.length; z++) {
						if (storeItems[z].get('selezionato')) {
							famigliaGrid.getSelectionModel().select(
									storeItems[z], true);
						}
						if (storeItems[z].get('tipo') === "1") {
							// se uno degli interventi è padre allora la tabella
							// deve essere non modificabile
							locked = true;
						}
					}
					famigliaGrid.getSelectionModel().setLocked(locked);
					if (locked) {
						famigliaGrid.on('beforeedit', function() {
									return false;
								});
					}
				};

				Ext.getCmp('wcs_interventiForm').setTariffaCaricata(idTariffa);
				famigliaGrid
						.on('afterrender', selezionaFamigliariSalvati, this);

			};

			var caricamentoForm = function() {

				interventiFormWindow.down('wcs_interventiform').getForm().load(
						{
							url : '/CartellaSociale/popola',
							params : {
								codPai : codPai,
								cntTipint : cntTipint,
								tipo : tipo,
								action : 'interventi'
							},
							// waitMsg: 'Caricamento...',
							failure : function(form, action) {
								interventiFormWindow.close();
								waitPopup.close();
								Ext.Msg.alert("Errore nel caricamento",
										action.result.errorMessage);
							},
							success : function(form, action) {
								log('caricati dati finestra pai intervento = ',
										data);
								inizializzazioneForm(
										form,
										Ext.JSON
												.decode(action.response.responseText).data.famigliari);
							}
						});
			}

			var windowTitle = 'Intervento';

			if (duplicateInt) {

				windowTitle += ' (intervento copiato)';
			}

			windowTitle += ' ' + tipo + ' ' + cntTipint;

			var interventiFormWindow = Ext.create('Ext.window.Window', {

				title : windowTitle,
				closable : true,
				// closeAction: 'hide',
				width : 950,
				// height: 550,
				modal : true,
				autoScroll : true,
				items : [{
					xtype : 'wcs_interventiform',
					id : 'wcs_interventiForm',
					listeners : {
						afterrender : function() {
							// waitPopup.toFront();
							// interventiFormWindow.toBack();
							Ext.getCmp('wcs_keyboardInterventiForm')
									.fireEvent('afterrender');
							log(
									'aperta finestra pai vecchio intervento (afterrender) = ',
									data);

						}
					}
				}]
			});

			caricamentoForm();

		};

		this.items = [

		{
					xtype : 'hiddenfield',
					id : 'wcs_paiCodPai',
					name : 'codPai'
				}, {
					xtype : 'hiddenfield',
					name : 'codAnag'
				}, {
					xtype : 'hiddenfield',
					name : 'codUot',
					id : 'wcs_paiUot'
				}, {
					xtype : 'hiddenfield',
					name : 'statoPai',
					id : 'wcs_statoPai'
				}, {
					xtype : 'container',
					anchor : '100%',
					layout : 'column',
					items : [{
						xtype : 'container',
						columnWidth : .5,
						layout : 'anchor',
						items : [{
									xtype : 'datefield',
									format : 'd/m/Y',
									fieldLabel : 'Data apertura',
									tabIndex : 1,
									readOnly : wcs_paiDataAperturaRO,
									id : 'wcs_paiDataApertura',
									name : 'dtApePai'
								}, {
									xtype : 'textfield',
									fieldLabel : 'Protocollo',
									readOnly : true,
									tabIndex : 3,
									// maxLength: 10,
									maxLengthText : 'Lunghezza massima 10 caratteri',
									// readOnly: wcs_paiPaiProtocolloRO,
									id : 'wcs_paiProtocollo',
									name : 'protocollo'
								}]
					}, {
						xtype : 'container',
						columnWidth : .5,
						layout : 'anchor',
						items : [{
									xtype : 'datefield',
									format : 'd/m/Y',
									readOnly : wcs_paiDataChiusuraRO,
									fieldLabel : 'Data chiusura',
									tabIndex : 2,
									id : 'wcs_paiDataChiusura',
									name : 'dtChiusPai'
								}, {
									xtype : 'datefield',
									format : 'd/m/Y',
									tabIndex : 4,
									fieldLabel : 'Data protocollo',
									readOnly : wcs_paiDataProtocolloRO,
									id : 'wcs_paiDataProtocollo',
									name : 'dataProtocollo'
								}]
					}]
				}, {
					xtype : 'textarea',
					fieldLabel : 'Motivazione',
					tabIndex : 5,
					maxLength : 255,
					maxLengthText : 'Lunghezza massima 255 caratteri',
					readOnly : wcs_paiPaiMotivazioneChiusuraRO,
					hidden : wcs_paiMotivazioneChiusuraHidden,
					id : 'wcs_paiMotivazione',
					name : 'motivo'
				}, {
					xtype : 'fieldset',
					title : 'Dati ISEE',
					collapsible : true,
					collapsed : true,
					layout : 'anchor',
					items : [{
						xtype : 'container',
						anchor : '100%',
						layout : 'column',
						defaults : {
							anchor : '97%'
						},
						items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
										xtype : 'weuronumberfield',
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										// maxLength: 9,
										tabIndex : 14,
										allowDecimals : true,
										minValue : 0,
										fieldLabel : 'Ordinario',
										name : 'isee',
										id : 'wcs_paiISEE',
										anchor : '97%'
									}, {
										xtype : 'weuronumberfield',
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										// maxLength: 9,
										tabIndex : 14,
										allowDecimals : true,
										minValue : 0,
										fieldLabel : 'Socio-Sanitario',
										name : 'isee2',
										id : 'wcs_paiISEE2',
										anchor : '97%'
									}, {
										xtype : 'weuronumberfield',
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										// maxLength: 9,
										tabIndex : 14,
										allowDecimals : true,
										minValue : 0,
										fieldLabel : 'prestazioni agevolate rivolte a minori',
										name : 'isee3',
										id : 'wcs_paiISEE3',
										anchor : '97%'
									}]
						}, {
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
								xtype : 'combo',
								queryMode : 'local',
								displayField : 'name',
								tabIndex : 5,
								value : wcs_fasciaDiRedditoDefaultCombo,
								valueField : 'value',
								store : wcs_interventoFasciaDiRedditoStore,
								fieldLabel : 'Fascia di reddito (compilare solo per domiciliari)',
								name : 'fasciaDiReddito'
							}, {
								xtype : 'datefield',
								format : 'd/m/Y',
								tabIndex : 15,
								fieldLabel : 'Data rilascio ISEE',
								name : 'dataScadenzaIsee'
							},

							{
								xtype : 'datefield',
								format : 'd/m/Y',
								// tabIndex: 6,
								fieldLabel : 'Data cambio fascia isee',
								id : 'wcs_paiDataCambioFascia',
								name : 'dtCambioFascia'
							}]

						}]

					}]
				},

				{
					xtype : 'fieldset',
					title : 'Profilo',
					collapsible : true,
					collapsed : true,
					hidden : wcs_paiProfiloFieldsetHidden,
					layout : 'anchor',
					items : [{
						xtype : 'container',
						anchor : '100%',
						layout : 'column',
						defaults : {
							anchor : '97%'
						},
						items : [{
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
										xtype : 'combo',
										fieldLabel : 'Certificato L104*',
										name : 'certificatoL104',
										displayField : 'name',
										valueField : 'value',
										store : 'certificatoL104Store',
										value : '00',
										allowBlank : false
									}, {
										xtype : 'numberfield',
										id : 'wcs_paiNumeroFigli',
										hideTrigger : true,
										allowDecimals : false,
										minValue : 0,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										minLength : 1,
										maxLength : 5,
										tabIndex : 9,
										validator : function(value) {
											var nucleo = Ext
													.getCmp('wcs_paiNucleoFamigliare'), numeroNucleo = nucleo
													? nucleo.getValue()
													: null;
											var numeroFigli = value;

											if ((typeof numeroNucleo == "number")
													&& (typeof numeroFigli == "number")
													&& numeroFigli > (numeroNucleo - 1)) {
												return "Il numero di figli non deve essere superiore al numero di componenti del nucleo famigliare";
											} else {
												return true;
											}
										},
										fieldLabel : 'Numero figli',
										readOnly : wcs_paiNumeroFigliRO,
										name : 'numeroFigli'
									}, {
										xtype : 'combo',
										displayField : 'name',
										valueField : 'value',
										tabIndex : 11,
										typeAhead : true,
										queryMode : 'local',
										forceSelection : true,
										store : wcs_paiHabitatStore,
										fieldLabel : 'Partecipa a progetto Habitat?',
										name : 'habitat'
									}]
						}, {
							xtype : 'container',
							columnWidth : .5,
							layout : 'anchor',
							items : [{
										xtype : 'numberfield',
										id : 'wcs_paiNucleoFamigliare',
										hideTrigger : true,
										keyNavEnabled : false,
										allowDecimals : false,
										minValue : 1,
										mouseWheelEnabled : false,
										minLength : 1,
										allowBlank : false,
										blankText : 'Questo campo è obbligatorio',
										maxLength : 5,
										value : 1,
										tabIndex : 8,
										fieldLabel : 'Numerosità nucleo familiare*',
										name : 'numeroNucleoFamigliare',
										readOnly : wcs_paiNumeroNucleoFamigliareRO
									}, {
										xtype : 'numberfield',
										id : 'wcs_paiNumeroFigliConviventi',
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										allowDecimals : false,
										minValue : 0,
										minLength : 1,
										maxLength : 5,
										tabIndex : 10,
										validator : function(value) {
											var numeroFigli = 0;
											var numeroFigliConviventi = 0;
											var numeroFigliComponent = Ext
													.getCmp('wcs_paiNumeroFigli');
											if (numeroFigliComponent.getValue() != "") {
												numeroFigli = parseInt(numeroFigliComponent
														.getValue());
											}
											if (value != "") {
												numeroFigliConviventi = parseInt(value);
											}
											if (numeroFigliConviventi > numeroFigli) {
												return "Il numero di figli conviventi non deve essere superiore al numero di figli";
											} else {
												return true;
											}
										},
										readOnly : wcs_paiNumeroFigliConviventiRO,
										fieldLabel : 'Numero figli conviventi',
										name : 'numeroFigliConviventi'
									}, {
										xtype : 'combo',
										fieldLabel : 'Provvedimento Giudiziario*',
										name : 'provvedimentoGiudiziario',
										displayField : 'name',
										valueField : 'value',
										store : 'provvedimentoGiudiziarioComboStore',
										allowBlank : false
									}]
						}]
					}]
				}, { // TODO fix tabIndex
					xtype : 'fieldset',
					title : 'Problematiche',
					hidden : wcs_paiDisabilitaRO,
					collapsible : true,
					collapsed : true,
					layout : 'anchor',
					items : [{
								margin : '0 0 5 30',
								anchor : '50%',
								xtype : 'datefield',
								format : 'd/m/Y',
								fieldLabel : 'Data diagnosi',
								tabIndex : 17,
								maxValue : new Date(),
								id : 'wcs_paiDiagnosiDataDiagnosi',
								name : 'dataDiagnosi'
							}, {
								xtype : 'label',
								anchor : '50%',
								text : 'Problematiche rilevate:',
								margin : '10 0 10 0'
							}, Ext.create('wcs.view.pai.DiagnosiCsrFields'), {
								xtype : 'checkboxgroup',
								fieldLabel : 'Già in carico a',
								columns : 2,
								vertical : true,
								margin : 5,
								border : 1,
								id : 'wcs_paiDiagnosiInCarico',
								items : [{
											boxLabel : 'Socio assistenziali',
											name : 'diagnosiInCaricoSA',
											tabIndex : 11,
											inputValue : '1'
										}, {
											boxLabel : 'Sanitari',
											name : 'diagnosiInCaricoS',
											tabIndex : 12,
											inputValue : '2'
										}, {
											boxLabel : 'Volontariato',
											name : 'diagnosiInCaricoV',
											tabIndex : 13,
											inputValue : '3'
										}, {
											boxLabel : 'Parrocchia',
											name : 'diagnosiInCaricoP',
											tabIndex : 14,
											inputValue : '4'
										}, {
											boxLabel : 'Altro',
											name : 'diagnosiInCaricoA',
											tabIndex : 15,
											inputValue : '5'
										}, {
											xtype : 'textfield',
											name : 'diagnosiInCaricoAltro',
											tabIndex : 16,
											maxLength : 100,
											width : 250,
											maxLengthText : 'Lunghezza massima 100 caratteri'
										}]
							}]
				}, {
					xtype : 'fieldset',
					itemId : 'interventiFieldset',
					title : 'Interventi',
					collapsible : true,
					collapsed : true,
					layout : 'anchor',
					items : [{
								xtype : 'wcs_interventilist',
								listeners : {
									itemdblclick : function(grid, record) {
										apriFinestraPaiIntervento(record.data);
									}
								},
								autoHeight : true
							}, {
								xtype : 'wcs_paicronologia',
								itemId : 'wcs_paiCronologiaList',
								autoHeight : true
							}, {
								xtype : 'wcs_documentilist',
								itemId : 'wcs_paiDocumentiList',
								autoHeight : true
							}]
				}, {
					xtype : 'fieldset',
					itemId : 'interventiSocialCrtFieldset',
					title : 'Interventi SocialCrt',
					collapsible : true,
					collapsed : true,
					layout : 'anchor',
					items : [{
								xtype : 'wcs_interventilistsocialcrt',
								listeners : {
									itemdblclick : function(grid, record) {
										apriFinestraPaiIntervento(record.data);
									}
								},
								autoHeight : true
							}]
				}, {
					xtype : 'fieldset',
					itemId : 'liberatoriaSocialCrtFieldset',
					title : 'Liberatorie firmate SocialCrt',
					collapsible : true,
					collapsed : true,
					layout : 'anchor',
					items : [{
								xtype : 'wcs_liberatorialistsocialcrt',
								autoHeight : true
							}]
				}];

		this.buttons = [

				Ext.create('Ext.ux.VirtualKeyboardButton', {
							id : 'wcs_keyboardPaiForm'
						}), {
					text : 'Nuovo PAI',
					id : 'wcs_nuovoPaiButton',
					disabled : true,
					handler : function() {
						var win = Ext.create('Ext.window.Window', {
							title : 'Nuovo PAI',
							closable : true,
							width : 600,
							layout : 'fit',
							bodyStyle : 'padding: 5px;',

							items : [{
								xtype : 'form',
								frame : true,
								layout : 'anchor',
								items : [{
									xtype : 'combo',
									displayField : 'name',
									valueField : 'value',
									store : wcs_anagraficaUOTStore,
									typeAhead : true,
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									readOnly : wcs_anagraficaUOTRO,
									fieldLabel : 'UOT*',
									name : 'uot',
									listeners : {
										'select' : function(combo, record) {
											var assCombo = Ext
													.getCmp('wcs_nuovoPaiAssistenteSociale');
											assCombo.clearValue();
											assCombo.store.getProxy().extraParams.codUot = this
													.getValue();
											assCombo.store.load();
										}
									},
									anchor : '95%'
								}, {
									xtype : 'combo',
									displayField : 'name',
									valueField : 'value',
									allowBlank : false,
									blankText : 'Questo campo è obbligatorio',
									store : new Ext.data.Store({
										model : 'wcs.model.AssistenteSocialeModel'
									}),
									readOnly : wcs_anagraficaAssistenteSocialeRO,
									id : 'wcs_nuovoPaiAssistenteSociale',
									fieldLabel : 'Assistente sociale*',
									name : 'assSoc',
									anchor : '95%'
								}],
								buttons : [{
											text : 'Pulisci',
											handler : function() {
												this.up('form').getForm()
														.reset();
											}
										}, {
											text : 'Salva',
											handler : function() {
												var form = this.up('form')
														.getForm();
												if (form.isValid()) {
													var codAnag = Ext
															.getCmp('wcs_anagraficaCodAna')
															.getValue();
													form.submit({
														url : '/CartellaSociale/pai',
														waitTitle : 'Salvataggio',
														waitMsg : 'Sto salvando i dati...',
														params : {
															codAnag : codAnag,
															action : 'write'
														},
														success : function(
																form, action) {
															var json = Ext.JSON
																	.decode(action.response.responseText);
															if (json.success) {
																Ext.MessageBox
																		.show({
																			title : 'Esito operazione',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			fn : function() {
																				win
																						.close();
																				ricaricaPai(codAnag);
																			}
																		});
															} else {
																Ext.MessageBox
																		.show({
																			title : 'Errore',
																			msg : json.message,
																			buttons : Ext.MessageBox.OK,
																			icon : Ext.window.MessageBox.ERROR,
																			fn : function() {
																				win
																						.close();
																				ricaricaPai(codAnag);
																			}
																		});
															}
														},
														failure : function(
																form, action) {
															Ext.MessageBox
																	.show({
																		title : 'Esito operazione',
																		msg : action.result.message,
																		buttons : Ext.MessageBox.OK,
																		icon : Ext.window.MessageBox.ERROR,
																		fn : function() {
																			win
																					.close();
																			ricaricaPai(codAnag);
																		}
																	});
														}
													});
												}
											}
										}]
							}]
						});
						win.show();
					}
				}, {
					text : 'Copia PAI',
					disabled : true,
					id : 'wcs_paiFormNuovoButton',
					handler : function() {
						var form = this.up('form').getForm();
						var codAnag = Ext.getCmp('wcs_anagraficaCodAna')
								.getValue();
						if (form.isValid()) {
							form.submit({
								url : '/CartellaSociale/pai',
								waitTitle : 'Salvataggio',
								waitMsg : 'Sto salvando i dati...',
								params : {
									action : 'copy'
								},
								success : function(form, action) {
									var json = Ext.JSON
											.decode(action.response.responseText);
									if (json.success) {
										Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													fn : function() {
														ricaricaPai(codAnag);
													}
												});
									} else {
										Ext.MessageBox.show({
													title : 'Errore',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													icon : Ext.window.MessageBox.ERROR,
													fn : function() {
														ricaricaPai(codAnag);
													}
												});
									}
								},
								failure : function(form, action) {
									Ext.MessageBox.show({
										title : 'Esito operazione',
										msg : 'Si è verificato un errore durante l\'operazione di salvataggio',
										buttons : Ext.MessageBox.OK,
										icon : Ext.window.MessageBox.ERROR,
										fn : function() {
											ricaricaPai(codAnag);
										}
									});
								}
							});
						} else {
							Ext.MessageBox
									.alert('Errore',
											'Verifica che tutti i campi obbligatori siano compilati.');
						}
					}
				}, {
					text : 'Chiudi PAI',
					id : 'wcs_paiChiudiPaiButton',
					disabled : true,
					handler : function() {
						var form = this.up('form').getForm();
						var values = form.getValues();
						var codPai = values['codPai'];
						var codAnag = values['codAnag'];
						var confirmWindow = Ext.create('Ext.window.Window', {
							layout : 'fit',
							autoHeight : true,
							modal : true,
							items : [{
										xtype : 'datefield',
										editable : false,
										format : 'd/m/Y',
										value : new Date(),
										fieldLabel : 'Data chiusura',
										name : 'dataChiusuraPai',
										id : 'dataChiusuraInserita'
									}

							],
							buttons : [{
								text : 'Conferma',
								handler : function() {
									var dataChiusura = Ext
											.getCmp('dataChiusuraInserita')
											.getRawValue();
									Ext.Ajax.request({
										url : '/CartellaSociale/pai',
										params : {
											codPai : codPai,
											codAnag : codAnag,
											dataChiusura : dataChiusura,
											action : 'close'
										},
										success : function(response) {
											var json = Ext.JSON
													.decode(response.responseText);
											if (json.success) {
												Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													fn : function() {
														confirmWindow.close();
														ricaricaPai(codAnag);
													}
												});
											} else {
												Ext.MessageBox.show({
													title : 'Esito operazione',
													msg : json.message,
													buttons : Ext.MessageBox.OK,
													icon : Ext.window.MessageBox.ERROR,
													fn : function() {
														ricaricaPai(codAnag);
													}
												});
											}
										}
									});
								}

							}],
							title : 'Chiusura Pai',
							width : 300,
							height : 300,
							id : 'confirmWindow'
						}).show();

					}
				}, {
					text : 'Salva PAI e apri un nuovo intervento',
					id : 'wcs_paiFormNuovoInterventoButton',
					disabled : true,
					handler : function() {
						var form = this.up('form').getForm();
						salvaPaiForm(form, 'nuovointervento');
					}
				}, {
					text : 'Salva',
					disabled : true,
					id : 'wcs_paiFormSalvaButton',
					handler : function() {
						var form = this.up('form').getForm();
						salvaPaiForm(form, 'default');
					}
				}];

		this.callParent();
	},

	setActiveRecord : function(record) {

		this.activeRecord = record;
		// peggior risoluzione di problema di sempre . . ma funziona . .
		for (var i = 0; i < 3; i++) {
			try {
				if (record) {
					this.getForm().loadRecord(record);
				} else {
					this.getForm().reset();
					//            var form=this.getForm(); //fix for extensible
					//            form.legacyReset=function(){
					//                var me = this;
					//                me.batchLayouts(function() {
					//                    me.getFields().each(function(f) {
					//                        f.reset();
					//                    });
					//                });
					//                return me;
					//            };
					//            form.legacyReset();
				}
				break;
			} catch (e) {
				log('ignorable error', e);
			}
		}
	}
});