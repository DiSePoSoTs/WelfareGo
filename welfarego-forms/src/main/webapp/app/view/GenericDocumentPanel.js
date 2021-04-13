Ext.define('wf.view.GenericDocumentPanel',{
	extend: 'wf.view.GenericFormPanel',
	initComponent: function() {
		this.codForm = 'default';
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
					id: 'wa_generazione_documento_nome_utente',
					fieldLabel: 'Nome Utente',
					name: 'nome_utente_ver_dati'
				},{
					xtype: 'datefield',
					id: 'wa_generazione_documento_data_apert_pai',
					fieldLabel: 'Data apertura PAI',
					name: 'data_apert_pai'
				},{
					id: 'wa_generazione_documento_n_pai',
					fieldLabel: 'PAI n°',
					name: 'n_pai'
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
					id: 'wa_generazione_documento_cognome_ut',
					fieldLabel: 'Cognome utente',
					name: 'cognome_ut'
				},{
					id: 'wa_generazione_documento_assist_soc',
					fieldLabel: 'Assistente sociale',
					name: 'assist_soc'
				},{
					id: 'wa_generazione_documento_descr',
					fieldLabel: 'Descrizione',
					name: 'descrizione'
				}]
			}]
		},{
			xtype: 'textfield',
			id: 'wa_generazione_documento_intervento',
			name: 'intervento',
			fieldLabel: 'Intervento',
			readOnly: true
		},{
			xtype: 'textfield',
			id: 'wa_generazione_documento_documento',
			name: 'documento',
			fieldLabel: 'Documento',
			readOnly: true
		}];
		this.callParent(arguments);
	}
});