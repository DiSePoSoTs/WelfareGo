Ext.define('wf.view.rivedi_documento.RivediDocumentoPanel',{
	extend: 'wf.view.GenericDocumentPanel',
	title: 'Revisione documento',
	initComponent: function() {
		this.buttons = [
		Ext.create('wf.view.DocumentButton',{
			text: 'Apri documento'
		}),{
			xtype:'wf_submit_button'
		}];
		this.callParent(arguments);
		  
		this.load();
	}
});