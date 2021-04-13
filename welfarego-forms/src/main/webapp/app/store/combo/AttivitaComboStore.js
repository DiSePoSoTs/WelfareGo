Ext.define('wf.store.combo.AttivitaComboStore',{
	extend: 'wf.store.combo.ComboStore',
	constructor:function(){
		this.callParent(arguments);
		this.setStoreName("attivita");
		this.addListener('load',function(){
			this.insert(0,{
				name:'qualsiasi attività',
				value:''
			})
		})
	}
});
