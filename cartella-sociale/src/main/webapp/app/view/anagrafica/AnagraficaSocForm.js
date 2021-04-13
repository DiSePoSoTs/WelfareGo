Ext.require('wcs.view.anagrafica.IndirizzoFieldSet');

Ext.define('wcs.view.anagrafica.AnagraficaSocForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_anagraficasocform',
    id:'anagraficasocform',
    name:'anagraficasocform',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    autoScroll: true,
    layout:'anchor',            
    defaults:{                
        anchor:'100%'
    },
    initComponent: function() {
                
        this.items=[
        //            {
        //            xtype: 'hiddenfield',
        //            name: 'codiceAnagrafica'
        //        },
        {
            xtype:'container',
            layout:'column',            
            defaults:{             
                columnWidth:.5,
                labelWidth: 100
            },
            items:[
            // prima colonna
            {
                xtype:'container',
                layout:'anchor',
                defaultType:'textfield',            
                defaults:{                
                    anchor:'95%'
                },                
                items:[
                    // ragioneSociale
                    {
                        name: 'ragioneSociale',
                        fieldLabel: 'Ragione Sociale*',
                        allowBlank: false,
                        tabIndex:1
                    },
                    // codiceAnagrafica
                    {
                        name: 'codiceAnagrafica',
                        fieldLabel: 'Codice Anagrafica',
                        readOnly:true
                    },
                    //iban
                    {
                        tabIndex: 5,
                        fieldLabel: 'IBAN',
                        //minLength: 27,
                        //minLengthText: 'La lunghezza minima del campo è 27',
                        //maxLength: 27,
                        readOnly: wcs_condizioneIBANRO,
                        name: 'IBAN'
                   }
               ]
            },
            // seconda colonna
            {
                xtype:'container',
                layout:'anchor',
                defaultType:'textfield',            
                defaults:{                
                    anchor:'95%'
                },
                items:[{
                    name: 'codiceFiscale',
                    fieldLabel: 'Codice Fiscale',
                    allowBlank: true    ,
                    tabIndex:2      
                //                    regex:/^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/,
                //                    regexText:'Deve essere un codice fiscale corretto'
                },{
                    name: 'partitaIva',
                    fieldLabel: 'Partita Iva*',
                    allowBlank: false  ,
                    tabIndex:3                        
                }]
            }]
        },{
            xtype:'wcs_anagraficaindirizzofieldset',
            title:'Sede Legale',
            fieldNameSpace:'sedeLegale',
            id:'sedeLegale',
            tabOffset:4
        },{
            xtype:'wcs_anagraficaindirizzofieldset',
            title:'Sede Operativa',
            fieldNameSpace:'sedeOperativa',
            id:'sedeOperativa',
            allowBlank: true,
            tabOffset:10
        },{
            xtype:'fieldset',
            title: 'Contatti',
            collapsible: true,
            //            collapsed: true,
            defaultType: 'textfield',
            layout:'column',       
            defaults:{
                columnWidth:.3
            },
            items: [{
                //                        readOnly: wcs_anagraficaTelefonoDomicilioRO,
                fieldLabel: 'Telefono',
                maxLength: 20,
                tabIndex: 16,
                maxLengthText: 'Lunghezza massima 20 caratteri',
                //                        di: 'wcs_anagraficaTelefono',
                name: 'telefono'
            //                        anchor:'95%'

            }, {
                //                        readOnly: wcs_anagraficaCellulareDomicilioRO,
                fieldLabel: 'Cellulare',
                tabIndex: 17,
                maxLength: 20,
                maxLengthText: 'Lunghezza massima 20 caratteri',
                //                        id: 'wcs_anagraficaCellulare',
                name: 'cellulare',
                margin:'0 15 0 15'
            //                        anchor:'95%'
                    
            }, {
                //                        readOnly: wcs_anagraficaEmailDomicilioRO,
                fieldLabel: 'Email',
                tabIndex: 18,
                maxLength: 200,
                maxLengthText: 'Lunghezza massima 200 caratteri',
                //                        id: 'wcs_anagraficaEmail',
                name: 'email',
                vtype: 'email',
                vtypeText: 'Il campo deve essere un indirizzo email valido'
            //                        anchor:'95%'
            }]
        }];
    
        var me=this;
        
        var reloadFields=function(responseData){
            me.getForm().getFields().each(function(field){
                if( !(field.name=='sedeLegaleCivico') && !(field.name=='sedeOperativaCivico')){
                    var newValue=field.getValue();
                    field.fireEvent('change',field,newValue,'',{});

                } else if (field.name=='sedeLegaleCivico'){
                    var codVia = responseData.sedeLegaleVia;
                    var codCivico = responseData.sedeLegaleCivico;
                    field.setValue(codCivico, codVia);

                } else if (field.name=='sedeOperativaCivico'){
                     var codVia = responseData.sedeOperativaVia;
                     var codCivico = responseData.sedeOperativaCivico;
                     field.setValue(codCivico, codVia);

                } else {
                    log('ERROR, cosa devo fare di lui ?', field);
                }


            });
        };
            
        me.loadAnagrafica=function(codiceAnagrafica){
            log('loading anagrafica for codiceAnagrafica = ',codiceAnagrafica);
            me.getForm().load({
                url:'/CartellaSociale/anagraficaSoc',
                params: {
                    action:'load',
                    codiceAnagrafica:codiceAnagrafica
                },
                success:function(owner, form){
                    var data = form.result.data;
                    reloadFields(data);
                },
                failure:function(form, action){
                    Ext.Msg.alert('Errore', 'si e\' verificato un errore : '+action.result.message);                            
                }
            });            
        }
    
        this.buttons=[{
            text: 'Ricerca', 
            tabIndex: 19,
            handler:function(){
                me.getForm().reset();
                Ext.create('wcs.view.anagrafica.AnagraficaSocRicerca',{
                    anagraficaSocForm:me
                });
            }     
        },{
            text: 'Ricarica',
            tabIndex: 20,
            handler:function(){
                var codiceAnagrafica=me.getForm().getValues().codiceAnagrafica;
                if(codiceAnagrafica&&codiceAnagrafica!=''){
                    me.getForm().reset();
                    me.loadAnagrafica(codiceAnagrafica);
                }                
            }
        },{
            text: 'Pulisci',
            tabIndex: 20,
            handler:function(){
                me.getForm().reset();
            }
        },{
            text: 'Salva',   
            tabIndex: 21,
            handler:function(){
                // Per vedere cosa invierò ala back end:
                // var data=me.getForm().getValues(); ---> Ext.getCmp('wcs_anagraficaSocTab').form.getValues();
                // Ext.ComponentQuery.query('[name=sedeLegaleComune]')[0].getStore().data.items
                // non invierò mai i codici sempre le descrizioni di provincia, comune, via, civico
                if(me.getForm().isValid()){
                    var data=me.getForm().getValues();

                    if(Ext.ComponentQuery.query('[name=sedeOperativaStato]')[0].getStore().data.items[0]){
                        data.sedeOperativaStato = Ext.ComponentQuery.query('[name=sedeOperativaStato]')[0].getStore().data.items[0].data.codStato;
                    }
                    if(Ext.ComponentQuery.query('[name=sedeLegaleStato]')[0].getStore().data.items[0]){
                        data.sedeLegaleStato = Ext.ComponentQuery.query('[name=sedeLegaleStato]')[0].getStore().data.items[0].data.codStato;
                    }



                    log('submitting form data: ',data);

                    me.getForm().submit({
                        url: '/CartellaSociale/anagraficaSoc',
                        params: {
                            data:Ext.JSON.encode(data),
                            action:'save'
                        },
                        success: function(form, action){
                            Ext.Msg.alert('Operazione completata', action.result.message); 
                            form.setValues(action.result.data);
                        },
                        failure:function(form, action){
                            Ext.Msg.alert('Errore', 'si e\' verificato un errore: <br/>'+(action&&action.result&&action.result.message?action.result.message:'errore generico'));                            
                        }
                    });
                }else{                    
                    Ext.Msg.alert('Attenzione','Dati invalidi o mancanti, si prega di controllare'); 
                }
            }         
        }]
        
        this.callParent(arguments);
    }
});
