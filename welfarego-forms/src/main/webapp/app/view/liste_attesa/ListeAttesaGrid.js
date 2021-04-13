(function() {
    Ext.require('wf.utils.CheckColumn');
    var store;

    Ext.define('wf.view.liste_attesa.ListeAttesaGrid', {
        extend: 'Ext.grid.Panel',
        alias: 'widget.wd_lista_attesa_grid',
        title: 'Liste attesa',
        store: store = Ext.create('wf.store.ListeAttesaStore', {
            autoLoad: false
        }),
        bbar: {
            xtype: 'pagingtoolbar',
            store: store
        },
        //  loadMask: true,
        columnLines: true,
        frame: true,
        initComponent: function() {
            this.columns = [{
                    header: 'Intervento',
                    dataIndex: 'intervento',
                    flex: 1
                }, {
                    header: 'Cognome',
                    dataIndex: 'cognome',
                    flex: 1
                }, {
                    header: 'Nome',
                    dataIndex: 'nome',
                    flex: 1
                }, {
                    header: 'Data richiesta',
                    dataIndex: 'data_richiesta',
                    flex: 1
                }, {
                    header: 'ISEE',
                    dataIndex: 'isee',
                    flex: 1,
                    xtype: 'numbercolumn',
                    format: '0,000.00 €'
                }, {
                    header: 'Stato',
                    dataIndex: 'stato',
                    flex: 1
                }, {
                    header: 'Approvato',
                    dataIndex: 'approvato',
                    xtype: 'checkcolumn',
                    sortable: false,
                    width: 60,
                    listeners: {
                        checkchange: function(checkcolumn, index, value) {
                            if (value) {
                                this.up('gridpanel').getStore().getAt(index).set('respinto', !value);
                            }
                        }
                    }
                }, {
                    header: 'Respinto',
                    dataIndex: 'respinto',
                    xtype: 'checkcolumn',
                    sortable: false,
                    width: 60,
                    listeners: {
                        checkchange: function(checkcolumn, index, value) {
                            if (value) {
                                this.up('gridpanel').getStore().getAt(index).set('approvato', !value);
                            }
                        }
                    }

                }];

            this.buttons = [{
                    text: 'Anteprima in Excel',
                    handler: function() {
                        var url = wf.config.path.base + '/PreviewReport?parameters=';
                        var parameters = new Object();
                        parameters.tipo_report = "LISTE_ATTESA";
                        parameters.tip_intervento = Ext.getCmp('wd_list_attesa_tipi_int_filter').getValue();

                        var urlParameters = Ext.encode(parameters);
                        window.open(url + urlParameters, '_blank', '');
                    }
                }, {
                    text: 'Procedi',
                    handler: function() {
                        Ext.getCmp('wd_liste_attesa_container').setLoading(true);
                        var parameters = {
                            tip_intervento: Ext.getCmp('wd_list_attesa_tipi_int_filter').getValue(),
                            lista_attesa: Ext.getCmp('wd_lista_attesa_filter').getValue()
                        }
                        var store = this.up('gridpanel').getStore();

                        parameters.interventi = [];
                        for (var i = 0; i < store.data.items.length; i++) {
                            var item = store.data.items[i].data;
                            if (!item.approvato && !item.respinto)
                                continue;
                            parameters.interventi.push({
                                cod_pai: item.cod_pai,
                                cod_tip_int: item.cod_tip_int,
                                cnt_tip_int: item.cnt_tip_int,
                                esito: item.approvato ? 'approvato' : 'respinto'
                            });
                        }

                        parameters = Ext.encode(parameters);
                        Ext.Ajax.request({
                            url: wf.config.path.base + '/ListeAttesaServlet',
                            params: {
                                data: parameters,
                                action: "PROCEED"
                            },
                            success: function(response) {
                                var data = Ext.JSON.decode(response.responseText);
                                if (data.success) {
                                    Ext.getCmp('wd_liste_attesa_container').setLoading(false);
                                    Ext.Msg.show({
                                        title: 'Successo',
                                        msg: data.message,
                                        buttons: Ext.Msg.OK,
                                        fn: function() {
                                            store.load();
                                            //Ext.getCmp('wd_determine_container').getLayout().next();
                                        }
                                    });
                                } else {
                                    Ext.getCmp('wd_liste_attesa_container').setLoading(false);
                                    Ext.Msg.show({
                                        title: 'Errore',
                                        msg: data.message,
                                        buttons: Ext.Msg.OK
                                    });
                                }
                            },
                            failure: function(response) {
                                Ext.getCmp('wd_liste_attesa_container').setLoading(false);
                                var data = new Object();
                                data.message = 'Errore generico, si prega di contattare l\'amministratore';
                                if (response != null && response.responseText != null) {
                                    data = Ext.JSON.decode(response.responseText);
                                }
                                Ext.Msg.show({
                                    title: 'Errore',
                                    msg: data.message,
                                    buttons: Ext.Msg.OK
                                });
                            }
                        });
                    }
                }];

            this.callParent(arguments);
        }
    });

})();