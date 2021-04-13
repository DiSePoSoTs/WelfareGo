Ext.require('wf.view.FilterField');
Ext.define('wf.view.cassa.LogCassaGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wf_logCassa_grid',
    title: 'Storico cassa a mani',
    store:'LogCassaStore',
    /*verticalScroller: {
        xtype: 'paginggridscroller'
      },*/
    loadMask: true,
    columnLines: true,
    frame:true,
    
    bbar:{
    	xtype:'pagingtoolbar',
    store: 'LogCassaStore',
    displayMsg: 'Visualizzo le operazioni cassa  da {0} a {1} di {2}',
    emptyMsg: 'Nessun log',
    displayInfo:true,
    items :['-',{
		xtype:'combo',
		queryMode: 'local',
		displayField: 'name',
		valueField: 'value',
		store: 'combo.CassaSearchStore',
		id: 'wtl_searchCassaCombo',
		name: 'wtl_searchCassaCombo',
		value: 'UTENTE',
		width:150,
		//        forceSelection: true,
		selectOnFocus:true,
		editable: false
    },{
		xtype:'filter_field',
		emptyText: 'Cerca nella griglia ',
		searchByFieldId: 'wtl_searchCassaCombo',
		disableKeyFilter : true        
	}/*{
	 xtype: 'textfield',
     readOnly: true,
     minValue: 0,
     fieldLabel: 'Totale euro in cassa:',
     itemId: 'totaleField',
     name: 'totale',
     value: '0.00'
	}*/
    ]
    },
    
    initComponent: function() {
    	 var getIntervento=function(grid,row){
             return grid.getStore().getAt(row).data;
         }
           this.columns = [{
            header: 'Id ',
            dataIndex: 'id',
            sortable: false,
            renderer: this.renderGridCell,
            hidden:true,
            flex: 1
        },{
            header: 'Data operazione',
            dataIndex: 'data_operazione',
            sortable: true,
            renderer: this.renderGridCell,
            flex: 1
        },{
            header: 'Tipo operazione',
            dataIndex: 'tipo',
            sortable: true,
            renderer: this.renderGridCell,
             flex: 1
        },{
            header: 'Dettagli',
            dataIndex: 'dettagli',
            sortable: false,
            renderer: this.renderGridCell,
            width:372
           
        },{
            header: 'Importo operazione ',
            dataIndex: 'importo',
            sortable: false,
//            renderer: this.renderGridCell,
           flex: 1,
            xtype: 'numbercolumn',
            format: '0,000.00 €'
        },{
            header: 'Totale cassa',
            dataIndex: 'totale',
            sortable: false,
//            renderer: this.renderGridCell,
            flex: 1,
            xtype: 'numbercolumn',
            format: '0,000.00 €'
        },
        {
            header: 'pai',
            dataIndex: 'pai',
            sortable: false,
            hidden: true,
            renderer: this.renderGridCell,
            flex: 1
        
           
        },
        {
            header: 'intervento',
            dataIndex: 'intervento',
            sortable: false,
            hidden: true,
            renderer: this.renderGridCell,
            flex: 1
          
          
        },
        {
            header: 'Stampa ricevuta ',
            xtype:'actioncolumn',
            items: [{
            	   getClass: function(v, meta, rec) {          
            		   return rec.data.flgRicevuta=='S'?'wcs_print_icon':'wcs_print_icon_disabled';
                   },
                   tooltip: 'Stampa ricevuta',
                   handler: function(grid, rowIndex, colIndex) {
                	   var pai = getIntervento(grid,rowIndex).pai;
                	   var intervento =getIntervento(grid,rowIndex).intervento;
                	   var ricevuta = getIntervento(grid,rowIndex).flgRicevuta;
                       if(ricevuta=='S'){                    
                           window.open(wf.config.path.base+'/CassaDocumentsServlet?action=RICEVUTA&codPai='+pai+'&cntTipInt='+intervento);
                       }
                   }   
            }]
        
        }],
       
      
         
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