/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il controller che gestisce le operazioni della sezione Acquisizione
 * dati
 */

Ext.define('Wp.controller.Acquisizioni', {
	extend: 'Ext.app.Controller',

	views: [
		'acquisizioni.Form',
		'acquisizioni.List',
		'acquisizioni.Detail'
	],

	models: [
		'acquisizioni.Acquisizioni',
		'acquisizioni.DettaglioAcquisizioni'
	],

	stores: [
		'acquisizioni.Acquisizioni',
		'acquisizioni.AcquisizioniShadow',
		'acquisizioni.DettaglioAcquisizioni'
	],

	refs: [{
			ref: 'acquisizioniForm',
			selector: 'wpacquisizioniform'
		},

		{
			ref: 'acquisizioniList',
			selector: 'wpacquisizionilist'
		},

		{
			ref: 'acquisizioniDetail',
			selector: 'wpacquisizionidetail'
		}
	],

	init: function () {
		var dettaglioAcquisizioniStore = this.getAcquisizioniDettaglioAcquisizioniStore(),
			dettaglioAcquisizioniProxy = dettaglioAcquisizioniStore.getProxy();

		// setta le azioni del controller
		this.control({
			// preselezione classe tipologia
			'wpacquisizioniform combobox[name=classe_intervento]': {
				select: this.filtroTipoIntervento
			},
			// pulsante di ricerca di un acquisizione
			'wpacquisizioniform button[action=search]': {
				click: this.search
			},
			// pulsante per chiudere i risultati di una ricerca
			'wpacquisizioniform button[action=back]': {
				click: this.chiudiLista
			},
			'wpacquisizionilist gridpanel': {
				// cliccando su uno dei risultati di ricerca, si apre il dettaglio
				itemdblclick: this.detail
			},
			'wpacquisizionidetail gridpanel': {
				itemclick: this.qtDefault
			},

			// pulsante per mandare avanti le acquisizioni  acquisizioni
			'wpacquisizionilist button[action=save]': {
				click: this.save
			},
			// pulsante per mandare avanti tutte  acquisizioni
			'wpacquisizionilist button[action=save_all]': {
				click: this.saveAll
			},

			// pulsante per salvare un dettaglio modificato
			'wpacquisizionidetail button[action=save]': {
				click: this.detailSave
			},
			// pulsante per salvare un dettaglio modificato ma senza mandarlo avanti
			'wpacquisizionidetail button[action=saveNoProgress]': {
				click: this.detailSaveNoProgress
			},
			// pulsante per chiudere un dettaglio aperto precedentemente
			'wpacquisizionidetail button[action=back]': {
				click: this.chiudiDettaglio
			}
		});


		/*
		 * Inizializza gli store
		 */

		// evento richiamato in caso di esecuzione corretta dell'operazione di
		// salvataggio dei dati del dettaglio acquisizioni
		dettaglioAcquisizioniStore.addListener('write', function (proxy, operation) {
			var grid = this.getAcquisizioniDetail().down('gridpanel');
			Ext.Msg.alert('Salvataggio', 'Modifiche salvate con successo.');

			// nascondi messaggio di loading
			grid.setLoading(false, true);
			Ext.getCmp('listacquisizioni').store.load();
		}, this);
		// evento richiamato prima dell'operazione di salvataggio delle modifiche
		// al dettaglio acquisizioni
		dettaglioAcquisizioniStore.addListener('beforesync', function (records) {
			var grid = this.getAcquisizioniDetail().down('gridpanel');

			// mostra messaggio di loading
			grid.setLoading(true, true);
		}, this);
		// evento richiamato in caso di errore nella chiamata ajax effettuata
		// dallo store DettaglioAcquisizioni
		//        dettaglioAcquisizioniProxy.addListener('exception', function(proxy, response, operation) {
		//            var grid = this.getAcquisizioniDetail().down('gridpanel');
		//
		//            switch (operation.action) {
		//                case 'update':
		//                    Ext.Msg.alert('Errore!', 'Impossibile salvare i dati.');
		//                    // nascondi messaggio di loading
		//                    grid.setLoading(false, true);
		//                    break;
		//                case 'read':
		//                    Ext.Msg.alert('Errore!', 'Errore nel caricamento dei dati.');
		//                    break;
		//                default:
		//                    Ext.Msg.alert('Errore!', 'Si è verificato un errore.');
		//                    break;
		//            }
		//        }, this);

	},

	/*
	 * Action per filtrare la lista dei tipi di intervento
	 */
	filtroTipoIntervento: function () {
		var comboClasse = this.getAcquisizioniForm().down('combobox[name=classe_intervento]');
		var comboTipo = this.getAcquisizioniForm().down('combobox[name=tipo_intervento]');
		comboTipo.store.clearFilter();
		comboTipo.store.filter('classe', comboClasse.getValue());
	},
	/*
	 * Action che esegue la ricerca delle acquisizioni e carica i risultati
	 * nella lista
	 */
	search: function () {

		function eseguiRicerca() {
			var form = this.getAcquisizioniForm().getForm(),
				list = this.getAcquisizioniList(),
				store = this.getAcquisizioniAcquisizioniStore();
			shadowStore = this.getAcquisizioniAcquisizioniShadowStore();
			if (form.isValid()) {

				var params = form.getValues();
				log('triggering aquisizioni search with params : ', params)
				newParams = params
				newParams.page = 1;
				store.proxy.extraParams = newParams;
				store.sort([{
					property: 'cognome',
					direction: 'ASC'
				}]);
				//  store.loadPage(1);


				shadowStore.proxy.extraParams = params;
				shadowStore.load();
				list.down('gridpanel').doLayout();
				list.show();
			}
		}

		// chiudi l'eventuale dettaglio già aperto
		if (!this.getAcquisizioniDetail().isHidden()) {
			this.chiudiDettaglio({
				success: eseguiRicerca
			});
		} else {
			eseguiRicerca.call(this);
		}

	}, // eo search

	/*
	 * Action che chiude i risultati della ricerca
	 *
	 * @param Object options: un oggetto contenente varie opzioni utilizzate
	 * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
	 *   eseguita nello scope del controller, non accetta nessun parametro
	 */
	chiudiLista: function (options) {
		options = options || {};

		function eseguiChiudiLista() {
			this.getAcquisizioniAcquisizioniStore().removeAll();
			this.getAcquisizioniAcquisizioniShadowStore().removeAll();
			this.getAcquisizioniList().hide();

			if (typeof options.success != 'undefined') {
				options.success.call(this);
			}
		}

		// chiudi l'eventuale dettaglio aperto
		if (!this.getAcquisizioniDetail().isHidden()) {
			this.chiudiDettaglio({
				success: eseguiChiudiLista
			});
		} else {
			eseguiChiudiLista.call(this);
		}
	}, // eo chiudiLista

	/*
	 * Imposta le quantita di default, se mancanti
	 */
	qtDefault: function (view, record, item, node, e) {
		//imposto, se necessatio il default sulle qunatità erogata e beneficiata
		var erogata = record.get('qt_erogata');
		if (erogata == undefined || erogata == null) {
			var pevista = record.get('qt_prevista');
			record.set('qt_erogata', pevista);
			record.set('qt_beneficiata', pevista);
		}
	},

	/*
	 * Action che carica il dettaglio di un risultato della ricerca
	 */
	detail: function (grid, record) {

		function apriDettaglio() {
			var form = this.getAcquisizioniDetail().down('form'),
				store = this.getAcquisizioniDettaglioAcquisizioniStore(),
				editor = this.getAcquisizioniDetail().down("#assenze").getEditor();

			form.loadRecord(record);
			store.proxy.extraParams = {
				id: record.getId()
			}
			store.load({
				callback: function () {
				    var codTipInt =  store.first().get('cod_tip_int');
					/*
					var id = store.first().get('id');
					ID61: abilitare il campo assenze per tutti i tipi di intervento.
					if (id.indexOf('"codTipint":"AZ007"') != -1
						||id.indexOf('"codTipint":"AZ007A"') != -1
						||id.indexOf('"codTipint":"AZ007B"') != -1
					    || id.indexOf('"codTipint":"AZ008"') != -1
					    || id.indexOf('"codTipint":"AZ008A"') != -1
					    || id.indexOf('"codTipint":"AZ008B"') != -1
					    || id.indexOf('"codTipint":"AZ010"') != -1
					    || id.indexOf('"codTipint":"AZ009"') != -1
					    || id.indexOf('"codTipint":"DI008"') != -1){
					    editor.enable();
					} else {
					    editor.disable();
					}
					*/
					var um = store.first().get('unita_di_misura'),
						showQt = (um && um.match('euro')) ? false : true;
					try {
						Ext.getCmp('wpAqDetQuantitaHeader').setVisible(showQt);
						Ext.getCmp('wpAqDetUnitaDiMisura').setVisible(showQt);
						Ext.getCmp('wpAqDetQuantitaErogata').setVisible(showQt);
						Ext.getCmp('wpAqDetQuantitaBeneficiata').setVisible(showQt);
						var store2 = Ext.getCmp('codice_imp').getEditor().getStore();

						store2.getProxy().extraParams = {
							codTipInt: codTipInt
						};
						store2.load();


					} catch (e) {
						log('this can fail : ', e);
					}
				}
			});

			this.getAcquisizioniDetail().show();
		}


		// chiudi l'eventuale dettaglio già aperto
		if (!this.getAcquisizioniDetail().isHidden()) {
			this.chiudiDettaglio({
				success: apriDettaglio
			});
		} else {
			apriDettaglio.call(this);
		}


	}, // eo detail

	/*
	 * Action che salva il contenuto di un dettaglio modificato
	 */
	detailSave: function () {
		log('salviamo il form');
		var store = this.getAcquisizioniDettaglioAcquisizioniStore(),
			valid = true,
			errors = {},
			errorMsg = '';

		// force update
		store.each(function (record) {
			record.setDirty();
		});

		// controlla che tutti i record modificati contengano valori corretti
		store.each(function () {
			var variazione = this.get('variazione_straordinaria'),
				causale = this.get('causale'),
				qtErogata = this.get('qt_erogata'),
				qtBeneficiata = this.get('qt_beneficiata');

			if (this.dirty) {
				if (!Ext.isEmpty(variazione) && Ext.isEmpty(causale) ||
					!Ext.isEmpty(causale) && Ext.isEmpty(variazione)) {
					valid = false;
					errors.variazione = 'Ogni variazione straordinaria deve avere una causale associata e viceversa.'
				}

				if (Ext.isEmpty(qtErogata) || Ext.isEmpty(qtBeneficiata)) {
					valid = false;
					errors.quantita = 'Tutti i valori di quantità erogata e quantità beneficiata devono essere specificati.'
				}
			}
		});

		if (!valid) {
			errorMsg = 'Impossibile salvare i dati.';
			Ext.Object.each(errors, function (key, error) {
				errorMsg += '<br/>- ' + error;
			});
			Ext.Msg.alert('Attenzione!', errorMsg);
		} else {
			//store.getProxy().extraParams = {};
			store.sync();
			store.each(function (record) {
				record.commit();
			});
			this.chiudiDettaglio();
		}
	}, // eo detailSave

	/*
	 * Action che salva il contenuto di un dettaglio modificato
	 */
	detailSaveNoProgress: function () {
		log('salviamo il form senza far proseguire l intervento.');
		var store = this.getAcquisizioniDettaglioAcquisizioniStore(),
			valid = true,
			errors = {},
			errorMsg = '';

		// force update
		store.each(function (record) {
			record.setDirty();
		});

		// controlla che tutti i record modificati contengano valori corretti
		store.each(function () {
			var variazione = this.get('variazione_straordinaria'),
				causale = this.get('causale'),
				qtErogata = this.get('qt_erogata'),
				qtBeneficiata = this.get('qt_beneficiata');

			if (this.dirty) {
				if (!Ext.isEmpty(variazione) && Ext.isEmpty(causale) ||
					!Ext.isEmpty(causale) && Ext.isEmpty(variazione)) {
					valid = false;
					errors.variazione = 'Ogni variazione straordinaria deve avere una causale associata e viceversa.'
				}

				if (Ext.isEmpty(qtErogata) || Ext.isEmpty(qtBeneficiata)) {
					valid = false;
					errors.quantita = 'Tutti i valori di quantità erogata e quantità beneficiata devono essere specificati.'
				}
			}
		});

		if (!valid) {
			errorMsg = 'Impossibile salvare i dati.';
			Ext.Object.each(errors, function (key, error) {
				errorMsg += '<br/>- ' + error;
			});
			Ext.Msg.alert('Attenzione!', errorMsg);
		} else {
			store.getProxy().extraParams = {
				nonProseguire: "nonProseguire"
			};

			store.sync();
			store.each(function (record) {
				record.commit();
			});
			this.chiudiDettaglio();
		}
	}, // eo detailSave

	// funzione di salvataggio super pippa
	save: function () {


		var selectedRowIndexes = [];
		var grid = this.getAcquisizioniList().down('gridpanel');
		// returns an array of selected records
		var selectedBanners = grid.getSelectionModel().getSelection();

		Ext.iterate(selectedBanners, function (record) {

			selectedRowIndexes.push(record.get('id'));
		});


		this.acquisiciRecord(selectedRowIndexes);
	},

	// acquisice tutti i record caricati nello store
	saveAll: function () {

		var store = this.getAcquisizioniAcquisizioniShadowStore();

		// var allRecords = store.snapshot || store.data;
		var ids = [];
		store.each(function (record, id) {
			log(record);
			ids.push(record.get('id'));
		});

		this.acquisiciRecord(ids);
	},

	acquisiciRecord: function (records) {

		var grid = this.getAcquisizioniList().down('gridpanel');
		var myMask = new Ext.LoadMask(grid.getEl(), {
			msg: "Salvataggio in corso... "
		});
		myMask.show();
		// Basic request
		Ext.Ajax.request({
			url: wp_url_servizi.SalvaAcquisizioni,
			timeout: 300000,
			success: function (response) {
				var risposta = Ext.decode(response.responseText);
				log('risposta', risposta);
				myMask.hide();
				if (risposta.success == false) {
                    (window=Ext.create('Ext.window.Window',{
                                title: 'Si sono verificati degli errori sulle seguenti acquisizioni:',
                                height: 250,
                                width: 400,
                                layout:'border',
                                modal:true,
                                items:[{
                                          xtype:'textarea',
                                          region:'center',
                                          value:risposta.acquisizioniConErrori,
                                          readOnly: true
                                      }]
                            })).show();
				} else {
					Ext.Msg.alert('Salvataggio riuscito', 'Operazione andata a buon fine: numero di record acquisiti con successo ' + risposta.acquisiti);
				}
			},
			failure: function () {
				Ext.Msg.alert('Attenzione ', 'Il salvataggio dei record selezionati non è riuscito');
			},
			params: {
				data: records
			}
		});
	},

	/*
	 * Action che chiude un dettaglio aperto
	 *
	 * @param Object options: un oggetto contenente varie opzioni utilizzate
	 * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
	 *   eseguita nello scope del controller, non accetta nessun parametro
	 */
	chiudiDettaglio: function (options) {

		var dettaglioAcquisizioni = this.getAcquisizioniDettaglioAcquisizioniStore(),
			nonSalvati = false;
		options = options || {};

		function eseguiChiudiDettaglio(button) {
			if (Ext.isEmpty(button) || button == 'yes') {
				this.getAcquisizioniDettaglioAcquisizioniStore().removeAll();
				this.getAcquisizioniDetail().hide();

				if (typeof options.success != 'undefined') {
					options.success.call(this);
				}
			}
		}

		// controlla che non ci siano modifiche non salvate
		dettaglioAcquisizioni.each(function () {
			if (this.dirty) {
				nonSalvati = true;

				// termina il ciclo
				return false;
			}
		});

		if (nonSalvati) {
			Ext.Msg.show({
				title: 'Attenzione!',
				msg: 'Chiudendo la finestra le modifiche non salvate andranno perse. Vuoi continuare?',
				buttons: Ext.Msg.YESNO,
				fn: eseguiChiudiDettaglio,
				scope: this,
				icon: Ext.Msg.QUESTION
			});
		} else {
			eseguiChiudiDettaglio.call(this);
		}
	} // eo chiudiDettaglio
});