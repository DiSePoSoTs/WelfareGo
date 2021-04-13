Ext.define('wf.store.combo.AssistenteStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(options){
		this.callParent(arguments);
		this.setStoreName("assistente");
		this.addListener('load',function(){
			this.insert(0,{
				name:'qualsiasi AS',
				value:''
			})
		})
	}
});