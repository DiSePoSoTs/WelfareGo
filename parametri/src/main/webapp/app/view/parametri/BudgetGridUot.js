// variabile che mi serve per la widget 
// uot con il calcolo dei limiti del budget
//var max_uot_budget = 0;

Ext.define('wp.view.parametri.BudgetGridUot', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_budgetgriduot',
    id: 'wp_budgetgriduot',
    frame: false,
    store: 'BudgetUotStore',
    title: 'Budget per UOT',
    columns: [{
            dataIndex: 'id_param_uot',
            hidden: true
        }, {
            text: 'UOT',
            dataIndex: 'des_param_uot',
            flex: 1
        }, {
            text: 'Disp. Euro',
            dataIndex: 'bdg_disp_eur',
            flex: 1,
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
                    disabled: true,
                    id: 'wp_budget_grid_uot_add',
                    text: 'Aggiungi UOT',
                    tooltip: 'Aggiungi un nuovo UOT',
                    handler: function() {

                        if (wp_window_widget == null) {
                            wp_window_widget = Ext.widget('wp_budgetuotwidget');
                        }

                        wp_window_widget.show();

                    }
                }, {
                    disabled: true,
                    id: 'wp_budget_grid_uot_del',
                    text: 'Rimuovi UOT',
                    tooltip: 'Rimuovi Budget UOT selezionato',
                    handler: function() {

                        Ext.MessageBox.show({
                            title: 'Attenzione',
                            msg: 'Stai per eliminare un dato dal sistema.\nSei sicuro di voler continuare?',
                            buttons: Ext.MessageBox.OKCANCEL,
                            fn: function(btn) {
                                if (btn == 'ok') {

                                    var selected = Ext.getCmp('wp_budgetgriduot').getSelectionModel().selected.first();

                                    Ext.Ajax.request({
                                        url: '/Parametri/BudgetServlet',
                                        params: {
                                            action: 'DELETEUOT',
                                            codTipint: selected.get('cod_tipint'),
                                            codAnno: selected.get('cod_anno'),
                                            codImpe: selected.get('cod_impe'),
                                            idParamUot: selected.get('id_param_uot')
                                        },
                                        success: function(response) {
                                            Ext.getCmp('wp_budgetgriduot').store.load({
                                                params: {
                                                    codTipint: selected.get('cod_tipint'),
                                                    codAnno: selected.get('cod_anno'),
                                                    codImpe: selected.get('cod_impe')
                                                }
                                            });
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
                // se è un parametro
                Ext.getCmp('wp_budget_grid_uot_del').enable();

                // quando clicco su un OUT mostro il dato nel form

                if (wp_window_widget == null) {
                    wp_window_widget = Ext.widget('wp_budgetuotwidget');
                }
                wp_window_widget.show();


            }
        }
    },
    // quando carico lo store, se NON ci 
    // sono record attivo il tasto elimina dalla grid padre.
    onLoadSuccess: function() {
        // attivo disattivo il bottone di eliminazione
        if (this.store.getTotalCount() < 1)
        {
            Ext.getCmp('wp_del_budget_btn').enable();
        } else {
            Ext.getCmp('wp_del_budget_btn').disable();
        }

//        // calcolo la somma dei budget in questa grid per darla poi in pasto
//        // come limite al widget modale
//        max_uot_budget = 0;
//        for(i=0; i<this.store.getTotalCount(); i++)
//        {
//            max_uot_budget = max_uot_budget + parseInt(this.store.data.getAt(i).get('bdg_disp_eur'));
//        }

    }

});