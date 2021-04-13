
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la lista con i risultati della ricerca dei pagamenti
 * da generare per la sezione Pagamenti
 */

Ext.define('Wp.view.pagamenti.List', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.wppagamentilist',
    margin: '0 0 10 0',
    border: 0,
    items: [{
            xtype: 'gridpanel',
            store: 'pagamenti.Pagamenti',
            selType: 'checkboxmodel',
            selModel: {
                checkOnly: true,
                mode: 'MULTI'
            },
            //height: 260,
            autoHeight:true,
            invalidateScrollerOnRefresh:true,
            loadMask:true,
            
          /*  plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 1,
                    pluginId: 'cell-editing-plugin',
                    listeners: {
                        // TODO: forse sarebbe meglio utilizzare l'evento
                        // validateedit ma è buggato in questa versione del
                        // framework; event.value viene settato al valore
                        // vecchio invece che a quello nuovo
                        edit: function(editor, event, options) {
                            var field = 'azione', setActionValue = function(record, newValue, oldValue) {
                                log('changing status for ', record, ' from ', oldValue, ' to ', newValue);
                                if (newValue == oldValue) {
                                    log('skipping');
                                    return;
                                }
                                var stato = record.get('codice_stato');
                                if ((newValue == 'Emetti' && stato != 'de') || (newValue == 'Invia' && stato != 'em')) {
                                    log('skipping');
                                    record.set(field, oldValue);
                                } else {
                                    record.set(field, newValue);
                                }
                            }
                            var newValue = event.value, oldValue = event.originalValue, record = event.record;
                            setActionValue(record, newValue, oldValue);
                            Ext.each(event.grid.getSelectionModel().getSelection(), function(record) {
                                setActionValue(record, newValue, record.get(field));
                            });
                        }//edit
                    }
                })],*/
            columns: [{
                    text: 'Cognome',
                    dataIndex: 'cognome',
                    flex: 1
                }, {
                    text: 'Nome',
                    dataIndex: 'nome',
                    flex: 1
                }, {
                    text: 'Fascia',
                    dataIndex: 'fascia',
                    flex: 1
                }, {
                    text: 'Tipo intervento',
                    dataIndex: 'tipo_intervento',
                    flex: 1
                }, {
                    xtype: 'wpfloatnumbercolumn',
                    text: 'Importo',
                    dataIndex: 'importo',
                    flex: 1
                }, {
                    text: 'N. Mandato',
                    dataIndex: 'n_mandato',
                    flex: 1
                }, {
                    text: 'Stato',
                    dataIndex: 'stato',
                    flex: 1
                }, {
                    text: 'Delegato',
                    dataIndex: 'delegato',
                    flex: 1
                }


                /*{
                    text: 'Azione',
                    dataIndex: 'azione',
                    flex: 1,
                    editor: {
                        xtype: 'combobox',
                        queryMode: 'local',
                        store: [
                            '-',
                            'Emetti',
                            'Invia'
                        ],
                        forceSelection: true,
                        allowBlank: false
                    }
                }*/],
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
                store: 'pagamenti.Pagamenti',
                displayInfo: true
            }
            
        }],
    buttons: [{
            text: 'Emetti tutti',
            action: 'save'
        }, {
            text: 'Emetti selezionati',
            action: 'save_send'
        },{
            text: 'Anteprima file ragioneria',
            action: 'file_preview'
        }]
});
