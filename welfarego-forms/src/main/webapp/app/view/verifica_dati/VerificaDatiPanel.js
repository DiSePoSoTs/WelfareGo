Ext.require('wf.view.SubmitVerificaDatiDeterminaButton');
Ext.define('wf.view.verifica_dati.VerificaDatiPanel', {
    extend: 'wf.view.VerificaImpegniGenericPanel',
    alias: 'widget.wa_verifica_dati_panel',
    title: 'Verifica Dati',
    initComponent: function() {

        function setVisibleQuantitaFields(visible) {

            try {
                Ext.getCmp('wf_verifica_impegni_acarico_sum_qta').setVisible(visible);
                Ext.getCmp('wf_verifica_impegni_acarico_rem_qta').setVisible(visible);
                Ext.getCmp('wf_unitaDiMisuraDescColumn').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaColumn').setVisible(visible);
                Ext.getCmp('wf_bdgPrevQtaColumn').setVisible(visible);
                Ext.getCmp('quantitaUot1').setVisible(visible);
                Ext.getCmp('quantitaUot2').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaDispColumn').setVisible(visible);

            } catch (e) {
                // if invoked before the grid is shown, will throw an error
                // simplest thing is, catch the error and ignore it. The function will
                // be invoked successfully later
                log('expected error : ', e);
            }
        }

        this.buttons = [{
            xtype: 'wf_submit_doc_button',
            id: 'wf_gendoc_button'
        }];


        this.callParent(arguments);

        this.add(
        {
            xtype: 'combo',
            displayField: 'name',
            valueField: 'value',
            fieldLabel: 'Motivazione respingimento',
            store: 'combo.CauseRespingimentoStore',
            name: 'cause_respingimento'
        },
         {
            xtype: 'textarea',
            id: 'testo_autorizzazione',
            name: 'testo_autorizzazione',
            fieldLabel: 'testo autorizzazione',
            valueField: 'testo_autorizzazione'
         },
         {
            xtype: 'radiogroup',
            fieldLabel: 'Esito',
            name: 'esito',
            cls: 'x-check-group-alt',
            items: [{
                id: 'wa_verifica_dati_approva',
                boxLabel: 'Approva',
                name: 'esito',
                inputValue: 'approvato'
            }, {
                id: 'wa_verifica_dati_respingi',
                boxLabel: 'Rimanda all \'A.S.',
                name: 'esito',
                inputValue: 'rimandato',
                checked: true
            }, {
                id: 'wa_verifica_dati_rifiuta',
                boxLabel: 'Respingi',
                name: 'esito',
                inputValue: 'respinto'

            }]
        }, {
            xtype: 'radiogroup',
            fieldLabel: 'Tipo determina',
            name: 'tipo_determina',
            cls: 'x-check-group-alt',
            hidden: true, //disabled
            items: [{
                id: 'wf_verifica_dati_singola',
                boxLabel: 'Singola',
                name: 'tipo_determina',
                inputValue: 'singola',
                readOnly: true
            }, {
                id: 'wf_verifica_dati_multipla',
                boxLabel: 'Multipla',
                name: 'tipo_determina',
                inputValue: 'multipla',
                readOnly: true
            }]
        }, {
            xtype: 'hidden',
            name: 'singola'
        }, {
            xtype: 'hidden',
            name: 'multipla'
        });

        this.load(
            {
            success: function(form) {
                var values = form.getValues();
                var field;

                if (values.singola == 'true') {
                    field = Ext.getCmp('wf_verifica_dati_singola');
                    field.setReadOnly(false);
                    field.setValue(true);
                }
                if (values.multipla == 'true') {
                    field = Ext.getCmp('wf_verifica_dati_multipla');
                    field.setReadOnly(false);
                    field.setValue(true);
                }


                setTimeout(function() { /// FIX ORRIBILE
                    var store = Ext.getCmp('wf_verifica_dati_grid_impegni').getStore();
                    if (store.count() > 0) {
                        var value = store.first().get('unitaDiMisuraDesc');
                        setVisibleQuantitaFields((value && value.match('euro')) ? false : true);
                    }
                }, 500);


                var cod_tmpl_documento_di_autorizzazione = Ext.getCmp('cod_tmpl_documento_di_autorizzazione').getValue();
                log(cod_tmpl_documento_di_autorizzazione);

                if( Number(cod_tmpl_documento_di_autorizzazione) <= 0 ){
                    Ext.getCmp('testo_autorizzazione').setVisible(false);
                }

            }
        }
        );

        // da analisi deve essere dificabile, eppure era stato reso readonly . . boh
        //		Ext.getCmp('wf_verifica_dati_a_carico').getEditor().setReadOnly(true);

        //		Ext.getCmp('wf_generic_submit_button').success=function(message,form){
        //			var values=form.getValues(),showDocument=(values['esito']=='approvato')&&(values['tipo_determina']=='singola');
        //			var window;
        //			(window=Ext.create('Ext.window.Window',{
        //				title: 'Info',
        //				height: 100,
        //				width: 400,
        //				layout:'border',
        //				modal:true,
        //				items:[{
        //					xtype:'label',
        //					region:'center',
        //					text:message
        //				},{
        //					xtype:'panel',
        //					layout:'column',
        //					region:'south',
        //					items:[
        //					Ext.create('wf.view.DocumentButton',{
        //						text: 'Apri determina',
        //						columnWidth:.5,
        //						hidden:!showDocument
        //					}),{
        //						xtype:'button',
        //						text:'OK',
        //						columnWidth:.5,
        //						handler:function(){
        //							window.close();
        //						}
        //					}]
        //				}],
        //				listeners:{
        //					destroy:function(){
        //						if(wfg.utils.scrollToTasklist)
        //							wfg.utils.scrollToTasklist();
        //					}
        //				}
        //			})).show();

        //			Ext.create('Ext.window.MessageBox',{
        //				title:'Info',
        //				msg: message,
        //				buttons: [{
        //					xtype:'wf_dav_document_button',
        //					text:'Apri determina'
        //				},{
        //					xtype:'button',
        //					text:'OK',
        //					handler:function(){
        //						this.close();
        //					}
        //				}],
        //				fn:function(){
        //				}
        //			}).show();
        //		};


    }
});