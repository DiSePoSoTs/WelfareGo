Ext.define('wf.store.BudgetStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.BudgetModel',
	pageSize: 50,
	id:'BudgetStore',
	storeId:'BudgetStore',
	name:'BudgetStore',

	autoLoad:false,

	config:{
		autoLoad:false
	},

	proxy: {
		type: 'ajax',
		url: wf.config.path.seleziona_budget_multipli,
		writer: {
			type: 'json'
		},
        reader: {
            type: 'json',
            root: 'map.budgets.myArrayList'
        }
	}
});