Ext.require('wf.view.DocumentButton');
Ext.define('wf.view.SubmitVerificaDatiDeterminaButton', {
	extend: 'Ext.button.Button',
	alias: 'widget.wf_submit_doc_button',
	config: {
		text: 'Procedi',
		id: 'wf_gendoc_button',
		handler: (function () {
			var handleError = function (message) {
				Ext.Msg.show({
					title: 'Errore',
					msg: 'Si è verificato un errore durante la generazione del documento:<br/>' + message,
					buttons: Ext.Msg.OK
				});
			};

			return function () {
				var formPanel = this.up('form');
				var form = formPanel.getForm();

				log('wf.view.SubmitDocButton: ', form);

				var impegniStore = null;
				if (Ext.getCmp('wf_verifica_dati_grid_impegni') != null) {
					impegniStore = Ext.getCmp('wf_verifica_dati_grid_impegni').store;
				}

				Ext.Ajax.request({
					url: wf.config.path.form,
					params: {
						generateDocument: 'generateDocument',
						task_id: wf.config.taskId,
						action: "PROCEED",
						data: Ext.JSON.encode(form.getValues()),
						impegniStore: createJSONFromImpegniStore(impegniStore)
					},
					success: function (response) {
						var data = Ext.JSON.decode(response.responseText);


						if (data.success) {
							Ext.getCmp('wf_gendoc_button').disable();

							if (wfg.removeTaskFromList) {
								wfg.removeTaskFromList(wf.config.taskId);
							}


							var idDocumentoAutorizzazione = data.data.idDocumentoAutorizzazione;

							var items = [{
								xtype: 'label',
								text: data.message,
								margin: '0 0 10px 0',
								docked: 'top'
							}];


							var window;
							var cmp = Ext.getCmp('wf_verifica_dati_singola');
							var that = this;

							var aproDocumento = !cmp || cmp.getValue();
							if (aproDocumento) {
								items.push(Ext.create('wf.view.DocumentButton', {
									text: 'Apri documento',
									region: 'south',
									columnWidth: .5
								}));
							} else {
								items.push({
									xtype: 'button',
									text: 'OK',
									width: 400,
									margin: '0 0 10px 0',
									handler: function () {
										window.destroy();
									}
								});
							}

							if ( Number(idDocumentoAutorizzazione) > 0) {
								items.push({
									xtype: 'button',
									text: 'scarica file approvazione',
									width: 400,
									handler: function () {
										var downloadUrl = document.location.origin + '/WelfaregoForms/DownloadDocumentoServlet?idDocumento=' + idDocumentoAutorizzazione;
										that.open(downloadUrl, '_blank');
									}
								});
							}

							(window = Ext.create('Ext.window.Window', {
								title: 'Documento generato',
								height: 140,
								width: 400,
								layout: 'vbox',
								modal: true,
								defaults: {
									flex: 1
								},
								items: items,
								listeners: {
									destroy: function () {
										if (wfg.utils.scrollToTasklist)
											wfg.utils.scrollToTasklist();
									}
								}
							})).show();


						} else {
							handleError(data.message);
						}
					},
					failure: function (response) {
						handleError(response);
					}
				});
			};
		})()
	},
	constructor: function (config) {
		this.initConfig(config);
		this.callParent(arguments);
	}


});

function createJSONFromImpegniStore(store) {
	var json = [];
	if (store != null) {
		var items = store.data.items;

		for (var i = 0; i < items.length; i++) {
			var item = items[i];
			json.push({
				carico: String(item.data.a_carico),
				anno: item.data.anno,
				capitolo: item.data.capitolo,
				id: item.data.id,
				impegno: item.data.impegno,
				quantita: item.data.bdgPrevQta
			});
		}
	}
	return Ext.JSON.encode(json);
}