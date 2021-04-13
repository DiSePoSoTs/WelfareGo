Ext.define('wp.view.parametri.StruttureForm',{
   
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_struttform',
    id: 'wp_struttform',
    frame: true,
    autoScroll: true,
    layout: 'anchor',
        defaults: {
            xtype: 'numberfield',
            anchor: '100%',
            labelWidth: 200
        },

        items :[{
           
            name: 'id',
            xtype: 'textfield',
            readOnly: true,
            hidden:true
            
            
        },{
            fieldLabel: 'Codice Tipologia Int.*',
            name: 'cod_tipint',
            xtype: 'textfield',
            allowBlank: true,
            readOnly: true
            
        }, {
            fieldLabel: 'Nome struttura*',
            xtype: 'textfield',
            name: 'nome',
            allowBlank: false
        }, {
            fieldLabel: 'Indirizzo struttura*',
            xtype: 'textfield',
            name: 'indirizzo',
            allowBlank: false
        }, {
            fieldLabel: 'Codice CSR*',
            xtype: 'textfield',
            name: 'cod_csr',
            allowBlank: false
        }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        width: 60,
        items: [{
            id: 'wp_strutt_form_salva',
            text: 'Salva',
            disabled: true,
            scope: this,
            handler: function() {
                form = Ext.getCmp('wp_struttform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/StruttureServlet',
                        params: {
                            action: 'INSERTSTRUTTURA'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            
                            Ext.getCmp('wp_struttgrid').getStore().load({
                                params: {
                                    codTipint: form.findField("cod_tipint").getValue()
                                }
                            }); // aggiorno la grid
                            
                            Ext.getCmp('wp_struttform').setActiveRecord(null);
                            var codTipint = Ext.getCmp('wp_struttree').getSelectionModel().selected.first().get('pk');
                            Ext.getCmp('wp_struttform').getForm().findField('cod_tipint').setValue(codTipint);
                          
                            
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