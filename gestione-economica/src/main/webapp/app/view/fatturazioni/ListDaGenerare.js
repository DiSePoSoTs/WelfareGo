
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la lista con i risultati della ricerca delle fatture
 * da generare per la sezione Fatturazioni
 */

Ext.define('Wp.view.fatturazioni.ListDaGenerare' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wpfatturazionilistdagenerare',
    margin:'0 0 10 0',
    border:0,
	autoHeight:true,
    invalidateScrollerOnRefresh:true,
    items :[{
        xtype: 'gridpanel',
        store: 'fatturazioni.FatturazioniDaGenerare',
        id:'fatturazionidagenerare',
    //    height: 200,
        autoHeight:true,
        columns: [{
            text: 'Cognome',
            dataIndex: 'cognome',
            flex: 1
        },{
            text: 'Nome',
            dataIndex: 'nome',
            flex: 1
        },{
            text: 'Fascia',
            dataIndex: 'fascia',
            flex: 1
        },{
            text: 'Tipo intervento',
            dataIndex: 'tipo_intervento',
            flex: 1,
            hidden: true
        },{
            text: 'Importo',
            dataIndex: 'importo',
            flex: 1
        },{
            text: 'Riscosso',
            dataIndex: 'riscosso',
            flex: 1
        },{
            text: 'N. Fattura',
            dataIndex: 'n_fattura',
            flex: 1
        },{
            text: 'Fattura/Nota credito',
            dataIndex: 'fattura_nota_credito',
            flex: 1
        },{
            text: 'Stato',
            dataIndex: 'stato',
            flex: 1
        }],
        listeners:{
        	scrollershow: function(scroller) {
        		  if (scroller && scroller.scrollEl) {
        		    scroller.clearManagedListeners(); 
        		    scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller); 
        		  }
        		}
        }
    }]    
});


