Ext.define('wp.view.parametri.StruttureGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_struttgrid',
    id: 'wp_struttgrid',
    frame: false,
    store: 'StruttureStore',
    bindForm: 'wpstruttform',
    title: 'Strutture tipo intervento',
    columns: [{
            text: 'cod_tipint',
            dataIndex: 'cod_tipint',
            hidden: true
        },
        { text: 'id',
        dataIndex: 'id',
        hidden: true
    },
        {
            text: 'Nome',
            dataIndex: 'nome',
            flex: 1
        },{
            text: 'Indirizzo',
            dataIndex: 'indirizzo',
            flex: 1
        }, {
            text: 'Codice csr',
            dataIndex: 'cod_csr',
            flex: 1
        }],
    dockedItems: [{
            xtype: 'toolbar',
            dock: 'bottom',
            items: [{
                    id: 'wp_add_struttura_btn',
                    disabled: true,
                    text: 'Aggiungi Struttura',
                    tooltip: 'Aggiungi una nuova struttura',
                    handler: function() {

                        // ma per poter fare un insert devo poter avere sempre a disposizione
                        // il cod_tipint che lo carico dal form prima di svuotarlo

                        // tampone per codTipint
                        var codTipint = Ext.getCmp('wp_struttree').getSelectionModel().selected.first().get('pk');
                        // var oldCodTipint = Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').getValue();

                        Ext.getCmp('wp_struttform').setActiveRecord(null); // form vuoto per insert

                        // Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').setValue( oldCodTipint );
                        Ext.getCmp('wp_struttform').getForm().findField('cod_tipint').setValue(codTipint);


                    }
                }, {
                    id: 'wp_del_strutt_btn',
                    disabled: true,
                    text: 'Elimina struttura',
                    tooltip: 'Elimina una struttura',
                    handler: function() {

                        Ext.MessageBox.show({
                            title: 'Attenzione',
                            msg: 'Stai per eliminare un dato dal sistema.\nSei sicuro di voler continuare?',
                            buttons: Ext.MessageBox.OKCANCEL,
                            fn: function(btn) {
                                if (btn == 'ok') {

                                    var selected = Ext.getCmp('wp_struttgrid').getSelectionModel().selected.first();

                                    Ext.Ajax.request({
                                        url: '/Parametri/StruttureServlet',
                                        params: {
                                            action: 'DELETESTRUTTURA',
                                            id: selected.get('id')
                                            
                                        },
                                        success: function(response) {
                                            Ext.getCmp('wp_struttgrid').store.load({
                                                params: {
                                                    codTipint: selected.get('cod_tipint')
                                                  
                                                }
                                            });

                                            // resetto il form e reimposto il valore codTipint dall'albero                                    Ext.getCmp('wp_budgetform').setActiveRecord(null);
                                            var codTipint = Ext.getCmp('wp_struttree').getSelectionModel().selected.first().get('pk');
                                            Ext.getCmp('wp_struttform').setActiveRecord(null);
                                            Ext.getCmp('wp_struttform').getForm().findField('cod_tipint').setValue(codTipint);
                                            

                                        }
                                    });

                                }
                            }
                        });

                    }
                }]
        }],
    listeners: {
        selectionchange: function(model, records) {

            if (records[0]) {
                // imposto il form
                Ext.getCmp('wp_struttform').setActiveRecord(records[0]);
                var codTipint = Ext.getCmp('wp_struttree').getSelectionModel().selected.first().get('pk');
                Ext.getCmp('wp_struttform').getForm().findField('cod_tipint').setValue(codTipint);
                Ext.getCmp('wp_del_strutt_btn').enable(); 
                          
            }
        }
    }

});