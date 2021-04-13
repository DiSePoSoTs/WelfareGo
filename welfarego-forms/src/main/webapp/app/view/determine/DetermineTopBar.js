Ext.define('wf.view.determine.DetermineTopBar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.wf_determine_top_bar',
	id: 'determine_top_bar',
	name: 'determine_top_bar',
	store: 'DetermineStore',
	layout: 'column',
	initComponent: function () {

		var tipintComboStore = Ext.create('wf.store.combo.TipologieInterventoStore', {
			//            fields: ['name','value','classTipint']
			//            listeners:{
			//                load:function(store){
			////                    log('loaded Tipologie Intervento combo values');
			////                    store.insert(0,{
			////                        name:'tutti gli interventi per questa lista d\'attesa',
			////                        value:'all'
			////                    });
			////                    Ext.getCmp('wd_list_attesa_tipi_int_filter').setValue('void');
			////                    Ext.getCmp('wd_list_attesa_tipi_int_filter').setValue('all');
			////                    var mainStore
			////                    var store
			//                    delete determineStore.getProxy().extraParams.tipo_intervento;
			//                    determineStore.load();
			//                }
			//            }
		});

        Ext.create('wf.store.BudgetStore', {
            autoLoad: false
        });

		var determineStore = Ext.getStore('DetermineStore');

		this.items = [{
			xtype: 'combo',
			queryMode: 'local',
			editable: false,
			displayField: 'name',
			valueField: 'value',
			store: 'combo.ClassiTipologieInterventoStore',
			id: 'wf_classe_tipi_int_filter',
			columnWidth: .3,
			emptyText: 'Classe tipo intervento ...',
			selectOnFocus: true,
			listeners: {
				change: function (combo, value) {
					//TODO
					//					var store=this.up('gridpanel').getStore();
					//					store.getProxy().extraParams.tipo_intervento=value;
					//					if(store.getProxy().extraParams.stato_intervento)
					//						store.load();
					//                    Ext.get('wf_tipi_int_filter').reset();
					if (value != "0") {
						tipintComboStore.clearFilter(true);
						tipintComboStore.filter('class_tipint', value);
					} else {
						tipintComboStore.clearFilter(false);
					}
					Ext.getCmp('wf_tipi_int_filter').reset();
					//                    delete determineStore.getProxy().extraParams.tipo_intervento;
					//                    determineStore.load();
				}
			}
		}, {
			xtype: 'combo',
			queryMode: 'local',
			displayField: 'name',
			valueField: 'value',
			store: tipintComboStore,
			id: 'wf_tipi_int_filter',
			name: 'wf_tipi_int_filter',
			columnWidth: .4,
			emptyText: 'Tipo intervento ...',
			selectOnFocus: true,
			editable: true,
			listeners: {
				select: function (combo, record, value) {
					Ext.getCmp('data_avvio_field').reset();
					var store = this.up('gridpanel').getStore();
					//  record = combo.getStore().getById(value);
					var rinnovo = record[0].data.rinnovo;
					if (rinnovo == "D") {
						Ext.getCmp('determinaParzialeButton').enable();
					} else {
						Ext.getCmp('determinaParzialeButton').disable();
					}
					var misura = record[0].data.misura;
					Ext.getCmp('misura').setValue(misura);
					store.getProxy().extraParams.tipo_intervento = record[0].data.value;
					if (store.getProxy().extraParams.stato_intervento) {
						store.load();
						this.up('gridpanel').doLayout();
					}
				},
				afterrender: function (field) { // fix filter
					setTimeout(function () {
						var event = document.createEvent("HTMLEvents");
						event.initEvent('click', true, true);
						Ext.getCmp('wf_tipi_int_filter').getEl().down('input').dom.dispatchEvent(event);
					}, 100);

				},
				beforequery: function (q) {
					if (q.query) {
						var length = q.query.length;
						q.query = new RegExp(Ext.escapeRe(q.query), 'i');
						q.query.length = length;
					}
				}
			}
		}, {
			xtype: 'combo',
			queryMode: 'local',
			displayField: 'name',
			valueField: 'value',
			store: 'combo.StatoInterventoStore',
			id: 'wf_stato_int_filter',
			columnWidth: .3,
			emptyText: 'Stato intervento ...',
			selectOnFocus: true,
			editable: false,
			listeners: {
				change: function (combo, value) {
					var store = this.up('gridpanel').getStore();
					store.getProxy().extraParams.stato_intervento = value;

					if (store.getProxy().extraParams.tipo_intervento) {
						store.load();
						this.up('gridpanel').doLayout();
						Ext.getCmp('data_avvio_field').reset();
					}
				}
			}
		}, {
			xtype: 'datefield',
			id: 'data_avvio_field',
			format: 'd/m/Y',
			fieldLabel: 'Data avvio intervento',
			columnWidth: .2,
			selectOnFocus: true,
			listeners: {
				change: function (datefield) {
					var store = this.up('gridpanel').getStore();
					var newValue = datefield.getRawValue();
					if (newValue != null && newValue != '') {
						store.getProxy().extraParams.data_avvio = newValue;
						if (store.getProxy().extraParams.tipo_intervento) {
							store.load();
							this.up('gridpanel').doLayout();
						}

					}
				}
			}
		},
		 {
			xtype: 'hiddenfield',
			id: 'misura'

		}, {
			xtype: 'hiddenfield',
			id: 'selectedEvento'

		}];

		this.callParent(arguments);
	}
})