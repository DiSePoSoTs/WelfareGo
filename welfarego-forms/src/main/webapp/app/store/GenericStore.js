Ext.define('wf.store.GenericStore',{
	extend: 'Ext.data.Store',
	config:{		
		autoLoad:true
	},
	load:function(){
		var name=this.getStoreName();
		if(!name&&this.proxy&&this.proxy.extraParams){
			name=this.proxy.extraParams.table
		}
		var n1 = wf.data.stores[name];

		if(name && n1){
			log("loading static data for store : "+name); 
			var data=wf.data.stores[name];
//			delete wf.data.stores[name];
			var oldProxy=this.getProxy();
			this.setProxy({
				type: 'memory',
				data:data,
				reader: {
					type: 'json'
				}
			});
			this.callParent(arguments);
			this.setProxy(oldProxy);
		}else{
			this.callParent(arguments);
		}
	},
	setStoreName:function(name){
		this.wfStoreName=name;
	},
	getStoreName:function(){
		return this.wfStoreName;
	},
	constructor:function(config){
		this.initConfig(config);
		this.callParent(arguments);
		var proxy=(this.getProxy?this.getProxy():null);
		if(proxy){
			if(wf.config.taskId){
				proxy.extraParams.task_id=wf.config.taskId;
			}
			proxy.addListener('exception',function(proxy,response){
				var message=Ext.JSON.decode(response.responseText);
                                if(message){
                                    message=message.message;
                                }else{
                                    message="errore generico";
                                }
				var config={
					title:"Errore",
					msg:"Si è verificato un errore durante il caricamento dei dati:<br/>"+message,
					buttons:Ext.Msg.OK
				};
				if(message.match('devi essere loggato')&&wf.utils.doLiferayLogin){
					config.buttons+=Ext.Msg.CANCEL;
					config.msg+="<br/>ricaricare la pagina?";
					config.fn=function(buttonId){
						if(buttonId=="ok")
							wf.utils.doLiferayLogin();
					}
				}
				Ext.Msg.show(config);
			});
		}
		return this;
	}
});
