Ext.define('wf.store.combo.UotStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("uot");
		this.addListener('load',function(){
			this.insert(0,{
				name:'qualsiasi UOT',
				value:''
			})
		})	
	}
});