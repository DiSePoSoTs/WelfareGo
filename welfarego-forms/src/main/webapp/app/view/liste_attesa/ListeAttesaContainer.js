Ext.define('wf.view.liste_attesa.ListeAttesaContainer',{
	extend: 'Ext.form.Panel',
	alias: 'widget.wd_liste_attesa_container',
	layout:'anchor',
	frame:true,
	initComponent:function(){
		this.items=[{
			xtype:'fieldset',
			title:'Filtra',
			layout:'column',
			anchor:'100%',
			items:[{
				xtype:'container',
				columnWidth:.5,
				layout:'anchor',
				items:[{
					xtype:'combo',
					fieldLabel:'Lista d\'attesa',
					queryMode: 'local',
					displayField: 'name',
					valueField: 'value',
					store: Ext.create('wf.store.combo.ListeAttesaStore',{
						listeners:{
							load:function(store){
								log('loaded Lista Attesa combo values');
							//						Ext.getCmp('wd_lista_attesa_filter').setValue(store.first());
							}
						}
					}),
					id: 'wd_lista_attesa_filter',
					name: 'wd_lista_attesa_filter',
					emptyText: 'Lista attesa ...',
					selectOnFocus:true,
					editable: false,
					anchor:'98%',
					listeners: {
						change: function(combo, value) {		
							log('Lista Attesa combo value change');				
							Ext.getCmp('wd_list_attesa_tipi_int_filter').store.load({
								params:{
									lista_attesa:value
								}
							});
							Ext.getCmp('wd_lista_attesa_grid_panel').getStore().proxy.extraParams.lista_attesa=value;						
						}
					}
				}]
			},{
				xtype:'container',
				columnWidth:.5,
				layout:'anchor',
				items:[{
					xtype:'combo',
					fieldLabel:'Intervento',
					queryMode: 'local',
					displayField: 'name',
					valueField: 'value',
					store: Ext.create('wf.store.combo.TipologieInterventoStore',{
						autoLoad:false,
						listeners:{
							load:function(store){
								log('loaded Tipologie Intervento combo values');	
								store.insert(0,{
									name:'tutti gli interventi per questa lista d\'attesa',
									value:'all'
								});
								Ext.getCmp('wd_list_attesa_tipi_int_filter').setValue('void');
								Ext.getCmp('wd_list_attesa_tipi_int_filter').setValue('all');
							}
						}
					}),
					id: 'wd_list_attesa_tipi_int_filter',
					name: 'wd_list_attesa_tipi_int_filter',
					emptyText: 'Tipo intervento ...',
					selectOnFocus:true,
					editable: false,
					anchor:'98%',
					listeners: {
						change:function(combo, value) {
							if(value=='void')
								return;
							log('Tipologie Intervento combo value change');	
							var store=Ext.getCmp('wd_lista_attesa_grid_panel').getStore();
							store.proxy.extraParams.tipologia_intervento=value;
							store.load();						
						}
					}
				}]
			}]
		},{
			anchor:'100%',
			xtype:'wd_lista_attesa_grid',
			id:'wd_lista_attesa_grid_panel',
			height:520
		}];
	
		this.callParent(arguments);
	}
});