Ext.define('wcs.view.cartella.Trasferimento', {
    extend: 'Ext.window.Window',
    alias : 'widget.wcs_trasferimento',
    title : 'Trasferimento/Descesso',
    layout: 'fit',
    modal: true,
    width: 500,
    autoShow: true,

    initComponent: function() {
        this.items = [
        {
            xtype: 'form',
            frame: true,
            id: 'wcs_contattoForm',
            items: [{
                xtype:'datefield',
                format: 'd/m/Y',
                allowBlank: false,
                blankText: 'Questo campo è obbligatorio',
                fieldLabel: 'Data chiusura*',
                name: 'datachiusura'
            },
            {
                xtype:'combo',
                queryMode: 'local',
                store: wcs_motivazioneChiusuraStore,
                displayField: 'name',
                allowBlank: false,
                blankText: 'Questo campo è obbligatorio',
                valueField: 'value',
                name : 'motivazione',
                fieldLabel: 'Motivazione chiusura*'
            },
            {
                xtype: 'textarea',
                anchor: '100%',
                allowBlank: false,
                blankText: 'Questo campo è obbligatorio',
                name : 'note',
                fieldLabel: 'Note*'
            }],
            buttons: [{
                itemId: 'wcs_traferimentoClose',
                text: 'Annulla',
                scope: this,
                handler: function(button) {
                    button.up('form').getForm().reset();
                    this.up('window').close();
                }
            },
            {
                id: 'wcs_traferimentoSalva',
                text: 'Salva',
                formBind: true,
                handler: function(button) {
                    var form = button.up('form').getForm();

                    if (form.isValid()) {
                        form.submit({
                            url: '/CartellaSociale/StartForm',
                            waitTitle: 'Salvataggio',
                            waitMsg:'Sto salvando i dati...',
                            params: {
                                codAnag: Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                                action: 'trasferimento'
                            },
                            success: function(form, action){
                                var json = Ext.JSON.decode(action.response.responseText);
                                if (json.success) {
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: json.message,
                                        buttons: Ext.MessageBox.OK,
                                        fn: function(){
                                            form.reset();
                                            button.up('window').close();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: json.message,
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.window.MessageBox.ERROR,
                                        fn: function(){
                                            form.reset();
                                            button.up('window').close();
                                        }
                                    });
                                }
                            },
                            failure: function (form, action){
                                var json = Ext.JSON.decode(action.response.responseText);
                                Ext.MessageBox.show({
                                    title: 'Esito operazione',
                                    msg: json.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.window.MessageBox.ERROR,
                                    fn: function(){
                                        form.reset();
                                        button.up('window').close();
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                    }
                }
            }]
        }];

        this.callParent(arguments);
    }
});