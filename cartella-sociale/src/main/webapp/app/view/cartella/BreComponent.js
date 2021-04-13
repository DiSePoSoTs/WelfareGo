Ext.define('wcs.view.cartella.BreComponent', {
    extend: 'Ext.form.FieldSet',
    alias : 'widget.wcs_breComponent',
    initComponent : function() {
        this.fieldDefaults= {
            title: 'Richiesta di approvazione'
           // width: '100%'
        };
        this.items =[{
            xtype: 'container',
            layout: 'column',
            items:[{
                xtype: 'textarea',
                id: 'wcs_breEsito',
              //  fieldLabel: 'Esito verifica automatica',
                name: 'esito_ver',
                value: 'Se si desidera produrre la domanda per i nuovi interventi bisogna farlo PRIMA di richiedere approvazione.',
                readOnly: true,
               // columnWidth:.9
              //  autoWidht:true,
                width: '100%',
              	columnWidth: .9

              
            }/*{
                xtype: 'button',
                text:'Ripeti verifica',
                handler:function(){
                    Ext.getCmp('wcs_breEsito').setValue('Caricamento...').setFieldStyle('{color:black;}');
                    Ext.getCmp('wcs_breEsitoGrid').getStore().load();
                }
            }*/]
        }

      /*  {
            xtype: 'gridpanel',         // inizio grid Messaggi verifica automatica
            title: 'Messaggi verifica automatica',
            height: 150,
            store: Ext.create('Ext.data.Store',{
                model: 'wcs.model.MessaggioBreModel',
                pageSize: 20,
                autoLoad: true,
                autoSync: true,
                proxy: {
                    type: 'ajax',
                    url: '/CartellaSociale/StartForm',
                    extraParams: {
                        codAnag:  Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                        action: 'approvazione'
                    }
                },
                listeners:{
                    load:function(store){
                        var result="OK",style="color:green";
                        var confermaButton = Ext.getCmp('wcs_breConfermaButton');
                        if(store.getTotalCount()==0){
                            result="La verifica automatica non ha prodotto risultati";
                            style="color:black";
                            confermaButton.setDisabled(false);
                        }else if(store.find("level","WARN")!=-1){
                            result="WARN";
                            style="color:orange";
                            confermaButton.setDisabled(false);
                        }else if(store.find("level","ERROR")!=-1){
                            result="ERROR";
                            style="color:red";
                            confermaButton.setDisabled(false);
                        } else {
                            confermaButton.setDisabled(false);
                        }
                        Ext.getCmp('wcs_breEsito').setValue(result).setFieldStyle(style);
                    },
                    render: function(){
                        var confermaButton = Ext.getCmp('wcs_breConfermaButton');
                        confermaButton.setDisabled(false);
                    }
                }
            }),
            stateful: true,
            id: 'wcs_breEsitoGrid',
            defaults: {
                sortable:false
            },
            columns:[
            {
                id:'wcs_breLevel',
                header: "Livello",
                dataIndex: 'level'
            },{
                id:'wcs_breMessage',
                header: "Messaggio",
                dataIndex: 'message',
                width:400
            },{
                id:'wcs_breSubject',
                header: "Intervento",
                dataIndex: 'subject'
            }],
            viewConfig: {
                stripeRows: true
            }
        }*/];
        this.callParent(arguments);
    }
});