Ext.define('wf.view.tasklist.TaskListTopBar', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.wtl_tasklist_top_bar',
    store: 'TaskListStore',
    layout: 'vbox',
    height: 100,
    width: "100%",
    items: [
        // prima riga
        {
            xtype: 'toolbar',
            id: 't2',
            width: "100%",
            items: [
                {
                    xtype: 'combo',
                    queryMode: 'local',
                    editable: true,
                    displayField: 'name',
                    valueField: 'value',
                    store: 'combo.POStore',
                    id: 'combo_po_filter',
                    name: 'combo_po_filter',
                    emptyText: 'PO...',
                    columnWidth: 0.2,
                    width: 200,
                    margin: '2 10 2 0',
                    selectOnFocus: true,
                    listeners: {
                        collapse: function(combo) {
                            var store = this.up('gridpanel').getStore();
                            if (!combo.getValue() || combo.getValue() == "") {
                                delete store.proxy.extraParams.poFilter;
                            } else {
                                store.proxy.extraParams.poFilter = combo.getValue();
                            }
                            store.load();
                        },
                        beforequery: function(q) {
                            if (q.query) {
                                var length = q.query.length;
                                q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                q.query.length = length;
                            }
                        }
                    }
                },
                {
                    xtype: 'combo',
                    queryMode: 'local',
                    editable: true,
                    displayField: 'name',
                    valueField: 'value',
                    store: 'combo.UotStore',
                    id: 'combo_uot_filter',
                    name: 'combo_uot_filter',
                    emptyText: 'UOT...',
                    columnWidth: 0.2,
                    margin: '2 10 2 0',
                    selectOnFocus: true,
                    listeners: {
                        collapse: function(combo) {
                            var store = this.up('gridpanel').getStore();
                            if (!combo.getValue() || combo.getValue() == "all") {
                                delete store.proxy.extraParams.uotFilter;
                            } else {
                                store.proxy.extraParams.uotFilter = combo.getValue();
                            }
                            store.load();
                        },
                        beforequery: function(q) {
                                                    if (q.query) {
                                                        var length = q.query.length;
                                                        q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                                        q.query.length = length;
                                                    }
                                                }
                    }
                },
                {
                    xtype: 'combo',
                    queryMode: 'local',
                    editable: true,
                    displayField: 'name',
                    valueField: 'value',
                    //store: Ext.create('wf.store.combo.AssistenteStore',{addValueAll:true}),
                    store: 'combo.AssistenteStore',
                    id: 'combo_assistente_filter',
                    name: 'combo_assistente_filter',
                    emptyText: 'Assistente sociale...',
                    width: 200,
                    margin: '2 10 2 0',
                    columnWidth: 0.3,
                    selectOnFocus: true,
                    listeners: {
                        collapse: function(combo) {
                            var store = this.up('gridpanel').getStore();
                            if (!combo.getValue() || combo.getValue() == "all") {
                                delete store.proxy.extraParams.asSocFilter;
                            } else {
                                store.proxy.extraParams.asSocFilter = combo.getValue();
                            }
                            store.load();
                        },
                        beforequery: function(q) {
                                                    if (q.query) {
                                                        var length = q.query.length;
                                                        q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                                        q.query.length = length;
                                                    }
                                                }
                    }
                }
            ]
        },
        // seconda riga
        {
            xtype: 'toolbar',
            id: 't1',
            width: "100%",
            items: [
                {
                    xtype: 'combo',
                    queryMode: 'local',
                    editable: true,
                    displayField: 'name',
                    valueField: 'value',
                    //store: Ext.create('wf.store.combo.UotStore',{addValueAll:true}),
                    store: 'combo.AttivitaComboStore',
                    id: 'combo_attivita_filter',
                    name: 'combo_attivita_filter',
                    emptyText: 'Attività',
                    columnWidth: 0.3,
                    width: 400,
                    margin: '2 10 2 0',
                    selectOnFocus: true,
                    listeners: {
                        collapse: function(combo) {
                            var store = this.up('gridpanel').getStore();
                            if (!combo.getValue() || combo.getValue() == "all") {
                                delete store.proxy.extraParams.attivitaFilter;
                            } else {
                                store.proxy.extraParams.attivitaFilter = combo.getValue();
                            }
                            store.load();
                        },
                        beforequery: function(q) {
                                                    if (q.query) {
                                                        var length = q.query.length;
                                                        q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                                        q.query.length = length;
                                                    }
                                                }
                    }
                },
                {
                    xtype: 'combo',
                    queryMode: 'local',
                    editable: true,
                    displayField: 'name',
                    valueField: 'value',
                    //store: Ext.create('wf.store.combo.UotStore',{addValueAll:true}),
                    store: 'combo.InterventiComboStore',
                    id: 'combo_interventi_filter',
                    name: 'combo_interventi_filter',
                    emptyText: 'Intervento',
                    columnWidth: 0.2,
                    width: 400,
                    margin: '2 10 2 0',
                    selectOnFocus: true,
                    listeners: {
                        collapse: function(combo) {
                            var store = this.up('gridpanel').getStore();
                            if (!combo.getValue() || combo.getValue() == "") {
                                delete store.proxy.extraParams.interventiFilter;
                            } else {
                                store.proxy.extraParams.interventiFilter = combo.getValue();
                            }
                            store.load();
                        },
                        beforequery: function(q) {
                                                    if (q.query) {
                                                        var length = q.query.length;
                                                        q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                                        q.query.length = length;
                                                    }
                                                }
                    }
                },
                {
                    id: 'btn_reset_filtri',
                    columnWidth: 0.5,
                    xtype: 'button',
                    text: 'reset filtri',
                    handler: function (){
                                            var combo_po_filter = Ext.ComponentQuery.query('#combo_po_filter')[0];
                                            var combo_uot_filter = Ext.ComponentQuery.query('#combo_uot_filter')[0];
                                            var combo_assistente_filter = Ext.ComponentQuery.query('#combo_assistente_filter')[0];
                                            var combo_attivita_filter = Ext.ComponentQuery.query('#combo_attivita_filter')[0];
                                            var combo_interventi_filter = Ext.ComponentQuery.query('#combo_interventi_filter')[0];

                                            Ext.getCmp('combo_po_filter').reset();
                                            Ext.getCmp('combo_uot_filter').reset();
                                            Ext.getCmp('combo_assistente_filter').reset();
                                            Ext.getCmp('combo_attivita_filter').reset();
                                            Ext.getCmp('combo_interventi_filter').reset();

                                            combo_po_filter.reset();
                                            combo_uot_filter.reset();
                                            combo_assistente_filter.reset();
                                            combo_attivita_filter.reset();
                                            combo_interventi_filter.reset();

                                            delete this.up('gridpanel').getStore().proxy.extraParams.poFilter;
                                            delete this.up('gridpanel').getStore().proxy.extraParams.uotFilter;
                                            delete this.up('gridpanel').getStore().proxy.extraParams.asSocFilter;
                                            delete this.up('gridpanel').getStore().proxy.extraParams.attivitaFilter;
                                            delete this.up('gridpanel').getStore().proxy.extraParams.interventiFilter;

                                        }
                }
            ]
        }
    ]
})








