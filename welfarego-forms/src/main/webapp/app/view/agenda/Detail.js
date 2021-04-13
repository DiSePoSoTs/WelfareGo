/**
 * LEGACY, should be safe to delete
 */
Ext.define('wf.view.agenda.Detail',{
	extend: 'Ext.form.Panel',
	alias: 'widget.wtl_agenda_detail',
	bodyStyle:'padding:5px 5px 0',
	frame: true,
	collapsible: false,
	autoScroll:true,
	height:275,
	store:'AgendaStore',
	fieldDefaults: {
		labelAlign: 'top',
		msgTarget: 'side'
	},
	initComponent: function() {
		me = this;
		this.reader = Ext.create('Ext.data.reader.Reader', {
			model: 'wf.model.DettaglioModel',
			type: 'json',
			root : 'data',
			successProperty: 'success'
		});
		this.items = [{
			xtype:'fieldset',
			title:'Dettaglio',
			items:[{
				xtype: 'container',
				anchor: '100%',
				layout:'anchor',
				defaults: {
					anchor: '100%'
				},
				items:[{
					xtype: 'container',
					layout: 'column',
					items: [{
						xtype:'textfield',
						columnWidth:.50,
						fieldLabel: 'Cognome e Nome',
						cls: 'wtl_agenda_nome',
						name: 'nome',
						disabled:true
					},{
						xtype:'datefield',
						columnWidth:.20,
						format: 'd/m/Y',
						fieldLabel: 'Data*',
						//                        id: 'wtl_agenda_data',
						editable:false,
						name: 'date',
						allowBlank: false
					//                        vtype: 'daterange',
					//                        endDateField: 'wtl_agenda_data_end'
					},{
						xtype:'timefield',
						//xtype:'textfield',
						columnWidth:.15,
						fieldLabel: 'Dalle ore*',
						//                        id: 'wtl_agenda_dalle_ore',
						name: 'dalle_ore',
						minValue: '7:00 AM',
						maxValue: '18:30 PM',
						increment: 30,
					//	vtype:'Ora',
						allowBlank: false,
                                                format:"H:i"
					//                        vtype: 'timerange',
					//                        endTimeField: 'wtl_agenda_alle_ore'
					},{
						//xtype:'textfield',
						xtype:'timefield',
						columnWidth:.15,
						fieldLabel: 'Alle ore*',
						//                        id: 'wtl_agenda_alle_ore',
						name: 'alle_ore',
						minValue: '7:30 AM',
						maxValue: '19:00 PM',
						increment: 30,
					//	vtype:'Ora',
						allowBlank: false,
                                                format:"H:i"
					//                        vtype: 'timerange',
					//                        startTimeField: 'wtl_agenda_dalle_ore'
					}]
				},{
					xtype:'textarea',
					fieldLabel: 'Note',
					cls: 'wtl_agenda_note',
					id: 'wtl_agenda_note',
					name: 'note'
				},{
					xtype: 'radiogroup',
					name:'tipi',
					columns: 2,
					vertical: true,
					allowBlank:false,
					items: [{
						boxLabel  : 'Appuntamento',
						name      : 'tipo',
						cls      : 'wtl_agenda_tipo',
						inputValue: '3',
						//disabled:true,
                                                checked: true
					},{
						boxLabel  : 'Indisponibile',
						name      : 'tipo',
						cls      : 'wtl_agenda_tipo',
						inputValue: '2'
//                                                ,
//						disabled:true
					}]
				},{
					xtype:'hidden',
					name:'id_as'
				},{
					xtype:'hidden',
					name:'cod_pai'
				},{
					xtype:'hidden',
					name:'id_ts'
				},{
					xtype:'hidden',
					name:'tipo_hidden'
				}]
			}]
		}];
		this.buttons=[{
			xtype:'button',
			text:'Salva',
			handler: function(){
				form = this.up('form').getForm();
				if (form.isValid()) {
					form.submit({
						url:  wf.config.path.base+'/AgendaServlet',
						params: {
							action:'UPDATE',
							data:Ext.JSON.encode(form.getValues())
						},
						waitMsg: 'Aggiornamento in corso...',
						success: function(form, submit) {
							Ext.Msg.show({
								title:'Info',
								msg: submit.result.message,
								buttons: Ext.Msg.OK
							});
							wf.utils.reloadAgenda();
							form.reset();
						},
						failure: function(form, submit) {									
							Ext.Msg.show({
								title:'Errore',
								msg: submit.result.message,
								buttons: Ext.Msg.OK
							});
						}
					});
				}
			}
		},{
			xtype:'button',
			text:'Elimina',
			handler: function(){
				form = this.up('form').getForm();
				Ext.Msg.show({
					title:'Conferma cancellazione?',
					msg: 'Sei sicuro di voler cancellare questo impegno?',
					buttons: Ext.Msg.YESNO,
					fn: function(btn, text){
						if(btn=='yes'){
							if (form.isValid()) {
								form.submit({
									url:  wf.config.path.base+'/AgendaServlet',
									params: {
										action:'DELETE',
										data:Ext.JSON.encode(form.getValues())
									},
									waitMsg: 'Cancellazione in corso...',
									success: function(form, submit) {
										Ext.Msg.show({
											title:'Info',
											msg: submit.result.message,
											buttons: Ext.Msg.OK
										});
										wf.utils.reloadAgenda();
										form.reset();
									},
									failure: function(form, submit) {										
										Ext.Msg.show({
											title:'Errore',
											msg: submit.result.message,
											buttons: Ext.Msg.OK
										});
									}
								});
							}
						}
					}
				});
			}
		}];
		this.callParent(arguments);
	},

	setActiveRecord: function(codAs, idImpegno, tipo){
		this.getForm().load({
			url:  wf.config.path.base+'/AgendaServlet',
			params: {
				action:'LOAD',
				loadDetail:'loadDetail',
				data:Ext.JSON.encode({
					cod_as: codAs,
					id_impegno: idImpegno,
					tipo: tipo
				})
			},
			waitMsg: 'Caricamento...',
			failure: function(form, submit) {
				var response = Ext.JSON.decode(submit.response.responseText);
				Ext.Msg.alert("Errore nel caricamento", response.message);
			},
			success: function(form, submit) {						
					
				var tipo = form.findField("tipo").getGroupValue();
				var note = form.findField("note");
				
				if(tipo==2){
					note.setDisabled(true);
				}
				if(tipo==3){
					note.setDisabled(false);
				}
				var pai = form.findField("cod_pai").getValue();
				if(window.ricaricaAnagraficaDaPai) {
					ricaricaAnagraficaDaPai(pai);
				}		
			}
		});
	}
});