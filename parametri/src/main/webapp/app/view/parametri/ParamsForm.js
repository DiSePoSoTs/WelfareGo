Ext.define('wp.view.parametri.ParamsForm',{
   
    id: 'wp_paramsform',
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_paramsform',
    store: 'ParamsStore',
    frame: true,
	
    fieldDefaults:  {
        labelAlign: 'left',
        labelWidth: 100,
        anchor: '90%'
    },
    
    items: [{
        xtype: 'fieldset',
        title: 'Parametro',
        defaults: {
            xtype: 'textfield',
            anchor: '100%',
            labelWidth: 150
        },
        layout: 'anchor',
        items :[{
            fieldLabel: 'Id Parametro',
            name: 'id_param',
            hidden: true
        },{
            fieldLabel: 'Tipo Parametro*',
            name: 'tip_param',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_tipologieParametriStore
        }, {
            fieldLabel: 'Codice Parametro*',
            name: 'cod_param',
            maxLength: 10,
            maxLengthText: 'Lunghezza massima 10 caratteri.',
            enforceMaxLength: true,
            allowBlank: false
        }, {
            fieldLabel: 'Attivo*',
            name: 'flg_attivo',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        }]
    }, {
        xtype: 'fieldset',
        title: 'Dettaglio',
        defaults: {
            xtype: 'textfield',
            anchor: '100%',
            labelWidth: 150
        },
        layout: 'anchor',
        items :[{
            fieldLabel: 'codice indata',
            name: 'id_param_indata',
            readOnly: true
        },{
            fieldLabel: 'Descrizione*',
            name: 'des_param',
            allowBlank: false
        },{
            fieldLabel: 'Data',
            name: 'date_param',
            xtype: 'datefield',
            format: 'd/m/Y'
        }, {
            fieldLabel: 'Numero',
            name: 'decimal_param',
            xtye:' numberfield'
        }, {
            fieldLabel: 'Testo 1',
            name: 'txt1_param'
        }, {
            fieldLabel: 'Testo 2',
            name: 'txt2_param'
        }, {
            fieldLabel: 'Testo 3',
            name: 'txt3_param'
        }, {
            fieldLabel: 'Testo 4',
            name: 'txt4_param'
        }]
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        scope: this,
        items: [{
            text: 'Nuovo Parametro',
            handler: function()
            {
                Ext.getCmp('wp_paramsform').setActiveRecord(null);
                wp_parametri_toggle_lock(false); // sblocco i campi
            }
        },{
            text: 'Salva',
            width: 60,
            scope: this,
            handler: function() {
                form = Ext.getCmp('wp_paramsform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/ParametriServlet',
                        params: {
                            action: 'SAVE'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            
//                            Ext.getCmp('wp_paramstree').store.load(); // aggiorno il tree
//                            form.reset();

                        },
                        failure: function(form, submit) {
                            
                            showFailureMsg("Attenzione! Impossibile salvare il dato.");
                            
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
            id: 'wp_params_btn_elimina',
            
            scope: this,
            handler: function(){

                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_paramstree').getSelectionModel().getSelection()[0];

                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('text') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/ParametriServlet',
                                    params: {
                                        idParam: selection.get('pk'),
                                        tipParam: selection.get('tip_param'),
                                        action: "DELETEPARAM"
                                    },
                                    success: function(response, opts) {

                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {

                                            Ext.getCmp('wp_paramsform').setActiveRecord(null);
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_paramstree').store.load(); // aggiorno il tree

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

        if (record) {
			
            this.getForm().reset();

            this.getForm().load({
                url: '/Parametri/ParametriServlet',
                params: {
                    action: 'LOADFORM',
                    idParam: record.get('pk'),
                    tipParam: record.get('tip_param')
                },

                failure: function(form, action) {
                    Ext.Msg.alert("Errore", "<b>Impossibile caricare il dato.</b><br/>" + action.result.message);
                }
            });
            
            Ext.getCmp('wp_params_btn_elimina').enable();

        } else {
            this.getForm().reset();
            Ext.getCmp('wp_params_btn_elimina').disable();
        }
		
    }
    
});