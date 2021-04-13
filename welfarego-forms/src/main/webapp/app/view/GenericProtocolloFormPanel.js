Ext.define('wf.view.GenericProtocolloFormPanel',{
    extend: 'wf.view.GenericFormPanel',
    initComponent: function() {
        this.fieldDefaults.labelWidth = 120;
        this.items = [
        {
            xtype: 'container',
            anchor: '100%',
            layout: 'column',

            items: [{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [{
                    xtype: 'textfield',
                    id: 'wa_protocollo_nome_utente',
                    fieldLabel: 'Nome utente',
                    name: 'nome_utente',
                    readOnly: true
                },{
                    xtype: 'datefield',
                    id: 'wa_protocollo_data_apert_pai',
                    fieldLabel: 'Data apertura PAI',
                    name: 'data_apert_pai',
                    format: 'd/m/Y',
                    readOnly: true
                }]
            },{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [{
                    xtype: 'textfield',
                    id: 'wa_protocollo_cognome_utente',
                    fieldLabel: 'Cognome utente',
                    name: 'cognome_utente',
                    readOnly: true
                },{
                    xtype: 'textfield',
                    id: 'wa_protocollo_assist_soc',
                    fieldLabel: 'Assistente sociale',
                    name: 'assist_soc',
                    readOnly: true
                }]
            }]
        },{
            xtype: 'fieldset',
            title: 'Protocollo generale',
            items: [{
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                items: [{
                    xtype: 'datefield',
                    columnWidth:.5,
                    id: 'wa_protocollo_data_protoc',
                    fieldLabel: 'Data*',
                    name: 'data_protoc',
                    format: 'd/m/Y'
                },{
                    xtype: 'textfield',
                    id: 'wa_protocollo_numero',
                    columnWidth:.5,
                    fieldLabel: 'Numero prot*',
                    name: 'numero'					  
                }]
            }]
        }
        ];
        this.buttons = [this.submitButton];
        this.callParent(arguments);
    }
});
