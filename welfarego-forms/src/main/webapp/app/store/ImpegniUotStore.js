Ext.define('wf.store.ImpegniUotStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.ImpegnoUotModel',
	pageSize: 20,
	autoLoad: true,
	autoSync: true,
	proxy: {
		type: 'ajax',
		url: wf.config.path.form,
	    timeout: 120000,
		extraParams: {
//			task_id:wf.config.taskId,
			table:"impegniuot",
			action:"load"
		},
		writer: {
			type: 'json',
			allowSingle: false
		}
	}
});