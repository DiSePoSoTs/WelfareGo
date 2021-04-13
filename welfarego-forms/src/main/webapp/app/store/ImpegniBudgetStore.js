Ext.define('wf.store.ImpegniBudgetStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.ImpegnoModel',
	pageSize: 50,
	autoLoad: false,
	autoSync: true,
	proxy: {
		type: 'ajax',
		url: wf.config.path.determine,
		extraParams: {
//			task_id:wf.config.taskId,
			budgetIntervento:"budgetIntervento",
			action:"load"
		},
		writer: {
			type: 'json',
			allowSingle: false
		}
	}
});