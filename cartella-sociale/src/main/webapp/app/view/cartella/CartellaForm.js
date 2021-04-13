Ext.define('wcs.view.cartella.CartellaForm',{
    extend: 'Ext.Panel',
    alias: 'widget.wcs_cartellaform',
    url: '/CartellaSociale/cartellasociale',
    layout: 'anchor',
    frame: true,
    defaults: {
        anchor: '100%'
    },

    initComponent: function() {
        this.items = [
        {
            xtype: 'wcs_cartellatab',
            listeners: {
                beforetabchange: function(tabPanel, newTab, activeTab){
                    var idTab = newTab.getId();
                    var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                    if (wcs_isModified != ''){
                        if (wcs_isModified == activeTab.getId()){
                            Ext.MessageBox.confirm('Attenzione!', 'Hai apportato delle modifiche alla sezione corrente. Vuoi continuare?', function(btn){
                                if (btn == 'yes'){
                                    wcs_isModified = '';
                                    tabPanel.setActiveTab(newTab);
                                    ricaricaTab(idTab, codAnag);
                                } else {
                                    tabPanel.setActiveTab(activeTab);
                                }
                            });
                        }
                    } else {
                        ricaricaTab(idTab, codAnag);
                    }
                }
            }
        }
    ];

        this.buttons = [
        {
            text: 'Registra contatto',
            disabled: true,
            id: 'wcs_cartellaRegistraContattoButton',
            hidden: wcs_registraContattoButtonHidden,
            handler: function() {

                    var wcs_contattoPopup = Ext.create('Ext.window.Window', {
                        title: 'Registra contatto',
                        width: 500,
                        closable: true,
                        closeAction: 'hide',
                        autoScroll: true,
                        items: [{
                            xtype: 'wcs_contattoform'
                        }]
                    });

                wcs_contattoPopup.show();
            }
        }, {
            text: 'Fissa appuntamento',
            disabled: true,
            id: 'wcs_cartellaFissaAppuntamentoButton',
            hidden: wcs_fissaAppuntamentoButtonHidden,
            handler: function() {
                var codAna = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                var nome = Ext.getCmp('wcs_anagraficaNome').getValue();
                var cognome = Ext.getCmp('wcs_anagraficaCognome').getValue();
                var win = Ext.create('Ext.window.Window', {
                    title: 'Registra appuntamento',
                    id: 'wcs_interventoRegistraAppuntamento',
                    width: 900,
                    modal: true,
                    height: 600,
                    layout: 'fit',
                    items: [{
                        xtype: 'wcs_agenda_container',
                        id: 'wcs_agenda_container'
                    }]
                })
                win.show();
                Ext.getCmp('wcs_agenda_codAna').setValue(codAna);
                var nomeCognome = cognome + " " + nome;
                Ext.getCmp('wcs_agenda_nome_cognome').setValue(nomeCognome);
            }
        }, {
            text: 'Trasferimento/Decesso',
            hidden: wcs_decessoButtonHidden,
            id: 'wcs_cartellaDecessoButton',
            disabled: true,
            handler: function() {
                Ext.MessageBox.confirm('Conferma', 'Attenzione: stai per chiudere gli interventi attivi e il PAI: vuoi proseguire?',
                    function(btn){
                        if (btn == 'yes') {
                            Ext.widget('wcs_trasferimento');
                        }
                    });
            }
        }, {
            text: 'Stampa',
            hidden: wcs_stampaCartellaButtonHidden,
            id: 'wcs_cartellaStampaButton',
            disabled: true,
            handler: function() {
                var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                window.open("/CartellaSociale/Stampa?codAnag="+codAnag+"&action=cartella");
            }
        },{
            text: 'Produci domanda',
            hidden: wcs_richiediApprovazioneButtonHidden,
            id: 'wcs_cartellaProduciDomandaButton',
            disabled: true,
            handler: function(btn) {
            	 var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
            	 var box = Ext.MessageBox.wait('Produzione domanda in corso', 'Produci domanda');
            	  var win = btn.up('window');
            	 Ext.Ajax.request({
            		 url: '/CartellaSociale/StartForm',
                     waitTitle: 'Avvio',
                     waitMsg:'Sto avviando il processo...',
                     
                     params: {
                         codAnag: codAnag,
                         action: 'producidomanda'
                     },
                     success: function(response,opts){
                    	 box.hide();
                         var json = Ext.JSON.decode(response.responseText);
                         if (json.success) {
                             if(json.data && json.data.skipDocument){
                                 return;
                             }

                            var interventiFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('interventiFieldset');
                            var paiDocumentiStore = interventiFieldSet.items.get('wcs_paiDocumentiList').store;

                            paiDocumentiStore.proxy.extraParams = {
                                action: 'read',
                                codPai: json.codPai
                            }
                            paiDocumentiStore.load();

                            var idDocumento = json.idDoc;
                            var downloadUrl = document.location.origin + '/CartellaSociale/downloadDocumento?idDocumento=' + idDocumento;

                            Ext.MessageBox.confirm(
                                '',
                                'Vuoi scaricare il file ?',
                                function(btn) {
                                    if(btn=='yes') {
                                        window.open(downloadUrl, '_blank');
                                    }
                                }
                            );


                          
                         } else {
                        	 
                             Ext.MessageBox.show({
                                 title: 'Errore',
                                 msg: json.message,
                                 buttons: Ext.MessageBox.OK,
                                 icon: Ext.window.MessageBox.ERROR
                                 
                             });
                         }
                     },
                     failure: function (response,opts){
                    	 box.hide();
                         var json = Ext.JSON.decode(response.responseText);
                         Ext.MessageBox.show({
                             title: 'Esito operazione',
                             msg: json.message,
                             buttons: Ext.MessageBox.OK,
                             icon: Ext.window.MessageBox.ERROR
                           
                         });
                     }
            	 });
            	
            }
        }, {
            text: 'Richiedi approvazione',
            hidden: wcs_richiediApprovazioneButtonHidden,
            id: 'wcs_cartellaRichiediApprovazioneButton',
            disabled: true,
            handler: function() {
                Ext.create('Ext.window.Window', {
                    title: 'Richiesta approvazione',
                    width: 700,
                    closable: true,
                    modal: true,
                    closeAction: 'destroy',
                    autoScroll: true,
                    items: [{
                        xtype: 'wcs_breform'
                    }]
                }).show();
                Ext.getCmp('wcs_brePopupFieldset').down('gridpanel').store.load();
            }
        }];

        this.callParent(arguments);
    }
});