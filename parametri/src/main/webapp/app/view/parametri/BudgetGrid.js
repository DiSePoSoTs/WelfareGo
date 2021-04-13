Ext.define('wp.view.parametri.BudgetGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_budgetgrid',
    id: 'wp_budgetgrid',
    frame: false,
    store: 'BudgetStore',
    bindForm: 'wp_budgetform',
    title: 'Budget Tipo Intervento',
    columns: [{
            text: 'cod_tipint',
            dataIndex: 'cod_tipint',
            hidden: true
        }, {
            text: 'Anno',
            dataIndex: 'cod_anno',
            flex: 1
        }, {
            text: 'Capitolo',
            dataIndex: 'cod_cap',
            flex: 1
        }, {
            text: 'Impegno',
            dataIndex: 'cod_impe',
            flex: 1
        }, {
            text: 'Conto',
            dataIndex: 'cod_conto',
            flex: 1
        }, {
            text: 'Disp. Euro',
            dataIndex: 'bdg_disp_eur',
            flex: 1,
//        renderer: Ext.util.Format.euCurrency
            xtype: 'numbercolumn',
            format: '0,000.00 €'
        }, {
            text: 'Disp. Ore',
            dataIndex: 'bdg_disp_ore',
            flex: 1,
            xtype: 'numbercolumn',
            format: '0,000'
        }],
    dockedItems: [{
            xtype: 'toolbar',
            dock: 'bottom',
            items: [{
                    id: 'wp_add_budget_btn',
                    disabled: true,
                    text: 'Aggiungi Budget',
                    tooltip: 'Aggiungi un nuovo budget',
                    handler: function() {

                        // ma per poter fare un insert devo poter avere sempre a disposizione
                        // il cod_tipint che lo carico dal form prima di svuotarlo

                        // tampone per codTipint
                        var codTipint = Ext.getCmp('wp_budgettree').getSelectionModel().selected.first().get('pk');
                        // var oldCodTipint = Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').getValue();

                        Ext.getCmp('wp_budgetform').setActiveRecord(null); // form vuoto per insert

                        // Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').setValue( oldCodTipint );
                        Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').setValue(codTipint);


                    }
                }, {
                    id: 'wp_del_budget_btn',
                    disabled: true,
                    text: 'Elimina Budget',
                    tooltip: 'Elimina un budget',
                    handler: function() {

                        Ext.MessageBox.show({
                            title: 'Attenzione',
                            msg: 'Stai per eliminare un dato dal sistema.\nSei sicuro di voler continuare?',
                            buttons: Ext.MessageBox.OKCANCEL,
                            fn: function(btn) {
                                if (btn == 'ok') {

                                    var selected = Ext.getCmp('wp_budgetgrid').getSelectionModel().selected.first();

                                    Ext.Ajax.request({
                                        url: '/Parametri/BudgetServlet',
                                        params: {
                                            action: 'DELETEBUDGET',
                                            codTipint: selected.get('cod_tipint'),
                                            codAnno: selected.get('cod_anno'),
                                            codImpe: selected.get('cod_impe')
                                        },
                                        success: function(response) {
                                            Ext.getCmp('wp_budgetgrid').store.load({
                                                params: {
                                                    codTipint: selected.get('cod_tipint'),
                                                    codAnno: selected.get('cod_anno'),
                                                    codImpe: selected.get('cod_impe')
                                                }
                                            });

                                            // resetto il form e reimposto il valore codTipint dall'albero                                    Ext.getCmp('wp_budgetform').setActiveRecord(null);
                                            var codTipint = Ext.getCmp('wp_budgettree').getSelectionModel().selected.first().get('pk');
                                            Ext.getCmp('wp_budgetform').setActiveRecord(null);
                                            Ext.getCmp('wp_budgetform').getForm().findField('cod_tipint').setValue(codTipint);

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
                Ext.getCmp('wp_budgetform').setActiveRecord(records[0]);

                // imposto la grid uot con i dati corretti
                Ext.getCmp('wp_budgetgriduot').store.load({
                    params: {
                        codTipint: records[0].get('cod_tipint'),
                        codAnno: records[0].get('cod_anno'),
                        codImpe: records[0].get('cod_impe')
                    }
                });

                // abilito il tasto aggiungi uot
                Ext.getCmp('wp_budget_grid_uot_add').enable();
                Ext.getCmp('wp_budget_form_salva').enable();
            }
        }
    }

});