Ext.define('wcs.view.diario.NoteList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_notelist',
    title: 'Note condivise',
    store: 'NoteListStore',
    id:'wcs_notelist',
    loadMask: true,
    minHeight: 155,
    columns : [{
        header: 'id',
        dataIndex: 'id',
        sortable: true,
        hidden:true
/*        renderer : function(value, metadata,record) {
        	   metadata.tdAttr = 'data-qtip=" qui cè il tipo"';
               return value;
}*/

    },{
        header: 'Data apertura',
        dataIndex: 'dataApertura',
        sortable: true,
        xtype: 'datecolumn', 
        format:'d/m/Y',
        width: 90
    },{
        header: 'Titolo',
        dataIndex: 'titolo',
        sortable: true,
        width:200
       
    },{
        header: 'Utente',
        dataIndex: 'cognomeNomeOperatore',
        sortable: true,
        width:200
    },{
        header: 'Associazione',
        dataIndex: 'associazione',
          sortable: true,
        flex: 1
    }]
    
});