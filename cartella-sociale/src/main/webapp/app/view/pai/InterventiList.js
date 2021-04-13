Ext.define('wcs.view.pai.InterventiList', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.wcs_interventilist',
	title : 'Interventi attivati',
	store : 'InterventiListStore',
	id : 'wcs_paiInterventiList',
	loadMask : true,
	minHeight : 155,

	columns : [{
				header : 'Tipo intervento',
				dataIndex : 'tipo',
				sortable : true,
				width : 80
			}, {
				header : 'Contatore',
				dataIndex : 'cntTipint',
				sortable : true,
				width : 60
			}, {
				header : 'Prorogato da',
				dataIndex : 'cntTipintPadre',
				sortable : true,
				width : 75,
				renderer : function(value, meta, record) {
					// non utilizzare record.data.tipoLegame, è sempre undefined ...
					if (record.raw.tipoLegame == 'DETERMINA_PARZIALE') {
						return value;
					} else {
						return '';
					}

				}
			}, {
				header : 'Stato',
				dataIndex : 'statoIntervento',
				sortable : true,
				width : 40
			}, {
				header : 'Intervento',
				dataIndex : 'desInt',
				sortable : true,
				flex : 1
			}, {
				header : 'Data apertura',
				dataIndex : 'dataApertura',
				sortable : true,
				xtype : 'datecolumn',
				format : 'd/m/Y',
				width : 75
			}, {
				header : 'Data avvio',
				dataIndex : 'dataAvvio',
				sortable : true,
				xtype : 'datecolumn',
				format : 'd/m/Y',
				width : 75
			}, {
				header : 'Data chiusura',
				dataIndex : 'dataChiusura',
				sortable : true,
				xtype : 'datecolumn',
				format : 'd/m/Y',
				width : 75
			}, {
				header : 'Preventivato',
				dataIndex : 'importoPrevisto',
				sortable : true,
				renderer : Euro,
				flex : 1
			}, {
				header : 'Erogato',
				dataIndex : 'importoErogato',
				renderer : Euro,
				sortable : true,
				flex : 1
			}, {
				header : 'Stato attuale intervento',
				dataIndex : 'statoAttuale',
				sortable : true,
				flex : 1
			}, {
				header : 'Richiesta approvazione',
				dataIndex : 'sottoposto',
				sortable : true,
				hidden : true
			}, {
				header : 'Prevista proroga',
				dataIndex : 'previstaProroga',
				hidden : true,
				sortable : true
			}, {
				header : 'Contrib. previsti',
				dataIndex : 'contribuzioniPreviste',
				renderer : Euro,
				sortable : true,
				hidden : true,
				flex : 1
			}, {
				header : 'Contributi pagati',
				dataIndex : 'contribuzioniPagate',
				renderer : Euro,
				sortable : true,
				hidden : true,
				flex : 1
			}, {
				header : 'Data esecutivita',
				dataIndex : 'dataEsecutivita',
				sortable : true,
				hidden : true,
				xtype : 'datecolumn',
				format : 'd/m/Y',
				width : 70
			}, {
				xtype : 'actioncolumn',
				width : 30,
				items : [{
					getClass : function(a, b, record) {
						return record.data.flgRicevuta == 'S'
								? 'wcs_print_icon'
								: 'wcs_print_icon_disabled';
					},
					tooltip : 'Stampa ricevuta',
					handler : function(grid, rowIndex, colIndex) {
						var record = grid.getStore().getAt(rowIndex);
						if (record.data.flgRicevuta == 'S') {
							window
									.open('/CartellaSociale/ricevuta?action=PRINT&codPai='
											+ record.data.codPai
											+ '&codTipint='
											+ record.data.tipo
											+ '&cntTipint='
											+ record.data.cntTipint);
						}
					}
				}]
			}],

	bbar : {
		xtype : 'pagingtoolbar',
		store : 'InterventiListStore',
		displayMsg : 'Visualizzo gli interventi da {0} a {1} di {2}',
		emptyMsg : 'Nessun intervento',
		items : [{
			xtype : 'button',
			text : "duplica intervento",
			handler : function() {
				log("duplica intervento button pressed");
				var grid = Ext.getCmp('wcs_paiInterventiList');
				var record = grid.getSelectionModel().getSelection()[0];
				if (record) {
					log('selected record : ', record);
					var data = record.data;

					var procedi = function() {
						var wait = Ext.Msg.wait('operazione in corso...');
						Ext.Ajax.request({
							url : '/CartellaSociale/interventi',
							params : {
								action : 'clone',
								codPai : data.codPai,
								codTipint : data.tipo,
								cntTipint : data.cntTipint
							},
							callback : function(params, success, response) {
								wait.close();
								var json = success ? Ext.JSON
										.decode(response.responseText) : null;
								if (success && json.success) {
									var newPai = json.data[0];
									log('duplicated pai to ', newPai);
									wcs_apriFinestraPaiIntervento(newPai, data);
								} else {
									Ext.MessageBox.show({
										title : 'Errore',
										msg : json
												? json.message
												: 'Errore di comunicazione col server',
										buttons : Ext.MessageBox.OK,
										icon : Ext.window.MessageBox.ERROR
									});
								}
							}
						});
					};

					Ext.MessageBox.confirm('Duplicazione Intervento','Procedere con la duplicazione dell\'intervento ' + data.tipo + ' "' + data.desInt + '" ?',
							function(btn) {
								if (btn == 'yes') {
									procedi();
								}
							});
				}
			}
		}]

	}
});