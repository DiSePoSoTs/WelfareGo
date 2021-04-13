Ext.define('wf.view.approvazione_tecnica.ApprovazioneTecnicaPanel',{
	extend: 'wf.view.GenericFormPanel',
	alias: 'widget.wf_approvazione_tecnica_panel',
	title: 'Approvazione tecnica intervento',
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
					id: 'wf_approvazione_tecnica_nome_utente',
					name: 'nome_utente'                    
				},{
					xtype: 'datefield',
					fieldLabel: 'Data apertura PAI',
					id: 'wf_approvazione_tecnica_data_apert_PAI',
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
					id: 'wf_approvazione_tecnica_cognome_utente',
					name: 'cognome_utente'
				},{
					fieldLabel: 'Assistente sociale',
					id: 'wf_approvazione_tecnica_assist_soc',
					name: 'assist_soc'
				}]
			}]
		},{
			xtype: 'textarea',
			labelWidth:50,
			labelAlign:'top',
			id: 'wf_approvazione_tecnica_motivazione',
			anchor:'99%',
			//                height: 90,
			fieldLabel: 'Motivazione',
			name: 'motivazione',
			readOnly: true		
		},{
			xtype: 'textarea',
			labelWidth:50,
			labelAlign:'top',
			id: 'wf_approvazione_tecnica_informazioni',
			anchor:'99%',
			//                height: 90,
			fieldLabel: 'Informazioni',
			name: 'informazioni'
			//readOnly: true		
		},{
			xtype: 'radiogroup',
			fieldLabel: 'Esito',
			name: 'esito',
			cls: 'x-check-group-alt',
			items: [{
				id: 'wa_verifica_dati_approva', 
				boxLabel: 'Approva', 
				name: 'esito', 
				inputValue: 'approvato'
			},{
				id: 'wa_verifica_dati_rifiuta', 
				boxLabel: 'Respingi', 
				name: 'esito', 
				inputValue: 'respinto',
				checked:true
				
			}]
		}];
		this.buttons = [this.submitButton];
		this.callParent(arguments);
		  
		this.load();
	}
});