Ext.define('wf.store.combo.ListeAttesaStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("lista_attesa");
	}
});