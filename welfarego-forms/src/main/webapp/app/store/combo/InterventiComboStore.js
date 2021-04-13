Ext.define('wf.store.combo.InterventiComboStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("interventi");
		this.addListener('load',function(){
			this.insert(0,{
				name:'qualsiasi intervento',
				value:''
			})
		})
	}
});