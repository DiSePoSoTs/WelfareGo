Ext.define('wf.store.ImpegniStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.ImpegnoModel',
	pageSize: 20,
	autoLoad: true,
	autoSync: true,
	proxy: {
		type: 'ajax',
		url: wf.config.path.form,
		extraParams: {
//			task_id:wf.config.taskId,
			table:"impegni",
			action:"load"
		},
		writer: {
			type: 'json',
			allowSingle: false
		}
	}
});