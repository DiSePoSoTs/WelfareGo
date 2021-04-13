Ext.define('wf.store.InterventiStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.InterventoModel',
	pageSize: 20,
	autoLoad: true,
	proxy: {
		type: 'ajax',
		url: wf.config.path.form,
		extraParams: {
//			task_id:wf.config.taskId,
			table:"interventi",
			action:"load"
		}
	}
});