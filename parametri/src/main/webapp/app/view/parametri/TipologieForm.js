Ext.define('wp.view.parametri.TipologieForm',{
   
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_tipologieform',
    id: 'wp_tipologieform',
    standardSubmit: true,
    frame: true,
    autoScroll: true,
    
    items: [{
        xtype: 'fieldset',
        title: 'Dettaglio',
        defaults: {
            xtype: 'textfield',
            anchor: '100%',
            labelWidth: 200
        },
        layout: 'anchor',
        items :[{
            fieldLabel: 'Codice*',
            name: 'cod_tipint',
            allowBlank: false
        }, {
            fieldLabel: 'Descrizione*',
            name: 'des_tipint',
            allowBlank: false
        }, {
            fieldLabel: 'Intervento PAI*',
            name: 'flg_pai',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        }, {
            fieldLabel: 'Intervento CSR',
            name: 'cod_int_csr',
//            maxlength: 5,
            allowBlank: true
        }, {
            fieldLabel: 'Tipologia Intervento CSR',
            name: 'cod_tipint_csr',
//            maxlength: 5,
            allowBlank: true
        }, {
            fieldLabel: 'Classe tipologia intervento*',
            name: 'id_param_classe_tipint',
            xtype: 'combo',
            store: wp_classeInterventoStore,
            allowBlank: false,
            editable: false,
            forceSelection: true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Servizio competente*',
            name: 'id_param_srv',
            xtype: 'combo',
            store: wp_serviziStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Centro elementare di costo*',
            name: 'ccele',
            allowBlank: false
        }, {
            fieldLabel: "Lista d'attesa",
            name: 'cod_lista_att',
            xtype: 'combo',
            store: wp_listeAttesaStore,
            allowBlank: true,
            editable: false,
            forceSelection: true,
            displayField: 'name',
            valueField: 'value'
        },{
            fieldLabel: 'Modalita\' selezione durata intervento*',
            name: 'flgFineDurata',
            xtype: 'combo',
            store: [['D','Durata Mesi'],['F','Fine Intervento']],
            allowBlank: false,
            forceSelection: true,
            selectOnFocus:true
        }, {
            fieldLabel: 'Prevista fatturazione*',
            name: 'flg_fatt',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_fatturazioneComboStore
        }, {
            fieldLabel: 'Prevista liquidazione*',
            name: 'flg_pagam',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        },
        {
            fieldLabel: 'Rinnovo automatico*',
            name: 'flgRinnovo',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_rinnovoComboStore
        },
        {
            fieldLabel: 'Escludi chiusura automatica*',
            name: 'flgDeveRestareAperto',
            xtype: 'combo',
            store: wp_siNoComboStore,
            displayField: 'name',
            valueField: 'value',
            allowBlank: false,
            forceSelection: true,
            selectOnFocus:true
        },{
            fieldLabel: 'Struttura*',
            name: 'id_param_struttura',
            xtype: 'combo',
            store: wp_strutturaStore,
            allowBlank: true,
            editable: false,
            forceSelection: true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template esecutività singola*',
            name: 'cod_tmpl_ese',
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template esecutività multipla*',
            name: 'cod_tmpl_ese_mul', // @todo sistemare questo campo
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template variazione*',
            name: 'cod_tmpl_var',
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template variazione multipla*',
            name: 'cod_tmpl_var_mul', // @todo sistemare questo campo
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template chiusura anticipata singola*',
            name: 'cod_tmpl_chius',
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template chiusura anticipata multipla*',
            name: 'cod_tmpl_chius_mul', // @todo sistemare questo campo
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Template lettera di comunicazione mandato',
            name: 'cod_tmpl_comliq',
            xtype: 'combo',
            store: wp_templatesStore_mayBeNull,
            allowBlank: true,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        },{
            fieldLabel: 'Template lettera di assegnazione contributo',
            name: 'cod_tmpl_lett_pag',
            xtype: 'combo',
            store: wp_templatesStore_mayBeNull,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Prevede Ricevuta*',
            name: 'flg_ricevuta',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            value:'N',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        },{
            fieldLabel: 'Template ricevuta',
            name: 'cod_tmpl_ricevuta',
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: true,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Prevede documento di autorizzazione',
            name: 'flg_documento_di_autorizzazione',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            value:'N',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        },{
            fieldLabel: 'Template documento di autorizzazione',
            name: 'cod_tmpl_documento_di_autorizzazione',
            xtype: 'combo',
            store: wp_templatesStore,
            allowBlank: true,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Importo standard costo*',
            name: 'imp_std_costo',
            allowBlank: false,
            xtype: 'numberfield',
            decimalSeparator:','
        }, {
            fieldLabel: 'Importo standard entrata*',
            name: 'imp_std_entr',
            allowBlank: false,
            xtype: 'numberfield',
            decimalSeparator:','
        }, {
            fieldLabel: 'Importo spesa standard*',
            name: 'imp_std_spesa',
            allowBlank: false,
            xtype: 'numberfield',
            decimalSeparator:','
        }, {
            fieldLabel: 'Unità di misura*',
            name: 'id_param_uni_mis',
            xtype: 'combo',
            store: wp_unitamisuraStore,
            allowBlank: false,
            editable: false,
            selectOnFocus:true,
            displayField: 'name',
            valueField: 'value'
        }, {
            fieldLabel: 'Visibilità*',
            name: 'flg_vis',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        },
        {
            fieldLabel: 'Richiede approvazione tecnica*',
            name: 'flg_app_tec',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_siNoComboStore
        },
        {
            fieldLabel: 'Responsabile Procedimento (fatture)',
            name: 'responsabileProcedimento',
            allowBlank: true
        },{
            fieldLabel: 'Ufficio di Riferimento (fatture)',
            name: 'ufficioDiRiferimento',
            allowBlank: true
        }, {
            fieldLabel: 'Aliquota Iva (fatture)',
            name: 'aliquotaIva',
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            editable: false,
            selectOnFocus:true,
            allowBlank: false,
            store: wp_aliquotaIvaStore
        }]
    }],


    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        scope: this,
        items: [{
            text: 'Nuovo Intervento no-PAI',
            handler: function()
            {
                Ext.getCmp('wp_tipologieform').setActiveRecord(null);
            }
        },{
            text: 'Salva',
            width: 60,
            scope: this,
            handler: function() {
                form = Ext.getCmp('wp_tipologieform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/TipologieServlet',
                        params: {
                            action: 'INSERTINT'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            Ext.getCmp('wp_tipologietree').store.load(); // aggiorno la grid
                        },
                        failure: function(form, action) {
                            var message=action&&action.result&&action.result.data&&action.result.data.message?action.result.data.message:"errore generico";
                            Ext.Msg.alert('Errore',"Si e' verificato un errore: "+message);
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
            id: 'wp_tipint_btn_elimina',
            
            scope: this,
            handler: function(){
			
                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_tipologietree').getSelectionModel().getSelection()[0];
                
                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('text') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/TipologieServlet',
                                    params: {
                                        codTipint: selection.get('pk'),
                                        action: "DELETEINT"
                                    },
                                    success: function(response, opts) {
                                        
                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {
                                            
                                            Ext.getCmp('wp_tipologieform').setActiveRecord(null);
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_tipologietree').store.load(); // aggiorno il tree
                                            
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
                url: '/Parametri/TipologieServlet',
                params: {
                    action: 'LOADFORM',
                    codTipint: record.get('pk')
                },

                failure: function(form, action) {
                    Ext.Msg.alert("Errore", "<b>Impossibile caricare il dato.</b><br/>" + action.result.message);
                }
            });
            
            Ext.getCmp('wp_tipint_btn_elimina').enable();
            
            wp_tipologie_toggle_lock(true);

        } else {
            
            this.getForm().reset();
            
            Ext.getCmp('wp_tipint_btn_elimina').disable();
            
            wp_tipologie_toggle_lock(false);
            
        }
		
    }

    
});