Ext.define('wf.view.notifica_revisione.NotificaRevisionePanel',{
	extend: 'wf.view.GenericFormPanel',
	alias: 'widget.wf_notifica_revisione_panel',
	title: 'Notifica Revisione',
	initComponent: function() {
		this.items = [{
			xtype: 'container',
			layout: 'column',
			items: [{
				xtype: 'container',
				columnWidth:.5,
				layout: 'anchor',
				defaults: {
					xtype: 'textfield',
					readOnly: true
				},
				items: [{
					fieldLabel: 'Nome utente',
					id: 'wf_notifica_revisione_nome_utente',
					name: 'nome_utente'                    
				},{
					xtype: 'datefield',
					fieldLabel: 'Data apertura PAI',
					id: 'wf_notifica_revisione_data_apert_PAI',
					name: 'data_apert_pai'
				}]
			},{
				xtype: 'container',
				columnWidth:.5,
				layout: 'anchor',
				defaults: {
					xtype: 'textfield',
					readOnly: true
				},
				items: [{
					fieldLabel: 'Cognome utente',
					id: 'wf_notifica_revisione_cognome_utente',
					name: 'cognome_utente'
				},{
					fieldLabel: 'Assistente sociale',
					id: 'wf_notifica_revisione_assist_soc',
					name: 'assist_soc'
				}]
			}]
		},{
			xtype: 'textarea',
			labelWidth:50,
			labelAlign:'top',
			id: 'wf_notifica_revisione_note',
			anchor:'99%',
			//                height: 90,
			fieldLabel: 'Note',
			name: 'note',
			readOnly: true		
		}];
		this.buttons = [this.submitButton];
		this.callParent(arguments);
		  
		this.load();
	}
});