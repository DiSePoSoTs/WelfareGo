Ext.define('wcs.view.pai.LiberatoriaListSocialCrt',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_liberatorialistsocialcrt',
    title: 'Liberatorie Social Crt',
    store: 'LiberatoriaSocialCrtListStore',
    id:'wcs_liberatoriaSocialCrt',
    loadMask: true,
    minHeight: 155,
    columns : [{
        header: 'Associazione',
        dataIndex: 'associazione',
        sortable: true,
        width: 150
    },{
        header: 'Utente',
        dataIndex: 'utente',
        sortable: true
    },{
        header: 'Data firma',
        dataIndex: 'dataFirma',
        sortable: true,
        xtype: 'datecolumn',
        format:'d/m/Y',
        width: 90
    }
    ],
    bbar: {
        xtype:'pagingtoolbar',
        store: 'LiberatoriaSocialCrtListStore',
        displayMsg: 'Visualizzo gli interventi da {0} a {1} di {2}',
        emptyMsg: 'Nessun intervento'
    }
});