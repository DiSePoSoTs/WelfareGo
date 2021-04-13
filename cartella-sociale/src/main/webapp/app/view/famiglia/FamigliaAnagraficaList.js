Ext.define('wcs.view.famiglia.FamigliaAnagraficaList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_famigliaanagraficalist',
    title: 'Nucleo familiare anagrafe comunale',
    store: 'FamigliaAnagraficaStore',
    initComponent: function() {
        this.dockedItems = [{
            xtype: 'wcs_famigliabar',
            store: 'FamigliaAnagraficaStore',
            dock: 'bottom',
            displayInfo: true,
            items: ['-',{
                text: 'Copia su archivio utenti',
                handler: this.onCopy
            }]
        }];

        this.columns = [
        {
            header: 'Cognome',
            dataIndex: 'cognome',
            sortable: true,
            flex: 1
        },
        {
            header: 'Nome',
            dataIndex: 'nome',
            sortable: true,
            flex: 1
        },
        {
            header: 'Stato di nascita',
            dataIndex: 'statoNascita',
            sortable: false,
            hidden: true,
            flex: 1
        },
        {
            header: 'Codice fiscale',
            dataIndex: 'codiceFiscale',
            sortable: true,
            width: 120
        },
        {
            header: 'Comune di nascita',
            dataIndex: 'comuneNascitaDes',
            sortable: false,
           // flex: 1,
            width:120
        },
        {
            header: 'Sesso',
            dataIndex: 'sesso',
            hidden: false,
            sortable: true,
            width: 50
        },
        {
            header: 'Data di nascita',
            dataIndex: 'dataNascita',
            hidden: false,
            sortable: true,
            width: 80
        },
        {
            header: 'Data di morte',
            dataIndex: 'dataMorte',
            hidden: false,
            sortable: true,
            width: 80
        },  {
            header: 'P.A.',
            dataIndex: 'posizioneAnagrafica',
            hidden: false,
            sortable: true,
            width: 50
        }];

        this.callParent(arguments);
    },

    onCopy: function(btn){
        var grid = btn.up('wcs_famigliabar').up('wcs_famigliaanagraficalist');
        var selectedItem = grid.getSelectionModel().selected.items[0];
        var codAnaComunale = selectedItem.data.codAnaComunale;
        Ext.Ajax.request({
            url: '/CartellaSociale/famiglia',
            params: {
                codAnaComunale: codAnaComunale,
                codAnag: Ext.getCmp('wcs_anagraficaCodAna').getValue(),
                action: 'copy'
            },
            success: function(response){
                var json = Ext.JSON.decode(response.responseText);
                if (json.success) {
                    Ext.MessageBox.show({
                        title: 'Esito operazione',
                        msg: json.message,
                        buttons: Ext.MessageBox.OK,
                        fn: function(){
                            var famigliaSocialeGridStore = Ext.getCmp('wcs_famigliaTab').items.get('wcs_famigliaSocialeGrid').store;
                            famigliaSocialeGridStore.load();
                        }
                    });
                } else {
                    Ext.MessageBox.show({
                        title: 'Esito operazione',
                        msg: json.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.window.MessageBox.ERROR
                    });
                }
            }
        });
    }
});