Ext.define('wp.view.parametri.DatiGrid',{
   
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_datigrid',
    id: 'wp_datigrid',
    
    bindForm: 'wp_datiform',
    
    height: 365,
    store: 'DatiStore',
    frame: false,

    columns: [
    {
        text: 'Codice',
        flex: 1,
        dataIndex: 'cod_campo'
    },{
        text: 'Descrizione',
        flex: 1,
        dataIndex: 'des_campo'
    },{
        text: 'Tipo',
        flex: 1,
        dataIndex: 'tipo_campo',
        store: wp_tipiDatoComboStore,
        displayField: 'name',
        valueField: 'value'
    },{
        text: 'Valori Ammessi',
        flex: 1,
        dataIndex: 'val_amm'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text    : 'Aggiungi Dato',
            tooltip : 'Aggiungi un nuovo dato specifico',
            handler: function() {
                Ext.getCmp('wp_datiform').setActiveRecord(null); // form vuoto per insert
            }
        }]
    }, {
        xtype: 'pagingtoolbar',
        store: 'DatiStore',
        dock: 'bottom',
        displayInfo: true,
        items:['-',{
            xtype:'textfield',
            enableKeyEvents:true,
            listeners:{
                specialkey:function(field,event){
                    if (event.getKey() == event.ENTER) {
                        var value=field.getValue();
                        var store=field.up('#wp_datigrid').store;
                        if(value==''){
                            delete store.proxy.extraParams.filter;
                        }else{
                            store.proxy.extraParams.filter=value;
                        }
                        store.load();
                    }
                }
            }
        }]
    }],


    listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
                Ext.getCmp('wp_datiform').setActiveRecord(records[0]);
            } else {
                Ext.getCmp('wp_datiform').setActiveRecord(null);
            }
        }
    }
    
});