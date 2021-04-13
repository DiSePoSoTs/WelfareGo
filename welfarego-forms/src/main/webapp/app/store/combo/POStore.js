Ext.define('wf.store.combo.POStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("po");
		this.addListener('load',function(){
			this.insert(0,{
				name:'qualsiasi PO',
				value:''
			})
		})
	}
});