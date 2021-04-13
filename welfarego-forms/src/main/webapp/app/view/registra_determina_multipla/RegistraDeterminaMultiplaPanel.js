Ext.define('wf.view.registra_determina_multipla.RegistraDeterminaMultiplaPanel',{
    extend: 'wf.view.GenericFormPanel',
    initComponent: function() {
        var paiStore;
        this.fieldDefaults.labelWidth = 120;
        this.items = [{           
            xtype: 'gridpanel',   
            title: 'Protocollazione Determina',
            height: 200,
            store: paiStore=Ext.create('wf.store.PaiStore'),
            columns:[{
                header: "Nome utente", 
                dataIndex: 'nome_utente',
                width:200
            },{
                header: "Cognome utente", 
                dataIndex: 'cognome_utente',
                width:200
            },{
                header: "Data apertura Pai", 
                dataIndex: 'data_apertura_pai',
                width:200
            },{
                header: "Assistente sociale", 
                dataIndex: 'assistente_sociale',
                width:200
            }]         
        },{
            xtype: 'fieldset',
            title: 'Protocollo generale',
            defaults:{
                readOnly:true
            },
            items: [{
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                items: [{
                    xtype: 'datefield',
                    columnWidth:.5,
                    fieldLabel: 'Data',
                    name: 'data_protocollo',
                    format: 'd/m/Y'
                },{
                    xtype: 'textfield',
                    columnWidth:.5,
                    fieldLabel: 'Numero prot',
                    name: 'numero_protocollo'					  
                }]
            }],
            bbar: Ext.create('Ext.PagingToolbar', {
                store: paiStore,
                displayInfo: true
            })
        },{
            xtype: 'textfield',
            fieldLabel: 'Id Determina',
            name: 'id_determina',
            hidden: true,
            readOnly:true				 
        },{
            xtype: 'textfield',
            fieldLabel: 'Numero determina',
            name: 'numero_determina'
        }];
        this.buttons = [this.submitButton];
        this.callParent(arguments);
		  
        this.load();
    }
});
