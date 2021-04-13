Ext.define('wcs.view.agenda.Detail',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_agenda_detail',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    collapsible: false,
    autoScroll:true,
    height:240,

    fieldDefaults: {
        labelAlign: 'top',
        msgTarget: 'side'
    },
    initComponent: function() {
        var me = this;
        this.store = Ext.create('wcs.store.AgendaStore');
        this.reader = Ext.create('Ext.data.reader.Reader', {
            model: 'wcs.model.DettaglioModel',
            type: 'json',
            rootProperty : 'data',
            successProperty: 'success'
        });
        this.items = [
        {
            xtype:'fieldset',
            title:'Dettaglio',
            items:[
            {
                xtype: 'container',
                anchor: '100%',
                layout:'anchor',
                defaults: {
                    anchor: '100%'
                },

                items:[{
                    xtype: 'hiddenfield',
                    name: 'codAna',
                    id: 'wcs_agenda_codAna'
                },
                {
                    xtype: 'container',
                    layout: 'column',
                    items: [
                    {
                        xtype:'textfield',
                        columnWidth:.50,
                        fieldLabel: 'Cognome e Nome',
                        cls: 'wcs_agenda_nome',
                        id: 'wcs_agenda_nome_cognome',
                        name: 'nome',
                        disabled:true
                    },{
                        xtype:'datefield',
                        columnWidth:.20,
                        format: 'd/m/Y',
                        fieldLabel: 'Data',
                        editable:false,
                        //                        minValue: new Date(),
                        name: 'dataAppuntamento',
                        blankText: 'Questo campo è obbligatorio',
                        allowBlank: false
                    }, {
                        xtype: 'timefield',
                        name: 'dalleOre',
                        format: 'H:i',
                        fieldLabel: 'Dalle ore',
                        minValue: '7:30 AM',
                        maxValue: '19:00 PM',
                        blankText: 'Questo campo è obbligatorio',
                        increment: 30,
                        allowBlank: false
                    }, {
                        xtype: 'timefield',
                        name: 'alleOre',
                        format: 'H:i',
                        fieldLabel: 'Alle ore',
                        minValue: '8:00 AM',
                        maxValue: '19:30 PM',
                        increment: 30,
                        blankText: 'Questo campo è obbligatorio',
                        allowBlank: false
                    }]
                },
                {
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: 'value',
                    forceSelection: true,
                    tabIndex: 4,
                    store: new Ext.data.Store({
                        model: 'wcs.model.AssistenteSocialeModel',
                        autoLoad:true,
                        listeners: {
                            load: {
                                fn: function(a, b, c, d) {
                                    var assSocValue = Ext.getCmp('wcs_anagraficaAssistenteSocialeValue').getValue();
                                   
                                    if (assSocValue != null && assSocValue != '') {
                                        var assSocCombo = Ext.getCmp('wcs_appuntamentoAssistenteSociale');
                                        assSocCombo.setValue(assSocValue);
                                       
                                    }
                                    var dvCmp = Ext.getCmp('wcs_agenda_data_view');
                                    var store = dvCmp.getStore();
                                    var assSocCombo = Ext.getCmp('wcs_appuntamentoAssistenteSociale');
                                    store.proxy.extraParams['codAs'] =assSocCombo.getValue();
                                    store.load();
                                }
                            }
                           
                            
                        }
                    }),
                    listeners: {
                    	select: function(a, b, c, d) {
                            
                            var dvCmp = Ext.getCmp('wcs_agenda_data_view');
                            var store = dvCmp.getStore();
                            var assSocCombo = Ext.getCmp('wcs_appuntamentoAssistenteSociale');
                            store.proxy.extraParams['codAs'] =assSocCombo.getValue();
                            store.load();
                        
                    }
                    },
                    readOnly: wcs_anagraficaAssistenteSocialeRO,
                    id: 'wcs_appuntamentoAssistenteSociale',
                    fieldLabel: 'Assistente sociale',
                    name: 'appuntamentoAssistenteSociale',
                    anchor: '95%'
                },
                {
                    xtype:'textarea',
                    fieldLabel: 'Note',
                    cls: 'wcs_agenda_note',
                    id: 'wcs_agenda_note',
                    name: 'note'
                },
                {
                    xtype:'hiddenfield',
                    name:'tipo'
                },
                {
                    xtype:'hiddenfield',
                    name:'as',
                    value: wcs_codOperatore
                },
                {
                    xtype:'hiddenfield',
                    name:'ts'
                },
                {
                    xtype:'hiddenfield',
                    name:'tipoHidden'
                }]
            }]
        }];
        this.buttons=[{
            xtype: 'button',
            text: 'Pulisci',
            handler: function(){
                var codAna = Ext.getCmp('wcs_agenda_codAna').getValue();
                var nomeCognome = Ext.getCmp('wcs_agenda_nome_cognome').getValue();
                var codAs = Ext.getCmp('wcs_appuntamentoAssistenteSociale').getValue();
                this.up('form').getForm().reset();
                Ext.getCmp('wcs_agenda_nome_cognome').setValue(nomeCognome);
                Ext.getCmp('wcs_agenda_codAna').setValue(codAna);
                Ext.getCmp('wcs_appuntamentoAssistenteSociale').setValue(codAs);
            }
        },{
            xtype:'button',
            text:'Salva',
            handler: function(){
                form = this.up('form').getForm();
                var codAna = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                if (form.isValid()) {
                    function submitForm(forceInsert){
                        form.submit({
                            url: '/CartellaSociale/agenda',
                            params: {
                                action: 'insert',
                                codAna: codAna,
                                forceInsert:forceInsert
                            },
                            waitMsg: 'Aggiornamento in corso...',
                            success: function(form, submit) {
                                me.showResultMessage(form,submit,'Info',true);
                            },
                            failure: function(form, submit) {
                                if(submit.response.responseText.match('errore_sovrapposizione_intervallo')){
                                    Ext.Msg.show({
                                        title:'Attenzione',
                                        msg: 'E\' già presente un appuntamento nell\'intervallo scelto, si desidera procedere comunque con l\'inserimento del nuovo appuntamento?',
                                        buttons: Ext.Msg.YESNOCANCEL,
                                        fn: function(buttonId){
                                            if(buttonId=='yes'){
                                                submitForm(true);
                                            }
                                        }
                                    });            
                                }else{
                                    me.showResultMessage(form,submit,'Errore',true);
                                }
                            }
                        });
                    }
                    submitForm();
                } else {
                    Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                }
            }
        },{
            xtype: 'button',
            text: 'Chiudi',
            handler: function(btn){
                var codAna = Ext.getCmp('wcs_agenda_codAna').getValue();
                var nomeCognome = Ext.getCmp('wcs_agenda_nome_cognome').getValue();
                btn.up('form').getForm().reset();
                Ext.getCmp('wcs_agenda_nome_cognome').setValue(nomeCognome);
                Ext.getCmp('wcs_agenda_codAna').setValue(codAna);
                btn.up('window').close();
            }
        }];
    //   var campo =  Ext.getCmp('wcs_appuntamentoAssistenteSociale');
    //   campo.store.load();
      
        this.callParent(arguments);
        
    },

    setActiveRecord: function(idImpegno, tipo){
        this.getForm().load({
            url: '/CartellaSociale/agenda',
            params: {
                action: 'detail',
                idImpegno: idImpegno,
                tipo: tipo
            },
            waitMsg: 'Caricamento...',
            failure: function(form, action) {
                var response = Ext.JSON.decode(submit.response.responseText);
                Ext.Msg.alert("Errore nel caricamento", action.result.errorMessage);
            },
            success: function(form, submit) {
                tipo = form.findField("wcs_agenda_tipo").getGroupValue();
                var note = form.findField("wcs_agenda_note");
                if(tipo==2){
                    note.setDisabled(true);
                }
                if(tipo==3){
                    note.setDisabled(false);
                }

            }
        });
    },
    showResultMessage:function(form,submit,title,formReset){
        var response = Ext.JSON.decode(submit.response.responseText);
        var message = response.message;
        Ext.ns('wf.utils');
        if( wf.utils.reloadAgenda){
            wf.utils.reloadAgenda();
        }
        Ext.Msg.show({
            title:title,
            msg: message,
            buttons: Ext.Msg.OK,
            fn: function(){
                var date = form.findField("dataAppuntamento").getValue();
                if(formReset){
                    var codAna = Ext.getCmp('wcs_agenda_codAna').getValue();
                    var nomeCognome = Ext.getCmp('wcs_agenda_nome_cognome').getValue();
                    var codAs = Ext.getCmp('wcs_appuntamentoAssistenteSociale').getValue();
                    form.reset();
                    Ext.getCmp('wcs_agenda_nome_cognome').setValue(nomeCognome);
                    Ext.getCmp('wcs_agenda_codAna').setValue(codAna);
                    Ext.getCmp('wcs_appuntamentoAssistenteSociale').setValue(codAs);
                }
                if(form.dataViewId!=null){
                    var dvCmp = Ext.getCmp(form.dataViewId);
                    var store = dvCmp.getStore();
                    store.proxy.extraParams['agenda_current_date'] = new Date(date);
                    store.load();
                }
            //me.up('window').close();
            }
        });
    }
});