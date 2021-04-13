Ext.define('wf.view.cassa.LiquidareGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wf_liquidare_grid',
    title: 'Interventi da liquidare',
    store:'LiquidareStore',
    /*verticalScroller: {
        xtype: 'paginggridscroller'
      },*/
    loadMask: true,
    columnLines: true,
    frame:true,

    bbar:{
        xtype:'pagingtoolbar',
        store: 'LiquidareStore',
        displayMsg: 'Visualizzo gli interventi  da liquidare da {0} a {1} di {2}',
        emptyMsg: 'Nessun intervento',
        displayInfo:true
       /* items:[{
    		xtype:'filter_field',
    		emptyText: 'Cerca nella griglia ',
    	//	searchByFieldId: 'wtl_searchCassaCombo',
    		disableKeyFilter : true        
    	}
               ]*/
       	},
    
    initComponent: function() {
    	 var getIntervento=function(grid,row){
             return grid.getStore().getAt(row).data;
         };
           this.columns = [{
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
            header: 'Data esecutivit&agrave',
            dataIndex: 'data_esecutivita',
            sortable: false,
            renderer: this.renderGridCell,
            
            flex: 1
        },{
            header: 'Importo da pagare',
            dataIndex: 'importo',
            sortable: false,
//            renderer: this.renderGridCell,
            flex: 1,
            xtype: 'numbercolumn',
            format: '0,000.00 €'
        },{
            header: 'Delegato',
            dataIndex: 'delegato',
            sortable: false,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Paga',
            xtype:'actioncolumn',
            items: [{
                iconCls: 'wfg_azione_icon',
                tooltip: 'Paga contributo ',
                handler: function(grid, rowIndex, colIndex) {
                    
                    var pai = getIntervento(grid,rowIndex).pai;
                    var intervento = getIntervento(grid,rowIndex).num_intervento;
                    var nome = getIntervento(grid,rowIndex).nome;
                    var cognome =  getIntervento(grid,rowIndex).cognome;
                    var importo =  getIntervento(grid,rowIndex).importo;
                    Ext.MessageBox.confirm('Conferma', 'Attenzione si sta per effettuare un pagamento di euro '+ importo + ' all \'utente : ' +  nome + ' ' + cognome + '<br/>	proseguire con l \' operazione ?' ,
                    		function(btn){
                    	   if(btn === 'yes'){
                    	    	var wait = Ext.Msg.wait('operazione in corso...');
                    	    	Ext.Ajax.request({
                    	    		url: wf.config.path.base+'/CassaServlet',
                    	    		params:{
                    	    			action:'save',
                    	    			operation:'prelievo',
                    	    			codPai:pai,
                    	                cntTipint:intervento	
                    	    		},
                    	    		callback: function(params,success,response){
                    	    			wait.close();
                    	    			 var json = success?Ext.JSON.decode(response.responseText):null;
                    	                 if (success && json.success) {
                    	                	 
                    	                     log('Salvato con successo andiamo ad aprire la ricevuta');
                    	                     //TODO AGGIORNAMENTO VARIE GRID
                    	                     Ext.getCmp('wcs_daLiquidareList').loadStore();
                    	                     Ext.getCmp('wcs_logCassa').loadStore();
                    	                     Ext.Msg.show({
                    	                         title:'Successo',
                    	                         msg: 'Operazione completata con successo.<br/>Cliccare il pulsante OK per scaricare la ricevuta',
                    	                         buttons: Ext.Msg.OK,
                    	                         fn: function(){
                    	                        	  window.open(wf.config.path.base+'/CassaDocumentsServlet?action=RICEVUTA&codPai='+pai+'&cntTipInt='+intervento);
                    	                         }
                    	                     });
                    	                     
                    	                 } 
                    	                 else {
                    	                     Ext.MessageBox.show({
                    	                         title: 'Errore',
                    	                         msg: json?json.message:'Errore di comunicazione col server',
                    	                         buttons: Ext.MessageBox.OK,
                    	                         icon: Ext.window.MessageBox.ERROR
                    	                     });
                    	                 }   
                    	    		}
                    	    	});
                    	   }
                    	  
                    	 });
                    
                }                
            }]
         
        }];
      
          
           this.buttons=[{
               text:'Effettua versamento',
               handler:function(){
            	   var window=Ext.create('Ext.window.Window',{
                       title:'Dati versamento',
                       height: 300,
                       width: 250,
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
                               xtype:'wdecimalnumberfield',
                               fieldLabel: '<b>Importo</b>*',
                              
                               maxLength: 9,
                               maxLengthText: 'Lunghezza massima 255 caratteri',
                               allowBlank: false,
                               blankText: 'Questo campo è obbligatorio',
                               id: 'wcs_importoVersamento',
                               name: 'importoVersamento',
                               anchor:'95%',
                               minValue: 0,
                               allowDecimals: true,
                               value: 0
                             
                       },{
                    	   xtype:'textfield',
                           fieldLabel: '<b>Mandato</b>*',
                           tabIndex: 7,
                           maxLength: 255,
                           maxLengthText: 'Lunghezza massima 255 caratteri',
                           allowBlank: false,
                           blankText: 'Questo campo è obbligatorio',
                           id: 'wcs_mandatoVersamento',
                           name: 'mandatoVersamento',
                           anchor:'95%'
                    	   
                       },{
                    	   xtype:'textfield',
                           fieldLabel: '<b>Determina</b>*',
                           maxLength: 255,
                           maxLengthText: 'Lunghezza massima 255 caratteri',
                           allowBlank: false,
                           blankText: 'Questo campo è obbligatorio',
                           id: 'wcs_determinaVersamento',
                           name: 'determinaVersamento',
                           anchor:'95%'
                    	   
                       }],
                       buttons:[{
                    	   text:'Procedi',
                    	   handler:function(){
                    		   var form=window.items.first().getForm();
                    		   if(form.isValid()){
                    			   form.submit ({
                    				  url:wf.config.path.base+'/CassaServlet',
                    				  witTitle:'Salvataggio',
                    				  waitMsg:'Sto salvando i dati',
                    				  params:{
                    					  action: 'save',
                    					  operation :'versamento'
                    					 
                    				  },
                    			      success: function(form,action){
                    			    	  var json = Ext.JSON.decode(action.response.responseText); 
                    			    	  if (json.success) {
                                              Ext.MessageBox.show({
                                                  title: 'Esito operazione',
                                                  msg: json.message,
                                                  buttons: Ext.MessageBox.OK,
                                                  fn: function(){
                                                      window.close();
                                                      
                                                  }
                                              });
                                          } else {
                                              Ext.MessageBox.show({
                                                  title: 'Errore',
                                                  msg: json.message,
                                                  buttons: Ext.MessageBox.OK,
                                                  icon: Ext.window.MessageBox.ERROR
                                              });
                                          }
                    			      }
                    			   });
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
               window.show();
               }
           
           },
           {
        	   text:'Scarica riepiloghi',
        	   handler:function(){
        		  var values = new Ext.data.SimpleStore({
        		          fields: ['id', 'value'],
        		          data : [['ricevute_cassa','Elenco ricevute cassa a mani'],['log_cassa','Storico cassa a mani']]
        		      });
        		  var orderValue = new Ext.data.SimpleStore({
    		          fields: ['id', 'value'],
    		          data : [['ASC','Ascendente'],['DESC','Discendente']]
    		      });
            	   var win=Ext.create('Ext.window.Window',{
                       title:'Scarica riepiloghi ',
                       height: 220,
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
                           xtype: 'combo',
                           name: 'report',
                           fieldLabel: 'Tipo report ',
                           mode: 'local',
                           store: values,
                           displayField:'value',
                           valueField: 'id'
                          
                         },{
                        	 xtype:'datefield',
                             format: 'd/m/Y',
                             fieldLabel: 'Dal ',
                             maxValue: new Date(),
                             id: 'dal',
                             name: 'dal' 
                         },{
                        	 xtype:'datefield',
                             format: 'd/m/Y',
                             fieldLabel: 'Al ',
                             maxValue: new Date(),
                             id: 'al',
                             name: 'al'
                         },{
                             xtype: 'combo',
                             name: 'order',
                             fieldLabel: 'Ordinamento',
                             mode: 'local',
                             store: orderValue,
                             displayField:'value',
                             valueField: 'id'
                            
                           }]
                           
                           
                       }],
                       buttons:[{
                               text:'Procedi',
                         	   handler:function(){
                         		   var form=win.items.first().getForm();
                         		  if(form.isValid()){
                       			    var dal = form.findField('dal').getSubmitValue();
                       			    var al = form.findField('al').getSubmitValue();
                       			    var tipo = form.findField('report').getSubmitValue();
                       			    var ordine = form.findField('order').getSubmitValue();
                       			    window.open(wf.config.path.base+'/CassaDocumentsServlet?action=REPORT&tipo='+tipo+'&par_dal='+dal+'&par_al='+al+'&par_order='+ordine);
                         		  }  
                         		   
                         	   }    
                       
            	   }]
                                
            	   });
            	   win.show();
        	   }   
        	   
           }
           ];

        this.callParent(arguments);
       // this.on('render', this.loadStore, this);
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