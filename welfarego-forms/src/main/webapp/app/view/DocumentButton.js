Ext.require('wf.utils.WfUtils');
Ext.define('wf.view.DocumentButton',{
	extend: 'Ext.button.Button',
	alias: 'widget.wf_dav_document_button',
	config:{
		document:"requireDocument"
	},
	constructor:function(config){
		this.initConfig(config);
                this.handler=wf.utils.WfUtils.getLoadDavDocumentFunc({
			url: wf.config.path.form,
			params: {
				task_id:wf.config.taskId,
				action:"LOAD",
				requireDocument:this.document
			}
		});
		this.callParent(arguments);	
	}
});