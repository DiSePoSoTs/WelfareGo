
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la lista con i risultati della ricerca dei pagamenti
 * da generare per la sezione Pagamenti
 */

Ext.define('Wp.view.pagamenti.ListDaGenerare' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wppagamentilistdagenerare',
    items: [{
		xtype: 'gridpanel',
		store: 'pagamenti.PagamentiDaGenerare',
		selType: 'checkboxmodel',
		selModel: {
			checkOnly: true,
			mode: 'MULTI'
		},
		//height: 320,
		autoHeight:true,
		invalidateScrollerOnRefresh:true,
		columns: [
		            {
		                text: 'Cognome',
		                dataIndex: 'cognome',
		                flex: 1
		            },
		            {
		                text: 'Nome',
		                dataIndex: 'nome',
		                flex: 1
		            },
		            {
		                text: 'Fascia',
		                dataIndex: 'fascia',
		                flex: 1
		            },
		            {
		                text: 'Tipo intervento',
		                dataIndex: 'tipo_intervento',
		                flex: 1
		            },
		            {
		                text: 'Importo',
		                dataIndex: 'mandato',
		                flex: 1
		            },
		            {
		                text: 'N. Mandato',
		                dataIndex: 'n_mandato',
		                flex: 1
		            },
		            {
		                text: 'Data chiusura intervento',
		                dataIndex: 'data_chiusura',
		                flex: 1
		            },
		            {
		                text: 'Stato',
		                dataIndex: 'stato',
		                flex: 1
		            },
                   {
                       text: 'Delegato',
                       dataIndex: 'delegato',
                       flex: 1
                   }
		            ],
		bbar:{
			xtype: 'pagingtoolbar',
			store: 'pagamenti.PagamentiDaGenerare',
			displayInfo: true
		},
		listeners:{
			scrollershow: function(scroller) {
				  if (scroller && scroller.scrollEl) {
					scroller.clearManagedListeners(); 
					scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller); 
				  }
				}
		},
		buttons :  [{
			   text: 'Genera selezionati',
			   action: 'save'
		   },{
			   text: 'Genera tutti',
			   action: 'save_all_new'
		   }]
    }
    ]
});
		            
	/*	
    initComponent: function() {
        this.margin = '0 0 10 0';
        this.border = 0;

        this.items = [
        {
            xtype: 'gridpanel',
            store: 'pagamenti.PagamentiDaGenerare',
            height: 200,
            columns: [
            {
                text: 'Cognome',
                dataIndex: 'cognome',
                flex: 1
            },
            {
                text: 'Nome',
                dataIndex: 'nome',
                flex: 1
            },
            {
                text: 'Fascia',
                dataIndex: 'fascia',
                flex: 1
            },
            {
                text: 'Tipo intervento',
                dataIndex: 'tipo_intervento',
                flex: 1
            },
            {
                text: 'Importo',
                dataIndex: 'mandato',
                flex: 1
            },
            {
                text: 'N. Mandato',
                dataIndex: 'n_mandato',
                flex: 1
            },
            {
                text: 'Stato',
                dataIndex: 'stato',
                flex: 1
            }
            ],
            bbar:{
                xtype: 'pagingtoolbar',
                store: 'pagamenti.PagamentiDaGenerare',
                displayInfo: true
            }
        }
        ];

        this.callParent(arguments);
    }
});*/
