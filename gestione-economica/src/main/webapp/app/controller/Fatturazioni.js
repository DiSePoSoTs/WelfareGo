
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il controller che gestisce le operazioni della sezione Fatturazioni
 */

Ext.define('Wp.controller.Fatturazioni', {
    extend: 'Ext.app.Controller',
    views: [
        'fatturazioni.Form',
        'fatturazioni.List',
        'fatturazioni.ListDaGenerare',
        'fatturazioni.NuovaSelezionata',
        'fatturazioni.Nuova',
        'fatturazioni.Modifica',
        'fatturazioni.Dettaglio'
    ],
    models: [
        'fatturazioni.Fatturazioni',
        'fatturazioni.FatturazioniDaGenerare',

        'fatturazioni.ComboBoxIva',
        'fatturazioni.VociFatturazioni',
        'fatturazioni.DatiFatturazioni',
        'fatturazioni.MesiPrecedenti',
        'fatturazioni.QuoteObbligati',
        'fatturazioni.nuova.ComboBoxTipologiaInterventiNuova'
    ],
    stores: [
        'fatturazioni.Fatturazioni',
        'fatturazioni.FatturazioniDaGenerare',
        'fatturazioni.FatturazioniShadow',
        'fatturazioni.ModalitaPagamento',
        'fatturazioni.ValoriIva',
        //Nuova Selezionata
        'fatturazioni.nuovaselezionata.VociFatturazioni',
        'fatturazioni.nuovaselezionata.DatiFatturazioni',
        'fatturazioni.nuovaselezionata.MesiPrecedenti',
        'fatturazioni.nuovaselezionata.QuoteObbligati',
        //Nuova
        'fatturazioni.nuova.AutocompletePersone',
        'fatturazioni.nuova.VociFatturazioni',
        'fatturazioni.nuova.DatiFatturazioni',
        'fatturazioni.nuova.MesiPrecedenti',
        'fatturazioni.nuova.QuoteObbligati',
        'fatturazioni.nuova.ListaTipologiaInterventiNuova',
        //Dettaglio
        'fatturazioni.dettaglio.VociFatturazioni',
        'fatturazioni.dettaglio.DatiFatturazioni',
        'fatturazioni.dettaglio.MesiPrecedenti',
        'fatturazioni.dettaglio.QuoteObbligati'
    ],
    refs: [
        {
            ref: 'fatturazioniForm',
            selector: 'wpfatturazioniform'
        },
        {
            ref: 'fatturazioniList',
            selector: 'wpfatturazionilist'
        },
        {
            ref: 'fatturazioniListDaGenerare',
            selector: 'wpfatturazionilistdagenerare'
        },
        {
            ref: 'fatturazioniNuovaSelezionata',
            selector: 'wpfatturazioninuovaselezionata'
        },
        {
            ref: 'fatturazioniNuova',
            selector: 'wpfatturazioninuova'
        },
        {
            ref: 'fatturazioniModifica',
            selector: 'wpfatturazionimodifica'
        },
        {
            ref: 'fatturazioniDettaglio',
            selector: 'wpfatturazionidettaglio'
        }
    ],
    /*
     * Questa variabile viene utilizzata per contare il numero di task "sync()"
     * che hanno terminato
     */
    syncTaskCompleted: 0,
    /*
     * Questa variabile viene utilizzata per contare il numero massimo di task
     * "sync()" che sono stati avviati e che quindi devono concludersi
     */
    totalSyncTasks: 0,
    /*
     * Buffer per tenere traccia delle quote dei civilmente obbligait
     */
    quoteCivilmenteObbligati: new Array(),
    init: function() {
        var fatturazioniNuovaSelezionataStores = [
            this.getFatturazioniNuovaselezionataDatiFatturazioniStore(),
            this.getFatturazioniNuovaselezionataVociFatturazioniStore(),
            this.getFatturazioniNuovaselezionataMesiPrecedentiStore(),
            this.getFatturazioniNuovaselezionataQuoteObbligatiStore()
        ];

        var fatturazioniNuovaStores = [
            this.getFatturazioniNuovaDatiFatturazioniStore(),
            this.getFatturazioniNuovaVociFatturazioniStore(),
            this.getFatturazioniNuovaMesiPrecedentiStore(),
            this.getFatturazioniNuovaQuoteObbligatiStore()
        ];

        var fatturazioniDettaglioStores = [
            this.getFatturazioniDettaglioDatiFatturazioniStore(),
            this.getFatturazioniDettaglioVociFatturazioniStore(),
            this.getFatturazioniDettaglioMesiPrecedentiStore(),
            this.getFatturazioniDettaglioQuoteObbligatiStore()
        ];

        var controller = this;

        // setta le azioni del controller
        this.control({
            // preselezione classe tipologia
            'wpfatturazioniform combobox[name=classe_intervento]': {
                select: this.filtroTipoIntervento
            },
            // pulsante di ricerca di una fatturazione
            'wpfatturazioniform button[action=search]': {
                click: this.search
            },
            // pulsante per chiudere i risultati di una ricerca
            'wpfatturazioniform button[action=back]': {
                click: this.chiudiLista
            },
            // quando clicco su una fattura della lista "Da generare", apri la
            // finestra per la creazione di una "nuova fattura selezionata"
            'wpfatturazionilistdagenerare gridpanel': {
                itemdblclick: this.nuovaFatturaSelezionata
            },
            // pulsante per aprire la schermata Nuova Fattura
            'wpfatturazioniform button[action=nuovaFatturaVuota]': {
                click: this.nuovaFatturaVuota
            },
            // pulsante per il salvataggio del nuovo stato delle fatture
            'wpfatturazionilist button[action=save]': {
                click: this.salvaStato
            },
            // pulsante per il salvataggio del nuovo stato delle fatture e invio
            //di quelle che sono in attesa d'invio
            'wpfatturazionilist button[action=save_send]': {
                click: this.salvaStatoInvia
            },
            // pulsante per il salvataggio del nuovo stato delle fatture e invio
            //di quelle che sono in attesa d'invio
            'wpfatturazionilist button[action=save_all]': {
                click: this.emettiTutte
            },
            
            // pulsante per anteprima file delle fatturazioni
            'wpfatturazionilist button[action=file_preview]': {
                click: this.anteprimaFile
            },
            // quando clicco su una fattura della lista, apro il dettaglio della
            // fattura
            'wpfatturazionilist gridpanel': {
                itemdblclick: function(panel, record) {
                    panel.getSelectionModel().select([record], false);
                    controller.dettaglioFatturaSelezionata();
                }
            },
            // in una "nuova fattura selezionata", se modifico le voci
            // "Riduzione" o "Aumento" nelle voci fattura, ricalcola il totale
            // fattura
            'wpfatturazioninuovaselezionata #vociFatturazioni': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaTotaleFattura.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                    });
                }
            },
            // in una "nuova fattura selezionata", se una delle celle dei totali
            // della fattura viene modificata, ricalcola il totale periodo
            'wpfatturazioninuovaselezionata #totaliFatturazioni [name=iva]': {
                select: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    log('wpfatturazioninuovaselezionata iva : select');
                    this.ricalcolaTotaleFattura.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                    });
                }
            },
            'wpfatturazioninuovaselezionata #totaliFatturazioni [name=bollo]': {
                change: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    log('wpfatturazioninuovaselezionata bollo : change');
                    this.ricalcolaTotalePeriodo.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                    });
                }
            },
            //            'wpfatturazioninuovaselezionata #totaliFatturazioni [name=bollo]': {
            //                select: function() {
            //                    // chiama il metodo per ricalcolare il totale usando il
            //                    // controller come scope e passando la view "nuova fattura
            //                    // selezionata" su cui lavorare e lo store da cui recuperare
            //                    // i dati della fattura
            //                    this.ricalcolaTotalePeriodo.call(this, {
            //                        view: this.getFatturazioniNuovaSelezionata(),
            //                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
            //                    });
            //                }
            //            },
            // in una "nuova fattura selezionata", se abilito o disabilito un
            // record dai mesi precedenti, ricalcola il totale da pagare
            'wpfatturazioninuovaselezionata #mesiPrecedenti': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                    });
                }
            },
            // in una "nuova fattura selezionata", se modifico un importo delle
            // quote obbligati, controlla che la modifica sia lecita ed
            // eventualmente ricalcola il totale da pagare
            'wpfatturazioninuovaselezionata #quoteObbligati': {
                edit: function(editor, e) {
                    // chiama il metodo di validazione usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.validaModificaQuoteObbligati.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                    }, e);
                }
            },
            'wpfatturazioninuovaselezionata #fatturaUnica': {
                change: function() {
                    this.fatturaUnica.call(this, this.getFatturazioniNuovaSelezionata());
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniNuovaSelezionata(),
                        datiFatturazioni: this.getFatturazioniNuovaSelezionataDatiFatturazioniStore()
                    });
                }
            },
            // pulsante per chiudere una nuova fattura selezionata
            'wpfatturazioninuovaselezionata [action=back]': {
                click: this.chiudiNuovaFatturaSelezionata
            },
            // pulsante per salvare una nuova fattura selezionata
            'wpfatturazioninuovaselezionata button[action=save]': {
                click: this.salvaNuovaFatturaSelezionata
            },
            // in una "fattura selezionata", se modifico le voci
            // "Riduzione" o "Aumento" nelle voci fattura, ricalcola il totale
            // fattura
            'wpfatturazionimodifica #vociFatturazioni': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaTotaleFattura.call(this, {
                        view: this.getFatturazioniModifica(),
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    });
                }
            },
            // in un "dettaglio fattura", se una delle celle dei totali
            // della fattura viene modificata, ricalcola il totale periodo
            'wpfatturazionimodifica #totaliFatturazioni [name=iva]': {
                select: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    log('wpfatturazionimodifica iva : select');
                    this.ricalcolaTotalePeriodo.call(this, {
                        view: this.getFatturazioniModifica(),
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    });
                }
            },
            //            'wpfatturazionimodifica #totaliFatturazioni [name=bollo]': {
            //                select: function() {
            //                    // chiama il metodo per ricalcolare il totale usando il
            //                    // controller come scope e passando la view "nuova fattura
            //                    // selezionata" su cui lavorare e lo store da cui recuperare
            //                    // i dati della fattura
            //                    this.ricalcolaTotalePeriodo.call(this, {
            //                        view: this.getFatturazioniModifica(),
            //                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
            //                    });
            //                }
            //            },
            // in un "dettaglio fattura", se abilito o disabilito un
            // record dai mesi precedenti, ricalcola il totale da pagare
            'wpfatturazionimodifica #mesiPrecedenti': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniModifica(),
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    });
                }
            },
            // in un "dettaglio fattura", se modifico un importo delle
            // quote obbligati, controlla che la modifica sia lecita ed
            // eventualmente ricalcola il totale da pagare
            'wpfatturazionimodifica #quoteObbligati': {
                edit: function(editor, e) {
                    // chiama il metodo di validazione usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.validaModificaQuoteObbligati.call(this, {
                        view: this.getFatturazioniModifica(),
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    }, e);
                }
            },
            'wpfatturazionimodifica #fatturaUnica': {
                change: function() {
                    this.fatturaUnica.call(this, this.getFatturazioniModifica());
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniModifica(),
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    });
                }
            },
            // pulsante per chiudere la modifica della fattura
            'wpfatturazionimodifica button[action=back]': {
                click: this.chiudiModificaFattura
            },
            // pulsante per chiudere il dettaglio della fattura
            'wpfatturazionidettaglio button[action=back]': {
                click: this.chiudiDettaglioFattura
            },
            // pulsante per salvare le modifiche ad una fattura
            'wpfatturazionimodifica button[action=save]': {
                click: this.salvaDettaglioFattura
            },
            'wpfatturazioninuova #autocompletePersoneFatturazioni': {
                select: this.creaNuova
            },
            // pulsante per chiudere il dettaglio della fattura
            'wpfatturazioninuova  button[action=back]': {
                click: this.chiudiNuovaFattura
            },
            // pulsante per chiudere il dettaglio della fattura
            'wpfatturazioninuova  #vociFatturazioni-bottoneAggiungi': {
                click: this.aggiungiVoceNuova
            },
            // pulsante per chiudere il dettaglio della fattura
            'wpfatturazioninuova  #vociFatturazioni-bottoneRimuovi': {
                click: this.rimuoviVoceNuova
            },
            // pulsante per chiudere il dettaglio della fattura
            'wpfatturazioninuova  #vociFatturazioni': {
                edit: this.selezionaTipoServizio
            },
            // in una "nuova fattura", se una delle celle dei totali
            // della fattura viene modificata, ricalcola il totale periodo
            'wpfatturazioninuova #totaliFatturazioni [name=iva]': {
                select: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura"
                    // su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaTotalePeriodo.call(this, {
                        view: this.getFatturazioniNuova(),
                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                    });
                }
            },
            'wpfatturazioninuova #totaliFatturazioni [name=bollo]': {
                change: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    log('wpfatturazioninuova bollo : change');
                    this.ricalcolaTotalePeriodo.call(this, {
                        view: this.getFatturazioniNuova(),
                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                    });
                }
            },
            //            'wpfatturazioninuova #totaliFatturazioni [name=bollo]': {
            //                select: function() {
            //                    // chiama il metodo per ricalcolare il totale usando il
            //                    // controller come scope e passando la view "nuova fattura"
            //                    // su cui lavorare e lo store da cui recuperare
            //                    // i dati della fattura
            //                    this.ricalcolaTotalePeriodo.call(this, {
            //                        view: this.getFatturazioniNuova(),
            //                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
            //                    });
            //                }
            //            },
            // in una "nuova fattura", se abilito o disabilito un
            // record dai mesi precedenti, ricalcola il totale da pagare
            'wpfatturazioninuova #mesiPrecedenti': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura"
                    // su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniNuova(),
                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                    });
                }
            },
            // in una "nuova fattura", se modifico un importo delle
            // quote obbligati, controlla che la modifica sia lecita ed
            // eventualmente ricalcola il totale da pagare
            'wpfatturazioninuova #quoteObbligati': {
                edit: function(editor, e) {
                    // chiama il metodo di validazione usando il
                    // controller come scope e passando la view "nuova fattura"
                    // su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.validaModificaQuoteObbligati.call(this, {
                        view: this.getFatturazioniNuova(),
                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                    }, e);
                }
            },
            'wpfatturazioninuova #fatturaUnica': {
                change: function() {
                    this.fatturaUnica.call(this, this.getFatturazioniNuova());
                    this.ricalcolaDaPagare.call(this, {
                        view: this.getFatturazioniNuova(),
                        datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                    });
                }
            },
            // pulsante per chiudere una nuova fattura
            'wpfatturazioninuova button[action=back]': {
                click: this.chiudiNuovaFattura
            },
            // pulsante per salvare una nuova fattura
            'wpfatturazioninuova button[action=save]': {
                click: this.salvaNuovaFattura
            }
        });

        /*
         * Inizializza gli store
         */
        // inizializza gli store per la fattura nuova selezionata
        Ext.each(fatturazioniNuovaSelezionataStores, function(store) {
            if (Ext.getClassName(store) == 'Wp.store.fatturazioni.nuovaselezionata.DatiFatturazioni') {
                // lo store datiFatturazioni deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records) {
                    // mostra messaggio di loading
                    this.getFatturazioniNuovaSelezionata().setLoading(true, true);
                }, this);
                store.addListener('write', function(records) {
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getFatturazioniNuovaSelezionata());

                    // aggiorna tutti gli store con l'id della nuova fattura e
                    // sincronizza tutti gli altri store
                    Ext.each(fatturazioniNuovaSelezionataStores, function(store) {
                        if (Ext.getClassName(store) != 'Wp.store.fatturazioni.nuovaselezionata.DatiFatturazioni') {
                            store.each(function() {
                                this.set('id_fattura', records.first().get('id_fatt'));
                            });
                            store.sync();
                        }
                    });
                }, this);
            }

            else {
                // gli altri store, al termine della sincronizzazione chiamano
                // una callback che conta quanti di loro hanno terminato
                store.addListener('write', function() {
                    this.afterSyncComplete.call(this, this.getFatturazioniNuovaSelezionata());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation) {
                this.onSyncError.call(this, operation, this.getFatturazioniModifica());
            }, this);
        }, this);

        // inizializza gli store per il dettaglio della fattura
        Ext.each(fatturazioniDettaglioStores, function(store) {
            if (Ext.getClassName(store) == 'Wp.store.fatturazioni.dettaglio.DatiFatturazioni') {
                // lo store datiFatturazioni deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records) {
                    // mostra messaggio di loading
                    this.getFatturazioniModifica().setLoading(true, true);
                }, this);
                store.addListener('write', function(records) {
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getFatturazioniModifica());

                    // sincronizza tutti gli altri store
                    Ext.each(fatturazioniDettaglioStores, function(store) {
                        if (Ext.getClassName(store) != 'Wp.store.fatturazioni.dettaglio.DatiFatturazioni') {
                            store.sync();
                        }
                    });
                }, this);
            }

            else {
                // gli altri store, al termine della sincronizzazione chiamano
                // una callback che conta quanti di loro hanno terminato
                store.addListener('write', function() {
                    this.afterSyncComplete.call(this, this.getFatturazioniModifica());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation) {
                this.onSyncError.call(this, operation, this.getFatturazioniModifica());
            }, this);
        }, this);

        // inizializza gli store per la nuova fattua vuota
        Ext.each(fatturazioniNuovaStores, function(store) {
            if (Ext.getClassName(store) == 'Wp.store.fatturazioni.nuova.DatiFatturazioni') {
                // lo store datiFatturazioni deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records) {
                    // mostra messaggio di loading
                    this.getFatturazioniNuova().setLoading(true, true);
                }, this);
                store.addListener('write', function(records) {
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getFatturazioniNuova());

                    // aggiorna tutti gli store con l'id della nuova fattura e
                    // sincronizza tutti gli altri store
                    Ext.each(fatturazioniNuovaStores, function(store) {
                        if (Ext.getClassName(store) != 'Wp.store.fatturazioni.nuova.DatiFatturazioni') {
                            store.each(function() {
                                this.set('id_fattura', records.first().get('id_fatt'));
                            });
                            store.sync();
                        }
                    });
                }, this);
            }

            else {
                // gli altri store, al termine della sincronizzazione chiamano
                // una callback che conta quanti di loro hanno terminato
                store.addListener('write', function() {
                    this.afterSyncComplete.call(this, this.getFatturazioniNuova());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation) {
                this.onSyncError.call(this, operation, this.getFatturazioniModifica());
            }, this);
        }, this);

    },
    /*
     * Abilita o disabilita la possibilità di gestire le quote del
     * civilmente obbligati.
     * @param view Scope per il quale eseguire le funzione
     */
    fatturaUnica: function(view) {
        var obbligati = view.down('#quoteObbligati'),
                unica = view.down('#fatturaUnica').getValue(),
                i = 0,
                quote = this.quoteCivilmenteObbligati;

        if (unica) {
            obbligati.getStore().each(function() {
                quote[i++] = this.get('importo');
                this.set('importo', 0);
            });
            obbligati.disable();
        }//if
        else {
            obbligati.getStore().each(function() {
                this.set('importo', quote[i++]);
            });
            obbligati.enable();
        }
    },
    /*
     * Action per filtrare la lista dei tipi di intervento
     */
    filtroTipoIntervento: function() {
        var comboClasse = this.getFatturazioniForm().down('combobox[name=classe_intervento]');
        var comboTipo = this.getFatturazioniForm().down('combobox[name=tipo_intervento]');
        comboTipo.reset();
        comboTipo.store.clearFilter();
        comboTipo.store.filter('classe', comboClasse.getValue());
        log('updated combo tipo_intervento store filter', comboClasse.getValue());
    },
    /*
     * Action che esegue la ricerca delle fatturazioni e carica i risultati
     * nella lista
     */
    search: function() {

//        this.chiudiLista();
        this.chiudiLista({
            success: eseguiRicerca
        });
//        this.chiudiDettaglioFattura();
//        this.chiudiModificaFattura();
//        this.chiudiNuovaFatturaSelezionata();
//        this.chiudiNuovaFattura();

        function eseguiRicerca() {
            var form = this.getFatturazioniForm().getForm(),
                    list = null,
                    store = null;
                    shadowStore=null;

            if (form.isValid()) {
                // in base al checkbox selezionato, carica la lista e lo store corretti
                if (form.findField('stato_fatturazioni').getSubmitValue() == 'da_generare') {
                    list = this.getFatturazioniListDaGenerare();
                    store = this.getFatturazioniFatturazioniDaGenerareStore();
                }
                else {
                    list = this.getFatturazioniList();
                    store = this.getFatturazioniFatturazioniStore();
                    shadowStore = this.getFatturazioniFatturazioniShadowStore();
                }

                //                store.load({
                //                    params: form.getValues()
                //                });
                store.proxy.extraParams = form.getValues();
                store.loadPage(1,function() {
//                    log('resetting list selection : ', list);
                    list.items.items[0].getSelectionModel().selectAll();
                    list.items.items[0].getSelectionModel().deselectAll();
                });
                if(shadowStore!=null){
                	shadowStore.proxy.extraParams = form.getValues();
                	shadowStore.load();
                }

                list.down('gridpanel').doLayout();
                list.show();
            }
        }

//        eseguiRicerca.call(this);
    },
    /*
     * Action che emette le fatture selezionate 
     */
    salvaStato: function() {
      //  this.getFatturazioniFatturazioniStore().sync();
    	var selectedRowIndexes = [];
        var grid = this.getFatturazioniList().down('gridpanel');
	var selectedBanners = grid.getSelectionModel().getSelection(); 
    	
    	Ext.iterate(selectedBanners,function(record){
    		
    		selectedRowIndexes.push(record.get('id'));
    	}) ;
    	
    	this.emettiFatture(selectedRowIndexes);
    }, // eo salvaStato
    
    
    emettiFatture: function(records){
    	  var store = this.getFatturazioniFatturazioniStore();
    	  var shadowStore = this.getFatturazioniFatturazioniShadowStore();
    	  var grid = this.getFatturazioniList().down('gridpanel');
    	  
    	   function predisponiBottoneDownload(fileCode) {
               var url = '/GestioneEconomica/ScaricaFileServlet?fileCode=' + escape(fileCode);
               var button = Ext.getCmp('scaricaFileGeneratoFatturaButton');
               button.setHandler(function() {
                   window.open(url);
               });
               button.enable();
           }
    	   
    	   
    	  log('sto er fare la richiesta');
    	    var confirmWindow = Ext.create('Ext.window.Window',{
                layout: 'anchor',
               // autoHeight:true,
                modal:true,
                items: [{
                        xtype:'datefield',
                        editable:false,
                        format: 'd/m/Y',
                        value : new Date(),
                        fieldLabel: 'Data emissione',
                        name: 'dataEmissioneFattura',
                        id:'dataEmissioneInserita'
                },{
                    xtype:'datefield',
                    editable:false,
                    format: 'd/m/Y',
                    value : new Date(),
                    fieldLabel: 'Data scadenza',
                    name: 'dataScadenzaFattura',
                    id:'dataScadenzaInserita'
            } 
              
                        
                ],
               buttons:[{
            	   text: 'Conferma',
            	   handler:function(){
            		   var dataEmissione= Ext.getCmp('dataEmissioneInserita').getRawValue();
            		   var dataScadenza= Ext.getCmp('dataScadenzaInserita').getRawValue();
            		   log('sto er fare la richiesta');
            	          var myMask = new Ext.LoadMask(grid.getEl(), {msg:"Emissione fatture in corso.. "});
            	          myMask.show();
            	    	   Ext.Ajax.request({
            	    		 
            	                   url: wp_url_servizi.InviaFatturazioni,
            	                   timeout:600000,
            	                   success: function(response) {
            	                       var risposta = Ext.decode(response.responseText);
            	                       confirmWindow.close();
            	                       myMask.hide();
            	                       if(risposta.success){
            	                     	  var msg = 'Si è verificato un errore. Contattare l\'amministratore';
            	                     	  Ext.Msg.alert('Operazione fallita', msg);
            	                     }
            	                       else {
            	                       if (risposta.fileGenerato) {
            	                           predisponiBottoneDownload(risposta.fileGenerato);
            	                       }
            	                       
            	                       Ext.Msg.alert('Salvataggio riuscito', 'Operazione andata a buon fine: numero di fatture emesse  con successo '+ risposta.fatture_inviate);
            	                       store.load();
            	                       shadowStore.load();
            	                   }
            	                   },
            	                   failure : function(response){
            	                  	 myMask.hide();
            	                     confirmWindow.close();
            	                  	
            	                       var msg = 'Si è verificato un errore. Contattare l\'amministratore';
            	                       Ext.Msg.alert('Operazione fallita',msg);
            	                      
            	                  },
            	              
            	              params: { data: Ext.encode(records), dataEmissione:dataEmissione,dataScadenza:dataScadenza }  
            	           });
            		    
                     }	   
            		   
            	   
                }],
                title: 'EMISSIONE FATTURE',
                width: 300,
                height: 200,
                id: 'confirmWindow'
            }).show();
    
          
    },

    /*
     * Action che emette tutte le fatture che sono da emettere ( presenti nello shadow store ) 
     */
    salvaStatoInvia: function() {
            var store = this.getFatturazioniFatturazioniShadowStore();
       	 var ids = [];
       	 store.each(function(record,id){
        		
        		ids.push(record.get('id'));
        	}) ;
       	 this.emettiFatture(ids);

    },
    /*
     * Visualizza la schermata della nuova fattura.
     */
    nuovaFatturaVuota: function() {

        this.chiudiDettaglioFattura();
        this.chiudiModificaFattura();
        this.chiudiNuovaFatturaSelezionata();
        this.chiudiLista();

        var view = this.getFatturazioniNuova();
        function apriNuovaFattura()
        {
            view.show();//.setLoading(true, true);
        }

        function afterLoadingComplete() {
            view.setLoading(false, true);
        }// eo afterLoadingComplete
        // chiudi l'eventuale fattura già aperta
        if (!view.isHidden()) {
            //            this.chiudiNuovaFattura({
            //                success: apriNuovaFattura
            //            });
        }
        else {
            apriNuovaFattura.call(this);
        }

    },
    /*
     * Action che chiude i risultati della ricerca
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiLista: function(options) {
        log('chiudiLista : inizio');
        options = options || {};

        function eseguiChiudiLista() {
            log('chiudiLista : eseguiChiudiLista : inizio');
            // chiudi l'eventuale lista di fatture
            if (!this.getFatturazioniList().isHidden()) {
                this.getFatturazioniFatturazioniStore().removeAll();
                this.getFatturazioniFatturazioniShadowStore().removeAll();
                this.getFatturazioniList().hide();
            }
            // chiudi l'eventuale lista di fatture da generare
            if (!this.getFatturazioniListDaGenerare().isHidden()) {
                this.getFatturazioniFatturazioniDaGenerareStore().removeAll();
                this.getFatturazioniFatturazioniShadowStore().removeAll();
                this.getFatturazioniListDaGenerare().hide();
            }
            // chiudi l'eventuale schermata nuova fattura vuota
            if (!this.getFatturazioniNuova().isHidden()) {
                this.getFatturazioniNuovaAutocompletePersoneStore().removeAll();
                this.getFatturazioniNuova().hide();
            }
            if (options.success && options.success.call) {
                options.success.call(this);
            }
            log('chiudiLista : eseguiChiudiLista : fine');
        }//eseguiChiudiLista


        // chiudi l'eventuale schermata nuova fattura selezionata
        if (!this.getFatturazioniNuovaSelezionata().isHidden()) {
            log('chiudiLista : chiudiNuovaFatturaSelezionata');
            this.chiudiNuovaFatturaSelezionata({
                success: eseguiChiudiLista
            });
        }
        // chiudi l'eventuale schermata dettaglio fattura
        else if (!this.getFatturazioniModifica().isHidden()) {
            log('chiudiLista : chiudiModificaFattura');
            this.chiudiModificaFattura({
                success: eseguiChiudiLista
            });
        }
        // chiudi l'eventuale schermata nuova fattura vuota
        else if (!this.getFatturazioniNuova().isHidden()) {
            log('chiudiLista : chiudiNuovaFattura');
            this.chiudiNuovaFattura({
                success: eseguiChiudiLista
            });
        }
        else {
            log('chiudiLista : eseguiChiudiLista');
            eseguiChiudiLista.call(this);
        }
        log('chiudiLista : fine');
    },
    /*
     * Action che visualizza la schermata per la creazione di una nuova fattura
     * partendo da una selezionata dalla lista "Da Generare"
     */
    nuovaFatturaSelezionata: function() {
        var view = this.getFatturazioniNuovaSelezionata();

        this.chiudiDettaglioFattura();
        this.chiudiModificaFattura();
        this.chiudiNuovaFattura();

        function apriNuovaFatturaSelezionata() {
            var list = this.getFatturazioniListDaGenerare().down('gridpanel'),
                    selected = list.getSelectionModel().getLastSelected(),
                    taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted < 4) {
                    return;
                }

                log('apriNuovaFatturaSelezionata : afterLoadingComplete : caricata fattura, datiFattura = ', this.getFatturazioniNuovaselezionataDatiFatturazioniStore());

                // quando tutti i dati della fattura sono caricati, ricalcola
                // i totali e disabilita il messaggio di loading
                //
                // chiama il metodo per ricalcolare il totale usando il
                // controller come scope e passando la view "nuova fattura
                // selezionata" su cui lavorare e lo store da cui recuperare
                // i dati della fattura

                this.ricalcolaTotaleFattura.call(this, {
                    'view': view,
                    datiFatturazioni: this.getFatturazioniNuovaselezionataDatiFatturazioniStore()
                });

                view.setLoading(false, true);
            }// eo afterLoadingComplete

            // controlla che ci sia almeno una riga selezionata
            if (!list.getSelectionModel().hasSelection()) {
                Ext.Msg.alert('Attenzione!', 'Devi selezionare almeno una fattura da generare.');
            }
            else {
                // mostra la fattura e visualizza il messaggio di loading
                view.show().setLoading(true, true);

                // carica i dati per una nuova fattura
                this.getFatturazioniNuovaselezionataVociFatturazioniStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getFatturazioniNuovaselezionataDatiFatturazioniStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: function(records, operation, success) {
                        // inserisci nelle form i dati recuperati
                        var record = records[0];

//                        var str = JSON.stringify(record.data);
//                        log('apriNuovaFatturaSelezionata : getFatturazioniNuovaselezionataDatiFatturazioniStore.load : start : data = ', str);
                        view.down('#datiFatturazioni').loadRecord(record);
                        view.down('#totaliFatturazioni').loadRecord(record);
//                        view.down('#datiFatturazioni').loadRecord(record);
//                        view.down('#totaliFatturazioni').getForm().setValues(JSON.parse(str));
//                        view.down('#datiFatturazioni').getForm().setValues(JSON.parse(str));

//                        log('apriNuovaFatturaSelezionata : getFatturazioniNuovaselezionataDatiFatturazioniStore.load : end : data = ', str);

                        afterLoadingComplete.call(this);
                    }
                });

                this.getFatturazioniNuovaselezionataMesiPrecedentiStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getFatturazioniNuovaselezionataQuoteObbligatiStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });
            }
        }// eo apriNuovaFatturaSelezionata

        // chiudi l'eventuale fattura già aperta
        if (!view.isHidden()) {
            this.chiudiNuovaFatturaSelezionata({
                success: apriNuovaFatturaSelezionata
            });
        }
        else {
            apriNuovaFatturaSelezionata.call(this);
        }
    },
    /*
     * Action che chiude la schermata per la creazione di una nuova fattura
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiNuovaFatturaSelezionata: function(options) {
        log('chiudiNuovaFatturaSelezionata : inizio, options = ', options);
        var vociFatturazioni = this.getFatturazioniNuovaselezionataVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniNuovaselezionataDatiFatturazioniStore(),
                formTotaliFatturazioni = this.getFatturazioniNuovaSelezionata().down('#totaliFatturazioni').getForm(),
                mesiPrecedenti = this.getFatturazioniNuovaselezionataMesiPrecedentiStore(),
                quoteObbligati = this.getFatturazioniNuovaselezionataQuoteObbligatiStore(),
                formDatiFatturazioni = this.getFatturazioniNuovaSelezionata().down('#datiFatturazioni').getForm(),
                nonSalvati = false;
        options = options || {};

        function eseguiChiudiNuovaFatturaSelezionata(button) {
            log('chiudiNuovaFatturaSelezionata : eseguiChiudiNuovaFatturaSelezionata : inizio, button = ', button, ' , options = ', options);
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociFatturazioni.removeAll();
                datiFatturazioni.removeAll();
//                formTotaliFatturazioni.reset();
//                mesiPrecedenti.removeAll();
//                quoteObbligati.removeAll();
//                formDatiFatturazioni.reset();
                
                try {
                    formTotaliFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formTotaliFatturazioni.reset error = ', e);
                }
                mesiPrecedenti.removeAll();
                quoteObbligati.removeAll();
                try {
                    formDatiFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formDatiFatturazioni.reset error = ', e);
                }
                
                // nascondi la view
                this.getFatturazioniNuovaSelezionata().hide();

                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }
            }
            log('chiudiNuovaFatturaSelezionata : eseguiChiudiNuovaFatturaSelezionata : fine');
        }

        if (options.chiamataDaSalva == undefined)
        {
            log('chiudiNuovaFatturaSelezionata : no chiamataDaSalva');

            // controlla che non ci siano modifiche non salvate
            vociFatturazioni.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
            if (!nonSalvati) {
                datiFatturazioni.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }
            if (!nonSalvati) {
                mesiPrecedenti.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }
            if (!nonSalvati) {
                quoteObbligati.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }

            if (nonSalvati) {
                log('chiudiNuovaFatturaSelezionata : nonSalvati');
                Ext.Msg.show({
                    title: 'Attenzione!',
                    msg: 'Chiudendo la finestra le modifiche non salvate andranno perse. Vuoi continuare?',
                    buttons: Ext.Msg.YESNO,
                    fn: eseguiChiudiNuovaFatturaSelezionata,
                    scope: this,
                    icon: Ext.Msg.QUESTION
                });
            } else {
                log('chiudiNuovaFatturaSelezionata : no nonSalvati');
                eseguiChiudiNuovaFatturaSelezionata.call(this);
            }
        }//if chiamataDaSalva
        else
        {
            log('chiudiNuovaFatturaSelezionata : chiamataDaSalva');
            eseguiChiudiNuovaFatturaSelezionata.call(this);
        }
        log('chiudiNuovaFatturaSelezionata : fine');
    },
    /*
     * Action che salva una "nuova fattura selezionata"
     */
    salvaNuovaFatturaSelezionata: function() {
        log('salvaNuovaFatturaSelezionata : begin');
        var view = this.getFatturazioniNuovaSelezionata(),
                formTotaliFatturazioni = view.down('#totaliFatturazioni').getForm(),
                formDatiFatturazioni = view.down('#datiFatturazioni').getForm(),
                vociFatturazioni = this.getFatturazioniNuovaselezionataVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniNuovaselezionataDatiFatturazioniStore(),
                stores = [
            vociFatturazioni,
            datiFatturazioni,
            this.getFatturazioniNuovaselezionataMesiPrecedentiStore(),
            this.getFatturazioniNuovaselezionataQuoteObbligatiStore()
        ],
                vociIsValid = true,
                recordDatiFatturazioni = datiFatturazioni.first();

        // verifica che tutte le voci fattura abbiano un importo valido
        vociFatturazioni.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (formTotaliFatturazioni.isValid() &&
                formDatiFatturazioni.isValid() &&
                vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store) {
                var toCreate = store.getNewRecords().length > 0,
                        toUpdate = store.getUpdatedRecords().length > 0,
                        toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);
            // eseguo ancora la formattazione della data in formato italiano
//            formDatiFatturazioni.setValues({
//                scadenza : Ext.Date.format(formDatiFatturazioni.getFieldValues().scadenza, 'd/m/Y')
//            });
            //forzo la sincronizzazione degli store.
          
            //FIXME: se lo store datiFatturazioni non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri

            formDatiFatturazioni.updateRecord(recordDatiFatturazioni);
            // sincronizza tutti gli store
           

            log('salvaNuovaFatturaSelezionata : sync record = ', datiFatturazioni);
            datiFatturazioni.sync();
            log('salvaNuovaFatturaSelezionata : done');
            var mesiPrecedenti =  this.getFatturazioniNuovaselezionataMesiPrecedentiStore();
            mesiPrecedenti.sync();
        } else {
            log('salvaNuovaFatturaSelezionata : validation failed');
        }
    },
    /*
     * Callback eseguita nel caso in cui il sync di uno store sia andato a buon
     * fine
     */
    afterSyncComplete: function(view) {
        if (++this.syncTaskCompleted < this.totalSyncTasks) {
            return;
        }

        // se tutte le sincronizzazioni sono andate a buon fine
        Ext.Msg.alert('Salvataggio', 'Modifiche salvate con successo.', function() {
            if (Ext.getClassName(view) == 'Wp.view.fatturazioni.Nuova')
            {
                this.chiudiNuovaFattura({
                    chiamataDaSalva: true
                });
            }
            if (Ext.getClassName(view) == 'Wp.view.fatturazioni.NuovaSelezionata')
            {
                this.chiudiNuovaFatturaSelezionata({
                    chiamataDaSalva: true
                });
            }
        }, this);//Ext.Msg.alert
        // nascondi messaggio di loading
        view.setLoading(false, true);
        //ricarica la lista delle fatture da generare
        Ext.getCmp('fatturazionidagenerare').store.load();
    },
    /*
     * Callback eseguita nel caso in cui il sync di uno store non vada a buon
     * fine
     */
    onSyncError: function(operation, view) {
        switch (operation.action) {
            case 'update':
                Ext.Msg.alert('Errore!', 'Impossibile salvare i dati.');
                // nascondi messaggio di loading
                view.setLoading(false, true);
                break;
            case 'read':
                Ext.Msg.alert('Errore!', 'Errore nel caricamento dei dati.');
                break;
            default:
                Ext.Msg.alert('Errore!', 'Si è verificato un errore.');
                break;
        }
    },
    /*
     * Action che visualizza la schermata per la visualizzazione di una fattura
     * già esistente.
     */
    dettaglioFatturaSelezionata: function() {

        this.chiudiModificaFattura();
        this.chiudiNuovaFatturaSelezionata();
        this.chiudiNuovaFattura();

        var list = this.getFatturazioniList().down('gridpanel');
        var selected = list.getSelectionModel().getLastSelected();

        //Seleziono il tipo di view in base allo stato del documento
        var view;
        switch (selected.data.stato)
        {
            //dettaglio modificabile
            case 'da emettere':
                view = this.getFatturazioniModifica();
                break;

                //Dettaglio non modificabile
            case 'emesse':
            case 'emessa':
            case 'da inviare':
            case 'inviate':
            case 'pagate':
                view = this.getFatturazioniDettaglio();
                break;
        }//switch

        function apriFatturaSelezionata() {
            var taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted >= 4) {
                    // quando tutti i dati della fattura sono caricati, ricalcola
                    // i totali e disabilita il messaggio di loading
                    //
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuova fattura
                    // selezionata" su cui lavorare e lo store da cui recuperare
                    // i dati della fattura
                    this.ricalcolaTotaleFattura.call(this, {
                        'view': view,
                        datiFatturazioni: this.getFatturazioniDettaglioDatiFatturazioniStore()
                    });

                    view.setLoading(false, true);
                }
            }// eo afterLoadingComplete

            // controlla che ci sia almeno una riga selezionata
            if (!list.getSelectionModel().hasSelection()) {
                Ext.Msg.alert('Attenzione!', 'Devi selezionare almeno una fattura.');
            }
            else {
                // mostra la fattura e visualizza il messaggio di loading
                view.show().setLoading(true, true);

                // carica i dati per una nuova fattura
                this.getFatturazioniDettaglioVociFatturazioniStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getFatturazioniDettaglioDatiFatturazioniStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: function(records, operation, success) {
                        // inserisci nelle form i dati recuperati
                        view.down('#totaliFatturazioni').loadRecord(records[0]);
                        view.down('#datiFatturazioni').loadRecord(records[0]);

                        afterLoadingComplete.call(this);
                    }
                });

                this.getFatturazioniDettaglioMesiPrecedentiStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getFatturazioniDettaglioQuoteObbligatiStore().load({
                    params: {
                        id: selected.getId()
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });
            }
        }// eo apriFatturaSelezionata
        // chiudi l'eventuale fattura già aperta
        if (!view.isHidden()) {
            this.chiudiModificaFattura({
                success: apriFatturaSelezionata
            });
        }
        else {
            apriFatturaSelezionata.call(this);
        }
    }, //dettaglioFatturaSelezionata

    /*
     * Action che chiude la schermata per visuailzzare il dettaglio della fattura
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiModificaFattura: function(options) {
        log('chiudiModificaFattura : inizio');

        var vociFatturazioni = this.getFatturazioniDettaglioVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniDettaglioDatiFatturazioniStore(),
                formTotaliFatturazioni = this.getFatturazioniModifica().down('#totaliFatturazioni').getForm(),
                mesiPrecedenti = this.getFatturazioniDettaglioMesiPrecedentiStore(),
                quoteObbligati = this.getFatturazioniDettaglioQuoteObbligatiStore(),
                formDatiFatturazioni = this.getFatturazioniModifica().down('#datiFatturazioni').getForm(),
                nonSalvati = false;
        options = options || {};

        // controlla che non ci siano modifiche non salvate
        vociFatturazioni.each(function() {
            if (this.dirty) {
                nonSalvati = true;

                // termina il ciclo
                return false;
            }
        });
        if (!nonSalvati) {
            datiFatturazioni.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }
        if (!nonSalvati) {
            mesiPrecedenti.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }
        if (!nonSalvati) {
            quoteObbligati.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }

        if (nonSalvati) {
            Ext.Msg.show({
                title: 'Attenzione!',
                msg: 'Chiudendo la finestra le modifiche non salvate andranno perse. Vuoi continuare?',
                buttons: Ext.Msg.YESNO,
                fn: eseguiChiudiModificaFattura,
                scope: this,
                icon: Ext.Msg.QUESTION
            });
        }
        else {
            eseguiChiudiModificaFattura.call(this);
        }


        function eseguiChiudiModificaFattura(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociFatturazioni.removeAll();
                datiFatturazioni.removeAll();
//                formTotaliFatturazioni.reset();
//                mesiPrecedenti.removeAll();
//                quoteObbligati.removeAll();
//                formDatiFatturazioni.reset();                
                try {
                    formTotaliFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formTotaliFatturazioni.reset error = ', e);
                }
                mesiPrecedenti.removeAll();
                quoteObbligati.removeAll();
                try {
                    formDatiFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formDatiFatturazioni.reset error = ', e);
                }
                // nascondi la view
                this.getFatturazioniModifica().hide();

                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }
            }//if
        }//eseguiChiudiModificaFattura

        log('chiudiModificaFattura : fine');
    }, //chiudiModificaFattura

    /**
     * Chiude il dettaglio della fattura
     */
    chiudiDettaglioFattura: function() {
        log('chiudiDettaglioFattura : inizio');
        this.getFatturazioniDettaglio().hide();
        log('chiudiDettaglioFattura : fine');
    }, //chiudiDettaglioFattura

    salvaDettaglioFattura: function() {
        log('salvaDettaglioFattura : begin');
        var view = this.getFatturazioniModifica(),
                formTotaliFatturazioni = view.down('#totaliFatturazioni').getForm(),
                formDatiFatturazioni = view.down('#datiFatturazioni').getForm(),
                vociFatturazioni = this.getFatturazioniDettaglioVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniDettaglioDatiFatturazioniStore(),
                stores = [
            vociFatturazioni,
            datiFatturazioni,
            this.getFatturazioniDettaglioMesiPrecedentiStore(),
            this.getFatturazioniDettaglioQuoteObbligatiStore()
        ],
                vociIsValid = true,
                recordDatiFatturazioni = datiFatturazioni.first();

        // verifica che tutte le voci fattura abbiano un importo valido
        vociFatturazioni.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (formTotaliFatturazioni.isValid() &&
                formDatiFatturazioni.isValid() &&
                vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store) {
                var toCreate = store.getNewRecords().length > 0,
                        toUpdate = store.getUpdatedRecords().length > 0,
                        toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);
            // eseguo ancora la formattazione della data in formato italiano
//            formDatiFatturazioni.setValues({
//                scadenza : Ext.Date.format(formDatiFatturazioni.getFieldValues().scadenza, 'd/m/Y')
//            });

            //FIXME: se lo store datiFatturazioni non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri
            var mesiPrecedenti =  this.getFatturazioniNuovaselezionataMesiPrecedentiStore();
            //mesiPrecedenti.sync();
            formDatiFatturazioni.updateRecord(recordDatiFatturazioni);
            // sincronizza tutti gli store
            datiFatturazioni.sync();
        } else {
            log('salvaDettaglioFattura : invalid form');
        }
        log('salvaDettaglioFattura : done');
        //ricarica la lista delle fatture da generare
        Ext.getCmp('fatturazioni').store.load();
    },
    /*
     * Ricalcola il "Totale fattura" all'interno di una nuova fattura
     *
     * @param Object params: contiene i parametri rappresentanti il contesto in
     *     cui la funzione deve operare:
     *     - view: la view da cui recuperare i dati collegati (voci, mesi
     *         precedenti, quote obbligati)
     *     - datiFatturazioni: lo store da cui recuperare i dati della fattura
     */
    ricalcolaTotaleFattura: function(params) {
        log('ricalcolaTotaleFattura : start');
        var voci = params.view.down('#vociFatturazioni').getStore(),
                totaleConIva = 0, totaleSenzaIva = 0, totaleVariazioni = 0, totaleContributo = 0,
                form = params.view.down('#totaliFatturazioni').getForm(),
                record = params.datiFatturazioni.first();

        // somma le varie voci della fattura
        voci.each(function() {
            var importo = Ext.isEmpty(this.get('importo_dovuto')) ? 0 : this.get('importo_dovuto'),
                    //importoConIva = Ext.isEmpty(this.get('importoConIva')) ? 0 : this.get('importoConIva'),
                    importoConIva = Ext.isEmpty(this.get('importoConIva')) ? 0 : this.get('importoConIva')
            		importoSenzaIva = Ext.isEmpty(this.get('importoSenzaIva')) ? 0 : this.get('importoSenzaIva'),
                    aliquotaIva = Ext.isEmpty(this.get('aliquotaIva')) ? 0 : this.get('aliquotaIva'),
//                    totaleVariazioniConIva = Ext.isEmpty(this.get('totaleVariazioniConIva')) ? 0 : this.get('totaleVariazioniConIva'),
                    aumento = Ext.isEmpty(this.get('aumento')) ? 0 : this.get('aumento'),
                    riduzione = Ext.isEmpty(this.get('riduzione')) ? 0 : this.get('riduzione'),
                    variazione = Ext.isEmpty(this.get('variazione_straordinaria')) ? 0 : this.get('variazione_straordinaria'),
                    contributo = Ext.isEmpty(this.get('contributoRiga')) ? 0 : this.get('contributoRiga'),
                    variazioni = aumento - riduzione + variazione,
                    percentualeRiduzione = Ext.isEmpty(this.get('percentualeRiduzione')) ? 0 : this.get('percentualeRiduzione');
            totaleConIva += (importoConIva + ((1 + aliquotaIva) * variazioni))
            totaleSenzaIva += (importoSenzaIva + variazioni)
            totaleVariazioni += variazioni;
            totaleContributo += contributo * (100 - percentualeRiduzione) / 100;
            
        });
        log('ricalcolaTotaleFattura : voci = ', voci, ' , totaleConIva = ', totaleConIva, ' , totaleSenzaIva = ', totaleSenzaIva, ' totale variazioni = ', totaleVariazioni);

        // scrivi il totale fattura nella form
        form.setValues({
            totale_fattura: totaleConIva,
            totaleFatturaConIva: totaleConIva,
            totaleFatturaSenzaIva: totaleSenzaIva,
            importo_iva: totaleConIva - totaleSenzaIva,
            contributo:  Ext.isEmpty(form.getFieldValues().contributo) ? totaleContributo : form.getFieldValues().contributo 
        });

        // aggiorna il record per permettere il futuro salvataggio
        form.updateRecord(record);

        // ricalcola il totale periodo
        //
        // chiama il metodo per ricalcolare il totale usando il
        // controller come scope e passando i dati su cui lavorare
        this.ricalcolaTotalePeriodo.call(this, params);
    },
    /*
     * Ricalcola il "Totale del periodo" all'interno di una nuova fattura
     *
     * @param Object params: contiene i parametri rappresentanti il contesto in
     *     cui la funzione deve operare:
     *     - view: la view da cui recuperare i dati collegati (voci, mesi
     *         precedenti, quote obbligati)
     *     - datiFatturazioni: lo store da cui recuperare i dati della fattura
     */
    ricalcolaTotalePeriodo: function(params) {
        var record = params.datiFatturazioni.first(),
                form = params.view.down('#totaliFatturazioni').getForm(),
                values = form.getFieldValues();
        log('ricalcolaTotalePeriodo : start , values = ', values);
        var
            //    totaleFatturaConIva = Number(values.totaleFatturaConIva) || 0,
                totaleFatturaSenzaIva = Number(values.totaleFatturaSenzaIva) || 0,
//                totFattura = Ext.isEmpty(values.totale_fattura) ? 0 : values.totale_fattura,
                contributo = Ext.isEmpty(values.contributo) ? 0 : values.contributo,
                iva = Ext.isEmpty(this.getFatturazioniValoriIvaStore().getById(values.iva)) ? 0 : Number(this.getFatturazioniValoriIvaStore().getById(values.iva).get('valore')),
                bollo = iva != 0 ? 0 : Number(values.bollo || 0),
                totaleFatturaConIva = iva != 0?totaleFatturaSenzaIva +( totaleFatturaSenzaIva * iva) : totaleFatturaSenzaIva
                imponibile = totaleFatturaSenzaIva, // totFattura - contributo,
                importo_iva = iva == 0 ? 0 : (totaleFatturaConIva - totaleFatturaSenzaIva),
                totaleFattura = importo_iva + totaleFatturaSenzaIva;
                if(totaleFattura > 77.47){
                	bollo = 2.00;
                }
                else {
                	bollo=0.00;
                }
                totPeriodo = totaleFattura + bollo - contributo;
        log('ricalcolaTotalePeriodo : totaleFatturaConIva = ', totaleFatturaConIva, ' , totaleFatturaSenzaIva = ', totaleFatturaSenzaIva, ' , iva = ', iva, ' , bollo = ', bollo);
        log('ricalcolaTotalePeriodo : imponibile = ', imponibile, ' , importo_iva = ', importo_iva, ' , totPeriodo = ', totPeriodo);
//                bollo;
//        if (iva == 0) {
//            bollo = 1.81;
//        } else {
//            bollo = 0;
//        }
//        var ;

        // scrivi il totale periodo nella form
        form.setValues({
            totale_fattura: totaleFattura,
            'totale_periodo': totPeriodo,
            'imponibile': imponibile,
            'importo_iva': importo_iva,
            'bollo': bollo
        });

        // aggiorna il record per permettere il futuro salvataggio
        form.updateRecord(record);

        // ricalcola il totale da pagare
        //
        // chiama il metodo per ricalcolare il totale usando il
        // controller come scope e passando i dati su cui lavorare
        this.ricalcolaDaPagare.call(this, params);

    },
    /*
     * Ricalcola il totale "Da pagare" all'interno di una nuova fattura
     *
     * @param Object params: contiene i parametri rappresentanti il contesto in
     *     cui la funzione deve operare:
     *     - view: la view da cui recuperare i dati collegati (voci, mesi
     *         precedenti, quote obbligati)
     *     - datiFatturazioni: lo store da cui recuperare i dati della fattura
     */
    ricalcolaDaPagare: function(params) {
        var record = params.datiFatturazioni.first(),
                formDati = params.view.down('#datiFatturazioni').getForm(),
                formTotali = params.view.down('#totaliFatturazioni').getForm(),
                valuesTotali = formTotali.getFieldValues(),
                totPeriodo = Ext.isEmpty(valuesTotali.totale_periodo) ? 0 : valuesTotali.totale_periodo,
                mesiPrecedenti = params.view.down('#mesiPrecedenti').getStore(),
                quoteObbligati = params.view.down('#quoteObbligati').getStore(),
                totDaPagare = totPeriodo;

        // aggiungi al totale tutti i mesi precedenti selezionati
        mesiPrecedenti.each(function() {
            if (this.get('inserimento') == 'Si') {
                totDaPagare += Ext.isEmpty(this.get('importo')) ? 0 : this.get('importo');
            }
        });

        // sottrai dal totale tutte le quote obbligati
        quoteObbligati.each(function() {
            totDaPagare -= Ext.isEmpty(this.get('importo')) ? 0 : this.get('importo');
        });

        // scrivi il totale da pagere nella form
        formDati.setValues({
            da_pagare: totDaPagare
        });

        // aggiorna il record per permettere il futuro salvataggio
        formDati.updateRecord(record);
    },
    /*
     * Verifica che la modifica effettuata ad una delle "Quote obbligati" sia valida;
     * eventualmente ricalcola il totale da pagare
     *
     * @param Object params: contiene i parametri rappresentanti il contesto in
     *     cui la funzione deve operare:
     *     - view: la view da cui recuperare i dati collegati (voci, mesi
     *         precedenti, quote obbligati)
     *     - datiFatturazioni: lo store da cui recuperare i dati della fattura
     * @param Object e: il parametro "e" dell'evento "edit" della classe
     *     "Ext.grid.plugin.CellEditing"
     */
    validaModificaQuoteObbligati: function(params, e) {
        var quoteObbligati = params.view.down('#quoteObbligati').getStore(),
                mesiPrecedenti = params.view.down('#mesiPrecedenti').getStore(),
                form = params.view.down('#totaliFatturazioni').getForm(),
                values = form.getFieldValues(),
                totFattura = Ext.isEmpty(values.totale_periodo) ? 0 : values.totale_periodo,
                totMesiPrecedenti = 0,
                totQuoteObbligati = 0;

        // calcola il totale dei mesi precedenti
        mesiPrecedenti.each(function() {
            if (this.get('inserimento') == 'Si') {
                totMesiPrecedenti += Ext.isEmpty(this.get('importo')) ? 0 : this.get('importo');
            }
        });

        // calcola il totale delle quote obbligati
        quoteObbligati.each(function() {
            totQuoteObbligati += Ext.isEmpty(this.get('importo')) ? 0 : this.get('importo');
        });

        // se la nuova somma delle quote obbligati è valida
        if (totQuoteObbligati <= totFattura + totMesiPrecedenti) {
            // ricalcola il totale da pagare
            //
            // chiama il metodo per ricalcolare il totale usando il
            // controller come scope e passando i dati su cui lavorare
            this.ricalcolaDaPagare.call(this, params);
        }
        else {
            // ripristina il vecchio valore e mostra un messaggio di errore
            e.record.set(e.field, e.originalValue);
            Ext.Msg.alert('Attenzione!', 'Il valore inserito non è valido.');
        }
    },
    /**
     * La funzione viene chiamata quando l'utente esegue la scelta dell'
     * anagrafica alla quale vuovle creare una nuova fattura.
     * @param Object event: evento (la selezione dell'utente)
     * @param Object el: elemento selezionato
     * @param Object e:
     */
    creaNuova: function(event, el, o)
    {

        var view = this.getFatturazioniNuova().down('#panelFatturaNuova');

        function apriNuovaFattura() {
            var taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted < 4) {
                    return;
                }

                view.setLoading(false, true);
            }// eo afterLoadingComplete

            // controlla che ci sia almeno una riga selezionata
            if (el == undefined || el[0] == undefined || el[0].data == undefined || el[0].data.id == undefined) {
                Ext.Msg.alert('Attenzione!', 'Devi selezionare almeno un\'anagrafica valida.');
            }
            else {
                var selected = el[0].data.id;
                // mostra la fattura e visualizza il messaggio di loading
                view.show().setLoading(true, true);

                this.getFatturazioniNuovaDatiFatturazioniStore().load({
                    params: {
                        id: selected
                    },
                    scope: this,
                    callback: function(records, operation, success) {
                        // inserisci nelle form i dati recuperati
                        view.down('#datiFatturazioni').loadRecord(records[0]);
                        view.down('#totaliFatturazioni').loadRecord(records[0]);

                        afterLoadingComplete.call(this);
                    }
                });

                this.getFatturazioniNuovaMesiPrecedentiStore().load({
                    params: {
                        id: selected
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getFatturazioniNuovaQuoteObbligatiStore().load({
                    params: {
                        id: selected
                    },
                    scope: this,
                    callback: afterLoadingComplete
                });
                this.getFatturazioniNuovaListaTipologiaInterventiNuovaStore().proxy.extraParams = {
                    id: selected
                };
                this.getFatturazioniNuovaListaTipologiaInterventiNuovaStore().load({
                    scope: this,
                    callback: afterLoadingComplete
                });
            }
        }// eo apriNuovaFattura

        // chiudi l'eventuale fattura già aperta
        if (!view.isHidden()) {
            this.chiudiNuovaFattura({
                success: apriNuovaFattura
            });
        }
        else {
            apriNuovaFattura.call(this);
        }
    },
    /*
     * Action che chiude il pannello con il dettaglio nella schermata per la
     * creazione di una nuova fattura
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiPannelloNuovaFattura: function(options) {
        var vociFatturazioni = this.getFatturazioniNuovaVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniNuovaDatiFatturazioniStore(),
                formTotaliFatturazioni = this.getFatturazioniNuova().down('#totaliFatturazioni').getForm(),
                mesiPrecedenti = this.getFatturazioniNuovaMesiPrecedentiStore(),
                quoteObbligati = this.getFatturazioniNuovaQuoteObbligatiStore(),
                formDatiFatturazioni = this.getFatturazioniNuova().down('#datiFatturazioni').getForm(),
                nonSalvati = false;
        options = options || {};

        function eseguiChiudiPannelloNuovaFattura(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociFatturazioni.removeAll();
                datiFatturazioni.removeAll();
                formTotaliFatturazioni.reset();
                mesiPrecedenti.removeAll();
                quoteObbligati.removeAll();
                formDatiFatturazioni.reset();
                // nascondi la view
                this.getFatturazioniNuova().hide();
                this.getFatturazioniNuova().down('#panelFatturaNuova').hide();
                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }
            }
        }

        // controlla che non ci siano modifiche non salvate
        vociFatturazioni.each(function() {
            if (this.dirty) {
                nonSalvati = true;

                // termina il ciclo
                return false;
            }
        });
        if (!nonSalvati) {
            datiFatturazioni.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }
        if (!nonSalvati) {
            mesiPrecedenti.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }
        if (!nonSalvati) {
            quoteObbligati.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
        }

        if (nonSalvati) {
            Ext.Msg.show({
                title: 'Attenzione!',
                msg: 'Chiudendo la finestra le modifiche non salvate andranno perse. Vuoi continuare?',
                buttons: Ext.Msg.YESNO,
                fn: eseguiChiudiPannelloNuovaFattura,
                scope: this,
                icon: Ext.Msg.QUESTION
            });
        }
        else {
            eseguiChiudiPannelloNuovaFattura.call(this);
        }
    },
    /*
     * Action che aggiunge un riga tra le voci della fattura
     */
    aggiungiVoceNuova: function() {
        var voce = Ext.create('Wp.model.fatturazioni.VociFatturazioni', {});
        this.getFatturazioniNuovaVociFatturazioniStore().insert(this.getFatturazioniNuovaVociFatturazioniStore().count(), voce);
    }, //aggiungiVoceNuova

    /*
     * Action che rimuove le righe selezionate dalle voci della fattura
     */
    rimuoviVoceNuova: function() {
        var sm = this.getFatturazioniNuova().down('#vociFatturazioni').getSelectionModel();
        this.getFatturazioniNuovaVociFatturazioniStore().remove(sm.getSelection());
        if (this.getFatturazioniNuovaVociFatturazioniStore().getCount() > 0) {
            sm.select(0);
        }
    }, //rimuoviVoceNuova

    /*
     * Action che setta i valori corretti sulla voce fattura dopo aver
     * selezionato il tipo di servizio
     */
    selezionaTipoServizio: function(editor, e) {
        var record = e.record;
        if (record) {
            var tipoServizio = this.getFatturazioniNuovaListaTipologiaInterventiNuovaStore().getById(record.data.tipo_servizio),
                    quantita = Number(record.data.quantita), importoUnitario, aliquotaIva,percentualeRiduzione;
            log('selezionaTipoServizio : record = ', record, ' , tipoServizio = ', tipoServizio);
            if (tipoServizio) {
                record.set('unita_di_misura', tipoServizio.data.unita_di_misura);
                record.set('importo_unitario', importoUnitario = Number(tipoServizio.data.importo_unitario));
                record.set('tipo_servizio', tipoServizio.data.label);
                record.set('tipo_servizio_value', tipoServizio.data.id);
                record.set('aliquotaIva', aliquotaIva = Number(tipoServizio.data.aliquotaIva));
                record.set('percentualeRiduzione',percentualeRiduzione = Number(tipoServizio.data.percentualeRiduzione));
            } else {
                aliquotaIva = Number(record.get('aliquotaIva'));
                importoUnitario = Number(record.get('importo_unitario'));
            }
            if (quantita) {
                var importoSenzaIva = quantita * importoUnitario,
                        importoConIva = importoSenzaIva * (1 + aliquotaIva);
                log('selezionaTipoServizio : importoConIva = ', importoConIva, ' , importoSenzaIva = ', importoSenzaIva);
                record.set('importo_dovuto', importoSenzaIva);
                record.set('importoConIva', importoConIva);
                record.set('importoSenzaIva', importoSenzaIva);
                this.ricalcolaTotaleFattura.call(this, {
                    'view': this.getFatturazioniNuova(),
                    datiFatturazioni: this.getFatturazioniNuovaDatiFatturazioniStore()
                });
            } //if

        }//if
    }, //selezionaTipoServizio
  
    //funzione di anteprima file 
    anteprimaFile:function(){
    	
  	
	  var shadowStore = this.getFatturazioniFatturazioniShadowStore();
	  var grid = this.getFatturazioniList().down('gridpanel');
      var selectedBanners = grid.getSelectionModel().getSelection(); 
	  var records=[];
	Ext.iterate(selectedBanners,function(record){
     		
     		records.push(record.get('id'));
     }) ;
	  if(records.length == 0){
		  log('record a 0 sto predendo lo shadow store');
			 shadowStore.each(function(record,id){
		       		
		       		records.push(record.get('id'));
		       	}) ; 
	  }
            var confirmWindow = Ext.create('Ext.window.Window',{
                layout: 'anchor',
               // autoHeight:true,
                modal:true,
                items: [{
                        xtype:'datefield',
                        editable:false,
                        format: 'd/m/Y',
                        value : new Date(),
                        fieldLabel: 'Data emissione',
                        name: 'dataEmissioneFattura',
                        id:'dataEmissioneInserita'
                },{
                    xtype:'datefield',
                    editable:false,
                    format: 'd/m/Y',
                    value : new Date(),
                    fieldLabel: 'Data scadenza',
                    name: 'dataScadenzaFattura',
                    id:'dataScadenzaInserita'
            } 
              
                        
                ],
               buttons:[{
            	   text: 'Conferma',
            	   handler:function(){
            		   var dataEmissione= Ext.getCmp('dataEmissioneInserita').getRawValue();
            		   var dataScadenza= Ext.getCmp('dataScadenzaInserita').getRawValue();
            		   log('sto er fare la richiesta');
            	          var myMask = new Ext.LoadMask(grid.getEl(), {msg:"Creazione anteprima in corso.. "});
            	          myMask.show();
            	    	   Ext.Ajax.request({
            	               url: wp_url_servizi.AnteprimaFatturazioni,
            	               timeout:600000,
            	               success: function(response) {
            	                   var risposta = Ext.decode(response.responseText);
            	                   myMask.hide();
            	                   confirmWindow.close();           
            	                   if(risposta.success){
            	                   	  var msg = 'Si è verificato un errore. Contattare l\'amministratore';
            	                   	  Ext.Msg.alert('Operazione fallita', msg);
            	                 
            	                   }
            	                   else {
            	                   if(risposta.fileGenerato){
            	                	   
            	                 	   var url='/GestioneEconomica/ScaricaFileServlet?fileCode='+escape(risposta.fileGenerato);
            	                 	   window.open(url);
            	                 	   
            	                   }
            	                   else {
            	                 	  Ext.Msg.alert('Problemi nella generazione del file ');  
            	                   }
            	                   }
            	               },
            	               failure : function(response){
            	              	 myMask.hide();
            	              	 confirmWindow.close();            	              	 
            	              	
            	                   var msg = 'Si è verificato un errore. Contattare l\'amministratore';
            	                   Ext.Msg.alert('Operazione fallita',msg);
            	                  
            	              },
            	              
            	              params: { data: Ext.encode(records), dataEmissione:dataEmissione,dataScadenza:dataScadenza }  
            	           });
            		    
                     }	   
            		   
            	   
                }],
                title: 'Preview emissione fatture ',
                width: 300,
                height: 200,
                id: 'confirmWindow'
            }).show();
         
       
    
    	
    },

    /*
     * Action che salva una "nuova fattura"
     */
    salvaNuovaFattura: function() {
        var view = this.getFatturazioniNuova(),
                formTotaliFatturazioni = view.down('#totaliFatturazioni').getForm(),
                formDatiFatturazioni = view.down('#datiFatturazioni').getForm(),
                vociFatturazioni = this.getFatturazioniNuovaVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniNuovaDatiFatturazioniStore(),
                stores = [
            vociFatturazioni,
            datiFatturazioni,
            this.getFatturazioniNuovaMesiPrecedentiStore(),
            this.getFatturazioniNuovaQuoteObbligatiStore()
        ],
                vociIsValid = true,
                recordDatiFatturazioni = datiFatturazioni.first();

        // verifica che tutte le voci fattura abbiano un importo valido
        vociFatturazioni.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto')) || Ext.isEmpty(this.get('mese'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (formTotaliFatturazioni.isValid() &&
                formDatiFatturazioni.isValid() &&
                vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store) {
                var toCreate = store.getNewRecords().length > 0,
                        toUpdate = store.getUpdatedRecords().length > 0,
                        toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);
            // eseguo ancora la formattazione della data in formato italiano
//            formDatiFatturazioni.setValues({
//                scadenza : Ext.Date.format(formDatiFatturazioni.getFieldValues().scadenza, 'd/m/Y')
//            });

            //FIXME: se lo store datiFatturazioni non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri

            formDatiFatturazioni.updateRecord(recordDatiFatturazioni);
            // sincronizza tutti gli store
            datiFatturazioni.sync();
        } else {
            var error_message = '';
            error_message = error_message + (vociIsValid ? '' : 'Verificare di avere inserito quantità e mese per ogni voce della fattura!<br/>');
            error_message = error_message + (formTotaliFatturazioni.isValid() ? '' : 'Verificare di aver selezionato iva e bollo!<br/>');
            error_message = error_message + (formDatiFatturazioni.isValid() ? '' : 'Verificare di aver selezionato scadenza e modalità pagamento!');
            Ext.Msg.alert('Attenzione!', error_message);
        }
    },
    /*
     * Action che chiude la schermata per la creazione di una nuova fattura
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiNuovaFattura: function(options) {
        log('chiudiNuovaFattura : inizio');

        var vociFatturazioni = this.getFatturazioniNuovaVociFatturazioniStore(),
                datiFatturazioni = this.getFatturazioniNuovaDatiFatturazioniStore(),
                formTotaliFatturazioni = this.getFatturazioniNuova().down('#totaliFatturazioni').getForm(),
                mesiPrecedenti = this.getFatturazioniNuovaMesiPrecedentiStore(),
                quoteObbligati = this.getFatturazioniNuovaQuoteObbligatiStore(),
                formDatiFatturazioni = this.getFatturazioniNuova().down('#datiFatturazioni').getForm(),
                nonSalvati = false;
        options = options || {};

        function eseguiChiudiNuovaFattura(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociFatturazioni.removeAll();
                datiFatturazioni.removeAll();
                try {
                    formTotaliFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formTotaliFatturazioni.reset error = ', e);
                }
                mesiPrecedenti.removeAll();
                quoteObbligati.removeAll();
                try {
                    formDatiFatturazioni.reset();
                } catch (e) {
                    log('eseguiChiudiNuovaFattura : formDatiFatturazioni.reset error = ', e);
                }
                // nascondi la view
                this.getFatturazioniNuova().hide();
                this.getFatturazioniNuova().down('#panelFatturaNuova').hide();
                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }//if
            }//if
        }//function eseguiChiudiNuovaFattura

        if (options.chiamataDaSalva == undefined)
        {
            // controlla che non ci siano modifiche non salvate
            vociFatturazioni.each(function() {
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
            if (!nonSalvati) {
                datiFatturazioni.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }
            if (!nonSalvati) {
                mesiPrecedenti.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }
            if (!nonSalvati) {
                quoteObbligati.each(function() {
                    if (this.dirty) {
                        nonSalvati = true;

                        // termina il ciclo
                        return false;
                    }
                });
            }

            if (nonSalvati) {
                Ext.Msg.show({
                    title: 'Attenzione!',
                    msg: 'Chiudendo la finestra le modifiche non salvate andranno perse. Vuoi continuare?',
                    buttons: Ext.Msg.YESNO,
                    fn: eseguiChiudiNuovaFattura,
                    scope: this,
                    icon: Ext.Msg.QUESTION
                });
            }// if (nonSalvati)
            else {
                eseguiChiudiNuovaFattura.call(this);
            }

        }//if chiamataDaSalva
        else {
            eseguiChiudiNuovaFattura.call(this);
        }
        log('chiudiNuovaFattura : fine');
    }//chiudiNuovaFattura
});