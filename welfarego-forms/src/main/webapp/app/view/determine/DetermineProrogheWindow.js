Ext.define('wf.view.determine.DetermineProrogheWindow', {
    extend: 'Ext.window.Window',
    height: 300,
    width: 950,
    alias: 'widget.wf_determine_proroghe_window',
    title: 'Determina e proroga',
    layout: 'column',
    modal: true,
    initComponent: function() {
        var impegniStore = Ext.create('wf.store.ImpegniProrogheDetermineStore', {

            autoLoad: false
        });
        //funzione per ottenere la previsione di spesa lato server
        var ricaricaStore = function(field, newValue, oldValue) {

            var mesi = null;
            var al = null;
            if (field != null && field.getId() == 'numero_mesi') {
                mesi = newValue;
            }
            if (field != null && field.getId() == 'al') {
                al = newValue;
            }

            var parameters = {
                eventi: []
            };
            var selMod = Ext.getCmp('pannelloDetermine').getSelectionModel();
            if (selMod.selected.items.length > 0) {
                for (var i = 0; i < selMod.selected.items.length; i++) {
                    parameters.eventi.push(selMod.selected.items[i].data.id);
                }

                parameters = Ext.encode(parameters);
                console.log(parameters);
                impegniStore.proxy.extraParams.parameters = parameters;
                impegniStore.proxy.extraParams.numero_mesi = mesi;
                impegniStore.proxy.extraParams.al = al;
                impegniStore.proxy.extraParams.tipo_intervento = Ext.getCmp('wf_tipi_int_filter').getValue();
                impegniStore.load();
            }
        };
        var resendRequestToServer = function(paramsToSend) {

            paramsToSend.escludiVerificaEsistenzaBudget = true;
            log(paramsToSend);
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
                        Ext.getCmp('wf_determine_container').setLoading(false);
                        Ext.Msg.show({
                            title: 'Errore',
                            msg: data.message.replace(/ [^ ]*Exception: /, " "),
                            buttons: Ext.Msg.OK
                        });
                    }
                }, //fine success
                failure: function(response) {


                    debugger;
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
                } //fine failure
            }); // fine Ext.Ajax.request

        }; //fine resendRequestToServer


        this.items = [{

                xtype: 'container',
                layout: 'anchor',
                columnWidth: .5,
                items: [{

                        xtype: 'numberfield',
                        fieldLabel: 'Determina per numero mesi',
                        name: 'numero_mesi',
                        id: 'numero_mesi',
                        listeners: {
                            change: ricaricaStore
                        }
                    }, //chiusura numero mesi
                    {

                        xtype: 'datefield',
                        fieldLabel: 'Determina fino al',
                        name: 'al',
                        editable: false,
                        id: 'al',
                        format: 'd/m/Y',
                        listeners: {
                            change: ricaricaStore
                        }
                    } //chiusura determina fino al
                ]
            }, //chiusura prima container

            {
                xtype: 'gridpanel', // inizio grid Impegni
                title: 'Impegni',
                height: 200,
                columnWidth: 1,
                store: impegniStore,
                id: 'prorogheGrid',
                columns: [{
                    id: 'prorogheAnno',
                    header: "Anno",
                    dataIndex: 'anno',

                    flex: 0.10
                }, {
                    id: 'prorogheCapitolo',
                    header: "Capitolo",
                    dataIndex: 'capitolo',

                    flex: 0.25
                }, {
                    id: 'prorogheImpegno',
                    header: "Impegno",
                    dataIndex: 'impegno',
                    flex: 0.25
                }, {
                    id: 'prorogheAnnoErogazione',
                    header: "Anno erogazione",
                    dataIndex: 'anno_erogazione',

                    flex: 0.10
                }, {
                    header: 'Importo',


                    columns: [{
                        id: 'prorogheNetto',
                        header: "Disponibile al momento",
                        dataIndex: 'imp_disp_netto',
                        flex: 1,
                        width: 200,
                        xtype: 'numbercolumn',
                        format: '0,000.00 €'
                    }, {
                        id: 'prorogheDisp',
                        header: "Disponibile dopo la determina parziale",
                        dataIndex: 'imp_disp_proroghe',
                        width: 200,
                        flex: 1,
                        xtype: 'numbercolumn',
                        format: '0,000.00 €'
                    }]


                }] //chiusura header e colonne
            } //chiusura grid
        ]; //chiusura items ,
        this.buttons = [{

                text: 'OK',
                handler: function() {

                    var parameters = {
                        tip_intervento: Ext.getCmp('wf_tipi_int_filter').getValue(),
                        eventi: []
                    }

                    var selMod = Ext.getCmp('pannelloDetermine').getSelectionModel();
                    for (var i = 0; i < selMod.selected.items.length; i++) {
                        parameters.eventi.push(selMod.selected.items[i].data.id);
                    }

                    parameters = Ext.encode(parameters);
                    wf.determine = {
                        parameters: parameters
                    };

                    var mesi = Ext.getCmp('numero_mesi').getValue();
                    var al = Ext.getCmp('al').getValue();
                    var paramsToSend = {
                        data: parameters,
                        action: "PROCEED",
                        proroghe: "proroghe",
                        al: al,
                        numero_mesi: mesi
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
                                debugger;

                                if (data.message.indexOf('BudgetAssentiException') > 0) {
                                    var riga_1 = "Ad uno o più interventi selezionati non è stato assegnato alcun budget <br >";
                                    var riga_2 = "di conseguenza non verranno visualizzati in Gestione Economica. <br >";
                                    var riga_3 = "Vuoi proseguire ?<br >";
                                    var msg = riga_1 + riga_2;

                                    var config = {
                                        title: "Attenzione !",
                                        buttons: Ext.Msg.YESNO,
                                        msg: msg,
                                        fn: function(buttonId) {
                                            log(buttonId);
                                            if ('yes' === buttonId) {
                                                resendRequestToServer(paramsToSend);
                                            } else {
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
                                        msg: data.message.replace(/ [^ ]*Exception: /, " "),
                                        buttons: Ext.Msg.OK
                                    });
                                }
                            }
                        }, //fine success
                        failure: function(response) {


                            debugger;
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
                        } //fine failure
                    }); // fine Ext.Ajax.request

                    this.up('.window').close();
                } //fine handler
            }, //fine button
            {

                text: 'Annulla',
                handler: function() {
                    this.up('.window').close();
                }
            },
            {

                text: 'Anteprima lista',
                handler: function() {
                    var url = wf.config.path.base + '/PreviewReport?parameters=';
                    var mesi = Ext.getCmp('numero_mesi').getValue();
                    var al = Ext.getCmp('al').getValue();
                    var parameters = new Object();
                    parameters.al = al;
                    parameters.mesi = mesi;
                    parameters.proroghe = "proroghe";
                    parameters.tipo_report = "DETERMINE";
                    parameters.tip_intervento = Ext.getCmp('wf_tipi_int_filter').getValue();
                    parameters.eventi = new Array();
                    var selMod = Ext.getCmp('pannelloDetermine').getSelectionModel();
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
            }
        ]; //fine buttons

        ricaricaStore(null, null, null);
        this.callParent(arguments);
    } //fine this




});