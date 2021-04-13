Ext.define('wcs.view.famiglia.FamigliaSocialeList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_famigliasocialelist',
    title: 'Nucleo familiare sociale',
    store: 'FamigliaSocialeStore',
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
        this.columns = [
        {
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
            },{
                header: 'Comune di residenza',
                dataIndex: 'comuneResidenzaDes',
                sortable: false,
                flex: 1
            },
            {
                header: 'Pai',
                dataIndex: 'flgPai',
                sortable: false,
                flex: 1
            },
            {
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
            },{
                xtype:'actioncolumn',
                width: 30,
                items: [{
                    getClass:function(a,b,record){
                        return record.data.flgPai=='No'?'wcs_print_icon_disabled':'wfg_cs_icon';
                    },
                    tooltip: 'Apri cartella',
                    handler: function(grid, rowIndex, colIndex) {
                        var record = grid.getStore().getAt(rowIndex);
                        if(window.ricaricaAnagraficaDaPai) {
                            ricaricaAnagraficaDaPai(record.data.pai);
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

        this.callParent(arguments);
    },
    onDeleteClick: function() {
        var selection = this.getView().getSelectionModel().getSelection()[0];
        if (selection) {
            this.store.remove(selection);
        }
    }

});