Ext.require('wf.view.SubmitDocButton');
Ext.define('wf.view.generazione_documento.GenerazioneDocumentoPanel',{
	extend: 'wf.view.GenericDocumentPanel',
	title: 'Generazione documento',
	initComponent: function() {
		this.buttons = [{
			xtype: 'wf_submit_doc_button',
			id:'wf_gendoc_button'
		}];
		this.callParent(arguments);
		  
		this.load();
	}
});