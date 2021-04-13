Ext.define('wcs.view.pai.InterventiListSocialCrt',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventilistsocialcrt',
    title: 'Interventi Social Crt',
    store: 'InterventiSocialCrtListStore',
    id:'wcs_paiInterventiListSocialCrt',
    loadMask: true,
    minHeight: 155,
    columns : [{
        header: 'Tipo intervento',
        dataIndex: 'tipo',
        sortable: true
/*        renderer : function(value, metadata,record) {
        	   metadata.tdAttr = 'data-qtip=" qui cè il tipo"';
               return value;
}*/

    },{
        header: 'Contatore intervento',
        dataIndex: 'cntTipint',
        hidden:true,
        sortable: true
    },{
        header: 'Stato',
        dataIndex: 'statoIntervento',
        sortable: true,
        width: 45
    },{
        header: 'Intervento',
        dataIndex: 'desInt',
        sortable: true,
        flex: 1
    },{
        header: 'Data apertura',
        dataIndex: 'dataApertura',
        sortable: true,
        xtype: 'datecolumn', 
        format:'d/m/Y',
        width: 90
    },{
        header: 'Data avvio',
        dataIndex: 'dataAvvio',
        sortable: true,
        xtype: 'datecolumn', 
        format:'d/m/Y',
        width: 90
    },{
        header: 'Data chiusura',
        dataIndex: 'dataChiusura',
        sortable: true,
        xtype: 'datecolumn', 
        format:'d/m/Y',
        width: 90
    },{
        header: 'Costo totale',
        dataIndex: 'importoPrevisto',
        sortable: true,
        renderer: Euro,
        flex: 1
    },{
        header: 'Operatore',
        dataIndex: 'cognomeNomeOperatore',
        sortable: true,
        flex: 1
    },{
        header: 'Associazione',
        dataIndex: 'associazione',
        sortable: true,
        
        flex: 1
    }
    ],
    bbar: {
        xtype:'pagingtoolbar',
        store: 'InterventiSocialCrtListStore',
        displayMsg: 'Visualizzo gli interventi da {0} a {1} di {2}',
        emptyMsg: 'Nessun intervento'
    }
});