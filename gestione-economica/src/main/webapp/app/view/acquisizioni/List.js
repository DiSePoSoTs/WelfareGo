
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la lista con i risultati della ricerca per la sezione
 * Acquisizioni
 */

Ext.define('Wp.view.acquisizioni.List' ,{
	extend: 'Ext.panel.Panel',
	alias : 'widget.wpacquisizionilist',

	items: [{
		xtype: 'gridpanel',
		store: 'acquisizioni.Acquisizioni',
		selType: 'checkboxmodel',
		id:'listacquisizioni',
		selModel: {
			checkOnly: true,
			mode: 'MULTI'
		},
		//height: 300,
		autoHeight:true,
		invalidateScrollerOnRefresh:true,
	    loadMask: true,
		columns : [

		           {
		        	   text: 'Cognome (o Ragione Sociale)',
		        	   dataIndex: 'cognome',
		        	   flex: 1
		           },
		           {
		        	   text: 'Nome',
		        	   dataIndex: 'nome',
		        	   flex: 1
		           },
		           {
		        	   text: 'UOT/Struttura',
		        	   dataIndex: 'uot_struttura',
		        	   flex: 1
		           },
		           {
		        	   text: 'Tipo intervento',
		        	   dataIndex: 'tipo_intervento',
		        	   flex: 1
		           },
		           {
		        	   text: 'Data apertura intervento',
		        	   dataIndex: 'data_apertura',
		        	   flex: 1
		           },
		           {
		        	   text: 'Data protocollazione determina',
		        	   dataIndex: 'data_inizio',
		        	   flex: 1
		           },
		           {
		        	   text: 'Data avvio intervento',
		        	   dataIndex: 'data_avvio',
		        	   flex: 1
		           },
		           {
		        	   text: 'Durata',
		        	   dataIndex: 'durata_mesi',
		        	   flex: 1
		           },
		           
		           {
		        	   text: 'Data fine intervento',
		        	   dataIndex: 'data_fine',
		        	   flex: 1
		           },
		           {
		        	   text: 'Costo previsto in euro',
		        	   dataIndex: 'previsto_euro',
		        	   flex: 1
		           },
		           {
		        	   text: 'Quantità erogata prevista',
		        	   dataIndex: 'previsto_quantita',
		        	   flex: 1
		           },
		         
		           {
		        	   text: 'Fascia ISEE',
		        	   dataIndex: 'fascia',
		        	   flex: 1
		           },
		           {
		        	   text: 'Validita ISEE',
		        	   dataIndex: 'data_isee',
		        	   flex: 1
		           },
		           {
		        	   text: 'Tipo',
		        	   dataIndex: 'tipo_pagfat',
		        	   flex: 1
		           },
		           {
		        	   text: 'Delegato',
		        	   dataIndex: 'delegato',
		        	   flex: 1
		           }
		           ],
		           listeners:{
	                	scrollershow: function(scroller) {
	                		  if (scroller && scroller.scrollEl) {
	                		    scroller.clearManagedListeners(); 
	                		    scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller); 
	                		  }
	                		}
	                },
		           bbar: {
		        	   xtype: 'pagingtoolbar',
		        	   store: 'acquisizioni.Acquisizioni',
		        	   displayInfo: true
		        	  /* items :['-',
		        		   {
		        				xtype:'filter_field',
		        				emptyText: 'Cerca nella griglia ',
		        				//searchByFieldId: 'cognome',
		        				//disableKeyFilter : true        
		        			}
		        	   ],*/
//		        	   displayMsg: 'visualizzati {0} - {1} di {2}'
		           },

		           buttons :  [{
		        	   text: 'Acquisici selezionati',
		        	   action: 'save'
		           },
		           {
		        	   text: 'Acquisici tutti',
		        	   action: 'save_all' //ID76
		           }]
	}]
});
/*    initComponent: function() {
        this.margin = '0 0 10 0';
        this.height = 260;

        this.store = 'acquisizioni.Acquisizioni';
       
        this.item = [{
            xtype: 'gridpanel',
            store: 'acquisizioni.Acquisizioni',
            selType: 'checkboxmodel',
            selModel: {
                checkOnly: true,
                mode: 'MULTI'
            }
        }
        ];
        this.columns = [
            {
                text: 'Cognome (o Ragione Sociale)',
                dataIndex: 'cognome',
                flex: 1
            },
            {
                text: 'Nome',
                dataIndex: 'nome',
                flex: 1
            },
            {
                text: 'UOT/Struttura',
                dataIndex: 'uot_struttura',
                flex: 1
            },
            {
                text: 'Tipo intervento',
                dataIndex: 'tipo_intervento',
                flex: 1
            },
            {
                text: 'Data esecutività',
                dataIndex: 'data_inizio',
                flex: 1
            },
            {
                text: 'Data fine intervento',
                dataIndex: 'data_fine',
                flex: 1
            },
            {
                text: 'Tipo',
                dataIndex: 'tipo_pagfat',
                flex: 1
            }
        ];
        
        this.bbar={
            xtype: 'pagingtoolbar',
            store: 'acquisizioni.Acquisizioni',
            displayInfo: true
//            displayMsg: 'visualizzati {0} - {1} di {2}'
        };
        
        this.buttons =  [{
            text: 'Salva',
            action: 'save'
        }];

        this.callParent(arguments);
    }*/



