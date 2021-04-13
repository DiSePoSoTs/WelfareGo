Ext.define('wf.view.valida_esecutivita.ValidaEsecutivitaPanel',{
	extend: 'wf.view.VerificaImpegniGenericPanel',
	title: 'Valida esecutività',
	initComponent: function() {
		this.callParent(arguments);
		this.add([{
			xtype: 'container',
			layout:'hbox',
			id: 'wf_dav_doc_container',
			layoutConfig: {
				pack:'center',
				align:'middle'
			},
			items: [				
			Ext.create('wf.view.DocumentButton',{				
				id: 'wf_valida_esecutivita_apri_determ',
				text: 'Apri testo determina',
				document:'determina'
			}),
			Ext.create('wf.view.DocumentButton',{				
				id: 'wf_valida_esecutivita_apri_report',
				text: 'Apri report determina',
				document:'report'
			})]
		},{
			xtype: 'radiogroup',
			fieldLabel: 'Esito',
			id: 'wf_valida_esecutivita_valid_esec_esito',
			name: 'esito',
			hideEmptyLabel:false,
			columns: 1,
			items: [{
				boxLabel: 'Approva',
				name: 'esito',
				inputValue: 'approva'
			},{
				boxLabel: 'Rimanda',
				name: 'esito',
				inputValue: 'rimanda',
				checked: true
			},{
				boxLabel: 'Respingi',
				name: 'esito',
				inputValue: 'respingi'
			}]
		},{
			xtype:'hidden',
			name:'report_doc'
		}]);
		Ext.getCmp('wf_verifica_dati_imp_mens').setReadOnly(true);
		Ext.getCmp('wf_verifica_dati_durata').setReadOnly(true);
		//Ext.getCmp('wf_verifica_dati_a_carico').setEditor(null); TODO set column readonly
		this.load({
			success:function(form){
				if(form.getValues().report_doc!='true'){
					Ext.getCmp('wf_valida_esecutivita_apri_report').hide();
				}
			}
		});
	}
});