Ext.define('wf.view.validazione_documento.ValidazioneDocumentoPanel',{
	extend: 'wf.view.GenericDocumentPanel',
	title: 'Validazione documento',
	initComponent: function() {
		this.buttons = [Ext.create('wf.ux.VirtualKeyboardButton',{hidden:true}),this.submitButton];
		this.callParent(arguments);
		this.add([
			Ext.create('wf.view.DocumentButton',{
			text: 'Apri documento'
		}),{
			xtype: 'container',
			layout: 'column',
			items: [{
				xtype: 'radiogroup',
				fieldLabel: 'Esito verifica',
				columnWidth:.4,
				name: 'esito_verifica',
				columns: 1,
				items: [{
					id: 'wa_validazione_documento_approva', 
					boxLabel: 'Approvato', 
					name: 'esito_verifica', 
					inputValue: 'approvato'
				},{
					id: 'wa_validazione_documento_respingi', 
					boxLabel: 'Da rivedere', 
					name: 'esito_verifica', 
					inputValue: 'rivedere', 
					checked: true
				}]
			},{
				xtype: 'textarea',
				columnWidth:.6,
				id: 'wa_validazione_documento_note',
				name: 'note',
				fieldLabel: 'Note',
				height: 100,
				hidden:true
			}]
		}]);
		  
		this.load();
	}
});