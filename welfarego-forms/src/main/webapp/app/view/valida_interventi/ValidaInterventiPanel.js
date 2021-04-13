Ext.define('wf.view.valida_interventi.ValidaInterventiPanel',{
    extend: 'wf.view.GenericFormPanel',
    alias: 'widget.wa_valida_interventi_panel',
    title: 'Valida interventi',
    initComponent: function() {
        var interventiStore,me=this;
        this.items = [{
            xtype:'hidden',
            name:'interventi_selezionati'
        },{
            xtype: 'container',
            layout: 'column',
            items: [{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                defaults: {
                    xtype: 'textfield',
                    readOnly: true
                },
                items: [{
                    fieldLabel: 'Nome utente',
                    id: 'wa_valida_interventi_nome_utente',
                    name: 'nome_utente_valid'                    
                },{
                    xtype: 'datefield',
                    fieldLabel: 'Data apertura PAI',
                    id: 'wa_valida_interventi_data_apert_PAI',
                    name: 'data_apert'
                }]
            },{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                defaults: {
                    xtype: 'textfield',
                    readOnly: true
                },
                items: [{
                    fieldLabel: 'Cognome utente',
                    id: 'wa_valida_interventi_cognome_utente',
                    name: 'cognome_utente'
                },{
                    fieldLabel: 'Assistente sociale',
                    id: 'wa_valida_interventi_assist_soc',
                    name: 'assist_soc_valid'
                }]
            }]
        },{
            xtype: 'gridpanel',
            title: 'Interventi',
            height: 250,
            store: interventiStore=Ext.create('wf.store.InterventiStore'),
            //stateful: true,
            id: 'wa_valida_interventi_grid_interventi',
            defaults: {
                sortable:true//,
            //width:150
            },
            selType: 'checkboxmodel',
            selModel:{
                mode: 'MULTI'
            },
            listeners:{
                selectionchange:function(selModel,data){
                    var interventiSelezionati=Ext.Array.map(data,function(record){
                        return record.data.pai_intervento_pk;
                    });
                    interventiSelezionati=Ext.JSON.encode(interventiSelezionati);
                    me.getForm().setValues({
                        'interventi_selezionati':interventiSelezionati
                    });
                },
                itemclick:function(view,record,item,index,event){
                    var message=record.get('motivazione');
                    if(message && event.target.parentElement.className.match(/wa_valida_interventi_motivazione/)){
                        Ext.Msg.alert('Motivazione',message);
                    }
                }
            },
            columns:
            [{
                id:'wa_valida_interventi_int_int',
                header: "Intervento",
                dataIndex: 'intervento',
                width:300,
                flex:1
            },{
                id:'wa_valida_interventi_motivazione',
                header: "Motivazione",
                dataIndex: 'motivazione',
                width:300,
                flex:1
            },{
                id:'wa_valida_interventi_data_apert',
                header: "Data apertura",
                dataIndex: 'data_apertura',
                flex:0.3
            },{
                id:'wa_valida_interventi_costo_prev',
                header: "Costo previsto",
                dataIndex: 'costo_prev',
                flex:0.3
            },{
                id:'wa_valida_interventi_budget_disp',
                header: "Budget disponibile",
                dataIndex: 'budget_disp',
                flex:0.3
            }],
            viewConfig: {
                stripeRows: true
            },
            bbar: Ext.create('Ext.PagingToolbar', {
                store: interventiStore,
                displayInfo: true,
                items:['-','<i>( selezionare gli interventi che si desidera approvare )</i>']
            })
        },
        Ext.create('wf.view.BreComponent')
        ,{
            xtype: 'container',
            layout:'hbox',
            id: 'wa_dav_doc_container',
            layoutConfig: {
                pack:'center',
                align:'middle'
            },
            items: [Ext.create('wf.view.DocumentButton',{
                text: 'Visualizza domanda',
                id: 'wa_valida_interventi_button_visual_dom'
            })]
        },{
            xtype: 'container',
            layout: 'column',
            items: [{
                xtype: 'radiogroup',
                labelWidth:50,
                labelAlign:'top',
                fieldLabel: 'Esito',
                id: 'wa_valida_interventi_valid_int_app',
                name: 'wa_valid_int_app',
                hideEmptyLabel:false,
                columns: 1,
                columnWidth: .25,
                items: [{
                    boxLabel: 'Conferma<br/><i>(approva gli interventi selezionati,<br/>respingi gli interventi non selezionati)</i>',
                    name: 'wa_valid_int_app',
                    inputValue: 'conferma',
                    checked: true
                },{
                    boxLabel: 'Rimanda',
                    name: 'wa_valid_int_app',
                    inputValue: 'rimanda'
                }]
            },{
                xtype: 'textarea',
                labelWidth:50,
                labelAlign:'top',
                id: 'wa_valida_interventi_note',
                anchor:'99%',
                //                height: 90,
                fieldLabel: 'Note',
                name: 'note',
                columnWidth: .75
            }]
        }];
        this.buttons = [Ext.create('wf.ux.VirtualKeyboardButton'),this.submitButton];
        this.callParent(arguments);		
        this.load();
    }
});