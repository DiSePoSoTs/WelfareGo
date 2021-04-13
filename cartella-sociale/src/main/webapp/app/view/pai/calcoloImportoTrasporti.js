Ext.define('wcs.view.pai.calcoloImportoTrasporti',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_calcoloimporto_trasporti',
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
                   
                defaults:{                
                    anchor:'95%'
                },
                items:[{
                	xtype: 'wdecimalnumberfield',
                    name: 'importo_isee',
                    fieldLabel: 'Importo ISEE',
                    tabIndex:2
                  
              
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
                fieldLabel: 'Tipo trasporto',
                name:'tipo_trasporto',
               
                tabIndex:3,
                store:
                    [ 'Taxi', 'Trasporto attrezzato ']
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
        	var isee =  Number(values.importo_isee.replace(',','.'));
        	var tipoTrasporto = values.tipo_trasporto;
        	var massimoErogabile=0;
        	
        	log("isee è" + isee);
        	log("tipo trasporto è " + tipoTrasporto);
        	if(tipoTrasporto=="Taxi"){
        		switch(true){
        		case (isee < 15000):
        	        massimoErogabile=3500;
        	        break;
        		case (isee > 15000 && isee <= 25000):
        			massimoErogabile=2500;
        			break;
        		case (isee >25000 && isee <=35000):
        			massimoErogabile=1500;
        			break;
        		case (isee>35000):
        		    massimoErogabile=0;
        	     	break;
        		
        		
        		}
        	}
        	
        	else {
        		switch(true){
        		case isee < 15000:
        	        massimoErogabile=8000;
        	        break;
        		case isee > 15000 && isee <= 25000:
        			massimoErogabile=6000;
        			break;
        		case isee >25000 && isee <=35000:
        			massimoErogabile=5000;
        			break;
        		case isee>35000:
        		    massimoErogabile=0;
        	     	break;
        	}
        	
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