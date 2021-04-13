Ext.define('wf.view.lettere.LettereGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wf_lettere_grid',
    title: 'Lettere',
    store:'LettereStore',
    
    /*verticalScroller: {
        xtype: 'paginggridscroller'
      },*/
    loadMask: true,
    tbar: {
        xtype: 'wtl_lettere_top_bar'
    },
    columnLines: true,
    frame:true,
	autoHeight:true,
    invalidateScrollerOnRefresh:true,
    bbar:{
        xtype:'pagingtoolbar',
        store: 'LettereStore',
        displayMsg: 'Visualizzo  da {0} a {1} di {2}',
        emptyMsg: 'Nessuna lettera',
        displayInfo:true
       	},
    //	height: 600,

    initComponent: function() {
        this.selModel = Ext.create('Ext.selection.CheckboxModel');
      
        this.columns = [{
            header: 'Id evento',
            dataIndex: 'id',
            sortable: false,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 1
        },{
            header: 'Task id',
            dataIndex: 'task_id',
            sortable: false,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 1
        },{
            header: 'Cognome',
            dataIndex: 'cognome',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Nome',
            dataIndex: 'nome',
            sortable: false,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'UOT',
            dataIndex: 'uot',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Tipo lettera',
            dataIndex: 'attivita',
            sortable: false,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Intervento',
            dataIndex: 'intervento',
            sortable:true,
            renderer: this.renderGridCell,
            flex: 1
           
        },{
            header: 'Data',
            dataIndex: 'data_task',
            sortable: true,
            xtype:'datecolumn',
            width: 90,
            format:'d/m/Y'
        }];

        this.buttons=[{
            text:'Produci lettere selezionate',
            handler:function(){
            	var win=
            		Ext.create('Ext.window.Window',{
                        title:'Dati protocollo',
                        height: 250,
                        width: 300,
                        modal:true,
                        layout:'fit',
                        items:[{
                     	          
                                frame:true,  
                                autoScroll:true,       
                                xtype:'form',
                                bodyStyle:'padding:5px 5px 0',
                                defaults: {
                                    labelWidth: 100
                                },
                                
                                items:[{ 
                                xtype:'textfield',
                                fieldLabel: '<b>Protocollo</b>*',
                               
                                maxLength: 255,
                                maxLengthText: 'Lunghezza massima 255 caratteri',
                                allowBlank: false,
                                blankText: 'Questo campo è obbligatorio',
                                id: 'wcs_protocollo',
                                name: 'protocollo',
                                anchor:'95%'
                               
                             
                              
                        },{
                     	   xtype:'textfield',
                            fieldLabel: '<b>Protocollo generale</b>*',
                            tabIndex: 7,
                            maxLength: 255,
                            maxLengthText: 'Lunghezza massima 255 caratteri',
                            allowBlank: false,
                            blankText: 'Questo campo è obbligatorio',
                            id: 'wcs_protocolloGenerale',
                            name: 'protocolloGenerale',
                            anchor:'95%'
                     	   
                        }],
                        buttons:[{
                     	   text:'Procedi',
                     	   handler:function(){
                     		var myMask = new Ext.LoadMask(	Ext.getCmp('wf_lettere_grid').getEl(), {msg:"Generazione in corso....."});
                     		myMask.show();
                     		 var form=win.items.first().getForm();
                     		 if(form.isValid()){
                     		   var parameters = new Object();
                     		  parameters.eventi = new Array();
                     		  
                              var selMod = Ext.getCmp('wf_lettere_grid').getSelectionModel();
                               for(var i=0;i<selMod.selected.items.length;i++){
                    parameters.eventi.push(selMod.selected.items[i].data.id);
                             }
                              var urlParameters = Ext.encode(parameters);
                              if (selMod.selected.items.length < 1){
                                  Ext.Msg.alert("Attenzione!", "Non è stato selezionato nessun valore");
                              }
                              else {
                            	  var numeroProtocollo = form.findField("protocollo").getValue();
                            	  var protocolloGenerale = form.findField("protocolloGenerale").getValue();
                            	  win.close();
                             	 Ext.Ajax.request({
                             		 url: wf.config.path.base+'/LettereServlet',
                      	    		params:{
                      	    			action:'save',
                      	    			data:urlParameters,
                      	    			numProt:numeroProtocollo,
                      	    			dtProt:protocolloGenerale
                      	    			
                      	    		},
                      	    		success: function(response){
                      	    			myMask.hide();
                      	    			
                      	    			  var json = Ext.JSON.decode(response.responseText); 
                      	    			  if(json.success){
                      	    			 Ext.Msg.show({
                                              title:'Successo',
                                              msg:json.message,
                                              buttons: Ext.Msg.OK,
                                              fn:function(){
                                             	  Ext.getCmp('wf_lettere_grid').loadStore();
                                             	 var url = wf.config.path.base+'/ScaricaZipServlet?scaricaFile='
                                             	 window.open(url+json.data.filename,'_blank',''); 
                                              }
                      	    			 });
                      	    			  }
                      	    			  else {
                      	    				myMask.hide();
                                              Ext.Msg.show({
                                                  title:'Errore',
                                                  msg:json.message,
                                                  buttons: Ext.Msg.OK
                                              });
                      	    			  }
                      	    		},
                      	    		failure:function(){
                      	    			myMask.hide();
                                          var data = new Object();
                                          data.message = 'Errore generico, si prega di contattare l\'amministratore';
                                          if(response!=null&&response.responseText!=null){
                                              data=Ext.JSON.decode(response.responseText);
                                          }
                                          Ext.Msg.show({
                                              title:'Errore',
                                              msg: data.message,
                                              buttons: Ext.Msg.OK
                                          });
                      	    		}
                      	    		
                             	 });
                             }
                     		   
                     	   }
                     		 else {
                  			   Ext.MessageBox.show({
                                     title: 'Esito operazione',
                                     msg: 'Verifica che i campi siano compilati correttamente',
                                     buttons: Ext.MessageBox.OK,
                                     icon: Ext.window.MessageBox.ERROR
                                 }); 
                  		   }
                     	  }
                        
                        },{text:'Anteprima',
                  	   handler:function(){
                    		var myMask = new Ext.LoadMask(	Ext.getCmp('wf_lettere_grid').getEl(), {msg:"Generazione in corso....."});
                    		myMask.show();
                    		 var form=win.items.first().getForm();
                    		 if(form.isValid()){
                    		   var parameters = new Object();
                    		  parameters.eventi = new Array();
                    		  
                             var selMod = Ext.getCmp('wf_lettere_grid').getSelectionModel();
                              for(var i=0;i<selMod.selected.items.length;i++){
                   parameters.eventi.push(selMod.selected.items[i].data.id);
                            }
                             var urlParameters = Ext.encode(parameters);
                             if (selMod.selected.items.length < 1){
                                 Ext.Msg.alert("Attenzione!", "Non è stato selezionato nessun valore");
                             }
                             else {
                           	  var numeroProtocollo = form.findField("protocollo").getValue();
                           	  var protocolloGenerale = form.findField("protocolloGenerale").getValue();
                           	//  win.close();
                            	 Ext.Ajax.request({
                            		 url: wf.config.path.base+'/LettereServlet',
                     	    		params:{
                     	    			action:'save',
                     	    			data:urlParameters,
                     	    			numProt:numeroProtocollo,
                     	    			dtProt:protocolloGenerale,
                     	    			anteprima:'anteprima'
                     	    			
                     	    		},
                     	    		success: function(response){
                     	    			myMask.hide();
                     	    			
                     	    			  var json = Ext.JSON.decode(response.responseText); 
                     	    			  if(json.success){
                     	    			 Ext.Msg.show({
                                             title:'Successo',
                                             msg:json.message,
                                             buttons: Ext.Msg.OK,
                                             fn:function(){
                                            	 // Ext.getCmp('wf_lettere_grid').loadStore();
                                            	 var url = wf.config.path.base+'/ScaricaZipServlet?scaricaFile='
                                            	 window.open(url+json.data.filename,'_blank',''); 
                                             }
                     	    			 });
                     	    			  }
                     	    			  else {
                     	    				myMask.hide();
                                             Ext.Msg.show({
                                                 title:'Errore',
                                                 msg:json.message,
                                                 buttons: Ext.Msg.OK
                                             });
                     	    			  }
                     	    		},
                     	    		failure:function(){
                     	    			myMask.hide();
                                         var data = new Object();
                                         data.message = 'Errore generico, si prega di contattare l\'amministratore';
                                         if(response!=null&&response.responseText!=null){
                                             data=Ext.JSON.decode(response.responseText);
                                         }
                                         Ext.Msg.show({
                                             title:'Errore',
                                             msg: data.message,
                                             buttons: Ext.Msg.OK
                                         });
                     	    		}
                     	    		
                            	 });
                            }
                    		   
                    	   }
                    		 else {
                 			   Ext.MessageBox.show({
                                    title: 'Esito operazione',
                                    msg: 'Verifica che i campi siano compilati correttamente',
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.window.MessageBox.ERROR
                                }); 
                 		   }
                    	  }
                       
                       }]
                                
                      }]
            		});
                               
            	
             
               
              win.show(); 
            }
        }
     
       ];
        this.callParent(arguments);
    },
    renderGridCell: function(value, p, record){
        value = value.toString();
        p.attr = 'ext:qtip="'+value.replace( /\"/g, "'" ).replace(/\n/g,"<br/>")+'" ext:qtitle="'+this.header+'"';
        return value;
    },
    getStore:function(){
        return this.store;
    },
    loadStore: function() {
        this.getStore().load();
    },
    showResultMessage:function(form,submit,title,formReset){
        var response = Ext.JSON.decode(submit.response.responseText);
        var message = response.data.message;
        Ext.Msg.show({
            title:title,
            msg: message,
            buttons: Ext.Msg.OK,
            fn: function(){
                if(formReset){
                    form.reset();
                }
            }
        });
    }
});