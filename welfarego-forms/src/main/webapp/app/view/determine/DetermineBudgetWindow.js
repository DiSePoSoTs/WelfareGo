Ext.define('wf.view.determine.DetermineBudgetWindow', {
    extend: 'Ext.window.Window',
    height: 500,
    width: 1050,
    alias: 'widget.wf_determine_budget_window',
    title: 'Modifica budget',
    id: 'Modifica_budget',
    name: 'Modifica_budget',
    layout: 'column',
    modal: true,
    initComponent: function() {

        Ext.require('Ext.grid.property.HeaderContainer', function() {

            Ext.override(Ext.grid.property.HeaderContainer, {
                constructor: function() {
                    //enable resizing
                    this.enableColumnResize = true;
                    this.callParent(arguments);
                }
            });
        });

        Ext.regModel('StateModel', {

            fields: [
                {type: 'string', name: 'abbr'},
                {type: 'string', name: 'name'},
                {type: 'string', name: 'slogan'}
            ]
        });

        var currentTime = new Date();
        var currentYear = currentTime.getFullYear();
        var anniArray = [];
        var index = 0;

        for(var i = 2011; i <= currentYear + 1; i++){

            anniArray[index++]=[i, i];
        }

        var anniStore = new Ext.data.SimpleStore({

            fields: ['id', 'value'],
            data : anniArray
        });

        var impegniStore = Ext.create('wf.store.ImpegniBudgetStore', {

            autoLoad: false,
            listeners: {
                datachanged: function(store) {
                    //                        store.load(function(){
                    updateBudgetResiduo();
                    //                        });
                }
            }

        });

        function setVisibleQuantitaFields(visible) {

            log('visibilita campi quantita : ', visible);
            try {
                Ext.getCmp('wf_verifica_impegni_acarico_sum_qta').setVisible(visible);
                Ext.getCmp('wf_verifica_impegni_acarico_rem_qta').setVisible(visible);
                Ext.getCmp('wf_unitaDiMisuraDescColumn').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaColumn').setVisible(visible);
                Ext.getCmp('wf_bdgPrevQtaColumn').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaDispColumn').setVisible(visible);

            } catch (e) {
                // if invoked before the grid is shown, will throw an error
                // simplest thing is, catch the error and ignore it. The function will
                // be invoked successfully later
                log('expected error : ', e);
            }
        }

        var updateBudgetResiduo = function() {

            if (Ext.getCmp('wf_verifica_dati_grid_impegni') === undefined) {

            } else {
                var
                    store = Ext.getCmp('wf_verifica_dati_grid_impegni').getStore();
                sum = store.sum('a_carico'),
                    sumQta = store.sum('bdgPrevQta'),
                    prev = Ext.getCmp('wf_verifica_dati_cost_tot').getValue(),
                    prevQta = Ext.getCmp('wf_verifica_dati_qta_tot').getValue(),
                    res = prev - sum,
                    resQta = prevQta - sumQta;
//                log('DetermineBudgetWindow sum = ', sum, ' sumQta = ', sumQta, ' prev = ', prev, ' prevQta = ', prevQta, ' res = ', res, ' resQta = ', resQta);
                Ext.getCmp('wf_verifica_impegni_acarico_sum').setValue(sum);
                Ext.getCmp('wf_verifica_impegni_acarico_rem').setValue(res);
                Ext.getCmp('wf_verifica_impegni_acarico_sum_qta').setValue(sumQta);
                Ext.getCmp('wf_verifica_impegni_acarico_rem_qta').setValue(resQta);
                //            var bdgDispQta=store.sum('bdgDispQta');
                //            if(bdgDispQta==0&&sumQta==0){*/
                /*  if (store.count() > 0) {
                      var value = store.first().get('unitaDiMisuraDesc');
                     // setVisibleQuantitaFields((value && value.match('euro')) ? false : true);
                  }*/
            }

        };

        this.items = [
            {
                xtype: 'container',
                layout: 'anchor',
                columnWidth: .9,
                items: [

                    {
                        xtype:'container',
                        layout:'column',
                        defaults:{
                            columnWidth:.5,
                            labelWidth: 100
                        },
                        items:[
                        // prima colonna
                        {
                            xtype:'container',
                            layout:'anchor',
                            defaultType:'textfield',
                            defaults:{
                                anchor:'55%'
                            },
                            items:[
                                // prima riga prima colonna
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'Cognome',
                                    name: 'cognome',
                                    id: 'cognome',
                                    readOnly: true
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'Nome',
                                    name: 'nome',
                                    id: 'nome',
                                    readOnly: true
                                },
                                {
                                    xtype: 'hiddenfield',
                                    fieldLabel: 'idevento',
                                    name: 'idevento',
                                    id: 'evento',
                                    readOnly: true
                                },
                                {
                                   xtype: 'combo',
                                   name: 'anni',
                                   fieldLabel: 'Anni',
                                   mode: 'local',
                                   store: anniStore,
                                   displayField:'value',
                                   valueField: 'id',
                                   id: 'anni_ricerca_budget',
                                   name: 'anni_ricerca_budget',
                                   multiSelect: true
                                 },
                                {
                                    xtype: 'button',
                                    text : 'cerca',
                                    anchor:'30%',
                                    handler: function() {
                                      //this.up('.window')
                                      //http://docs.sencha.com/extjs/4.0.7/#!/api/Ext.form.field.ComboBox-method-getValue
                                      //log('anni: ', this);
                                      //debugger;
                                      var filtroAnni = Ext.getCmp('anni_ricerca_budget').getValue().toString();
                                      log('anni: ', filtroAnni);
                                      impegniStore.load({
                                                params: {filtroAnni: filtroAnni},
                                                callback: function(records, operation, success){
                                                    log("success: ", success);
                                                }
                                      });
                                    }
                                }
                            ]
                        },
                        // seconda colonna
                        {
                            xtype:'container',
                            layout:'anchor',
                            defaultType:'textfield',
                            defaults:{
                                anchor:'95%'
                            },
                            items:[
                                // prima riga seconda colonna
                                {
                                    xtype: 'button',
                                    text : 'azzera budget',
                                    anchor:'40%',
                                    id: 'azzera_budget',
                                    name: 'azzera_budget',
                                    cls: 'azzera_budget_btn_class',
                                    handler: function() {

                                        Ext.Ajax.request({
                                            url: '/WelfaregoForms/ModificaBudget/azzerabudget',
                                            params: {
                                                idEvento: Ext.getCmp('evento').getValue()
                                            },
                                            success: function (response) {
                                              log('success', response);
                                              var filtroAnni = Ext.getCmp('anni_ricerca_budget').getValue().toString();
                                              log('anni: ', filtroAnni);
                                              if(filtroAnni){
                                                    impegniStore.load({
                                                        params: {filtroAnni: filtroAnni},
                                                        callback: function(records, operation, success){
                                                            log("success: ", success);
                                                        }
                                                    });
                                              }
                                            },
                                            failure: function (aa, bb, cc) {
                                                log('failure', aa, bb, cc);
                                                Ext.MessageBox.show({
                                                    title: 'Errore',
                                                    msg: 'Contattate l\' assistenza tecnica, si è verificato un errore',
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.window.MessageBox.ERROR
                                                });
                                            }
                                        });
                                    }
                                }
                            ]
                        }
                        ]
                    },
                    {
                        xtype: 'wdecimalnumberfield',
                        allowDecimals: true,
                        id: 'wf_verifica_dati_cost_tot',
                        hidden: true
                    },
                    {
                        xtype: 'wdecimalnumberfield',
                        id: 'wf_verifica_dati_qta_tot',
                        //                fieldLabel: 'Costo totale previsto \u20ac',
                        // name: 'quantita_tot',
                        hidden: true
                    }
                ]
            } ,
            {
                xtype: 'gridpanel',
                title: 'Impegni',
                autoHeight: true,
                // height: 400,
                columnWidth: 0.99,
                store: impegniStore,
                stateful: true,
                id: 'wf_verifica_dati_grid_impegni',
                defaults: {
                    sortable: true
                },
                //			features: [{                //risultati raggruppabili
                //				ftype: 'grouping'
                //			}],
                columns: [{
                        id: 'wf_verifica_dati_anno',
                        header: "Anno",
                        dataIndex: 'anno'
                        //  flex: 1
                    }, {
                        id: 'wf_verifica_dati_capitolo',
                        header: "Capitolo",
                        dataIndex: 'capitolo'
                        //flex: 1
                    }, {
                        id: 'wf_verifica_dati_impegno',
                        header: "Impegno",
                        dataIndex: 'impegno'
                        //flex: 1
                    },
                    {
                        header: 'importo',
                        columns: [
                            {
                                id: 'wf_verifica_dati_imp_disp_gda',
                                header: "Budget disponibile ",
                                dataIndex: 'imp_disp_netto',
                                //    flex: 1,
                                width: 120,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €'
                            }, {
                                id: 'wf_verifica_dati_imp_disp',
                                header: "Budget disponibile con prenotazioni",
                                dataIndex: 'imp_disp',
                                //      flex: 1,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €'
                            },
                            {
                                id: 'wf_verifica_dati_imp_disp_proroghe',
                                header: "Disponibile con proroghe",
                                dataIndex: 'imp_disp_proroghe',
                                //    flex: 1,
                                //    width:120,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €'
                            }, {
                                id: 'wf_verifica_dati_a_carico',
                                header: "A carico",
                                dataIndex: 'a_carico',
                                //  flex: 1,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €',
                                editor: {
                                    xtype: 'weuronumberfield',
                                    allowBlank: false,
                                    //    	                                    decimalSeparator: ',',
                                    listeners: {
                                        edit: function() {
                                            log('reloading impegni store');
                                            impegniStore.load();
                                        }
                                    }
                                }
                            }
                        ]
                    }, {
                        header: 'quantita',
                        columns: [
                        {
                                //                id:'wf_verifica_dati_imp_disp',
                                header: " ",
                                dataIndex: 'unitaDiMisuraDesc',
                                width: 90,
                                id: 'wf_unitaDiMisuraDescColumn',
                                hidden: true
                            }, {
                                //                id:'wf_verifica_dati_imp_disp',
                                header: "disponibile",
                                dataIndex: 'bdgDispQta',
                                //flex: 1,
                                id: 'wf_bdgDispQtaColumn',
                                xtype: 'numbercolumn'
                            },
                            {
                                //                id:'wf_verifica_dati_imp_disp',
                                header: "disponibile senza prenotazioni",
                                dataIndex: 'bdgDispQtaCons',
                                // flex: 1,
                                id: 'wf_bdgDispQtaDispColumn',
                                xtype: 'numbercolumn'
                            }, {
                                //                id:'wf_verifica_dati_a_carico',
                                header: "A carico",
                                dataIndex: 'bdgPrevQta',
                                xtype: 'numbercolumn',
                                // flex: 1,
                                editor: {
                                    xtype: 'numberfield',
                                    allowBlank: false,
                                    decimalSeparator: ',',
                                    listeners: {
                                        edit: function() {
                                            log('reloading impegni store');
                                            impegniStore.load();
                                        }
                                    }
                                },
                                id: 'wf_bdgPrevQtaColumn'
                            }
                        ]
                    }
                ],
                viewConfig: {
                    stripeRows: true
                },
                bbar: Ext.create('Ext.PagingToolbar', {
                    //  store: impegniStore,
                    displayInfo: true,
                    items: [{
                        //	labelWidth:120,
                        labelWidth: 80,
                        xtype: 'weuronumberfield',
                        id: 'wf_verifica_impegni_acarico_sum',
                        fieldLabel: 'importo assegnato €',
                        readOnly: true,
                        //                    hidden:true,
                        width: 180
                        //    	                            format: '0,000.00 €', Won't work on numberfield :/
                        //    	                            decimalSeparator: ','
                    }, {
                        //	labelWidth:120,
                        labelWidth: 80,
                        xtype: 'weuronumberfield',
                        id: 'wf_verifica_impegni_acarico_rem',
                        fieldLabel: 'importo da assegnare €',
                        readOnly: true,
                        width: 180
                        //    	                            format: '0,000.00 €', Won't work on numberfield :/
                        //    	                            decimalSeparator: ','
                    }, {
                        //	labelWidth:120,
                        labelWidth: 80,
                        xtype: 'numberfield',
                        id: 'wf_verifica_impegni_acarico_sum_qta',
                        fieldLabel: 'quantita assegnata',
                        readOnly: true,
                        //                    hidden:true,
                        //    	                            decimalSeparator: ',',
                        width: 160
                    }, {
                        //	labelWidth:120,
                        labelWidth: 80,
                        xtype: 'numberfield',
                        id: 'wf_verifica_impegni_acarico_rem_qta',
                        fieldLabel: 'quantita da assegnare',
                        readOnly: true,
                        //    	                            decimalSeparator: ',',
                        width: 160
                    }]
                }),
                plugins: [
                    Ext.create('Ext.grid.plugin.RowEditing', {
                        clicksToEdit: 1
                    })
                ]
            }
        ];


        this.buttons = [{
                text: 'Chiudi',
                handler: function() {
                // dopo le modifiche bisogna aspettare che il triangolo rosso sparisca se si vuole che la griglia degli implegni
                // venga aggiornata in automatico.
                    debugger;
                    log('reloading impegni store');
                    var determineStore=Ext.getStore('DetermineStore');
                    determineStore.load();
                    this.up('.window').close();
                } //fine handler
            }];

        // ricaricaStore();
        this.callParent(arguments);

    } //fine this
});
