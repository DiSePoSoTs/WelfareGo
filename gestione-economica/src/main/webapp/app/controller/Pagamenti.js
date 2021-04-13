
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il controller che gestisce le operazioni della sezione Pagamenti
 */

Ext.define('Wp.controller.Pagamenti', {
    extend: 'Ext.app.Controller',

    views: [
    'pagamenti.Form',
    'pagamenti.List',
    'pagamenti.ListDaGenerare',
    'pagamenti.NuovoSelezionato',
    'pagamenti.Nuovo',
    'pagamenti.Modifica',
    'pagamenti.Dettaglio'
    ],

    models: [
    'pagamenti.Pagamenti',
    'pagamenti.PagamentiDaGenerare',
    'pagamenti.VociPagamenti',
    'pagamenti.DatiPagamenti',
    'pagamenti.nuovo.ComboBoxTipologiaInterventiNuovo'
    ],

    stores: [
    'pagamenti.Pagamenti',
    'pagamenti.PagamentiDaGenerare',
    'pagamenti.ModalitaErogazione',
    //shadow store per mandare avanti tutti i pagamenti 
    'pagamenti.PagamentiShadow',
    'pagamenti.PagamentiDaGenerareShadow',
    //nuovo selezionato
    'pagamenti.nuovoselezionato.VociPagamenti',
    'pagamenti.nuovoselezionato.DatiPagamenti',
    //nuovo
    'pagamenti.nuovo.AutocompletePersone',
    'pagamenti.nuovo.VociPagamenti',
    'pagamenti.nuovo.DatiPagamenti',
    'pagamenti.nuovo.ListaTipologiaInterventiNuovo',
    'pagamenti.nuovo.Decreti',
    //dettaglio
    'pagamenti.dettaglio.VociPagamenti',
    'pagamenti.dettaglio.DatiPagamenti'
    ],

    refs: [
    {
        ref: 'pagamentiForm', 
        selector:'wppagamentiform'
    },

    {
        ref: 'pagamentiList', 
        selector: 'wppagamentilist'
    },

    {
        ref: 'pagamentiListDaGenerare', 
        selector: 'wppagamentilistdagenerare'
    },

    {
        ref: 'pagamentiNuovoSelezionato', 
        selector: 'wppagamentinuovoselezionato'
    },

    {
        ref: 'pagamentiNuovo', 
        selector: 'wppagamentinuovo'
    },

    {
        ref: 'pagamentiModifica', 
        selector: 'wppagamentimodifica'
    },

    {
        ref: 'pagamentiDettaglio', 
        selector: 'wppagamentidettaglio'
    }
    ],

    init: function() {
        var pagamentiNuovoSelezionatoStores = [
        this.getPagamentiNuovoselezionatoDatiPagamentiStore(),
        this.getPagamentiNuovoselezionatoVociPagamentiStore()
        ];

        var pagamentiNuovoStores = [
        this.getPagamentiNuovoDatiPagamentiStore(),
        this.getPagamentiNuovoVociPagamentiStore()
        ];

        var pagamentiDettaglioStores = [
        this.getPagamentiDettaglioDatiPagamentiStore(),
        this.getPagamentiDettaglioVociPagamentiStore()
        ];
        // setta le azioni del controller
        this.control({
            // preselezione classe tipologia
            'wppagamentiform combobox[name=classe_intervento]': {
                select: this.filtroTipoIntervento
            },
            // pulsante di ricerca di uno pagamento
            'wppagamentiform button[action=search]': {
                click: this.search
            },
            // pulsante per chiudere i risultati di una ricerca
            'wppagamentiform button[action=back]': {
                click: this.chiudiLista
            },
            // quando clicco su un pagamento della lista "Da generare", apri la
            // finestra per la creazione di una "nuovo pagamento selezionato"
            'wppagamentilistdagenerare gridpanel': {
                itemdblclick: this.nuovoPagamentoSelezionato
            },
            // quando clicco su un pagamento della lista "Da eseguire" , apre la
            // finestra per la modifica di un pagamento
            'wppagamentilist gridpanel': {
                itemdblclick: this.dettaglioPagamentoSelezionato
            },
            // quando clicco su un pagamento della lista "Da generare", apri la
            // finestra per la creazione di una "nuovo pagamento selezionato"
            // Genera Selezionati
            'wppagamentilistdagenerare button[action=save]': {
                click: this.salvaNuoviPagamenti
            },
            // quando clicco su un pagamento della lista "Da generare", apri la
            // finestra per la creazione di una "nuovo pagamento selezionato"
            'wppagamentilistdagenerare button[action=save_all_new]': {
                click: this.salvaTuttiNuoviPagamenti
            },
            // pulsante per aprire la schermata Nuovo Pagamento
            'wppagamentiform button[action=nuovoPagamentoVuoto]': {
                click: this.nuovoPagamentoVuoto
            },
            // pulsante per il salvataggio del nuovo stato dei pagamenti
            'wppagamentilist button[action=save]': {
                click: this.salvaStato
            },
            // pulsante per il salvataggio del nuovo stato dei pagamenti e invio
            //di quegli che sono in attesa d'invio
            'wppagamentilist button[action=save_send]': {
                click: this.salvaStatoInvia
            },
            // pulsante per anteprima file dei pagamenti 
            'wppagamentilist button[action=file_preview]': {
                click: this.anteprimaFile
            },
            // in un "pagamento selezionato", se si modifico le voci
            // "Riduzione" o "Aumento" nelle voci pagamento, ricalcola il totale
            // "Da pagare"
            'wppagamentinuovoselezionato #vociPagamenti': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuovo pagamento
                    // selezionato" su cui lavorare e lo store da cui recuperare
                    // i dati del pagamento
                    this.ricalcolaTotalePagamento.call(this, {
                        view: this.getPagamentiNuovoSelezionato(),
                        datiPagamenti: this.getPagamentiNuovoselezionatoDatiPagamentiStore()
                    });
                }
            },
            // pulsante per chiudere il nuovo pagamento
            'wppagamentinuovoselezionato button[action=back]': {
                click: this.chiudiNuovoPagamentoSelezionato
            },
            // pulsante per salvare una nuovo pagamento selezionato
            'wppagamentinuovoselezionato button[action=save]': {
                click: this.salvaNuovoPagamentoSelezionato
            },
            // in un "pagamento selezionato", se modifico le voci
            // "Riduzione" o "Aumento" nelle voci del vpagamento, ricalcola il
            // totale del pagamento
            'wppagamentimodifica #vociPagamenti': {
                edit: function() {
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuovo pagamento
                    // selezionato" su cui lavorare e lo store da cui recuperare
                    // i dati del pagamento
                    this.ricalcolaTotalePagamento.call(this, {
                        view: this.getPagamentiModifica(),
                        datiPagamenti: this.getPagamentiDettaglioDatiPagamentiStore()
                    });
                }
            },
            // pulsante per chiudere la modifica del pagamento
            'wppagamentimodifica button[action=back]': {
                click: this.chiudiModificaPagamento
            },
            // pulsante per chiudere il dettaglio del pagamento
            'wppagamentidettaglio button[action=back]': {
                click: this.chiudiDettaglioPagamento
            },
            // pulsante per salvare le modifiche di un pagamento
            'wppagamentimodifica button[action=save]': {
                click: this.salvaDettaglioPagamento
            },
            'wppagamentinuovo #autocompletePersone': {
                select: this.creaNuovo
            },
            // pulsante per chiudere il dettaglio del pagamento
            'wppagamentinuovo  button[action=back]': {
                click: this.chiudiNuovoPagamento
            },
            // pulsante per chiudere il dettaglio del pagamento
            'wppagamentinuovo #vociPagamenti-bottoneAggiungi': {
                click: this.aggiungiVoceNuovo
            },
            // pulsante per chiudere il dettaglio del pagamenti
            'wppagamentinuovo #vociPagamenti-bottoneRimuovi': {
                click: this.rimuoviVoceNuovo
            },
            // pulsante per chiudere il dettaglio del pagamento
            'wppagamentinuovo #vociPagamenti': {
                edit: this.selezionaTipoServizio
            },
            // pulsante per chiudere un nuovo pagamento
            'wppagamentinuovo button[action=back]': {
                click: this.chiudiNuovoPagamento
            },
            // pulsante per salvare un nuovo pagamento
            'wppagamentinuovo button[action=save]': {
                click: this.salvaNuovoPagamento
            }
        });

        /*
         * Inizializza gli store
         */
        // inizializza gli store per il pagamento nuovo selezionato
        Ext.each(pagamentiNuovoSelezionatoStores, function(store){
            if (Ext.getClassName(store) == 'Wp.store.pagamenti.nuovoselezionato.DatiPagamenti') {
                // lo store datiPagamenti deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records){
                    // mostra messaggio di loading
                    this.getPagamentiNuovoSelezionato().setLoading(true, true);
                }, this);
                store.addListener('write', function(records){
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getPagamentiNuovoSelezionato());

                    // sincronizza tutti gli altri store
                    Ext.each(pagamentiNuovoSelezionatoStores, function(store){
                        if (Ext.getClassName(store) != 'Wp.store.pagamenti.nuovoselezionato.DatiPagamenti') {
                            store.sync();
                        }
                    });
                }, this);
            }

            else {
                // gli altri store, al termine della sincronizzazione chiamano
                // una callback che conta quanti di loro hanno terminato
                store.addListener('write', function() {
                    this.afterSyncComplete.call(this, this.getPagamentiNuovoSelezionato());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation){
                this.onSyncError.call(this, operation, this.getPagamentiModifica());
            }, this);
        }, this);

        // inizializza gli store per il dettaglio del pagamento
        Ext.each(pagamentiDettaglioStores, function(store){
            if (Ext.getClassName(store) == 'Wp.store.pagamenti.dettaglio.DatiPagamenti') {
                // lo store datiPagamenti deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records){
                    // mostra messaggio di loading
                    this.getPagamentiModifica().setLoading(true, true);
                }, this);
                store.addListener('write', function(records){
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getPagamentiModifica());

                    // sincronizza tutti gli altri store
                    Ext.each(pagamentiDettaglioStores, function(store){
                        if (Ext.getClassName(store) != 'Wp.store.pagamenti.dettaglio.DatiPagamenti') {
                            store.sync();
                        }
                    });
                }, this);
            }

            else {
                // gli altri store, al termine della sincronizzazione chiamano
                // una callback che conta quanti di loro hanno terminato
                store.addListener('write', function() {
                    this.afterSyncComplete.call(this, this.getPagamentiModifica());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation){
                this.onSyncError.call(this, operation, this.getPagamentoModifica());
            }, this);
        }, this);

        // inizializza gli store per la nuovo pagamento vuoto
        Ext.each(pagamentiNuovoStores, function(store){
            if (Ext.getClassName(store) == 'Wp.store.pagamenti.nuovo.DatiPagamenti') {
                // lo store datiPagamenti deve sincronizzarsi per primo; al termine
                // fa sincronizzare anche gli altri store
                store.addListener('beforesync', function(records){
                    // mostra messaggio di loading
                    this.getPagamentiNuovo().setLoading(true, true);
                }, this);
                store.addListener('write', function(records){
                    // richiama la callback che conta i task terminati
                    this.afterSyncComplete.call(this, this.getPagamentiNuovo());

                    // sincronizza tutti gli altri store
                    Ext.each(pagamentiNuovoStores, function(store){
                        if (Ext.getClassName(store) != 'Wp.store.pagamenti.nuovo.DatiPagamenti') {
                            store.each(function() {
                                this.set('id_pagam', records.first().get('id_pagam'));
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
                    this.afterSyncComplete.call(this, this.getPagamentiNuovo());
                }, this);
            }

            // tutti gli store, in caso di errore, visualizzano un messaggio di
            // errore
            store.getProxy().addListener('exception', function(proxy, response, operation){
                this.onSyncError.call(this, operation, this.getPagamentiModifica());
            }, this);
        }, this);
    },


    /*
     * Action per filtrare la lista dei tipi di intervento
     */
    filtroTipoIntervento: function () {
        var comboClasse = this.getPagamentiForm().down('combobox[name=classe_intervento]');
        var comboTipo = this.getPagamentiForm().down('combobox[name=tipo_intervento]');
        comboTipo.reset();
        comboTipo.store.clearFilter();
        comboTipo.store.filter('classe', comboClasse.getValue());
    },

    /*
     * Action che esegue la ricerca dei pagamenti e carica i risultati
     * nella lista
     */
    search: function() {
        log('search');
        this.chiudiLista();
        this.chiudiDettaglioPagamento();
        this.chiudiModificaPagamento();
        this.chiudiNuovoPagamentoSelezionato();
        this.chiudiNuovoPagamento();

        function eseguiRicerca() {
            var form = this.getPagamentiForm().getForm(),
            list = null,
            store = null;
            shadowStore=null;

            if (form.isValid()) {
                // in base al checkbox selezionato, carica la lista e lo store corretti
                if (form.findField('stato_pagamenti').getSubmitValue() == 'da_generare') {
                    list = this.getPagamentiListDaGenerare();
                    store = this.getPagamentiPagamentiDaGenerareStore();
                    shadowStore=this.getPagamentiPagamentiDaGenerareShadowStore();
                }
                else {
                    list = this.getPagamentiList();
                    store = this.getPagamentiPagamentiStore();
                    shadowStore=this.getPagamentiPagamentiShadowStore();
                }
                

                //                store.load({
                //                    params: form.getValues()
                //                });
                store.proxy.extraParams=form.getValues();
                store.loadPage(1,function(){
//                    log('resetting list selection : ', list);
                    list.items.items[0].getSelectionModel().selectAll();
                    list.items.items[0].getSelectionModel().deselectAll();
                });
                shadowStore.proxy.extraParams=form.getValues();

                shadowStore.load();

                list.down('gridpanel').doLayout();
                
                list.show();
            }
        }

        eseguiRicerca.call(this);
    },

 /*   
     * Action che salva il nuovo stato dei pagamenti
     
    salvaStato: function() {
        this.getPagamentiPagamentiStore().sync();
    },// eo detailSave
    
    emissione di tutti i pagamenti*/
 
    salvaStato:function(){
    	 var store = this.getPagamentiPagamentiShadowStore();
    	// var allRecords = store.snapshot || store.data;
    	 var ids = [];
    	 store.each(function(record,id){
     		
     		ids.push(record.get('id'));
     	}) ;
    	 this.inviaPagamenti(ids);
        
    },
    
    // Salvataggio pagamenti nuovi tramite selezione (superpippa)
  salvaNuoviPagamenti: function(){
    	
    	
    	var selectedRowIndexes = [];
      var grid = this.getPagamentiListDaGenerare().down('gridpanel');
    	// returns an array of selected records
    	var selectedBanners = grid.getSelectionModel().getSelection(); 
    	
    	Ext.iterate(selectedBanners,function(record){
    		
    		selectedRowIndexes.push(record.get('id'));
    	}) ;
    		
        this.generaPagamenti(selectedRowIndexes);
    },
    
    // genera tutti i pagamenti presenti nello store 
    salvaTuttiNuoviPagamenti:function(){
    	var store = this.getPagamentiPagamentiDaGenerareShadowStore();
    	
    	// var allRecords = store.snapshot || store.data;
   	 var ids = [];
   	 store.each(function(record,id){
    		
    		ids.push(record.get('id'));
    	}) ;
   	 this.generaPagamenti(ids);
    },
    
    anteprimaFile:function(){
   	 var store = this.getPagamentiPagamentiShadowStore();
   	// var allRecords = store.snapshot || store.data;
   	 var ids = [];
   	 store.each(function(record,id){
    		
    		ids.push(record.get('id'));
    	}) ;
   	 this.previewFile(ids);
       
   },
    
    // genera un gruppo di  o 
   generaPagamenti:function(records){
	   var store = this.getPagamentiPagamentiDaGenerareStore();
	   var shadowStore=  this.getPagamentiPagamentiDaGenerareShadowStore();
   var grid = this.getPagamentiListDaGenerare().down('gridpanel');
   var myMask = new Ext.LoadMask(grid.getEl(), {msg:"Salvataggio in corso... "});
	myMask.show();
   	log('sto per fare la richiesta');
   	// Basic request
   	Ext.Ajax.request({
   	   url:  wp_url_servizi.SalvaNuoviPagamenti,
   	   timeout: 300000, 
   	   success: function(response){
   		   myMask.hide();
   		 var risposta = Ext.decode(response.responseText);
   		 if(risposta.success){
       	  var msg = 'Si è verificato un errore. Contattare l\'amministratore';
       	  Ext.Msg.alert('Operazione fallita', msg);
       }
   		 
   		if(typeof risposta.pagamenti_generati === 'undefined'){
   		 Ext.Msg.alert('Salvataggio FALLITO', 'Si è verificato un problema con uno o più record(codici fiscali mancanti,dati incompleti etc ) , conttattare l \' assistenza.');
   		 }
   		else if (isNaN(risposta.pagamenti_generati)){
   		 Ext.Msg.alert('Salvataggio FALLITO', 'Si è verificato un problema con uno o più record per dati non validi:Problema ,' +risposta.pagamenti_generati);
   		}
   		 else {
   		   Ext.Msg.alert('Salvataggio riuscito', 'Operazione andata a buon fine sono stati generati' +  risposta.pagamenti_generati + ' pagamenti');
   		 }
   		 store.load();
   		 shadowStore.load();
   	   },
   	   failure: function(){
   		   Ext.Msg.alert('Attenzione ', 'Il salvataggio dei record selezionati non è riuscito');
   	   },
   	   params: { data: records }
   	});
	   
   },


    
    /**
     * funzione che invia pagamenti 
     */
    
    inviaPagamenti:function(records){
    	  var store = this.getPagamentiPagamentiStore();
    	  var shadowStore = this.getPagamentiPagamentiShadowStore();
    	  var grid = this.getPagamentiList().down('gridpanel');
    	  function predisponiBottoneDownload(fileCode){
              var url='/GestioneEconomica/ScaricaFileServlet?fileCode='+escape(fileCode);
              var button=Ext.getCmp('scaricaFileGeneratoPagamentiButton');
              button.setHandler(function(){
                  window.open(url);
              });
              button.enable();
          }

      	log('sto er fare la richiesta');
      	var myMask = new Ext.LoadMask(grid.getEl(), {msg:"Salvataggio in corso... "});
      	myMask.show();
        Ext.Ajax.request({
            url: wp_url_servizi.InviaPagamenti,
            timeout:600000,
            success: function(response) {
                var risposta = Ext.decode(response.responseText);
                myMask.hide();
                //èarso il json per vedere se mi ha tornato un fallimento 
                if(risposta.success){
                	  var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                	  Ext.Msg.alert('Operazione fallita', msg);
                }
                else {
                if(risposta.fileGenerato){
                    predisponiBottoneDownload(risposta.fileGenerato);
                }
               
                Ext.Msg.alert('Salvataggio riuscito', 'Operazione andata a buon fine: numero di pagamenti inviati con successo '+ risposta.pagamenti_inviati);
             //   alert ('Numero di pagamenti inviati con successo: ' + risposta.pagamenti_inviati);
            store.load();
            shadowStore.load();
            }
            },
            failure : function(response){
            	 myMask.hide();
            	
                 var msg = 'Si è verificato un errore. Contattare l\'amministratore';
                 Ext.Msg.alert('Operazione fallita',msg);
                
            },
        params: { data: Ext.encode(records) }
        });

      	
    },
    
    previewFile: function(records){
    	
    	  var grid = this.getPagamentiList().down('gridpanel');
    	  var myMask = new Ext.LoadMask(grid.getEl(), {msg:"Generazione in corso... "});
    	  myMask.show();
    	  Ext.Ajax.request({
              url: wp_url_servizi.anteprimaFilePagamenti,
              timeout:600000,
              success:function(response){
            	  var risposta = Ext.decode(response.responseText);
                  myMask.hide();
                  //èarso il json per vedere se mi ha tornato un fallimento 
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
    	  params: { data: Ext.encode(records) }
    	  });
    },
    /*
     * Action che salva il nuovo stato dei pagamenti
     * le invia
     */
    salvaStatoInvia: function() {
             
    	var selectedRowIndexes = [];
        var grid = this.getPagamentiList().down('gridpanel');
    	// returns an array of selected records
    	var selectedBanners = grid.getSelectionModel().getSelection(); 
    	
    	Ext.iterate(selectedBanners,function(record){
    		
    		selectedRowIndexes.push(record.get('id'));
    	}) ;
    	this.inviaPagamenti(selectedRowIndexes);
    		
    	
    	
        

    },

    /*
     * Visualizza la schermata del nuovo pagamento.
     */
    nuovoPagamentoVuoto: function() {

        this.chiudiDettaglioPagamento();
        this.chiudiModificaPagamento();
        this.chiudiNuovoPagamentoSelezionato();
        this.chiudiLista();

        var view = this.getPagamentiNuovo();
        function apriNuovoPagamento()
        {
            view.show();//.setLoading(true, true);
        }

        function afterLoadingComplete() {
            view.setLoading(false, true);
        }// eo afterLoadingComplete
        // chiudi l'eventuale pagamento già aperta
        if (!view.isHidden()) {
        //            this.chiudiNuovoPagamento({
        //                success: apriNuovoPagamento
        //            });
        }
        else {
            apriNuovoPagamento.call(this);
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
        options = options || {};
        var pagamentiNuovoSelezionatoVisibile =!this.getPagamentiNuovoSelezionato().isHidden();
        var pagamentiDettaglioVisibile = !this.getPagamentiDettaglio().isHidden();
        log('chiudiLista pagamentiNuovoSelezionatoVisibile: ', pagamentiNuovoSelezionatoVisibile, 'pagamentiDettaglioVisibile: ', pagamentiDettaglioVisibile );



        function eseguiChiudiLista() {
            var pagamentiListVisible = !this.getPagamentiList().isHidden();
            var pagamentiListDaGenerare = !this.getPagamentiListDaGenerare().isHidden();
            var pagamentiNuovoVisible  = !this.getPagamentiNuovo().isHidden();

            log('pagamentiListVisible: ', pagamentiListVisible, 'pagamentiListDaGenerare: ', pagamentiListDaGenerare, 'pagamentiNuovoVisible: ', pagamentiNuovoVisible);

            // chiudi l'eventuale lista dei pagamenti
            if ( pagamentiListVisible ) {
                this.getPagamentiPagamentiStore().removeAll();
                this.getPagamentiPagamentiShadowStore().removeAll();
                this.getPagamentiList().hide();
            }
            // chiudi l'eventuale lista dei pagamenti da generare
            if ( pagamentiListDaGenerare ) {
                this.getPagamentiPagamentiDaGenerareStore().removeAll();
                this.getPagamentiPagamentiDaGenerareShadowStore().removeAll();
                this.getPagamentiListDaGenerare().hide();
            }
            // chiudi l'eventuale schermata nuovo pagamento
            if (pagamentiNuovoVisible) {
                this.getPagamentiNuovoAutocompletePersoneStore().removaAll();
                this.getPagamentiNuovo().hide();
            }
        }//eseguiChiudiLista


        // chiudi l'eventuale schermata nuovo pagamento selezionato
        if (pagamentiNuovoSelezionatoVisibile) {
        log('chiudiLista getPagamentiNuovoSelezionato' );
            this.chiudiNuovoPagamentoSelezionato();
            eseguiChiudiLista.call(this)
        }
        // chiudi l'eventuale schermata dettaglio pagamento
        else if (pagamentiDettaglioVisibile) {
        log('chiudiLista getPagamentiDettaglio' );
            this.chiudiDettaglioPagamento();
            eseguiChiudiLista.call(this)
        }
        // chiudi l'eventuale dettaglio aperto
        //this.detailBack();
        else {
            eseguiChiudiLista.call(this);
        }
    },

    /*
     * Action che visualizza la schermata per la creazione di una nuovo pagamento
     * partendo da uno selezionato dalla lista "Da Generare"
     */
    nuovoPagamentoSelezionato: function(grid,record) {
        log('nuovoPagamentoSelezionato con filtro stato ___Da generare___');
        var view = this.getPagamentiNuovoSelezionato();

        this.chiudiDettaglioPagamento();
        this.chiudiModificaPagamento();
        this.chiudiNuovoPagamento();

        function apriNuovoPagamentoSelezionato() {
        log('nuovoPagamentoSelezionato.apriNuovoPagamentoSelezionato con filtro stato ___Da generare___');
            var list = this.getPagamentiListDaGenerare().down('gridpanel'),
            selected = record;
            //  selected = list.getSelectionModel().getLastSelected(),
            taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted >= 2) {
                    // quando tutti i dati del pagamento sono caricati, ricalcola
                    // i totali e disabilita il messaggio di loading
                    //
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuovo pagamento
                    // selezionato" su cui lavorare e lo store da cui recuperare
                    // i dati dei pagamenti
                    this.ricalcolaTotalePagamento.call(this, {
                        'view': view,
                        datiPagamenti: this.getPagamentiNuovoselezionatoDatiPagamentiStore()
                    });

                    view.setLoading(false, true);
                }
            }// eo afterLoadingComplete

          
          
                // mostra il pagamentoe visualizza il messaggio di loading
                    view.show().setLoading(true, true);

                // carica i dati per un nuovo pagamento
                this.getPagamentiNuovoselezionatoVociPagamentiStore().proxy.extraParams={
                    id: selected.getId()
                };
                this.getPagamentiNuovoselezionatoVociPagamentiStore().load({
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getPagamentiNuovoselezionatoDatiPagamentiStore().proxy.extraParams={
                    id: selected.getId()
                };
                this.getPagamentiNuovoselezionatoDatiPagamentiStore().load({
                    scope: this,
                    callback: function(records,operation,success) {
                        // inserisci nelle form i dati recuperati
                        view.down('#decretoImpegno').loadRecord(records[0]);
                        view.down('#datiPagamenti').loadRecord(records[0]);

// questo blocco fa solo casino, preselezione per_cassa/accredito fatta lato server . . 
//                        //se iban non definito selezione modalità di
//                        //erogazione è obbligata
//                        if (records[0].get('iban_beneficiario') == undefined || records[0].get('iban_beneficiario') == null || records[0].get('iban_beneficiario') == '') {
//                            view.down('#modalita_erogazione').store.filter('id', 'PER_CASSA');
//                        }
//                        else {
//                            //riabilito la editabilità se per caso è stata disattivata in qualche
//                            //precedente caricamento dei dati di pagamento
//                            view.down('#modalita_erogazione').store.clearFilter();
//                        }
                        afterLoadingComplete.call(this);
                    }
                });
            }
        // eo apriNuovoPagamentoSelezionato

        // chiudi l'eventuale pagamentogià aperta
        if (!view.isHidden()) {
            this.chiudiNuovoPagamentoSelezionato({
                success: apriNuovoPagamentoSelezionato
            });
        }
        else {
            apriNuovoPagamentoSelezionato.call(this);
        }
    },

    /*
     * Action che chiude la schermata per la creazione di un nuovo pagamento
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiNuovoPagamentoSelezionato: function(options) {
        var vociPagamenti = this.getPagamentiNuovoselezionatoVociPagamentiStore(),
        datiPagamenti = this.getPagamentiNuovoselezionatoDatiPagamentiStore(),
        formDecretoImpegno = this.getPagamentiNuovoSelezionato().down('#decretoImpegno').getForm(),
        formDatiPagamenti = this.getPagamentiNuovoSelezionato().down('#datiPagamenti').getForm(),
        nonSalvati = false;
        options = options || {};

        function eseguiChiudiNuovoPagamentoSelezionato(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociPagamenti.removeAll();
                datiPagamenti.removeAll();
                formDecretoImpegno.reset();
                formDatiPagamenti.reset();
                // nascondi la view
                this.getPagamentiNuovoSelezionato().hide();

                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }
            }
        }

        if (options.chiamataDaSalva == undefined)
        {

            // controlla che non ci siano modifiche non salvate
            vociPagamenti.each(function(){
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
            if (!nonSalvati) {
                datiPagamenti.each(function(){
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
                    fn: eseguiChiudiNuovoPagamentoSelezionato,
                    scope: this,
                    icon: Ext.Msg.QUESTION
                });
            }
            else {
                eseguiChiudiNuovoPagamentoSelezionato.call(this);
            }
        }//if chiamataDaSalva
        else
        {
            eseguiChiudiNuovoPagamentoSelezionato.call(this);
        }


        if(options.success){
            log('options.success 2: ', options.success);
            options.success.call(this);
        }
    },

    /*
     * Action che salva un "nuovo pagamento selezionato"
     */
    salvaNuovoPagamentoSelezionato: function() {
        var view = this.getPagamentiNuovoSelezionato(),
        formDecretoImpegno = view.down('#decretoImpegno').getForm(),
        formDatiPagamenti = view.down('#datiPagamenti').getForm(),
        vociPagamenti = this.getPagamentiNuovoselezionatoVociPagamentiStore(),
        datiPagamenti = this.getPagamentiNuovoselezionatoDatiPagamentiStore(),
        stores = [
        vociPagamenti,
        datiPagamenti
        ],
        vociIsValid = true,
        recordDatiPagamenti = datiPagamenti.first();

        // verifica che tutte le voci del pagamento abbiano un importo valido
        vociPagamenti.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (formDecretoImpegno.isValid() &&
            formDatiPagamenti.isValid() &&
            vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store){
                var toCreate  = store.getNewRecords().length > 0,
                toUpdate  = store.getUpdatedRecords().length > 0,
                toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);

            //FIXME: se lo store datiPagamenti non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri

            formDatiPagamenti.updateRecord(recordDatiPagamenti);
            // sincronizza tutti gli store
            datiPagamenti.sync();
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
        Ext.Msg.alert('Salvataggio', 'Modifiche salvate con successo.', function(){
            if(Ext.getClassName(view) == 'Wp.view.pagamenti.Nuovo' )
            {
                this.chiudiNuovoPagamento({
                    chiamataDaSalva:true
                });
            }
            if(Ext.getClassName(view) == 'Wp.view.pagamenti.NuovoSelezionato' )
            {
                this.chiudiNuovoPagamentoSelezionato({
                    chiamataDaSalva:true
                });
            }
        },this);//Ext.Msg.alert
        // nascondi messaggio di loading
        view.setLoading(false, true);
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
     * Action che visualizza la schermata per la visualizzazione di un pagamento
     * partendo da uno selezionato dalla lista "Da Emettere"
     */
    dettaglioPagamentoSelezionato: function(grid,record) {

        this.chiudiModificaPagamento();
        this.chiudiNuovoPagamentoSelezionato();
        this.chiudiNuovoPagamento();

        var selected = record;


        //Seleziono il tipo di view in base allo stato del documento
        var view;
        switch (selected.data.stato)
        {
            //dettaglio modificabile
            case 'da emettere':
            case 'emesse':
                view = this.getPagamentiModifica();
                break;

            //Dettaglio non modificabile
            case 'da inviare':
            case 'inviata':
            case 'pagate':
                view = this.getPagamentiDettaglio();
                break;
        }//switch

        function apriPagamentoSelezionato(selected) {

            log('apriPagamentoSelezionato selected:', selected);

            var taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted >= 2) {
                    // quando tutti i dati del pagamento sono caricati, ricalcola
                    // i totali e disabilita il messaggio di loading
                    //
                    // chiama il metodo per ricalcolare il totale usando il
                    // controller come scope e passando la view "nuovo pagamento
                    // selezionato" su cui lavorare e lo store da cui recuperare
                    // i dati del pagamento
                    this.ricalcolaTotalePagamento.call(this, {
                        'view': view,
                        datiPagamenti: this.getPagamentiDettaglioDatiPagamentiStore()
                    });

                    view.setLoading(false, true);
                }
            }// eo afterLoadingComplete

            // controlla che ci sia almeno una riga selezionata
//            if (!list.getSelectionModel().hasSelection()) {
//                Ext.Msg.alert('Attenzione!', 'Devi selezionare almeno un pagamento.');
//            }
//            else {

                // mostra il pagamento e visualizza il messaggio di loading
                view.show().setLoading(true, true);

                // carica i dati per il pagamento
                this.getPagamentiDettaglioVociPagamentiStore().proxy.extraParams={
                    id: selected.getId()
                };
                this.getPagamentiDettaglioVociPagamentiStore().load({
                    scope: this,
                    callback: afterLoadingComplete
                });

                this.getPagamentiDettaglioDatiPagamentiStore().proxy.extraParams={
                    id: selected.getId()
                };
                this.getPagamentiDettaglioDatiPagamentiStore().load({
                    scope: this,
                    callback: function(records, operation, success) {
                        // inserisci nelle form i dati recuperati
                        view.down('#decretoImpegno').loadRecord(records[0]);
                        view.down('#datiPagamenti').loadRecord(records[0]);

                        //se iban non definito selezione modalità di
                        //erogazione è obbligata
                        if (records[0].get('iban_beneficiario') == undefined || records[0].get('iban_beneficiario') == null || records[0].get('iban_beneficiario') == '') {
                            view.down('#modalita_erogazione').store.filter('id', 'PER_CASSA');
                        }
                        else {
                            //riabilito la editabilità se per caso è stata disattivata in qualche
                            //precedente caricamento dei dati di pagamento
                            view.down('#modalita_erogazione').store.clearFilter();
                        }
                        afterLoadingComplete.call(this);
                    }
                });
//            }
        }// eo apriPagamentoSelezionato

        // chiudi l'eventuale pagamento già aperto
        if (!view.isHidden()) {
            this.chiudiModificaPagamento({
                success: apriPagamentoSelezionato,
                selected: selected
            });
        }
        else {
            apriPagamentoSelezionato.call(this, selected);
        }
    },//dettaglioPagamentoSelezionato

    /*
     * Action che chiude la schermata per visuailzzare il dettaglio del pagamento
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiModificaPagamento: function(options) {
        var vociPagamenti = this.getPagamentiDettaglioVociPagamentiStore(),
        datiPagamenti = this.getPagamentiDettaglioDatiPagamentiStore(),
        formDecretoImpegno = this.getPagamentiModifica().down('#decretoImpegno').getForm(),
        formDatiPagamenti = this.getPagamentiModifica().down('#datiPagamenti').getForm(),
        nonSalvati = false;
        options = options || {};

        // controlla che non ci siano modifiche non salvate
        vociPagamenti.each(function(){
            if (this.dirty) {
                nonSalvati = true;

                // termina il ciclo
                return false;
            }
        });
        if (!nonSalvati) {
            datiPagamenti.each(function(){
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
                fn: eseguiChiudiModificaPagamento,
                scope: this,
                icon: Ext.Msg.QUESTION
            });
        }
        else {
            eseguiChiudiModificaPagamento.call(this);
        }


        function eseguiChiudiModificaPagamento(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociPagamenti.removeAll();
                datiPagamenti.removeAll();
                formDecretoImpegno.reset();
                formDatiPagamenti.reset();
                // nascondi la view
                this.getPagamentiModifica().hide();

                if (typeof options.success != 'undefined') {
                    options.success.call(this, options.selected);
                }
            }//if
        }//eseguiChiudiModificaPagamenti

    },//chiudiModificaPagamenti

    /**
     * Chiude il dettaglio del pagamento
     */
    chiudiDettaglioPagamento: function(options){
        options = options || {};
        this.getPagamentiDettaglio().hide();
        if(options.success){
            log('option.success 3:', options.success);
            options.success.call(this);
        }
    },//chiudiDettaglioPagamento

    /**
     * Metodo per aggiornare i dati di un pagamento già presente nel sistema.
     */
    salvaDettaglioPagamento: function () {
        var view = this.getPagamentiModifica(),
        formDecretoImpegno = view.down('#decretoImpegno').getForm(),
        formDatiPagamenti = view.down('#datiPagamenti').getForm(),
        vociPagamenti = this.getPagamentiDettaglioVociPagamentiStore(),
        datiPagamenti = this.getPagamentiDettaglioDatiPagamentiStore(),
        stores = [
        vociPagamenti,
        datiPagamenti
        ],
        vociIsValid = true,
        recordDatiPagamenti = datiPagamenti.first();

        // verifica che tutte le voci del pagamento abbiano un importo valido
        vociPagamenti.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (//formDatiPagamenti.isValid() &&
            formDecretoImpegno.isValid() &&
            vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store){
                var toCreate  = store.getNewRecords().length > 0,
                toUpdate  = store.getUpdatedRecords().length > 0,
                toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);

            //FIXME: se lo store datiPagamenti non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri

            formDatiPagamenti.updateRecord(recordDatiPagamenti);
            // sincronizza tutti gli store
            datiPagamenti.sync();
        }
    },

    /*
     * Ricalcola il "Totale pagamento" all'interno di una nuovo pagamento
     *
     * @param Object params: contiene i parametri rappresentanti il contesto in
     *     cui la funzione deve operare:
     *     - view: la view da cui recuperare i dati collegati (voci)
     *     - datiPagamento: lo store da cui recuperare i dati del pagamento
     */
    ricalcolaTotalePagamento: function(params) {
        var voci = params.view.down('#vociPagamenti').getStore(),
        totale = 0,
        form = params.view.down('#datiPagamenti').getForm(),
        record = params.datiPagamenti.first();

        // somma le varie voci del pagamento
        voci.each(function(){
            var importo = Ext.isEmpty(this.get('importo_dovuto')) ? 0 : this.get('importo_dovuto'),
            aumento = Ext.isEmpty(this.get('aumento')) ? 0 : this.get('aumento'),
            riduzione = Ext.isEmpty(this.get('riduzione')) ? 0 : this.get('riduzione'),
            variazione = Ext.isEmpty(this.get('variazione_straordinaria')) ? 0 : this.get('variazione_straordinaria');
            var importo_consuntivato = this.get('importo_consuntivato');

//            if (!Ext.isEmpty(importo_consuntivato) && importo_consuntivato < 0){
//                totale += importo_consuntivato;
//            }

            totale += importo + aumento - riduzione + variazione;
        });
        
        //var giorniAssenza=Number(voci.first().get('assenze_totali'))||0;
        
        //TODO

        // scrivi il totale pagamento nella form
        form.setValues({
            da_liquidare: totale
        });

        // aggiorna il record per permettere il futuro salvataggio
        form.updateRecord(record);
    },

    /**
     * La funzione viene chiamata quando l'utente esegue la scelta dell'
     * anagrafica alla quale vuovle creare una nuovo pagamento.
     * @param Object event: evento (la selezione dell'utente)
     * @param Object el: elemento selezionato
     * @param Object e:
     */
    creaNuovo: function(event ,el , o)
    {

        var view = this.getPagamentiNuovo().down('#panelPagamentoNuovo');

        function apriNuovoPagamento() {
            var taskCompleted = 0;

            function afterLoadingComplete() {
                if (++taskCompleted >= 3) {
                    view.setLoading(false, true);
                }
            }// eo afterLoadingComplete

            // controlla che ci sia almeno una riga selezionata
            if (el==undefined || el[0]==undefined || el[0].data==undefined || el[0].data.id==undefined) {
                Ext.Msg.alert('Attenzione!', 'Devi selezionare almeno un\'anagrafica valida.');
            }
            else {
                var selected = el[0].data.id;
                // mostra il pagamento e visualizza il messaggio di loading
                view.show().setLoading(true, true);
                
                this.getPagamentiNuovoDatiPagamentiStore().proxy.extraParams={
                    id: selected    
                };
                this.getPagamentiNuovoDatiPagamentiStore().load({
                    scope: this,
                    callback: function(records,operation,success) {
                        // inserisci nelle form i dati recuperati
                        view.down('#decretoImpegno').loadRecord(records[0]);
                        view.down('#datiPagamenti').loadRecord(records[0]);

                        //se iban non definito selezione modalità di
                        //erogazione è obbligata
                        if (records[0].get('iban_beneficiario') == undefined || records[0].get('iban_beneficiario') == null || records[0].get('iban_beneficiario') == '') {
                            view.down('#modalita_erogazione').store.filter('id', 'PER_CASSA');
                        }
                        else {
                            //riabilito la editabilità se per caso è stata disattivata in qualche
                            //precedente caricamento dei dati di pagamento
                            view.down('#modalita_erogazione').store.clearFilter();
                        }

                        afterLoadingComplete.call(this);
                    }
                });

                this.getPagamentiNuovoListaTipologiaInterventiNuovoStore().proxy.extraParams = {
                    id: selected
                };
                this.getPagamentiNuovoListaTipologiaInterventiNuovoStore().load({
                    scope: this,
                    callback: afterLoadingComplete
                });
                this.getPagamentiNuovoDecretiStore().proxy.extraParams = {
                    id: selected
                };
                this.getPagamentiNuovoDecretiStore().load({
                    scope: this,
                    callback: afterLoadingComplete
                });
            }
        }// eo apriNuovoPagamento

        // chiudi l'eventuale pagamento già aperta
        if (!view.isHidden()) {
            this.chiudiNuovoPagamento({
                success: apriNuovoPagamento
            });
        }
        else {
            apriNuovoPagamento.call(this);
        }
    },

    /*
     * Action che chiude il pannello con il dettaglio nella schermata per la
     * creazione di una nuovo pagamento
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiPannelloNuovoPagamento: function(options) {
        var vociPagamenti = this.getPagamentiNuovoVociPagamentiStore(),
        datiPagamenti = this.getPagamentiDettaglioDatiPagamentiStore(),
        formDecretoImpegno = this.getPagamentiDettaglio().down('#decretoImpegno').getForm(),
        formDatiPagamenti = this.getPagamentiDettaglio().down('#datiPagamenti').getForm(),
        nonSalvati = false;
        options = options || {};

        function eseguiChiudiPannelloNuovoPagamento(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociPagamenti.removeAll();
                datiPagamenti.removeAll();
                formDecretoImpegno.reset();
                formDatiPagamenti.reset();
                // nascondi la view
                this.getPagamentiNuovo().down('#panelPagamentoNuovo').hide();
                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }
            }
        }

        // controlla che non ci siano modifiche non salvate
        vociPagamenti.each(function(){
            if (this.dirty) {
                nonSalvati = true;

                // termina il ciclo
                return false;
            }
        });
        if (!nonSalvati) {
            datiPagamenti.each(function(){
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
                fn: eseguiChiudiPannelloNuovoPagamento,
                scope: this,
                icon: Ext.Msg.QUESTION
            });
        }
        else {
            eseguiChiudiPannelloNuovoPagamento.call(this);
        }
    },

    /*
     * Action che aggiunge un riga tra le voci del pagamento
     */
    aggiungiVoceNuovo: function(){
        var voce = Ext.create('Wp.model.pagamenti.VociPagamenti',{});
        this.getPagamentiNuovoVociPagamentiStore().insert(this.getPagamentiNuovoVociPagamentiStore().count(), voce);
    },//aggiungiVoceNuovo

    /*
     * Action che rimuove le righe selezionate dalle voci del pagamento
     */
    rimuoviVoceNuovo: function(){
        var sm = this.getPagamentiNuovo().down('#vociPagamenti').getSelectionModel();
        this.getPagamentiNuovoVociPagamentiStore().remove(sm.getSelection());
        if (this.getPagamentiNuovoVociPagamentiStore().getCount() > 0) {
            sm.select(0);
        }
    },//rimuoviVoceNuovo

    /*
     * Action che setta i valori corretti sulla voce del pagamento dopo aver
     * selezionato il tipo di servizio
     */
    selezionaTipoServizio: function(editor,e){
        var record = e.record;
        if (record != undefined) {
            var tipoServizio = this.getPagamentiNuovoListaTipologiaInterventiNuovoStore().getById(record.data.tipo_servizio);
            if (tipoServizio != undefined) {
                record.set('unita_di_misura', tipoServizio.data.unita_di_misura);
                record.set('importo_unitario', tipoServizio.data.importo_unitario);
                record.set('tipo_servizio',  tipoServizio.data.label);
                record.set('tipo_servizio_value',  tipoServizio.data.id);
            }
            if (record.data.quantita != undefined && record.data.quantita != 0) {
                record.set('importo_dovuto', record.data.quantita * record.data.importo_unitario);
                this.ricalcolaTotalePagamento.call(this, {
                    'view': this.getPagamentiNuovo(),
                    datiPagamenti: this.getPagamentiNuovoDatiPagamentiStore()
                });
            } //if
        }//if
    },//selezionaTipoServizio

    /*
     * Action che salva uno "nuovo pagamento"
     */
    salvaNuovoPagamento: function() {
        var view = this.getPagamentiNuovo(),
        formDecretoImpegno = view.down('#decretoImpegno').getForm(),
        formDatiPagamenti = view.down('#datiPagamenti').getForm(),
        vociPagamenti = this.getPagamentiNuovoVociPagamentiStore(),
        datiPagamenti = this.getPagamentiNuovoDatiPagamentiStore(),
        stores = [
        vociPagamenti,
        datiPagamenti
        ],
        vociIsValid = true,
        recordDatiPagamenti = datiPagamenti.first();

        // verifica che tutte le voci del pagamento abbiano un importo valido
        vociPagamenti.each(function() {
            if (Ext.isEmpty(this.get('importo_dovuto')) || Ext.isEmpty(this.get('mese'))) {
                vociIsValid = false;

                // termina il loop
                return false;
            }
        });

        // verifica che i dati delle form siano corretti
        if (formDecretoImpegno.isValid() &&
            formDatiPagamenti.isValid() &&
            vociIsValid) {
            // prepara le variabili necessarie per capire se tutte le
            // sincronizzazioni terminano
            this.syncTaskCompleted = 0;
            this.totalSyncTasks = 0;

            // verifica quanti store necessitano sincronizzazione
            Ext.each(stores, function(store){
                var toCreate  = store.getNewRecords().length > 0,
                toUpdate  = store.getUpdatedRecords().length > 0,
                toDestroy = store.getRemovedRecords().length > 0;

                if (toCreate || toUpdate || toDestroy) {
                    this.totalSyncTasks++;
                }
            }, this);

            formDatiPagamenti.setValues({
                'decreto_impegno': formDecretoImpegno.getFieldValues().decreto_impegno
            });

            //FIXME: se lo store datiPagamenti non è da sincronizzare, nemmeno
            // gli altri si sincronizzeranno!!! Dovrei fare un controllo ed
            // eventualmente richiamare qui la sincronizzazione degli altri

            formDatiPagamenti.updateRecord(recordDatiPagamenti);
            // sincronizza tutti gli store
            datiPagamenti.sync();
        }else
        {
            var error_message = '';
            error_message = error_message + (vociIsValid ? '':'Verificare di avere inserito quantità e mese per ogni voce del pagamento!<br/>');
            error_message = error_message + (formDecretoImpegno.isValid() ? '':'Verificare di aver selezionato il decreto d\'impegno!<br/>');
            error_message = error_message + (formDatiPagamenti.isValid() ? '':'Verificare di aver selezionato la modalità di erogazione!');
            Ext.Msg.alert('Attenzione!', error_message);
        }
    },

    /*
     * Action che chiude la schermata per la creazione di un nuovo pagamento
     *
     * @param Object options: un oggetto contenente varie opzioni utilizzate
     * - success: callback da eseguire in caso la chiusura vada a buon fine; viene
     *   eseguita nello scope del controller, non accetta nessun parametro
     */
    chiudiNuovoPagamento: function(options) {
        var vociPagamenti = this.getPagamentiNuovoVociPagamentiStore(),
        datiPagamenti = this.getPagamentiNuovoDatiPagamentiStore(),
        formDecretoImpegno = this.getPagamentiDettaglio().down('#decretoImpegno').getForm(),
        formDatiPagamenti = this.getPagamentiDettaglio().down('#datiPagamenti').getForm(),
        nonSalvati = false;
        options = options || {};

        function eseguiChiudiNuovoPagamento(button) {
            if (Ext.isEmpty(button) || button == 'yes') {
                // elimina i dati
                vociPagamenti.removeAll();
                datiPagamenti.removeAll();
                formDecretoImpegno.reset();
                formDatiPagamenti.reset();
                // nascondi la view
                this.getPagamentiNuovo().hide();
                this.getPagamentiNuovo().down('#panelPagamentoNuovo').hide();
                if (typeof options.success != 'undefined') {
                    options.success.call(this);
                }//if
            }//if
        }//function eseguiChiudiNuovoPagamento

        if (options.chiamataDaSalva == undefined)
        {
            // controlla che non ci siano modifiche non salvate
            vociPagamenti.each(function(){
                if (this.dirty) {
                    nonSalvati = true;

                    // termina il ciclo
                    return false;
                }
            });
            if (!nonSalvati) {
                datiPagamenti.each(function(){
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
                    fn: eseguiChiudiNuovoPagamento,
                    scope: this,
                    icon: Ext.Msg.QUESTION
                });
            }// if (nonSalvati)
            else {
                eseguiChiudiNuovoPagamento.call(this);
            }

        }//if chiamataDaSalva
        else {
            eseguiChiudiNuovoPagamento.call(this);
        }
    }//chiudiNuovoPagamento
});