Ext.define('wf.store.ImpegniProrogheDetermineStore',{
	extend: 'wf.store.GenericStore',
	model: 'wf.model.ImpegnoProrogheDetermineModel',
	pageSize: 2000,
	autoLoad: false,
	//autoSync: true,
	proxy: {
		type: 'ajax',
		 url: wf.config.path.base+'/DetermineServlet',
		extraParams: {
//			task_id:wf.config.taskId,
			action:'load',
			impegniProroghe:"impegniProroghe"
		},
		 reader: {
		        type: 'json',
		        root: 'data'    
		       
		    }
	
	}
});