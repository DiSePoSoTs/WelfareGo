Ext.define('wf.store.PaiStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.PaiModel',
	pageSize: 20,
	config:{
		autoLoad: true
	},
	proxy: {
		type: 'ajax',
		url: wf.config.path.form,
		extraParams: {
			//			task_id:wf.config.taskId,
			table:"pai",
			action:"load"
		},
		reader: {
			type: 'json',
			root: 'data',    
			successProperty: 'success'
		}
	}
});