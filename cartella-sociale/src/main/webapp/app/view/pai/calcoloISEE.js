Ext.define('wcs.view.pai.calcoloISEE',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_calcoloisee',
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
                items:{
                	xtype:'combo',
                    fieldLabel: 'Scegliere iSEE da ricalcolare',
                    name:'calcolacon',
                    store:[ 'Ordinario', 'Socio-Sanitario','prestazioni agevolate rivolte a minori'],
                   
                    displayField: 'calcolacon',
                    typeAhead: true,
                    mode: 'local',
                    triggerAction: 'all',
                    selectOnFocus:true,
                    boxMaxWidth: 170,
                    tabIndex:1,
                    listeners:{
                        select:function(component,value){
                            if(this.getValue()=="Ordinario"){
                            	calcoloImportoPanel.getForm().findField('importo_isee').show();
                            	//calcoloImportoPanel.getForm().findField('importo_reddito').setValue("");
                            	calcoloImportoPanel.getForm().findField('importo_isee2').hide(); 
                            	calcoloImportoPanel.getForm().findField('importo_isee3').hide(); 
                            	 
                            }
                            else if(this.getValue()=="Socio-Sanitario"){
                            	calcoloImportoPanel.getForm().findField('importo_isee2').show();
                            	calcoloImportoPanel.getForm().findField('importo_isee').hide(); 
                            	calcoloImportoPanel.getForm().findField('importo_isee3').hide(); 
                            	
                            }
                            else if(this.getValue()=="prestazioni agevolate rivolte a minori"){
                            	calcoloImportoPanel.getForm().findField('importo_isee3').show();
                            	calcoloImportoPanel.getForm().findField('importo_isee2').hide(); 
                            	calcoloImportoPanel.getForm().findField('importo_isee').hide(); 
                            }
                        }
                    }
                }
            },{
                xtype:'container',
                layout:'anchor',
                   
                defaults:{                
                    anchor:'95%'
                },
                items:[{
                	xtype: 'wdecimalnumberfield',
                    name: 'importo_isee',
                    fieldLabel: 'Importo ISEE',
                    tabIndex:2,
                    hidden:true
              
                },{
                	xtype: 'wdecimalnumberfield',
                    name: 'importo_isee2',
                    fieldLabel: 'Importo ISEE',
                    tabIndex:2,
                    hidden:true                       
                },{
                	xtype: 'wdecimalnumberfield',
                    name: 'importo_isee3',
                    fieldLabel: 'Importo ISEE',
                    tabIndex:2,
                    hidden:true                       
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
              
            },{
            	xtype: 'wdecimalnumberfield',
                name: 'contributi_anno_precedente',
                fieldLabel: 'Contributi anno precedente',
                tabIndex:4
                       
            }]
        },
        {
        	xtype: 'wdecimalnumberfield',
            name: 'isee_ricalcolato',
            id:'isee_ricalcolato',
            fieldLabel: 'Isee ricalcolato',
            readOnly : true
      	
        	
        }
        
        ];
    
        var me=this;
        
      
        
      
        
       
       //in teoria sta funzione ricalcola l'isee non il masssimo erogabile...
        function calcolaMassimoErogabile(){
        	var values = calcoloImportoPanel.getForm().getValues();
        	var isee = 0;
        	
        
        	var contributiAnnoPrecedente = Number(values.contributi_anno_precedente.replace(',','.'));
        
        	
        	var iseeOrReddito =values.calcolacon;
        	var nucleoFamiliare = values.nucleo_familiare;
        	
        	
        	var tabella= new Array();
        	tabella[0]=new Array ("");
        	tabella[1]= new Array(1.00,480);
        	tabella[2]= new Array(1.57,753.60);
        	tabella[3]= new Array(2.04,979.20);
        	tabella[4]= new Array(2.46,1180.80);
        	tabella[5]= new Array(2.85,1368);
        	tabella[6]= new Array(3.20,1536);
        	tabella[7]= new Array(3.55,1704);
        	tabella[8]= new Array(3.9,2040);
        	tabella[9]= new Array(4.25,0);
        	tabella[10]=new Array(4.60,2208.00);
        	tabella[11]=new Array(4.95,2376.00);
        	tabella[12]=new	Array(5.30,2544.00);
        	tabella[13]=new Array(5.65,2712.00);
        	tabella[14]=new Array(6.00,2880.00);
        	tabella[15]=new Array(6.35,3048.00);
        	tabella[16]=new Array(6.70,3216.00);
        	tabella[17]=new Array(7.05,3384.00);
        	tabella[18]=new Array(7.40,3552.00);
        	tabella[19]=new Array(7.75,3720.00);
        	tabella[20]=new Array(8.10,3888.00);
        	var cifra = tabella[nucleoFamiliare][0];
        	
        	if(iseeOrReddito == 'Ordinario'){
        	  isee=Number(values.importo_isee.replace(',','.'));
       		        		
        	}
        	else if(iseeOrReddito=='Socio-Sanitario'){
        		  isee=Number(values.importo_isee2.replace(',','.'));
        		
        	}
        	else if(iseeOrReddito=='prestazioni agevolate rivolte a minori'){
        		 isee=Number(values.importo_isee3.replace(',','.'));
        		}
        	
        	var contributoFrattoScala=contributiAnnoPrecedente/cifra;
        	log('contributo fratto scala ='+  contributoFrattoScala);
        	var iseeRicalcolato = isee-contributoFrattoScala;
        	if(iseeRicalcolato<0){
        		iseeRicalcolato=0;
        	}
        	
            
        	calcoloImportoPanel.getForm().findField('isee_ricalcolato').setValue(iseeRicalcolato);
        	
           
        	
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