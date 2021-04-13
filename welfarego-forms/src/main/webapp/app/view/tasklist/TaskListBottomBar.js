Ext.require('wf.view.FilterField');
(function(){
	function getUserDesc(){
		if(!wf.info.user)
			return 'non loggato!';
		var user=wf.info.user,text='';
		text+='<b>uot:</b> '+user.uot.join(', ')+'<br/>';
		text+='<b>ruoli:</b> '+user.roles.join(', ')+'<br/>';
		text+='<b>servizi:</b> '+user.services.join(', ')+'<br/>';
		text+='--<br/>';
		text+='<i>gruppi:</i> '+user.groups.join(', ');
		return text;
	}
	Ext.define('wf.view.tasklist.TaskListBottomBar',{
		extend: 'Ext.PagingToolbar',
		alias: 'widget.wtl_tasklist_bottom_bar',
		store: 'TaskListStore',
		displayInfo: true,
		displayMsg: 'Visualizzo le attivit&agrave; da {0} a {1} di {2}',
		emptyMsg: 'Nessuna attivit&agrave;',
		items:['-',{
			xtype:'combo',
			queryMode: 'local',
			displayField: 'name',
			valueField: 'value',
			store: 'combo.TaskSearchStore',
			id: 'wtl_searchCombo',
			name: 'wtl_searchCombo',
			value: 'AUTO',
			width:150,
			//        forceSelection: true,
			selectOnFocus:true,
			editable: false
		},{
			xtype:'filter_field',
			emptyText: 'Cerca nella griglia',
			searchByFieldId: 'wtl_searchCombo',
			disableKeyFilter : true        
		},{
			xtype:'button',
			text:wf.info.user?wf.info.user.user_name:'non loggato!',
			tooltip:getUserDesc(),
			handler:function(){
				if(wf.info.user){
					Ext.Msg.alert("Dati utente : "+wf.info.user.user_name,getUserDesc());
				}
			}
		}]
	})
})();