Ext.define('wcs.view.pai.InterventoFamigliaList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventofamiglialist',
    title: 'Nucleo familiare sociale',
    store: 'FamigliaSocialeStore',
    selType: 'checkboxmodel',
    //    loadMask: true,
    //    autoScroll:true,
    //bindForm: 'wcs_famigliaSocialeForm',
   /* bbar: {
        xtype: 'wcs_famigliabar',
        store: 'FamigliaSocialeStore'
    },*/
    initComponent: function() {
    	
    	  this.dockedItems = [{
              xtype: 'wcs_famigliabar',
              store: 'FamigliaSocialeStore',
              dock: 'bottom',
              displayInfo: true
             
          }];
        this.columns = [{
                header: 'Cognome',
                dataIndex: 'cognome',
                sortable: true,
                flex: 1
            },{
                header: 'Nome',
                dataIndex: 'nome',
                sortable: true,
                flex: 1
            },  {
                header: 'Relazione',
                dataIndex: 'desQual',
                sortable: false,
                flex: 1
            }, {
                header: 'Codice fiscale',
                dataIndex: 'codiceFiscale',
                sortable: true,
                width: 120
            }, {
                header: 'Comune di nascita',
                dataIndex: 'comuneNascitaDes',
                sortable: false,
                flex: 1
            }, {
                header: 'Sesso',
                dataIndex: 'sesso',
                hidden: false,
                sortable: true,
                width: 50
            }, {
                header: 'Data di nascita',
                dataIndex: 'dataNascita',
                hidden: false,
                sortable: true,
                width: 80
            }, {
                header: 'Comune di residenza',
                dataIndex: 'comuneResidenzaDes',
                sortable: false,
                flex: 1
            }, {
                header: 'Codice anagrafica famigliare',
                dataIndex: 'codAnaFamigliare',
                hidden: true,
                sortable: false
            }, {
                header: 'Codice anagrafica',
                dataIndex: 'codAnag',
                hidden: true,
                sortable: false
            }, {
                xtype: 'numbercolumn',
                format: '0,000.00 €',
                header: 'Entrate ultimi 2 mesi',
                dataIndex: 'redditoFamiliare',
                hidden: true,
                sortable: true
            }, {
                header: 'aggiornate al',
                dataIndex: 'dataAggiornamentoRedditoFamiliare',
                hidden: true,
                sortable: true
            }];

        this.callParent(arguments);
    },
  listeners:{
	  selectionchange:function(component){
		  //log('bla bla');
		  this.up('widget.wcs_interventiform').aggiornaCosto(component);
		  
	  }
  }
   

});