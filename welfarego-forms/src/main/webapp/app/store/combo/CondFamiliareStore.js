Ext.define('wf.store.combo.CondFamiliareStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("cond_familiare");
		this.addListener('load',function(){
			
		});	
	}
});