Ext.define('wcs.view.pai.calcoloImportoEco',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_calcoloimportoeco',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    autoScroll: true,
    layout:'anchor',            
    defaults:{                
        anchor:'100%'
    },
    initComponent: function() {
        var calcoloImportoPanel = this;
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
            items:[{
                xtype:'container',
                layout:'anchor',
                defaultType:'textfield',            
                defaults:{                
                    anchor:'95%'
                },                
                               	
                  items:[{
                	xtype: 'wdecimalnumberfield',
                    name: 'importo_reddito',
                    fieldLabel: 'Importo Reddito',
                    tabIndex:1
                                    
                }]
            }]
        },{
            xtype:'container',
                   
           
            layout:'column',       
            defaults:{
                columnWidth:.3
            },
            items: [{
                //                        readOnly: wcs_anagraficaTelefonoDomicilioRO,
            	xtype:'combo',
                fieldLabel: 'Numerosità nucleo familiare',
                name:'nucleo_familiare',
               
                tabIndex:3,
                store:
                    [ '1', '2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20']
                ,
                mode: 'local'
              
            }]
        },
        {
        	xtype: 'wdecimalnumberfield',
            name: 'massimo_erogabile',
            id:'massimo_erogabile',
            fieldLabel: 'Massimo erogabile',
            readOnly : true
      	
        	
        }
        
        ];
    
        var me=this;
        
      
        
      
        
       
       
        function calcolaMassimoErogabile(){
        	var values = calcoloImportoPanel.getForm().getValues();
        	var reddito = Number(values.importo_reddito.replace(',','.'));
        	log("reddito è" + reddito);
        	  	
        	
        	var nucleoFamiliare = Number(values.nucleo_familiare);
        	
        	
        	var tabella= new Array();
        	tabella[0]=0;
        	tabella[1]= 480;
        	tabella[2]= 753.60;
        	tabella[3]= 979.20;
        	tabella[4]= 1180.80;
        	tabella[5]= 1368;
        	tabella[6]= 1536;
        	tabella[7]= 1704;
        	tabella[9]= 2040;
        	tabella[10]=2208.00;
        	tabella[11]=2376.00;
        	tabella[12]=2544.00;
        	tabella[13]=2712.00;
        	tabella[14]=2880.00;
        	tabella[15]=3048.00;
        	tabella[16]=3216.00;
        	tabella[17]=3384.00;
        	tabella[18]=3552.00;
        	tabella[19]=3720.00;
        	tabella[20]=3888.00;
        	
        	
        	var massimoErogabile;
        	var cifra=tabella[nucleoFamiliare];
        	massimoErogabile=cifra - reddito ;
        	if(massimoErogabile < 0){
        		massimoErogabile=0;
        		
        	}
        	calcoloImportoPanel.getForm().findField('massimo_erogabile').setValue(massimoErogabile);
           
        	
        }
        
        function copiaMassimoToUnderForm(){
        	var values = calcoloImportoPanel.getForm().getValues();
        	massimoErogabile = values.massimo_erogabile;
        	Ext.getCmp('wcs_interventoQuantita').setValue(massimoErogabile);
        	this.close();
        }
            
       
    
        this.buttons=[{
            text: 'Calcola ', 
            tabIndex: 19,
            handler:function(){
            	log('premuto il bottone di calcolo');
                 calcolaMassimoErogabile();
            }   
        },
        {
            text: 'Pulisci',
            tabIndex: 20,
            handler:function(){
                me.getForm().reset();
            }
        }];
        
        this.callParent(arguments);
    }
});