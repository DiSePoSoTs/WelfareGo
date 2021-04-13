Ext.define('wf.store.combo.ComboStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.ComboModel',
	proxy: {
		type: 'ajax',
		url: wf.config.path.combo
	},
	setStoreName:function(name){
		this.callParent(["combo."+name]);
		this.proxy.extraParams.table=name;
	}
});