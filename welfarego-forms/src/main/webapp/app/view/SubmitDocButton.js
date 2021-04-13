Ext.require('wf.view.DocumentButton');
Ext.define('wf.view.SubmitDocButton',{
	extend:'Ext.button.Button',
	alias: 'widget.wf_submit_doc_button',
	config:{
		text:'Procedi',
		id:'wf_gendoc_button',
		handler:(function(){
			var handleError=function(message){
				Ext.Msg.show({
					title:'Errore',
					msg: 'Si &egrave verificato un errore durante la generazione del documento:<br/>'+message,
					buttons: Ext.Msg.OK
				});
			};
			return function(){
				var formPanel = this.up('form'), form = formPanel.getForm();
			    var impegniStore = null;
			    if( Ext.getCmp('wf_verifica_dati_grid_impegni')!=null){
			    impegniStore = Ext.getCmp('wf_verifica_dati_grid_impegni').store;
			    }

				Ext.Ajax.request({
					url: wf.config.path.form,
					params: {
						generateDocument:'generateDocument',
						task_id:wf.config.taskId,
						action:"PROCEED",
						data:Ext.JSON.encode(form.getValues()),
					    impegniStore: createJSONFromImpegniStore(impegniStore)
					},
					success: function(response){
						var data=Ext.JSON.decode(response.responseText);
						if(data.success){
							Ext.getCmp('wf_gendoc_button').disable();
							if(wfg.removeTaskFromList)
								wfg.removeTaskFromList(wf.config.taskId);
							var items=[{
									xtype:'label',
									region:'center',
									text:data.message
								}];
							var window;
							var cmp=Ext.getCmp('wf_verifica_dati_singola');
							if(!cmp || cmp.getValue()){
								items.push(Ext.create('wf.view.DocumentButton',{
									text: 'Apri documento',
									region:'south',
									columnWidth:.5
								}));
							}else{
								items.push({
									xtype:'button',
									region:'south',
									text:'OK',
									handler:function(){
										window.destroy();
									}
								});
							}
							(window=Ext.create('Ext.window.Window',{
								title: 'Documento generato',
								height: 100,
								width: 400,
								layout:'border',
								modal:true,
								items:items,
								listeners:{
									destroy:function(){
										if(wfg.utils.scrollToTasklist)
											wfg.utils.scrollToTasklist();
									}
								}
							})).show();
						}else{
							handleError(data.message);
						}
					},
					failure: function(response) {
						handleError(response);
					}
				});
			};
		})()
	},
	constructor:function(config){
		this.initConfig(config);
		this.callParent(arguments);
	}


});
function createJSONFromImpegniStore(store){
	 var json = [];
   if(store!=null){
	 var items = store.data.items;

    for (var i=0; i<items.length; i++){
        var item = items[i];
        json.push({
            carico: String(item.data.a_carico),
            anno: item.data.anno,
            capitolo: item.data.capitolo,
            id: item.data.id,
            impegno: item.data.impegno,
            quantita: item.data.bdgPrevQta
        });
    }
   }
    return Ext.JSON.encode(json);
}