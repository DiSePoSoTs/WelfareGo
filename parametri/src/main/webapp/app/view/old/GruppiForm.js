Ext.define('wp.view.parametri.GruppiForm',{
   
    id: 'wp_gruppiform',
    extend: 'Ext.form.Panel',
    alias:  'widget.wp_gruppiform',
    
    bodyPadding: 10,
    bodyBorder: 0,
    border:0,
    frame: true,
	
    fieldDefaults:  {
        labelAlign: 'left',
        labelWidth: 200,
        xtype: 'combo',
        anchor: '100%'
    },	
	
    layout: 'anchor',
	
    items: [{
        xtype: 'fieldset',
        title: 'Dettaglio',
        layout: 'anchor',
			
        items: [{
            xtype: 'textfield',
            fieldLabel: 'Codice',
            name: 'cod_grp_tipint'
        }, {
            xtype: 'textfield',
            fieldLabel: 'Descrizione',
            name: 'des_grp_tipint'
        }, {
            xtype: 'combo',
            fieldLabel: 'Unità di misura*',
            store: wp_unitamisuraStore,
            name: 'id_param_uni_mis',
            allowBlank: false,
            editable: false,
            forceSelection: true,							
            displayField: 'name',
            valueField: 'value'
        },{
            xtype: 'container',
            layout: 'column',
			
            items: [{
                    
                xtype: 'container',
                width: 350,
                layout: 'anchor',
                height: 350,
                items: [{
                    fieldLabel: 'Durata Mesi',
                    name: 'flg_dur_mesi',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Importo mensile',
                    name: 'flg_imp_mes',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                },{
                    fieldLabel: 'Codice Struttura',
                    name: 'flg_cod_strutt',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Ore settimanali',
                    name: 'flg_ore_sett',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Numero pasti',
                    name: 'flg_num_pasti',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Importo',
                    name: 'flg_imp',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Importo Retta Giornaliera',
                    name: 'flg_imp_retta_gior',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {

                    fieldLabel: 'Giorni Presenza Mensili',
                    name: 'flg_gg_pres_mens',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {

                    fieldLabel: 'Importo Contribuzione Mensile',
                    name: 'flg_imp_mens_contr',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {

                    fieldLabel: 'Importo Civilmente Obbligati',
                    name: 'flg_imp_mens_civobb',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {

                    fieldLabel: 'Beneficiario',
                    name: 'flg_cod_ana_benef',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }]
                    
            }, {
                xtype: 'container',
                flex: 1,
                style: 'padding-left: 30px',
                layout: 'anchor',
                height: 350,
				
                items: [{
                    fieldLabel: 'Retta mensile',
                    name: 'flg_retta_mens',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Reddito pensione',
                    name: 'flg_redd_pens',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Assegno Accompagnamento',
                    name: 'flg_ass_acc',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Importo Giornaliero ISEE',
                    name: 'flg_giorn_isee',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Altri Redditi',
                    name: 'flg_altri_redd',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Codice Fiscale',
                    name: 'flg_cod_fisc',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'BINA',
                    name: 'flg_bina',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Quote Esclusione',
                    name: 'flg_quot_escl',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Comunità Assistenza in FVG',
                    name: 'flg_flg_com_ass_fvg',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Comunità madre-bambino in FVG',
                    name: 'flg_flg_com_madr_fvg',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Comunità Abitativa in FVG',
                    name: 'flg_flg_com_ter_fvg',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
                }, {
                    fieldLabel: 'Delegato Alla Riscossione',
                    name: 'flg_cod_ana_deleg',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    selectOnFocus:true,
                    store: wp_siNoComboStore
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
                form = Ext.getCmp('wp_gruppiform').getForm();
                if (form.isValid()) {
                    form.submit({
                        url: '/Parametri/TipologieServlet',
                        params: {
                            action: 'INSERTGRUPPO'
                        },
                        waitMsg: 'Aggiornamento in corso...',
                        success: function(form, submit) {
                            // me.showResultMessage(form,submit,'Info',true);
                            Ext.getCmp('wp_gruppigrid').getStore().load(); // aggiorno la grid
                            refreshRemoteStores(); // aggiorno gli store remoti
                            wp_gruppi_toggle_lock(true);
                            
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
            id: 'wp_gruppi_btn_elimina',
            hidden: true,
            scope: this,
            handler: function(){
			
                // @TODO completare la funzione
                var selection = Ext.getCmp('wp_gruppigrid').getSelectionModel().getSelection()[0];
                
                Ext.MessageBox.confirm(
                    'Conferma eliminazione?', 
                    'Sei proprio sicuro di voler eliminare <b>' + selection.get('des_grp_tipint') + '</b> ?',
                    function(btn) {
                        if(btn=='yes') { 

                            if (selection) {

                                Ext.Ajax.request({
                                    url: '/Parametri/TipologieServlet',
                                    params: {
                                        cod_grp_tipint: selection.get('cod_grp_tipint'),
                                        action: "DELETEGRUPPO"
                                    },
                                    success: function(response){
										
                                        Ext.getCmp('wp_gruppigrid').store.remove(selection);
										
                                        var data=Ext.JSON.decode(response.responseText);
										
                                        if(data.success){
                                            Ext.getCmp('wp_gruppigrid').store.load();
                                        }else{
                                            Ext.Msg.show({
                                                title:'Errore',
                                                msg: data.message,
                                                buttons: Ext.Msg.OK
                                            });
                                        }
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
            // this.down('#wp_gruppi_btn_elimina').enable();
        } else {
            this.getForm().reset();
            // this.down('#wp_gruppi_btn_elimina').disable();
        }
    }
    
});