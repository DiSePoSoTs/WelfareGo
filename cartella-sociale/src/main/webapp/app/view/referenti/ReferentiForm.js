Ext.define('wcs.view.referenti.ReferentiForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_referentiform',
    title: 'Dettaglio',
    id: 'wcs_referentiForm',
    autoScroll:true,
    frame: true,

    initComponent: function(){

        this.items = [
        {
            xtype: 'hiddenfield',
            id: 'wcs_referentiCodAnaFamigliare',
            name: 'codAnaFamigliare'
        },{
            xtype: 'hiddenfield',
            id: 'wcs_referentiCodAnaComunale',
            name: 'codAnaComunale'
        },
        {
            xtype: 'container',
            anchor: '100%',
            layout:'column',
            items:[
            {
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Cognome',
                    name: 'cognome',
                    anchor:'95%'
                },{
                    xtype:'combo',
                    readOnly: true,
                    queryMode: 'local',
                    displayField: 'name',
                    valueField: 'value',
                    store: wcs_condizioneStatoCivileStore,
                    fieldLabel: 'Stato civile',
                    name: 'statoCivile',
                    anchor:'97%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Stato di nascita',
                    name: 'statoNascita',
                    anchor:'95%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Stato di residenza',
                    name: 'statoResidenza',
                    anchor:'95%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Via di residenza',
                    name: 'viaResidenza',
                    anchor:'95%'
                },
                {
                    xtype:'datefield',
                    format: 'd/m/Y',
                    readOnly: true,
                    fieldLabel: 'Data decesso',
                    name: 'dataMorte',
                    anchor:'95%'
                }]
            },
            {
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Nome',
                    name: 'nome',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Codice fiscale',
                    name: 'codiceFiscale'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Comune di nascita',
                    name: 'comuneNascita',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Provincia di residenza',
                    name: 'provinciaResidenza',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Civico di residenza',
                    name: 'civicoResidenza',
                    anchor:'95%'
                }, {
                    xtype:'combo',
                    displayField: 'name',
                    valueField: 'value',
//                    store: wcs_referentiQualificaStore,
                    store: 'referentiQualificaComboStore',
                    readOnly: wcs_referentiQualificaRO,
                    fieldLabel: 'Qualifica*',
                    blankText: 'Questo campo è obbligatorio',
                    allowBlank: false,
                    name: 'qualifica',
                    anchor:'95%'
                }]
            },
            {
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'combo',
                    readOnly: true,
                    queryMode: 'local',
                    displayField: 'name',
                    valueField: 'value',
                    store: wcs_sessoStore,
                    fieldLabel: 'Sesso',
                    name: 'sesso',
                    anchor:'95%'
                },{
                    xtype:'datefield',
                    readOnly: true,
                    format: 'd/m/Y',
                    fieldLabel: 'Data di nascita',
                    name: 'dataNascita',
                    anchor:'95%'
                },{
                    xtype:'combo',
                    displayField: 'name',
                    readOnly:true,
                    valueField: 'value',
                    store: wcs_anagraficaCittadinanzaStore,
                    fieldLabel: 'Cittadinanza',
                    name: 'cittadinanza',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Comune di residenza',
                    name: 'comuneResidenza',
                    anchor:'95%'
                }]
            }]
        }];
        
        this.buttons = [ {
            text: 'Ricerca',
            id: 'wcs_referentiNewButton',
            hidden: wcs_referentiAggiungiReferenteButtonHidden,
            handler: function(){
                this.up('form').getForm().reset();
                Ext.widget('wcs_ReferentiRicercaPopup').show();
            }
        }, {
            text: 'Cancella referente',
            id: 'wcs_referentiDeleteButton',
            hidden: wcs_referentiAggiungiReferenteButtonHidden,
            handler: function(){
                var form = this.up('form').getForm();
                var values = form.getValues();
                Ext.Ajax.request({
                    url: '/CartellaSociale/referenti',
                    params: {
                        codAnaFamigliare: values['codAnaFamigliare'],
                        codAnag: Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                        action: 'delete'
                    },
                    success: function(response){
                        var json = Ext.JSON.decode(response.responseText);
                        if (json.success) {
                            Ext.MessageBox.show({
                                title: 'Esito operazione',
                                msg: json.message,
                                buttons: Ext.MessageBox.OK
                            });
                            Ext.getCmp('wcs_cartellaReferentiGrid').store.load();
                        } else {
                            Ext.MessageBox.show({
                                title: 'Esito operazione',
                                msg: 'Errore in referenti',
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.window.MessageBox.ERROR
                            });
                        }
                    }
                });
            }
        },{
            itemId: 'referentiSave',
            id: 'wcs_referentiSaveButton',
            text: 'Salva',
            hidden: wcs_referentiAggiungiReferenteButtonHidden,
            handler: function() {
                var form = this.up('form').getForm();
                var values = form.getValues();
                var codiceFiscale = values.codiceFiscale;
                if (codiceFiscale != null && "" != codiceFiscale){
                    if (form.isValid()) {
                        Ext.Ajax.request({
                            url: '/CartellaSociale/referenti',
                            params: {
                                codAnaFamigliare: values['codAnaFamigliare'],
                                codAnaComunale: values['codAnaComunale'],
                                qualifica: values['qualifica'],
                                codAnag: Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                                action: 'write'
                            },
                            success: function(response){
                                var json = Ext.JSON.decode(response.responseText);
                                if (json.success) {
                                    Ext.getCmp('wcs_referentiCodAnaFamigliare').setValue(json.codAnaFamigliare);
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: json.message,
                                        buttons: Ext.MessageBox.OK
                                    });
                                    Ext.getCmp('wcs_cartellaReferentiGrid').store.load();
                                } else {
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: json.message,
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.window.MessageBox.ERROR
                                    });
                                }
                            }
                        });
                    } else {
                        Ext.MessageBox.alert('Errore', 'Verifica che tutti i campi obbligatori siano compilati.');
                    }
                } else {
                    Ext.MessageBox.alert('Errore', 'Attenzione! Non è stato selezionato nessun dato. Effettua una ricerca oppure seleziona un nome dall\'elenco');
                }
            }
        }];

        this.callParent();
    },

    setActiveRecord: function(record){
        this.activeRecord = record;
        if (record) {
            this.getForm().loadRecord(record);
        } else {
            this.getForm().reset();
        }
    }

});