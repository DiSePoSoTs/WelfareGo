Ext.define('wf.view.determine.DetermineGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wf_determine_grid',
    id: 'pannelloDetermine',
    title: 'Determine',
    store: 'DetermineStore',
    autoScroll: true,
    /*verticalScroller: {
        xtype: 'paginggridscroller'
      },*/
    loadMask: true,
    columnLines: true,
    frame: true,
    tbar: {

        xtype: 'wf_determine_top_bar'
    },

    onViewRefresh: function() {

        var me = this;
        try {
            me.callParent();
        } catch (e) {};
    },

    listeners: {

        scrollershow: function(scroller) {
            if (scroller && scroller.scrollEl) {
                scroller.clearManagedListeners();
                scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller);
            }
        },
        itemdblclick: function(dv, record, item, index, e) {
            Ext.getCmp('selectedEvento').setValue(record.data.id);
            var window = Ext.create('wf.view.determine.DetermineBudgetWindow');
            Ext.getCmp('cognome').setValue(record.data.cognome);
            Ext.getCmp('nome').setValue(record.data.nome);
            Ext.getCmp('evento').setValue(record.data.id);
            Ext.getCmp('wf_verifica_dati_cost_tot').setValue(record.data.costo);
            Ext.getCmp('wf_verifica_dati_qta_tot').setValue(record.data.quantita);
            var impegniStore = Ext.getCmp('wf_verifica_dati_grid_impegni').getStore();
            impegniStore.proxy.extraParams.idEvento = record.data.id;
            /*
        impegniStore.load();
        */
            window.show();

        }
    },



    /*  bbar:{
          xtype:'pagingtoolbar',
          store:'DetermineStore',
          displayInfo : true
      },*/
    height: "auto !important",

    initComponent: function() {

        var resendRequestToServer = function(paramsToSend) {

            paramsToSend.escludiVerificaEsistenzaBudget = true;
            log(paramsToSend);
            Ext.Ajax.request({
                url: wf.config.path.determine,
                params: paramsToSend,
                success: function(response) {

                    var data = Ext.JSON.decode(response.responseText);
                    if (data.success) {
                        Ext.getCmp('wf_determine_container').setLoading(false);
                        Ext.Msg.show({
                            title: 'Successo',
                            msg: data.message,
                            buttons: Ext.Msg.OK,
                            fn: function() {
                                Ext.getCmp('wf_determine_container').add(Ext.create('wf.view.determine.DetermineResultPanel'));
                                Ext.getCmp('wf_determine_container').getLayout().next();
                            }
                        });
                    } else {
                        Ext.getCmp('wf_determine_container').setLoading(false);
                        Ext.Msg.show({
                            title: 'Errore',
                            msg: data.message.replace(/ [^ ]*Exception: /, " "),
                            buttons: Ext.Msg.OK
                        });
                    }
                },

                failure: function(response) {

                    log('4: ' + response);
                    Ext.getCmp('wf_determine_container').setLoading(false);
                    var data = new Object();
                    data.message = 'Errore generico, si prega di contattare l\'amministratore';
                    if (response != null && response.responseText != null) {
                        data = Ext.JSON.decode(response.responseText);
                        debugger;
                        data.message = data.message.replace(/ [^ ]*Exception: /, " ");
                    }
                    Ext.Msg.show({
                        title: 'Errore',
                        msg: data.message,
                        buttons: Ext.Msg.OK
                    });
                }
            });

        };

        this.selModel = Ext.create('Ext.selection.CheckboxModel');
        this.columns = [

            {
                header: 'Id evento',
                dataIndex: 'id',
                sortable: false,
                renderer: this.renderGridCell,
                hidden: true,
                flex: 1
            },
            {
                header: 'Cognome',
                dataIndex: 'cognome',
                sortable: true,
                renderer: this.renderGridCell,
                flex: 1
            }, {
                header: 'Nome',
                dataIndex: 'nome',
                sortable: false,
                renderer: this.renderGridCell,
                flex: 1
            }, {
                header: 'Data approvazione coordinatore',
                dataIndex: 'data_richiesta',
                sortable: true,
                sortable: true,
                renderer: this.renderGridCell,
                flex: 1
            }, {
               header: 'Data fine intervento',
               dataIndex: 'data_fine_intervento',
               sortable: true,
               renderer: this.renderGridCell,
               flex: 1
           }, {
                header: 'UOT',
                dataIndex: 'uot',
                sortable: true,
                renderer: this.renderGridCell,
                flex: 1
            }, {
                header: 'ISEE',
                dataIndex: 'isee',
                sortable: false,
                //            renderer: this.renderGridCell,
                flex: 1,
                xtype: 'numbercolumn',
                format: '0,000.00 €'
            }, {
                header: 'Impegni',
                dataIndex: 'budget',
                sortable: false,
                renderer: this.renderGridCell,
                flex: 1

            }, {
                header: 'Stato',
                dataIndex: 'stato',
                sortable: false,
                renderer: this.renderGridCell,
                hidden: true,
                flex: 1
            }, {
                header: 'Costo',
                dataIndex: 'costo',
                sortable: false,
                renderer: this.renderGridCell,
                xtype: 'numbercolumn',
                format: '0,000.00 €',
                flex: 1
            },
            {
                header: 'quantita',
                dataIndex: 'quantita',
                sortable: false,
                renderer: this.renderGridCell,
                xtype: 'numbercolumn',
                format: '0,000.00 €',
                hidden: true,
                flex: 1
            }
        ];


        this.buttons = [
            {
		    xtype: 'button',
		    text: 'Assegnazione multipla budget',
		    id: 'assegnazione_multipla_budget',
		    name: 'assegnazione_multipla_budget',
		    handler: function () {

                var codTipint = Ext.getCmp('wf_tipi_int_filter').getValue();

                if(codTipint){

                    var interventiSelezionati = Ext.getCmp('pannelloDetermine').getSelectionModel().selected.items;

                    if(!interventiSelezionati || interventiSelezionati.length === 0){
                        Ext.Msg.alert("Attenzione!", "Selezionare almeno un elemento dalla lista.");
                        return;
                    }

                    var budgetStore = Ext.data.StoreManager.lookup('BudgetStore');
                    budgetStore.getProxy().extraParams.codTipint = codTipint;
                    budgetStore.load();

                    var determineBudgetMultipliWindow = Ext.create('wf.view.determine.DetermineBudgetMultipliWindow');
                    determineBudgetMultipliWindow.show();

                }else {
                    Ext.Msg.alert("Attenzione!", "Selezionare una tipologia di intervento.");
                }
            }
		},
            {
                text: 'Anteprima in Excel',
                handler: function() {
                    var url = wf.config.path.base + '/PreviewReport?parameters=';
                    var parameters = new Object();
                    parameters.tipo_report = "DETERMINE";
                    parameters.tip_intervento = Ext.getCmp('wf_tipi_int_filter').getValue();
                    parameters.eventi = new Array();
                    var selMod = this.up('gridpanel').getSelectionModel();
                    for (var i = 0; i < selMod.selected.items.length; i++) {
                        parameters.eventi.push(selMod.selected.items[i].data.id);
                    }
                    var urlParameters = Ext.encode(parameters);
                    if (selMod.selected.items.length < 1) {
                        Ext.Msg.alert("Attenzione!", "Non è stato selezionato nessun valore");
                    } else {
                        window.open(url + urlParameters, '_blank', '');
                    }
                }
            },
            {
                text: 'Procedi',
                handler: function() {
                    Ext.getCmp('wf_determine_container').setLoading(true);
                    var parameters = {
                        tip_intervento: Ext.getCmp('wf_tipi_int_filter').getValue(),
                        eventi: []
                    };
                    var selMod = this.up('gridpanel').getSelectionModel();
                    for (var i = 0; i < selMod.selected.items.length; i++) {
                        parameters.eventi.push(selMod.selected.items[i].data.id);
                    }

                    parameters = Ext.encode(parameters);
                    wf.determine = {
                        parameters: parameters
                    };

                    var paramsToSend = {
                        data: parameters,
                        action: "PROCEED"
                    };

                    Ext.Ajax.request({
                        url: wf.config.path.determine,
                        params: paramsToSend,
                        success: function(response) {
                            debugger;
                            var data = Ext.JSON.decode(response.responseText);
                            if (data.success) {
                                Ext.getCmp('wf_determine_container').setLoading(false);
                                Ext.Msg.show({
                                    title: 'Successo',
                                    msg: data.message,
                                    buttons: Ext.Msg.OK,
                                    fn: function() {
                                        Ext.getCmp('wf_determine_container').add(Ext.create('wf.view.determine.DetermineResultPanel'));
                                        Ext.getCmp('wf_determine_container').getLayout().next();
                                    }
                                });
                            } else {

                                if (data.message.indexOf('BudgetAssentiException') > 0) {
                                    var riga_1 = "Ad uno o più interventi selezionati non è stato assegnato alcun budget <br >";
                                    var riga_2 = "di conseguenza non verranno visualizzati in Gestione Economica. <br >";
                                    var riga_3 = "Vuoi proseguire ?<br >";
                                    var msg = riga_1 + riga_2 + riga_3;

                                    var config = {
                                        title: "Attenzione !",
                                        buttons: Ext.Msg.YESNO,
                                        msg: msg,
                                        fn: function(buttonId) {

                                            log("buttonId: " + buttonId);

                                            if ('yes' === buttonId) {
                                                resendRequestToServer(paramsToSend);
                                            } else {
                                                Ext.getCmp('wf_determine_container').setLoading(false);
                                                log("devo _solo_ chiudere questo messaggio...");
                                                // non fare nulla
                                            }
                                        }
                                    };

                                    var messageBox = Ext.create('Ext.window.MessageBox', {
                                        buttonText: {
                                            yes: 'Procedi',
                                            no: 'Annulla'
                                        }
                                    });

                                    messageBox.show(config);

                                } else {
                                    Ext.getCmp('wf_determine_container').setLoading(false);
                                    Ext.Msg.show({
                                        title: 'Errore',
                                        msg: data.message,
                                        buttons: Ext.Msg.OK
                                    });
                                }


                            }
                        },
                        failure: function(response) {
                            debugger;
                            Ext.getCmp('wf_determine_container').setLoading(false);
                            var data = new Object();
                            data.message = 'Errore generico, si prega di contattare l\'amministratore';
                            if (response != null && response.responseText != null) {
                                data = Ext.JSON.decode(response.responseText);
                                data.message = data.message.replace(/ [^ ]*Exception: /, " ");
                            }
                            Ext.Msg.show({
                                title: 'Errore',
                                msg: data.message,
                                buttons: Ext.Msg.OK
                            });
                        }
                    });
                }
            },
            {
                text: 'Determina parzialmente e procedi',
                disabled: true,
                id: 'determinaParzialeButton',
                // mostro la finestra di proroga intervento, mostro i campi appropiati.
                handler: function() {
                    var window = Ext.create('wf.view.determine.DetermineProrogheWindow');
                    window.show();


                    var misura = Ext.getCmp('misura').getValue();
                    if (misura == "D") {
                        Ext.getCmp('al').hide();
                    } else {
                        Ext.getCmp('numero_mesi').hide();
                    }
                }
            }
            /* {
                 text:'Genera determina per tutti gli interventi',
                 handler:function(){
                     Ext.getCmp('wf_determine_container').setLoading(true);
                     var parameters = {
                         tip_intervento : Ext.getCmp('wf_tipi_int_filter').getValue(),
                         eventi : []
                     }
                     var selMod = this.up('gridpanel').getSelectionModel();
                     for(var i=0;i<selMod.selected.items.length;i++){
                         parameters.eventi.push(selMod.selected.items[i].data.id);
                     }

                     parameters = Ext.encode(parameters);
                     wf.determine={
                         parameters:parameters
                     };
                     Ext.Ajax.request({
                         url: wf.config.path.determine,
                         params: {
                             data:parameters,
                             action:"PROCEED",
                             proceedAll:"proceedAll"
                         },
                         success: function(response){
                             var data=Ext.JSON.decode(response.responseText);
                             if(data.success){
                                 Ext.getCmp('wf_determine_container').setLoading(false);
                                 Ext.Msg.show({
                                     title:'Successo',
                                     msg: data.message,
                                     buttons: Ext.Msg.OK,
                                     fn: function(){
                                         Ext.getCmp('wf_determine_container').add(Ext.create('wf.view.determine.DetermineResultPanel'));
                                         Ext.getCmp('wf_determine_container').getLayout().next();
                                     }
                                 });
                             }else{
                                 Ext.getCmp('wf_determine_container').setLoading(false);
                                 Ext.Msg.show({
                                     title:'Errore',
                                     msg: data.message,
                                     buttons: Ext.Msg.OK
                                 });
                             }
                         },
                         failure: function(response){
                             Ext.getCmp('wf_determine_container').setLoading(false);
                             var data = new Object();
                             data.message = 'Errore generico, si prega di contattare l\'amministratore';
                             if(response!=null&&response.responseText!=null){
                                 data=Ext.JSON.decode(response.responseText);
                             }
                             Ext.Msg.show({
                                 title:'Errore',
                                 msg: data.message,
                                 buttons: Ext.Msg.OK
                             });
                         }
                     });
                 }
             }*/
        ];

        this.callParent(arguments);
    },

    renderGridCell: function(value, p, record) {
        value = value.toString();
        p.attr = 'ext:qtip="' + value.replace(/\"/g, "'").replace(/\n/g, "<br/>") + '" ext:qtitle="' + this.header + '"';
        return value;
    },

    getStore: function() {
        return this.store;
    },
    showResultMessage: function(form, submit, title, formReset) {

        var response = Ext.JSON.decode(submit.response.responseText);
        var message = response.data.message;
        //        data.message = data.message.replace(/ .*Exception /, " ");
        Ext.Msg.show({
            title: title,
            msg: message,
            buttons: Ext.Msg.OK,
            fn: function() {
                if (formReset) {
                    form.reset();
                }
            }
        });
    }
});