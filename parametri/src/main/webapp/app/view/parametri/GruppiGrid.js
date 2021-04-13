Ext.define('wp.view.parametri.GruppiGrid',{

    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_gruppigrid',
    id: 'wp_gruppigrid',
	
    frame: false,
    height: 300,
	
    store: 'GruppiStore',
    bindForm: 'wp_gruppiform',

    columns: [
    {
        text: 'Codice',
        sortable: true,
        hideable: false,
        dataIndex: 'cod_grp_tipint'
    },{
        text: 'Gruppo Tipologia',
        flex: 1,
        sortable: true,
        hideable: false,
        dataIndex: 'des_grp_tipint'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text    : 'Aggiungi Gruppo',
            tooltip : 'Aggiungi un nuovo gruppo',
            handler: function() {
                Ext.getCmp('wp_gruppiform').setActiveRecord(null); // form vuoto per insert
                wp_gruppi_toggle_lock(false);
            }
        }]
    }],


    listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
                Ext.getCmp('wp_gruppiform').setActiveRecord(records[0]); // mostro form pieno per update
                
                wp_gruppi_toggle_lock(true);
                
            }
        }
    }

});