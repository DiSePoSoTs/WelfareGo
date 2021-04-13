Ext.define('wp.view.parametri.ListeForm',{
   
    id: 'wp_listeform',
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_listeform',
    standardSubmit: true,
    
    bodyPadding: 10,
    bodyBorder: 0,
    border:0,
    frame: true,
	
    fieldDefaults:  {
        labelAlign: 'left',
        labelWidth: 200,
        anchor: '100%',
        xtype: 'checkbox'
    },	
	
    layout: 'anchor',
	
    items: [{
        xtype: 'fieldset',
        title: 'Riepilogo',
        layout: 'anchor',
			
        items: [{
            xtype: 'numberfield',
            fieldLabel: 'Codice*',
            name: 'cod_lista_att',
            allowDecimals: false,
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: 'Descrizione*',
            name: 'des_lista_att',
            allowBlank: false
        },{
            xtype: 'fieldset',
            title: 'Opzioni attivabili',
            layout: 'column',
            items: [{
                    
                xtype: 'container',
                width: 350,
                layout: 'anchor',
                height: 200,
                items: [{
                    xtype: 'checkbox',
                    fieldLabel: 'Tipologia Intervento',
                    name: 'flg_tipint',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Cognome',
                    name: 'flg_cog',
                    inputValue: 'S'
                },{
                    xtype: 'checkbox',
                    fieldLabel: 'Nome',
                    name: 'flg_nom',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Codice Fiscale',
                    name: 'flg_cod_fisc',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'ISEE',
                    name: 'flg_isee',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Data di nascita',
                    name: 'flg_dt_nasc',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Data Domanda',
                    name: 'flg_dt_dom',
                    inputValue: 'S'
                }]
                    
            }, {
                xtype: 'container',
                column: .50,
                layout: 'anchor',
                height: 200,
                items: [{
                    xtype: 'checkbox',
                    fieldLabel: 'BINA',
                    name: 'flg_bina',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Assistente Sociale',
                    name: 'flg_as',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'UOT',
                    name: 'flg_uot',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Distretto Sanitario',
                    name: 'flg_dist_san',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Referente',
                    name: 'flg_ref',
                    inputValue: 'S'
                }, {
                    xtype: 'checkbox',
                    fieldLabel: 'Numero figli',
                    name: 'flg_num_figli',
                    inputValue: 'S'
                }]
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
                form = Ext.getCmp('wp_listeform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/ListeServlet',
                        params: {
                            action: 'SAVE'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            // me.showResultMessage(form,submit,'Info',true);
                            form.reset();
                            Ext.getCmp('wp_listegrid').getStore().load(); // aggiorno la grid
                            wp_liste_toggle_lock(true); // blocco i campi
                        },
                        failure: function(form, submit) {
                        // me.showResultMessage(form,submit,'Errore',true);
                        }
                    });
				
                } else {
                    Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                }		
            }
        },{
            text: 'Elimina',
            disabled: true,
            width: 60,
            id: 'wp_liste_btn_elimina',
            
            scope: this,
            handler: function(){
			
                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_listegrid').getSelectionModel().getSelection()[0];
                
                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('des_lista_att') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/ListeServlet',
                                    params: {
                                        codListaAtt: selection.get('cod_lista_att'),
                                        action: "DELETE"
                                    },
                                    success: function(response, opts) {
                                        
                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {

                                            Ext.getCmp('wp_listeform').setActiveRecord(null);
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_listegrid').store.load(); // aggiorno il tree

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