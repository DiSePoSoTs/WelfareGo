Ext.define('wcs.view.appuntamenti.AppuntamentiList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_appuntamentilist',
    title: 'Appuntamenti',
    store: 'AppuntamentiStore',
    loadMask: true,
    autoHeight:true,
    bbar: {
        xtype: 'wcs_appuntamentibar'
    },

    initComponent: function() {
        this.columns = [
        {
            id: 'wcs_appuntamentiData',
            header: 'Data',
            dataIndex: 'tsIniApp',
            sortable: true,
            flex: 1
        },
        {
//            id: 'wcs_appuntamentiOra',
            header: 'Ora',
            dataIndex: 'oraAppuntamento',
            sortable: false,
            flex: 1
        },
        {
            id: 'wcs_appuntamentoAssistenteSociale',
            header: 'Assistente sociale',
            dataIndex: 'nomeCompleto',
            sortable: false,
            flex: 1
        },
        {
            id: 'wcs_appuntamentoFissatoDa',
            header: 'Fissato da',
            dataIndex: 'fissato',
            sortable: false,
            flex: 1
        },
        {
            id: 'wcs_appuntamentoNote',
            header: 'Note',
            dataIndex: 'note',
            sortable: false,
            flex: 1
        }];

        this.callParent(arguments);
    }
});