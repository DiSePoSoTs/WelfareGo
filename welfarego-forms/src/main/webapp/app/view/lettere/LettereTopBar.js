Ext.define('wf.view.lettere.LettereTopBar',{
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.wtl_lettere_top_bar',
	store: 'LettereStore',
	layout: 'column',
	items : [{
		xtype:'combo',
		queryMode: 'local',
		displayField: 'name',
		valueField: 'value',
		//store: Ext.create('wf.store.combo.UotStore',{addValueAll:true}),
		store:'combo.UotStore',
		id: 'wtl_uot_filter',
		name: 'wtl_uot_filter',
		emptyText: 'UOT...',
		columnWidth:0.5,
		selectOnFocus:true,
		editable: false,
		listeners: {
			collapse:function(combo) {
				var store=this.up('gridpanel').getStore();
				if(!combo.getValue()||combo.getValue()=="all"){
					delete store.proxy.extraParams.uotFilter;
				}else{
					store.proxy.extraParams.uotFilter=combo.getValue();
				}
				store.load();
			}
		}
	},{
		xtype:'combo',
		queryMode: 'local',
		displayField: 'name',
		valueField: 'value',
		//store: Ext.create('wf.store.combo.AssistenteStore',{addValueAll:true}),
		store:'combo.LettereSearchStore',
		id: 'wtl_lettere_filter',
		name: 'wtl_lettere_filter',
		emptyText: 'Tipologia lettere',
		columnWidth:0.5,
		selectOnFocus:true,
		editable: false,
		listeners: {
			collapse:function(combo) {
				var store=this.up('gridpanel').getStore();
				if(!combo.getValue()||combo.getValue()=="all"){
					delete store.proxy.extraParams.tipoLettereFilter;					
				}else{
					store.proxy.extraParams.tipoLettereFilter=combo.getValue();
				}
				store.load();
			}
		}
	}]
})