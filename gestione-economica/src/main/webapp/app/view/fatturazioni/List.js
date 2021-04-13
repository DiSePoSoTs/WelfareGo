
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la lista con i risultati della ricerca per la sezione
 * Fatturazioni
 */

Ext.define('Wp.view.fatturazioni.List' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.wpfatturazionilist',
    margin:'0 0 10 0',
    border:0,
    items:{
        xtype: 'gridpanel',
        store: 'fatturazioni.Fatturazioni',        
        selType:'checkboxmodel',
        id:'fatturazioni',
        selModel:{
            checkOnly:true,
            mode:'MULTI'  
        },
      //  height: 200,
        autoHeight:true,
        invalidateScrollerOnRefresh:true,
       /* plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1,
            pluginId:'cell-editing-plugin',
            listeners: {
                // TODO: forse sarebbe meglio utilizzare l'evento
                // validateedit ma è buggato in questa versione del
                // framework; event.value viene settato al valore
                // vecchio invece che a quello nuovo
                edit: function(editor, event, options) {
                    var field='azione',setActionValue=function(record,newValue,oldValue){
                        log('changing status for ',record,' from ',oldValue,' to ',newValue);
                        if(newValue==oldValue){
                            log('skipping');
                            return;
                        }
                        var stato = record.get('codice_stato');
                        if((newValue == 'Emetti' && stato != 'de')||(newValue == 'Invia' && stato != 'em')){
                            log('skipping');
                            record.set(field,oldValue);
                        }else{
                            record.set(field,newValue);
                        }
                    }
                    var  newValue = event.value, oldValue = event.originalValue,record=event.record;
                    setActionValue(record,newValue,oldValue);
                    Ext.each(event.grid.getSelectionModel().getSelection(),function(record){
                        setActionValue(record,newValue,record.get(field));
                    });
                }//edit
            }
        })],*/
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
            flex: 1
        },{
            xtype: 'wpfloatnumbercolumn',
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
        }/*{
            text: 'Azione',
            dataIndex: 'azione',
            flex: 1,
            editor: {
                xtype: 'combobox',
                queryMode: 'local',
                store: ['-','Emetti','Invia'],
                forceSelection: true,
                allowBlank: false
            }*/
        ],
        bbar:{
            xtype: 'pagingtoolbar',
            store: 'fatturazioni.Fatturazioni'
        },
        listeners:{
        	scrollershow: function(scroller) {
        		  if (scroller && scroller.scrollEl) {
        		    scroller.clearManagedListeners(); 
        		    scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller); 
        		  }
        		}
        }
    },
    buttons : [{
        text: 'Emetti selezionate',
        action: 'save'
    },{
        text: 'Emetti tutte',
        action: 'save_send'
    },
    {
        text: 'Anteprima file',
        action: 'file_preview'
    }
    ]
});


