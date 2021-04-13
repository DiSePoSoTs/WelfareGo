Ext.define('wf.store.MessaggiBreStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.MessaggioBreModel',
	pageSize: 20,
	autoLoad: true,
	autoSync: true,
	proxy: {
		type: 'ajax',
		url: wf.config.path.form,
		extraParams: {
//			task_id:wf.config.taskId,
			table:"messaggiBre",
			action:"load"
		}
	}
});