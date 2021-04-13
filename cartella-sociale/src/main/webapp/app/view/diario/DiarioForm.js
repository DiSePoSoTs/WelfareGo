var apriFinestraNotaCondivisa = window.wcs_apriFinestraNotaCondivisa = function(
		data) {
	var wcs_notaFormWindow = Ext.create('Ext.window.Window', {
				title : 'Nota',
				width : 900,
				closable : true,
				//                    closeAction: 'hide',
				autoScroll : false,
				items : [{
							xtype : 'wcs_notaform',
							id : 'wcs_notaForm'
						}]
			});

	if (data != null) {
		//var id = data.id;
		var titolo = data.titolo;
		var nota = data.nota;
		Ext.getCmp('notaTitolo').setValue(titolo);
		Ext.getCmp('notaEsteso').setValue(nota);
		Ext.getCmp('notaTitolo').setReadOnly(true);
		Ext.getCmp('notaEsteso').setReadOnly(true);
		Ext.getCmp('salvaNotaButton').disable();
	}

	log('completata inizializzazione form finestra nota condivisa = ', data);
	wcs_notaFormWindow.show();

};
Ext.define('wcs.view.diario.DiarioForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.wcs_diarioform',
	title : 'Diario',
	bodyStyle : 'padding:5px 5px 0',
	frame : true,
	defaults : {
		labelWidth : 150
	},
	initComponent : function() {
		this.reader = Ext.create('Ext.data.reader.Reader', {
					model : 'wcs.model.DiarioModel',
					type : 'json',
					rootProperty : 'data',
					successProperty : 'success'
				});

		this.items = [{
			xtype : 'container',
			anchor : '100%',
			layout : 'fit',
			items : [{

						xtype : 'textareafield',
						grow : true,
						name : 'diario',
						fieldLabel : 'Diario:',
						anchor : '100%'

					}]
				//fine itemms contenitore

			}, {
			xtype : 'fieldset',
			itemId : 'noteFieldset',
			title : 'Note condivise',
			collapsible : true,
			collapsed : true,
			layout : 'anchor',
			items : [{
						xtype : 'wcs_notelist',
						itemId : 'wcs_noteCondiviseList',
						listeners : {
							itemdblclick : function(grid, record) {
								apriFinestraNotaCondivisa(record.data);
							}
						},
						// height: 200
						autoHeight : true
					}]
		}];

		this.buttons = [{
					text : 'Nuova nota',
					handler : function() {
						apriFinestraNotaCondivisa(null);
					}
				}, {
					text : 'Stampa diario',
					handler : function() {
						var codAnag = Ext.getCmp('wcs_anagraficaCodAna')
								.getValue();
						window.open("/CartellaSociale/Stampa?codAnag="
								+ codAnag + "&action=diario");
					}
				},

				{
					id : 'wcs_DiarioSalva',
					text : 'Salva',
					tabIndex : 25,
					formBind : true,
					handler : function() {
						var diarioTab = Ext.getCmp('wcs_diarioTab');
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
								url : '/CartellaSociale/diario',
								waitTitle : 'Salvataggio',
								waitMsg : 'Sto salvando i dati...',
								params : {
									codAnag : codAnag
								},
								success : function(form, action) {
									var json = Ext.JSON
											.decode(action.response.responseText);
									if (json.success) {
										diarioTab.setDisabled(wcs_diarioHidden);
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
	}

});