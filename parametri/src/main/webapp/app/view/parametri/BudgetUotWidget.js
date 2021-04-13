Ext.define('wp.view.parametri.BudgetUotWidget', {
    extend: 'Ext.window.Window',
    alias: 'widget.wp_budgetuotwidget',
    id: 'wp_budgetuotwidget',
    title: 'Budget UOT',
    height: 200,
    width: 350,
    modal: true,
    layout: 'fit',
    closable: false,
    closeAction: 'hide',
    listeners: {
        show: {
            fn: function() {

                // vado a selezionare il valore della grid
                // se c'è lo associo a questo form
                var sel = Ext.getCmp('wp_budgetgriduot').getSelectionModel().selected.first();
                if (sel) {
                    Ext.getCmp('wp_widget_modal_window_form').setActiveRecord(sel);
                    Ext.getCmp('id_param_uot_modal_field').setReadOnly(true);
                } else {
                    Ext.getCmp('wp_widget_modal_window_form').setActiveRecord(null);
                    Ext.getCmp('id_param_uot_modal_field').setReadOnly(false);
                }

            }
        }
    },
    items: {// Let's put an empty grid in just to illustrate fit layout
        xtype: 'form',
        id: 'wp_widget_modal_window_form',
        frame: true,
        layout: 'anchor',
        defaults: {
            anchor: '100%',
            labelWidth: 110
        },
        // carico il form direttamente dalla grid
        setActiveRecord: function(record) {
            if (record) {
                this.getForm().loadRecord(record);
            } else {
                this.getForm().reset();
            }
        },
        items: [{
                fieldLabel: 'UOT*',
                xtype: 'combo',
                displayField: 'name',
                valueField: 'value',
                editable: false,
                selectOnFocus: true,
                allowBlank: false,
                store: wp_anagraficaUOTStore,
                name: 'id_param_uot',
                id: 'id_param_uot_modal_field'
            }, {
                xtype: 'numberfield',
                fieldLabel: 'Disponibilità Euro € *',
                name: 'bdg_disp_eur',
//            maxValue: 99999999,
//            minValue: 0,
                allowBlank: false,
//            ,
                decimalSeparator: ','
            }, {
                xtype: 'numberfield',
                fieldLabel: 'Disponibilità Ore *',
                name: 'bdg_disp_ore',
                maxValue: 99999999,
                minValue: 0,
                allowBlank: false,
                decimalSeparator: ','
            }],
        dockedItems: [{
                xtype: 'toolbar',
                dock: 'bottom',
                width: 60,
                items: [{
                        text: 'Salva',
                        scope: this,
                        handler: function() {

                            // prendo record dalla grid
                            var record = Ext.getCmp('wp_budgetgrid').getSelectionModel().getSelection();
                            // prendo il form dentro al widget
                            form = Ext.getCmp('wp_widget_modal_window_form').getForm();

                            // il limite è dato da: 
//                    // budget base - la somma dei budget uot già presenti - il budget uot di questo form
//                    var minore = parseInt(record[0].get('bdg_disp_eur')) - max_uot_budget - parseInt(form.findField('bdg_disp_eur').getValue());
//
//                    // ho inserito un budget troppo alto
//                    if(minore > 0) // check rotto, disabilitato . . 
//                    {

                            if (form.isValid()) {

                                form.submit({
                                    url: '/Parametri/BudgetServlet',
                                    params: {
                                        action: 'INSERTUOT',
                                        codTipint: record[0].get('cod_tipint'),
                                        codImpe: record[0].get('cod_impe'),
                                        codAnno: record[0].get('cod_anno')
                                    },
                                    waitMsg: 'Aggiornamento in corso...',
                                    success: function(form, submit) {

                                        Ext.getCmp('wp_widget_modal_window_form').setActiveRecord(null);
                                        Ext.getCmp('wp_budgetuotwidget').close();

                                        // ricaro lo store della grid
                                        Ext.getCmp('wp_budgetgriduot').getStore().load({
                                            params: {
                                                codTipint: record[0].get('cod_tipint'),
                                                codAnno: record[0].get('cod_anno'),
                                                codImpe: record[0].get('cod_impe')
                                            }
                                        }); // aggiorno la grid

                                    },
                                    failure: function(form, submit) {
                                        // nulla per ora
                                    }
                                });


                            } else {
                                Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                            }

//                    } else {
//                        Ext.MessageBox.alert('Errore', 'Il budget inserito supera quello previsto.');
//                    }

                        }
                    }, {
                        text: 'Chiudi',
                        scope: this,
                        handler: function() {
                            Ext.getCmp('wp_widget_modal_window_form').setActiveRecord(null);
                            Ext.getCmp('wp_budgetuotwidget').close();
                        }
                    }]
            }]

    }
});