Ext.define('wf.view.determine.DetermineBudgetMultipliWindow', {
    extend: 'Ext.window.Window',
    height: 500,
    width: 1050,
    alias: 'widget.wf_determine_budget_multipli_window',
    title: 'Asseganzione budget multipli' ,
    id: 'seleziona_budget',
    name: 'seleziona_budget',
    layout: 'fit',
    modal: true,

    initComponent: function() {
        this.items = [{
                                     xtype:'container',
                                     type: 'vbox',
                                     layout: 'fit',
                                     align : 'stretch',
                                     pack  : 'start',
                                     items:[
                                         {
                                             xtype: 'grid',
                                             cls: 'asseganzione_budget_multipli_grid',
                                             id:'dati_budgets_grid',
                                             name:'dati_budgets_grid',
                                             border: false,
//                                             selModel: Ext.create('Ext.selection.CheckboxModel'),
                                             selType: 'checkboxmodel',
                                             selModel: {
                                                mode: 'MULTI',
                                                getHeaderConfig: function () {
                                                    var me = this;
                                                    return {
                                                        width: me.headerWidth,
                                                        renderer: Ext.Function.bind(me.renderer, me)
                                                    };
                                                }
                                            },
                                             store: Ext.data.StoreManager.lookup('BudgetStore'),
                                             defaults: {
                                                 sortable: true
                                             },
                                             scrollable: true,
                                             columns: [
                                                 { text:'anno Erogazione', dataIndex:'annoErogazione', flex: 1},
                                                 { text:'cod Anno', dataIndex:'codAnno', flex: 1},
                                                 { text:'cod Impegno', dataIndex:'codImpegno', flex: 1},
                                                 { text:'Budget iniziale', dataIndex:'bdgDisponibileEuro', flex: 1, align: 'right'}
                                             ]
                                         }
                                     ]
                         }];
        this.buttons = [{
         xtype: 'button',
         text: 'assegna',
         handler: function(){
             var budgetSelezionati = Ext.getCmp('dati_budgets_grid').getSelectionModel().selected.items;

             if(budgetSelezionati.length> 2){
                 Ext.Msg.alert("Attenzione!", "E' possibile selezionare al massimo 2 budget.");
                 return;
             }

             if(budgetSelezionati.length <1){
                 Ext.Msg.alert("Attenzione!", "Selezionare almeno un budget.");
                 return;
             }


             if(budgetSelezionati.length === 2){
                 if(budgetSelezionati[0].data.annoErogazione === budgetSelezionati[1].data.annoErogazione){
                     Ext.Msg.alert("Attenzione!", "Sono stati selezionati 2 budget con lo stesso anno di erogazione.");
                     return;
                 }
             }

             var datiBudgetSelezionati = [];
             budgetSelezionati.forEach(function(item) { datiBudgetSelezionati.push(item.data) })

             var datiDetermine = []
             var determine = Ext.getCmp('pannelloDetermine').getSelectionModel().selected.items;
             determine.forEach(function(item){ datiDetermine.push(item.data); } );

             Ext.Ajax.request({
                     url: '/WelfaregoForms/ModificaBudget/suddividi-importi-sui-budget',
                     method:'post',
                     jsonData:{
                         'datiBudgetSelezionati':datiBudgetSelezionati,
                         'datiDetermine':datiDetermine
                     },
                     success: function (response) {
                        var tuttoOk = true;

                        try{
                               var errore_generico = Ext.JSON.decode(response.responseText).map.errore_generico;
                               if(errore_generico){
                                    Ext.MessageBox.show({
                                         title: 'Errore',
                                         msg: 'non è stato possibile elaborare: ' + errore_generico,
                                         buttons: Ext.MessageBox.OK,
                                         icon: Ext.window.MessageBox.ERROR,
                                         fn: function(){
                                            var determineStore=Ext.getStore('DetermineStore');
                                            determineStore.load();
                                            Ext.getCmp('seleziona_budget').close();
                                         }
                                     });
                               }
                        }catch(err){
                            // non ho avuto problemi nel BE
                        }

                        try{
                            var soggetti_da_controllare = Ext.JSON.decode(response.responseText).map.soggetti_da_controllare.myArrayList;

                            var nomi = soggetti_da_controllare.join('\n');
                            if(nomi.length> 0){
                                tuttoOk = false;


                                var riga_1 = 'Non è stato possibile assegnare il budget a:' + '\n';
                                var riga_2 = nomi;
                                var riga_3 = '\nL\'anno del budget scelto non è compatibile con il periodo di validità dell\'intervento.'
                                var riga_4 = '\nEffettuare l\'assegnazione singolarmente.'
                                var messaggio = riga_1 + riga_2 + riga_3 + riga_4;

                                var win_msg_error = Ext.create('Ext.window.Window',{
                                    title: 'Errore',
                                    height: 250,
                                    width: 300,
                                    modal:true,
                                    layout:'fit',
                                    id: 'win_msg_error',
                                    name: 'win_msg_error',
                                    items:[
                                        {
                                            xtype:'textareafield',
                                            allowBlank: false,
                                            id: 'wcs_soggetti_da_controllare_manualmente',
                                            name: 'wcs_soggetti_da_controllare_manualmente',
                                            value: messaggio
                                        }
                                    ],

                                    buttons:[
                                        {
                                            text:'chiudi',
                                            handler:function(){
                                                var determineStore=Ext.getStore('DetermineStore');
                                                determineStore.load();
                                                Ext.getCmp('win_msg_error').close();
                                                Ext.getCmp('seleziona_budget').close();
                                            }
                                        }
                                    ]
                                });
                                win_msg_error.show();







                            }
                        }catch (err ){
                            // non ho avuto problemi nel BE
                        }

                        if(tuttoOk){
                            var determineStore=Ext.getStore('DetermineStore');
                            determineStore.load();
                            Ext.getCmp('seleziona_budget').close();
                        }


                     },
                     failure: function (aa, bb, cc) {
                         Ext.MessageBox.show({
                             title: 'Errore',
                             msg: 'Contattate l\' assistenza tecnica, si è verificato un errore',
                             buttons: Ext.MessageBox.OK,
                             icon: Ext.window.MessageBox.ERROR
                         });
                     }
                 });

         }

                                                                 }];

        this.callParent(arguments);
    }




    //@@EXT
});
