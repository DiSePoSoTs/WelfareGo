Ext.define('wf.view.tasklist.TaskListGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wtl_task_list_grid',
    title: 'Lista attivit&agrave;',
    loadMask: false,
    tbar: {
        xtype: 'wtl_tasklist_top_bar'
    },
    bbar: {
        xtype: 'wtl_tasklist_bottom_bar'
    },
    store:'TaskListStore',
    listeners:{
        itemdblclick:function(grid,record){
            if(wfg.refreshAzione) {
                wfg.refreshAzione(record.data.id);
            }
        }
    },
    viewConfig: {
        getRowClass: function(rec, rowIdx, params, store) {
        /*	if(rec.get('attivita')=='Verifica dati esecutivita'){
        		log('ho trovato un verifica dati');
        	  if(rec.get('urgente')=='S'){
        		 log('torno urgente');
        		  return 'urgente';
        		 
        	  }	
        	  if(rec.get('approvato')=='S'){
        		  log('torno approvato');
        		  return 'approvato';
        		  
        	  }
        	  log('non ritorno un cazzo');
        	  return '';
        	  
        	}
        	else {
        		return '';
        	}*/
        	if(rec.get('attivita') == 'Verifica dati esecutività'){
        		  if(rec.get('approvato')=='S'){
            		  log('torno approvato');
            		  return 'approvato';
            		  
            	  }
        		  if(rec.get('urgente')=='S'){  
        			  log('torno urgente')
        		return 'urgente';
        		  }
        	}
         // return rec.get('approvato') == 'S' && rec.get('attivita') == 'Verifica dati esecutività' ? 'approvato' : '';
        }
      },
    initComponent: function() {
            
        var getTask=function(grid,row){
            return grid.getStore().getAt(row).data;
        }
            
        //this.store=Ext.create('wf.store.TaskListStore');
        this.columns = [{
            header: 'Id',
            dataIndex: 'id',
            sortable: false,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 0
        },
        { header: 'urgente',
        dataIndex: 'urgente',
        sortable: false,
        renderer: this.renderGridCell,
        hidden:true,
        flex: 0
        },{ header: 'approvato',
            dataIndex: 'approvato',
            sortable: false,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 0
            },{
            header: 'Data',
            dataIndex: 'timestamp',
            sortable: true,
            xtype:'datecolumn',
            width: 90,
            format:'d/m/Y'
        },
        {
            header: 'TaskId',
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
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Attivit&agrave;',
            dataIndex: 'attivita',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 2
        },{
            header: 'Pai',
            dataIndex: 'pai',
            sortable: true,
            renderer: this.renderGridCell,
            size:20
        },{
            header: 'Intervento',
            dataIndex: 'intervento',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1.75
        },{
            header: 'Servizio',
            dataIndex: 'servizio',
            sortable: true,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 0.5
        },{
            header: 'Ruolo',
            dataIndex: 'ruolo',
            sortable: true,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 0.25
        },{
            header: 'Uot',
            dataIndex: 'uot',
            sortable: true,
            renderer: this.renderGridCell,
            flex:0.5
          //  size:15,
        },{
            header: 'Assistente',
            dataIndex: 'assistente',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1
        },{
            xtype:'actioncolumn', 
            width:50,
            items: [{
                iconCls: 'wfg_azione_icon',
                tooltip: 'Azione',
                handler: function(grid, rowIndex, colIndex) {
                    var id = getTask(grid,rowIndex).id;
                    if(wfg.refreshAzione) {
                        wfg.refreshAzione(id);
                    }else{
                        Ext.Msg.show({
                            title:'Attenzione',
                            msg: 'La portlet Azione non è al momento disponibile.',
                            buttons: Ext.Msg.OK
                        });
                    }
                }                
            },{
                iconCls: 'wfg_cs_icon', 
                tooltip: 'Cartella Sociale',
                handler: function(grid, rowIndex, colIndex) {
                    var pai = getTask(grid,rowIndex).pai;
                   //TODO cambio schermata
             /*       Ext.Ajax.request({
                    	  url: '/CartellaSociale/popola',
              	        params: {
              	            action: 'salvaInSessione',
              	            codPai:pai
              	        },
              	        success:function(){
              	        	//TODO CAMBIA A SECONDA CHE VADA IN TEST O IN PRODUZIONE
              	        	 var win = window.open('/it/web/guest/cartella-sociale', '_blank');
              	        	  win.focus();
              	        }
              	        	
                    });*/
                    if(window.ricaricaAnagraficaDaPai) {
                        ricaricaAnagraficaDaPai(pai);
                    }else{
                        Ext.Msg.show({
                            title:'Attenzione',
                            msg: 'La portlet per Cartella Sociale non è al momento disponibile.',
                            buttons: Ext.Msg.OK
                        });
                    }
                }
            }]
        }];
        
        //		this.getSelectionModel().on('selectionchange', function(sm, selectedRecord) {
        //			if (selectedRecord.length) {
        //				var id = selectedRecord[0].data.id;
        //				if(wf.refreshAzione) {
        //					wf.refreshAzione(id);
        //				}else{
        //					Ext.Msg.show({
        //						title:'Attenzione',
        //						msg: 'Per utilizzare la funzionalit&agrave; aggiungere la portlet Azione alla pagina.',
        //						buttons: Ext.Msg.OK
        //					});
        //				}
        //			}
        //		});

        this.callParent(arguments);
		log('wf.config.refreshTaskList:', wf.config.refreshTaskList);
        if(wf.config.refreshTaskList){
            var storeRefreshTask=new Ext.util.DelayedTask(function(){
                this.getStore().load();
                storeRefreshTask.delay(1000*15);
            },this);
            storeRefreshTask.delay(1000*15);
        }
    },
    renderGridCell: function(value, metadata, record){
        value = Ext.String.htmlEncode(value);
        //metadata.attr = 'ext:qtip="'+value.replace( /\"/g, "'" ).replace(/\n/g,"<br/>")+'" ext:qtitle="'+this.header+'"';
        //log('rendering cell value : '+value);
        metadata.tdAttr='title="' + value + '"';
        return value;
    },
    getStore:function(){
        return this.store;
    }
});