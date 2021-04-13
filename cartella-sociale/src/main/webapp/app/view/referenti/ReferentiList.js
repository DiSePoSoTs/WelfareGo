Ext.define('wcs.view.referenti.ReferentiList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_referentilist',
    store: 'ReferentiStore',
    loadMask: true,
    autoScroll:true,
    bbar: {
        xtype: 'wcs_referentibar',
        store: 'ReferentiStore'
    },

    initComponent: function() {
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
            flex: 1
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
            width: 90
        },{
            header: 'Qualifica',
            dataIndex: 'qualificaDes',
            hidden: false,
            sortable: false,
            flex: 1
        },{
            header: 'Pai',
            dataIndex: 'flgPai',
            hidden: false,
            sortable: false,
            flex: 1
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
//        this.getSelectionModel().on('selectionchange', this.onSelectChange, this);
    }
});