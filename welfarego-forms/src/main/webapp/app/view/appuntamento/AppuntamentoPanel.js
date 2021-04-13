Ext.define('wf.view.appuntamento.AppuntamentoPanel',{
	extend: 'wf.view.GenericFormPanel',
	alias: 'widget.wa_appuntamento_panel',
	title: 'Appuntamento',
	initComponent: function() {		
		this.codForm = 'appuntamento';
		this.items = [{				
			xtype: 'container',
			layout: 'column',
			items:[{
				xtype: 'container',
				layout: 'anchor',
				columnWidth:.5,
				items: [{
					xtype: 'textfield',
					id: 'wa_appuntamento_nome_cognome',
					fieldLabel: 'Cognome e nome',
					name: 'nome_cognome',
					readOnly: true
				},{
					xtype: 'datefield',
					id: 'wa_appuntamento_data',
					fieldLabel: 'Data',
					name: 'data'
				}]
			},{
				xtype: 'container',
				layout: 'anchor',
				columnWidth:.5,
				items: [{
					xtype: 'timefield',
					id: 'wa_appuntamento_dalle_ore',
					fieldLabel: 'Dalle ore',
					name: 'dalle_ore',
					minValue: '8:00am',
					maxValue: '5:30pm'
				},{
					xtype: 'timefield',
					id: 'wa_appuntamento_alle_ore',
					fieldLabel: 'Alle ore',
					name: 'alle_ore',
					minValue: '8:30am',
					maxValue: '6:00pm'
				}]
			}]
		},{
			xtype: 'textarea',
			id: 'wa_appuntamento_note',
			fieldLabel: 'Note',
			height: 100,
			name: 'note'
		},{
        xtype: 'hiddenfield',
        name: 'dalle_ore_orig'
    }];
	
		this.buttons = [
		Ext.create('wf.ux.VirtualKeyboardButton'),this.resetButton,
		this.saveButton,
		this.submitButton
		];
		
		this.callParent(arguments);
		
		this.load();
	}
});