Ext.define('wcs.view.famiglia.FamigliaSocialeForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_famigliasocialeform',
    title: 'Dettaglio',
    id: 'wcs_famigliaSocialeForm',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    collapsible: true,
    autoScroll:true,
    url: '/CartellaSociale/famiglia?action=write',
    initComponent: function() {
        this.items = [{
            xtype: 'hiddenfield',
            id: 'wcs_famigliaSocialeCodAnaFamigliare',
            name: 'codAnaFamigliare'
        },{
            xtype: 'hiddenfield',
            id: 'wcs_famigliaSocialeCodAnaComunale',
            name: 'codAnaComunale'
        },{
            xtype: 'container',
            anchor: '100%',
            layout:'column',
            items:[{
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'Cognome',
                    name: 'cognome'
                },{
                    fieldLabel: 'Stato civile',
                    name: 'statoCivile',
                    readOnly: true,
                    xtype:'textfield',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Stato di nascita',
                    name: 'statoNascita',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Stato di residenza',
                    name: 'statoResidenza',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Via di residenza',
                    name: 'viaResidenza',
                    anchor:'95%'
                },{
                    xtype:'datefield',
                    readOnly: true,
                    format: 'd/m/Y',
                    fieldLabel: 'Data decesso',
                    name: 'dataMorte',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                defaults:{                    
                    readOnly: true,
                    anchor:'95%'
                },
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'Nome',
                    name: 'nome',
                    readOnly: true,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Codice fiscale',
                    name: 'codiceFiscale'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Comune di nascita',
                    name: 'comuneNascita'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Provincia di residenza',
                    name: 'provinciaResidenza'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Civico di residenza',
                    name: 'civicoResidenzaDes'
                },{
                    xtype:'weuronumberfield',
                    fieldLabel: 'Entrate ultimi 2 mesi',
                    name:'redditoFamiliare',
                    hideTrigger: true,
                    step:100,
                    readOnly: false
//                    decimalSeparator:',',
//                    decimalPrecision:2
                },{
                    xtype:'datefield',
                    fieldLabel: 'aggiornate al',
                    name:'dataAggiornamentoRedditoFamiliare'
                }]
            },{
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'combo',
                    fieldLabel: 'Relazione',
                    store:'famigliaQualificaParentelaComboStore',
                    displayField: 'name',
                    valueField: 'value',
                    allowBlank: false,
                    name:'codQual',
                    anchor:'95%'
                },{
                    xtype:'combo',
                    queryMode: 'local',
                    readOnly: true,
                    displayField: 'name',
                    valueField: 'value',
                    store: wcs_sessoStore,
                    fieldLabel: 'Sesso',
                    name: 'sesso',
                    anchor:'95%'
                },{
                    xtype:'datefield',
                    format: 'd/m/Y',
                    readOnly: true,
                    fieldLabel: 'Data di nascita',
                    name: 'dataNascita',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Provincia di nascita',
                    name: 'provinciaNascita',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Comune di residenza',
                    name: 'comuneResidenza',
                    anchor:'95%'
                }, {
                    xtype:'combo',
                    readOnly: true,
                    displayField: 'name',
                    valueField: 'value',
                    store: wcs_anagraficaCittadinanzaStore,
                    fieldLabel: 'Cittadinanza',
                    name: 'cittadinanza',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Attività Lavoro/Studio',
                    name: 'attivitaLavoroStudio',
                    anchor:'95%'
                }]
            }]
        }];

        this.buttons = [{
            text: 'Pulisci',
            handler: function() {
                this.up('form').getForm().reset();
                Ext.getCmp('wcs_famigliaSocialeAddButton').setDisabled(false);
                Ext.getCmp('wcs_famigliaSocialeRemoveButton').setDisabled(true);
            }
        },{
            text: 'Ricerca',
            handler: function() {
                this.up('form').getForm().reset();
                Ext.widget('wcs_FamigliaSocialeRicercaPopup').show();
            }
        },{
            text: 'Salva',
            formBind: true,
            id: 'wcs_famigliaSocialeAddButton',
            handler: function() {
                var form = this.up('form').getForm();
                var values = form.getValues();
                var codiceFiscale = values.codiceFiscale;
                if (codiceFiscale != null && "" != codiceFiscale){
                    if (form.isValid()) {
                        Ext.Ajax.request({
                            url: '/CartellaSociale/famiglia',
                            params: {
                                codAnaFamigliare: values['codAnaFamigliare'],
                                codAnaComunale: values['codAnaComunale'],
                                redditoFamiliare: values['redditoFamiliare'],
                                codQual: values['codQual'],
                                attivitaLavoroStudio:values['attivitaLavoroStudio'],
                                codAnag: Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                                action: 'write'
                            },
                            success: function(response){
                                var json = Ext.JSON.decode(response.responseText);
                                if (json.success) {
                                    Ext.getCmp('wcs_famigliaSocialeCodAnaFamigliare').setValue(json.codAna);
                                    Ext.MessageBox.show({
                                        title: 'Esito operazione',
                                        msg: json.message,
                                        buttons: Ext.MessageBox.OK
                                    });
                                    Ext.getCmp('wcs_famigliaSocialeGrid').store.load();
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
        },{
            text: 'Cancella familiare',
            id: 'wcs_famigliaSocialeRemoveButton',
            disabled: true,
            handler: function() {
                var form = this.up('form').getForm();
                var values = form.getValues();
                Ext.Ajax.request({
                    url: '/CartellaSociale/famiglia',
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
                            Ext.getCmp('wcs_famigliaSocialeGrid').store.load();
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
            }
        }];

        this.callParent(arguments);
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