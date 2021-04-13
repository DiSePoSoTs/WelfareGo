Ext.define('wcs.view.cartella.ContattoForm',{
    extend: 'Ext.form.Panel',
    alias : 'widget.wcs_contattoform',
    frame: true,
    autoScroll: true,
    layout: 'fit',
    defaults: {
        labelWidth: 100
    },

    initComponent: function() {
        var wcs_contattoPopup=this;

        this.items = [{
            xtype:'combo',
            queryMode: 'local',
            store: wcs_motivazioneChiusuraStore,
            displayField: 'name',
            id: 'wcs_contattoMotivazione',
            valueField: 'value',
            width: 350,
            name : 'motivazioneChiusura',
            fieldLabel: 'Motivazione chiusura'
        },
        {
            xtype: 'textarea',
            id:'wcs_contattoNote',
            name : 'note',
            width: 350,
            maxLength: 1000,
            maxLengthText: "Il campo note può essere lungo al massimo 1000 caratteri",
            fieldLabel: 'Note'
        }];

        this.buttons = [{
            text: 'Salva',
            handler: function(){
                var form = this.up('form').getForm();
                var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                if (form.isValid()) {
                    form.submit({
                        url: '/CartellaSociale/registraContatto',
                        waitTitle: 'Salvataggio',
                        waitMsg:'Sto salvando i dati...',
                        params: {
                            codAnag: codAnag
                        },
                        success: function(form, action){
                            var json = Ext.JSON.decode(action.response.responseText);
                            if (json.success) {
                                Ext.MessageBox.show({
                                    title: 'Esito operazione',
                                    msg: json.data.message,
                                    buttons: Ext.MessageBox.OK,
                                    fn: function(){
                                        wcs_contattoPopup.hide();
                                    }
                                });

                            } else {
                                Ext.MessageBox.show({
                                    title: 'Errore',
                                    msg: json.data.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.window.MessageBox.ERROR,
                                    fn: function(){
                                        wcs_contattoPopup.hide();
                                    }
                                });
                            }
                        },
                        failure: function (form, action){
                            var json = Ext.JSON.decode(action.response.responseText);
                            Ext.MessageBox.show({
                                title: 'Esito operazione',
                                msg: json.data.message,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.window.MessageBox.ERROR,
                                fn: function(){
                                    wcs_contattoPopup.hide();
                                }
                            });
                        }
                    });
                } else {
                    Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                }
            }
        },{
            text: 'Annulla',
            handler: function(btn){
                wcs_contattoPopup.hide();
            }
        }];

        this.callParent(arguments);
    }
});