Ext.define('wf.view.BreComponent', {
	extend: 'Ext.form.FieldSet',
	constructor : function() {
		this.fieldDefaults= {
			title: 'Verifica automatica',
			width: '100%'
		};
		this.items =[{
			xtype: 'container',
			layout: 'column',
			items:[{
				xtype: 'textfield',
				id: 'wa_verifica_dati_esito_ver',
				fieldLabel: 'Esito verifica automatica',
				name: 'esito_ver',
				value: 'loading...',
				readOnly: true,
				columnWidth:.7
			},{
				xtype: 'button',
				text:'ripeti verifica',
				handler:function(){
					Ext.getCmp('wa_verifica_dati_esito_ver').setValue('loading...').setFieldStyle('{color:black;}');
					Ext.getCmp('wa_verifica_dati_grid_messaggi').getStore().load();
				}
			}]
			},{
			xtype: 'gridpanel',         // inizio grid Messaggi verifica automatica
			title: 'Messaggi verifica automatica',
			height: 150,
			store: Ext.create('wf.store.MessaggiBreStore',{
				listeners:{
					load:function(store){
						var result="OK",style="color:green";
						if(store.getTotalCount()==0){
							result="la verifica automatica non ha prodotto risultati";
							style="color:black";
						}else if(store.find("level","WARN")!=-1){
							result="WARN";
							style="color:orange";
						}else if(store.find("level","ERROR")!=-1){
							result="ERROR";
							style="color:red";
						}
							  
						Ext.getCmp('wa_verifica_dati_esito_ver').setValue(result).setFieldStyle(style);
					}
				}
			}),
		stateful: true,
		id: 'wa_verifica_dati_grid_messaggi',
		defaults: {
			sortable:false
		},
		columns:[{
			id:'wa_verifica_dati_int_messaggi', 
			header: "Intervento", 
			dataIndex: 'subject'
		},{
			id:'wa_verifica_dati_livello', 
			header: "Livello", 
			dataIndex: 'level',
			renderer:function(value, metadata, record){
				var style="color:black";
				if(value=="INFO"){
					style="color:blue";
				}else if(value=="WARN"){
					style="color:orange";
				}else if(value=="ERROR"){
					style="color:red";
				}
				metadata.tdAttr='style="'+style+'"';
				return value;
			}
		},{
			id:'wa_verifica_dati_messaggio', 
			header: "Messaggio", 
			dataIndex: 'message',
			width:500,
			flex:1,
			renderer:function(value, metadata, record){
				value = Ext.String.htmlEncode(value);
				metadata.tdAttr='title="' + value + '"';
				return value;
			}
		}],
		viewConfig: {
			stripeRows: true
		}
	}];
	this.callParent(arguments);
}
});