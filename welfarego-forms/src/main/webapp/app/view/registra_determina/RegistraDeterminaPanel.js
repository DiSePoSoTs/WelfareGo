Ext.define('wf.view.registra_determina.RegistraDeterminaPanel',{
    extend: 'wf.view.GenericProtocolloFormPanel',
    alias: 'widget.wa_registra_determina_panel',
    title: 'Registra determina',
    initComponent: function() {
        this.callParent(arguments);
        this.codForm = 'registra_determina';
		  
        this.load();
    }
});