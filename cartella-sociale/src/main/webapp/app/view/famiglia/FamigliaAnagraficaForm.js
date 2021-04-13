Ext.define('wcs.view.famiglia.FamigliaAnagraficaForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_famigliaanagraficaform',
    title: 'Dettaglio',
    id: 'wcs_famigliaAnagraficaForm',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    collapsible: true,
    autoScroll:true,

    initComponent: function() {
        this.items = [
        {
                xtype: 'hiddenfield',
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
                    queryMode: 'local',
                    displayField: 'name',
                    readOnly: true,
                    valueField: 'value',
                    store: wcs_condizioneStatoCivileStore,
                    fieldLabel: 'Stato civile',
                    name: 'statoCivile',
                    anchor:'97%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    name: 'statoNascitaDes',
                    fieldLabel: 'Stato di nascita',
                    anchor:'95%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Stato di residenza',
                    name: 'statoResidenzaDes',
                    anchor:'95%'
                },
                {
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Via di residenza',
                    name: 'viaResidenzaDes',
                    anchor:'95%'
                },
                {
                    xtype:'datefield',
                    readOnly: true,
                    format: 'd/m/Y',
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
                    name: 'comuneNascitaDes',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Provincia di residenza',
                    name: 'provinciaResidenzaDes',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    readOnly: true,
                    fieldLabel: 'Civico di residenza',
                    name: 'civicoResidenzaDes',
                    anchor:'95%'
                }]
            },
            {
                xtype: 'container',
                columnWidth:.3,
                layout: 'anchor',
                items: [{
                    xtype:'combo',
                    queryMode: 'local',
                    displayField: 'name',
                    readOnly: true,
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
                    xtype:'combo',
                    displayField: 'name',
                    readOnly: true,
                    valueField: 'value',
                    store: wcs_anagraficaCittadinanzaStore,
                    fieldLabel: 'Cittadinanza',
                    name: 'cittadinanza',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'Comune di residenza',
                    readOnly: true,
                    name: 'comuneResidenzaDes',
                    anchor:'95%'
                }]
            }]
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