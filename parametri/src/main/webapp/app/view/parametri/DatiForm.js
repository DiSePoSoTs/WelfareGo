Ext.define('wp.view.parametri.DatiForm',{
   
    id: 'wp_datiform',
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_datiform',
    frame: true,
    
    fieldDefaults:  {
        labelAlign: 'left',
        labelWidth: 170,
        anchor: '90%'
    },
    
    items: [{
        frame: true,
        xtype: 'panel',
        layout: 'column',
        
        items: [{
            xtype: 'container',
            columnWidth: .50,
            layout: 'anchor',
            items: [{            
                xtype: 'textfield',
                name: 'cod_campo',
                id: 'wp_datiCodCampo',
                fieldLabel: 'Codice*',
                allowBlank: false
            }, {
                xtype: 'textfield',
                name: 'des_campo',
                fieldLabel: 'Descrizione*',
                allowBlank: false
            }, {
                xtype: 'combo',
                name: 'tipo_campo',
                fieldLabel: 'Tipo*',
                store: wp_tipiDatoComboStore,
                allowBlank: false,
                editable: false,
                forceSelection: true,							
                displayField: 'name',
                valueField: 'value',
                allowBlank: false
            }, {
                xtype: 'textfield',
                name: 'val_amm',
                fieldLabel: 'Valori Ammessi',
                allowBlank: true
            }, {
                xtype: 'textfield',
                name: 'val_def',
                fieldLabel: 'Valore Default',
                allowBlank: true
            }, {
                xtype: 'textfield',
                name: 'cod_campo_csr',
                fieldLabel: 'Codice Campo CSR',
                allowBlank: true
            }]
        }, {
            xtype: 'container',
            columnWidth: .50,
            layout: 'anchor',
            items: [{            
                xtype: 'checkbox',
                name: 'flg_obb',
                fieldLabel: 'Obbligatorio',
                inputValue: 'S'
            }, {
                xtype: 'checkbox',
                name: 'flg_edit',
                fieldLabel: 'Modificabile',
                inputValue: 'S'
            }, {
                xtype: 'checkbox',
                name: 'flg_vis',
                fieldLabel: 'Visibile',
                inputValue: 'S'
            }, {
                xtype: 'textfield',
                name: 'reg_expr',
                fieldLabel: 'Espressione Validazione',
                allowBlank: true
            }, {
                xtype: 'textfield',
                name: 'msg_errore',
                fieldLabel: 'Msg. Errore',
                allowBlank: true
            }, {
                xtype: 'numberfield',
                name: 'lunghezza',
                fieldLabel: 'lunghezza*',
                allowBlank: false
            }, {
                xtype: 'numberfield',
                name: 'decimali',
                fieldLabel: 'Decimali',
                allowBlank: true
            }]
        }]

    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text: 'Salva',
            scope: this,
            handler: function() {
                form = Ext.getCmp('wp_datiform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/DatiServlet',
                        params: {
                            action: 'SAVE'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            form.reset();
                            Ext.getCmp('wp_datigrid').getStore().load(); // aggiorno la grid
                            showSuccessMsg("Aggiornamento eseguito.");
                        },
                        failure: function(form, submit) {
                            showFailureMsg("Impossibile eseguire l'aggiornamento.");
                        }
                    });
				
                } else {
                    Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                }		
            }
        }, {
            text: 'Elimina',
            disabled: true,
            width: 60,
            id: 'wp_dati_btn_elimina',
            
            scope: this,
            handler: function(){
			
                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_datigrid').getSelectionModel().getSelection()[0];
                
                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('nome') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/DatiServlet',
                                    params: {
                                        codCampo: selection.get('cod_campo'),
                                        action: "DELETE"
                                    },
                                    success: function(response, opts) {
                                        
                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {
                                            
                                            Ext.getCmp('wp_datiform').setActiveRecord(null);
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_datigrid').store.load(); // aggiorno il tree
                                            
                                        } else {
                                            
                                            showFailureMsg(obj.data.message);
                                            
                                        }
                                        
                                    },
                                    failure: function(response, opts) {
                                        
                                        var obj = Ext.decode(response.responseText);
                                        showFailureMsg(obj.data.message);
                                    }

                                });
						
						
                            }
						
                        }
                    });

            }
        }]
    
    }],

    setActiveRecord: function(record){
        this.activeRecord = record;
        if (record) {
            this.getForm().loadRecord(record);
            wp_dati_toggle_lock(true);
            Ext.getCmp('wp_dati_btn_elimina').enable();
            
        } else {
            this.getForm().reset();
            wp_dati_toggle_lock(false);
            Ext.getCmp('wp_dati_btn_elimina').disable();

        }
    }
    
});