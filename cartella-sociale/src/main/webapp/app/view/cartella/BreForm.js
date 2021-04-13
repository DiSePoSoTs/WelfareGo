Ext.define('wcs.view.cartella.BreForm',{
    extend: 'Ext.form.Panel',
    alias : 'widget.wcs_breform',
    frame: true,
    autoScroll: true,
    layout: 'fit',
    defaults: {
        labelWidth: 100
    },
    
    initComponent: function() {
        this.items = [{
            xtype: 'wcs_breComponent',
            id: 'wcs_brePopupFieldset'
        }];

        this.buttons = [{
            text: 'Conferma',
            disabled: false,
            id: 'wcs_breConfermaButton',
            handler: function(btn){
            	btn.setDisabled(true);
                var form = this.up('wcs_breform').getForm();
                var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                var win = btn.up('window');
                form.submit({
                    url: '/CartellaSociale/StartForm',
                    waitTitle: 'Avvio',
                    waitMsg:'Sto avviando il processo...',
                    params: {
                        codAnag: codAnag,
                        action: 'startprocess'
                    },
                    success: function(form, action){
                        var json = Ext.JSON.decode(action.response.responseText);
                        if (json.success) {
                            if(json.data && json.data.skipDocument){
                                return;
                            }
                            var codDoc = json.idDoc;
                            Ext.MessageBox.show({
                                title: 'Esito operazione',
                                msg: json.message,
                                buttons: Ext.MessageBox.OK,
                                fn: function(btn){
                                	 var interventiFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('interventiFieldset');
                                     var paiInterventiStore = interventiFieldSet.items.get('wcs_paiInterventiList').store;
                                     paiInterventiStore.load();
                                    win.close();
                       //             wcs.utils.wcsUtils.getLoadDavDocumentFunc({
                       //                 url: '/CartellaSociale/DomandaServlet',
                       //                 params: {
                       //                     codDoc: codDoc
                       //                 }
                       //             })();
//                                    var dav = new wcs.utils.wcsUtils();
//                                    dav.getLoadDavDocumentFunc({
//                                        url: '/CartellaSociale/DomandaServlet',
//                                        params: {
//                                            codDoc: codDoc
//                                        }
//                                    })();
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: 'Errore',
                                msg: json.message,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.window.MessageBox.ERROR,
                                fn: function(btn){
                                    win.close();
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
                            fn: function(btn){
                                win.close();
                            }
                        });
                    }
                });
            }
        }, {
            text: 'Annulla',
            handler: function(btn){
                btn.up('window').close();
            }
        }];
        
        this.callParent(arguments);
    }    
});