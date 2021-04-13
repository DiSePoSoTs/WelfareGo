Ext.define('wp.view.parametri.UtentiForm',{
   
    id: 'wp_utentiform',
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_utentiform',
    frame: true,
    
    fieldDefaults:  {
        labelAlign: 'left',
        labelWidth: 100,
        anchor: '90%'
    },
    
    items: [{
        xtype: 'fieldset',
        title: 'Dettaglio',
        layout: 'column',
        
        items: [{
            xtype: 'container',
            columnWidth: .33,
            layout: 'anchor',
            autoHeight: true,
            items: [{            
                xtype: 'hiddenfield',
                id: 'wp_utentiCodUte',
                name: 'cod_ute'
            }, {
                xtype: 'textfield',
                fieldLabel: 'Nome',
                name: 'nome'
            }, {
                xtype: 'combo',
                name: 'id_param_ser',
                fieldLabel: 'Servizio*',
                store: wp_serviziStore,
                editable: false,
                forceSelection: true,							
                displayField: 'name',
                allowBlank: false,
                valueField: 'value'
            }, {
                xtype: 'textfield',
                fieldLabel: 'Telefono',
                name: 'num_tel'
            }, {
                xtype: 'textfield',
                fieldLabel: 'Username*',
                allowBlank: false,
                name: 'username'
                
            },
            {   
                xtype: 'checkbox',
                name: 'problematiche',
                fieldLabel: 'Visualizza dati problematiche',
                inputValue: 'S'
            }]
        }, {
            xtype: 'container',
            columnWidth: .33,
            layout: 'anchor',
            items: [{
                xtype: 'textfield',
                fieldLabel: 'Cognome*',
                allowBlank: false,
                name: 'cognome'
                        
            }, {
                xtype: 'combo',
                displayField: 'name',
                valueField: 'value',
                store: wp_anagraficaUOTStore,
                fieldLabel: 'UOT',
                editable: false,
                forceSelection: true,
                name: 'id_param_uot'
            }, {
                xtype: 'textfield',
                fieldLabel: 'Cellulare',
                name: 'num_cell'
            }, {   
                xtype: 'checkbox',
                name: 'profilo',
                fieldLabel: 'Visualizza dati profilo',
                inputValue: 'S'
            }, {
                xtype: 'combo',
                displayField: 'name',
                valueField: 'value',
                store: wp_poStore,
                fieldLabel: 'P.O.',
                editable: false,
                forceSelection: true,
                name: 'id_param_po'
            }
            ]
        }, {
            xtype: 'container',
            columnWidth: .33,
            layout: 'anchor',
            items: [
            {
                xtype: 'textfield',
                fieldLabel: 'Codice fiscale*',
                allowBlank: false,
                name: 'cod_fisc'
                        
            }, {
                xtype: 'combo',
                displayField: 'name',
                valueField: 'value',
                store: wp_abilitazioniStore,
                fieldLabel: 'Abilitazione*',
                allowBlank: false,
                editable: false,
                forceSelection: true,
                name: 'id_param_lvl_abil'
            }, {
                xtype: 'textfield',
                fieldLabel: 'Email',
                name: 'email',
                vtype: 'email'
            },   {   
                xtype: 'checkbox',
                name: 'motivazione',
                fieldLabel: 'Visualizza motivazione intervento',
                inputValue: 'S'
            }, {
                xtype: 'combo',
                displayField: 'name',
                valueField: 'value',
                store: wp_associazioneStore,
                fieldLabel: 'Ente*',
                allowBlank: false,
                editable: false,
                forceSelection: true,
                name: 'id_associazione'
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
                form = Ext.getCmp('wp_utentiform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/Utenti',
                        params: {
                            action: 'SAVE'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            // me.showResultMessage(form,submit,'Info',true);
                            form.reset();
                            Ext.getCmp('wp_utentigrid').getStore().load(); // aggiorno la grid
                        },
                        failure: function(form, submit) {
                        // me.showResultMessage(form,submit,'Errore',true);
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
            id: 'wp_utenti_btn_elimina',
            
            scope: this,
            handler: function(){
			
                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_utentigrid').getSelectionModel().getSelection()[0];
                
                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('nome') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/Utenti',
                                    params: {
                                        codUte: selection.get('cod_ute'),
                                        action: "DELETE"
                                    },
                                    success: function(response, opts) {
                                        
                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {
                                            
                                            Ext.getCmp('wp_utentiform').setActiveRecord(null);
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_utentigrid').store.load(); // aggiorno il tree
                                            
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
            
        } else {
            this.getForm().reset();
            
        }
    }
    
});