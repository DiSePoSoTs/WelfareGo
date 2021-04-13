Ext.define('wp.view.parametri.TemplatesForm', {

    extend: 'Ext.form.Panel',
    alias:  'widget.wp_templatesform',
    id: 'wp_templatesform',
    frame: true,
    layout:'column',
    items: [{
        xtype: 'fieldset',
        title: 'Dettaglio',
        columnWidth:0.5,
        defaults: {
            anchor: '95%',
            labelWidth: 100
        },
        layout: 'anchor',
        items :[{
            xtype: 'numberfield',
            fieldLabel: 'Codice*',
            name: 'cod_tmpl',
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: 'Descrizione*',
            name: 'des_tmpl',
            allowBlank: false
        }, {
            xtype: 'filefield',
            name: 'template_file',
            id: 'wp_template_field',
            emptyText: 'Scegli un file template',
            fieldLabel: 'Template',
            buttonText: 'Scegli',
            vtype: 'odt',
            buttonConfig: {
                iconCls: 'upload-icon'
            }
        }]
    },{
        xtype:'panel', //spacer
        width:10
    },{
        xtype: 'fieldset',
        title: 'Test',
        columnWidth:0.5,
        items:[{
            xtype:'textfield',
            name:'test_codPai',
            fieldLabel:'codPai'
        },{
            xtype:'textfield',
            name:'test_codTipint',
            fieldLabel:'codTipint'
        },{
            xtype:'textfield',
            name:'test_cntTipint',
            fieldLabel:'cntTipint'
        },{
            xtype: 'toolbar',
            items:[{
                xtype:'button',
                text:'Esegui Test',
                handler:function(){
                    var values=this.up('form').getValues(),codPai=values.test_codPai,codTemplate=values.cod_tmpl;
                    if(codPai && codTemplate){
                        window.open('/Parametri/TemplateTestServlet?action=templateTest&codTemplate='+codTemplate
                                +'&codPai='+codPai
                                +'&codTipint='+values.test_codTipint
                                +'&cntTipint='+values.test_cntTipint);
                    }
                }
            },{
                xtype:'button',
                text:'Vedi ultimo content.xml generato',
                handler:function(){
                    window.open('/Parametri/TemplateTestServlet?action=getLatestContentXml');
                }
            },{
                xtype:'button',
                text:'Vedi ultimo prat_dox.xml generato',
                handler:function(){
                    window.open('/Parametri/TemplateTestServlet?action=getLatestPraticaDox');
                }
            }]
        }]
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text: 'Salva e carica',
            scope: this,
            handler: function() {
                var formT = Ext.getCmp('wp_templatesform').getForm();
                if (formT.isValid()) {

                    formT.submit({
                        url: '/Parametri/TemplateServlet',
                        params: {
                            action: 'SAVE'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            // me.showResultMessage(form,submit,'Info',true);
                            Ext.getCmp('wp_templatesgrid').getStore().load(); // aggiorno la grid
                        //                            formT.reset();
                        //                            Ext.getCmp('wp_template_field').reset();
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
            id: 'wp_templates_btn_elimina',

            scope: this,
            handler: function(){

                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_templatesgrid').getSelectionModel().getSelection()[0];

                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('des_tmpl') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/TemplateServlet',
                                    waitMsg: 'Eliminazione in corso...',
                                    params: {
                                        codTmpl: selection.get('cod_tmpl'),
                                        action: "DELETE"
                                    },
                                    success: function(response, opts) {

                                        var obj = Ext.decode(response.responseText);
                                        if(obj.success) {
                                            
                                            showSuccessMsg(obj.data.message);
                                            Ext.getCmp('wp_templatesgrid').store.load(); // aggiorno la grid
                                            Ext.getCmp('wp_templatesform').getForm().setActiveRecord(null);

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


    // carico il form direttamente dalla grid
    setActiveRecord: function(record){
        if (record) {
            this.getForm().loadRecord(record);
            wp_templates_toggle_lock(true);
            
        } else {
            this.getForm().reset();
            wp_templates_toggle_lock(false);
        }
    }

});