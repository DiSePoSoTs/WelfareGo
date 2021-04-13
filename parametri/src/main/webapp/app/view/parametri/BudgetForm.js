Ext.define('wp.view.parametri.BudgetForm',{
   
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_budgetform',
    id: 'wp_budgetform',
    frame: true,
    autoScroll: true,
    layout: 'anchor',
        defaults: {
            xtype: 'numberfield',
            anchor: '100%',
            labelWidth: 200
        },

        items :[{
            fieldLabel: 'Codice Tipologia Int.*',
            name: 'cod_tipint',
            xtype: 'textfield',
            allowBlank: true,
            readOnly: true
        },{
            fieldLabel: 'Data determina*',
            name: 'dt_dx',
            xtype: 'datefield',
            format: 'd/m/Y',
            allowBlank: false,
            editable: false
        }, {
            fieldLabel: 'Numero DX*',
            name: 'num_dx',
            allowBlank: false
        }, {
            fieldLabel: 'Conto*',
            name: 'cod_conto',
            allowBlank: false
        }, {
            fieldLabel: 'Sottoconto*',
            name: 'cod_sconto',
            allowBlank: false
        }, {
            fieldLabel: 'Anno*',
            name: 'cod_anno',
            allowBlank: false
        }, {
            fieldLabel: 'Anno erogazione*',
            name: 'annoSpesa',
            allowBlank: false
        },{
            fieldLabel: 'Capitolo*',
            name: 'cod_cap',
            allowBlank: false
        }, {
            xtype:'textfield',
            fieldLabel: 'Impegno*',
            name: 'cod_impe',
            allowBlank: false
        },{
            fieldLabel: 'Disponibilità Euro €',
            name: 'bdg_disp_eur',
            id: 'dbg_disp_euro_field',
//            decimalPrecision :     // the default decimal separator is dot . . and you can have only one (eventually multi-character) decimal separator . . 
//            ,
            decimalSeparator:','            
        },{
            fieldLabel: 'Disponibilità Ore',
            name: 'bdg_disp_ore',
            id: 'dbg_disp_ore_field',
            decimalSeparator:','
        }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        width: 60,
        items: [{
            id: 'wp_budget_form_salva',
            text: 'Salva',
            disabled: true,
            scope: this,
            handler: function() {
                form = Ext.getCmp('wp_budgetform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/BudgetServlet',
                        params: {
                            action: 'INSERTBUDGET'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            
                            Ext.getCmp('wp_budgetgrid').getStore().load({
                                params: {
                                    codTipint: form.findField("cod_tipint").getValue()
                                }
                            }); // aggiorno la grid
                            
                            form.setActiveRecord(null);
                            Ext.getCmp('wp_budgetgriduot').getStore().removeAll();
                            
                        },
                        failure: function(form, submit) {
                            
                        }
                    });
				
                } else {
                    Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                }		
            }
        }]
    }],

    // carico il form direttamente dalla grid
    setActiveRecord: function(record){
        if (record) {
            this.getForm().loadRecord(record);
        } else {
            this.getForm().reset();
        }
    }
    
});