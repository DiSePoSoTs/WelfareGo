Ext.define('wp.view.parametri.AssociaDispGrid',{
   
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_associadispgrid',
    id: 'wp_associadispgrid',
    
    title: 'Dati disponibili',
    
    height: 300,
    store: 'AssociaDispStore',
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
        dataIndex: 'tipo_campo'
    },{
        text: 'Valori Ammessi',
        flex: 1,
        dataIndex: 'val_amm'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            disabled: true,
            id      : 'wp_associa_btn',
            text    : 'Associa il dato',
            tooltip : 'Aggiungi il dato scelto alla tipologia intervento',
            iconCls: 'add-icon',
            handler: function() {
                
                var selected = Ext.getCmp('wp_associadispgrid').getSelectionModel().selected.first();
                var codTipint = Ext.getCmp('wp_associatree').getSelectionModel().selected.first().get('pk');
                
                Ext.MessageBox.show({
                    title: 'Attenzione',
                    msg: 'Stai per associare questo dato alla tipologia intervento: ' + codTipint ,
                    buttons: Ext.MessageBox.OKCANCEL,
                    fn: function(btn){
                        if (btn == 'ok'){
                            
                            Ext.Ajax.request({
                                url: '/Parametri/DatiServlet',
                                params: {
                                    action: 'ASSOCIA',
                                    codCampo: selected.get('cod_campo'),
                                    codTipint: codTipint
                                },
                                success: function(response){
                                    
                                    // aggiorno grid di relazione
                                    Ext.getCmp('wp_associarelgrid').store.load({
                                        params: {
                                            codTipint: codTipint
                                        }
                                    });
                                    
                                    // aggiorno grid con disponibilità
                                    Ext.getCmp('wp_associadispgrid').store.load({
                                        params: {
                                            codTipint: codTipint
                                        }
                                    });
                                }
                            });
                            
                        }
                    }
                });
            }
        }]
    }],


    listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
                Ext.getCmp('wp_associa_btn').enable();
            } else {
                Ext.getCmp('wp_associa_btn').disable();
            }
        }
    }
    
});