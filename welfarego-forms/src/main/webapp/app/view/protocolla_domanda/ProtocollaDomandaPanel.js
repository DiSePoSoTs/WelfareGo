Ext.define('wf.view.protocolla_domanda.ProtocollaDomandaPanel',{
    extend: 'wf.view.GenericProtocolloFormPanel',
    alias: 'widget.wa_protocolla_domanda_panel',
    title: 'Protocollazione domanda',
    initComponent: function() {
        this.callParent(arguments);
		  this.setFormName('protocolla_domanda');
        this.load();
    }
});
