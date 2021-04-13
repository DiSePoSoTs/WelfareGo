Ext.define('wcs.view.pai.calcoloImporto',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_calcoloimporto',
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
                    fieldLabel: 'Calcola con...',
                    name:'calcolacon',
                    store:[ 'ISEE', 'Reddito'],
                   
                    displayField: 'calcolacon',
                    typeAhead: true,
                    mode: 'local',
                    triggerAction: 'all',
                    selectOnFocus:true,
                    boxMaxWidth: 170,
                    tabIndex:1,
                    listeners:{
                        select:function(component,value){
                            if(this.getValue()=='ISEE'){
                            	calcoloImportoPanel.getForm().findField('importo_isee').show();
                            	calcoloImportoPanel.getForm().findField('importo_reddito').hide(); 
                            	 
                            }
                            else{
                            	calcoloImportoPanel.getForm().findField('importo_reddito').show();
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
                    name: 'importo_reddito',
                    fieldLabel: 'Importo Reddito',
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
            	xtype:'combo',
                fieldLabel: 'Numerosità nucleo familiare',
                name:'nucleo_familiare',
               
                tabIndex:3,
                store:
                    [ '1', '2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20']
                ,
                mode: 'local'
              
            }, {
            	xtype:'combo',
                fieldLabel: 'Numero di minori nel nucleo familiare',
                name:'numero_minori',
               
                tabIndex:4,
                store:['0', '1', '2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20'],
                
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
        	log("isee è" + isee);
        	var reddito = Number(values.importo_reddito.replace(',','.'));
        	log("reddito è" + reddito);
        	
        	var iseeOrReddito =values.calcolacon;
        	var numeroMinori = values.numero_minori;
        	var nucleoFamiliare = values.nucleo_familiare;
        	if (numeroMinori > nucleoFamiliare){
        		
        		   Ext.MessageBox.show({
                       title: 'Errore',
                       msg: 'Attenzione il numero di minori non può essere maggiori al numero di componenti del nucleo familiare',
                       buttons: Ext.MessageBox.OK,
                       icon: Ext.window.MessageBox.ERROR
                   });
        	}
        	var valore;
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
        	if(iseeOrReddito == 'ISEE'){
        		log('calcoliamo con l isee il valore');
        		valore = isee/12;
       		        		
        	}
        	else{
        		log('calcoliamo con il reddito il valore');
        		valore= reddito/cifra;
        		}
        	
        	var iseeMassima = 8180.87/12;
        	log('isee massima è '+ iseeMassima);
        	
        	
        	if(valore > iseeMassima){
        		valore=iseeMassima;
        	}  
        	var massimoErogabile;
        	massimoErogabile=((iseeMassima-valore)*0.7033)*cifra+(numeroMinori*70.33);
        	if(massimoErogabile < 100 && massimoErogabile > 0 ) {
        		massimoErogabile = 0;
        		
        	}
        	calcoloImportoPanel.getForm().findField('massimo_erogabile').setValue(massimoErogabile);
        	
        }
        
        function copiaMassimoToUnderForm(){
        	var values = calcoloImportoPanel.getForm().getValues();
        	massimoErogabile = values.massimo_erogabile;
        	Ext.getCmp('wcs_interventoQuantita').setValue(massimoErogabile);
        	Ext.getCmp('wcs_interventoQuantita').setReadOnly(true);
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