Ext.define('wp.view.parametri.ListeGrid',{

    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_listegrid',
    id: 'wp_listegrid',
	
    frame: false,	
	
    store: 'ListeStore',
    bindForm: 'wp_listeform',
    height: 400,

    columns: [
    {
        text: 'Codice',
        hidden: true,
        dataIndex: 'cod_lista_att'
    },{
        text: 'Descrizione',
        flex: 1,
        sortable: true,
        hideable: false,
        dataIndex: 'des_lista_att'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text    : 'Aggiungi Lista',
            tooltip : 'Aggiungi una nuova lista',
            handler: function() {
                Ext.getCmp('wp_listeform').setActiveRecord(null); // form vuoto per insert
                Ext.getCmp('wp_liste_btn_elimina').disable();
                wp_liste_toggle_lock(false); // sblocco i campi
            }
        }]
    }],


    listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
                Ext.getCmp('wp_listeform').setActiveRecord(records[0]); // mostro form pieno per update
                wp_liste_toggle_lock(true); // blocco i campi
                Ext.getCmp('wp_liste_btn_elimina').enable();
            } else {
                Ext.getCmp('wp_listeform').setActiveRecord(null); // mostro form pieno per update
                wp_liste_toggle_lock(false); // blocco i campi
                Ext.getCmp('wp_liste_btn_elimina').disable();
            }
        }
    }

    
    
});