Ext.define('wf.view.protocolla_documento.ProtocollaDocumentoPanel',{
    extend: 'wf.view.GenericProtocolloFormPanel',
    alias: 'widget.wa_protocolla_documento',
    title: 'Protocollazione documento',
    initComponent: function() {
        this.callParent(arguments);
        this.codForm = 'default';
		  
        this.load();
    }
});