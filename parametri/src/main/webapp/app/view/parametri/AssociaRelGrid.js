Ext.define('wp.view.parametri.AssociaRelGrid',{
   
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_associarelgrid',
    id: 'wp_associarelgrid',
    
    title: 'Dati Associati',
    
    height: 300,
    store: 'AssociaRelStore',
    frame: false,
    
    selType: 'rowmodel',
    plugins: [
    Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToEdit: 2,
        listeners: {
            edit: function(editor, e){
                Ext.Ajax.request({
                    url: '/Parametri/DatiServlet',
                    params: {
                        action: 'UPDATE',
                        codCampo: editor.record.get('cod_campo'),
                        codTipint: editor.record.get('cod_tipint'),
                        rowCampo: editor.record.get('row_campo'),
                        colCampo: editor.record.get('col_campo')
                    },
                    success: function(){
                        
                    }
                })
            }
        }
    })],

    columns: [{
        header: 'Codice',
        flex: 1,
        dataIndex: 'cod_campo'
    },{
        header: 'Riga',
        flex: 1,
        dataIndex: 'row_campo',
        editor: {
            xtype: 'numberfield',
            allowBlank: false
        }
    },{
        header: 'Colonna',
        flex: 1,
        dataIndex: 'col_campo',
        editor: {
            xtype: 'numberfield',
            allowBlank: false
        }
    },{
        header: 'Descrizione',
        flex: 1,
        dataIndex: 'des_campo'
    },{
        header: 'Tipo',
        flex: 1,
        dataIndex: 'tipo_campo'
    },{
        header: 'Valori Ammessi',
        flex: 1,
        dataIndex: 'val_amm'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            disabled: true,
            id: 'wp_disassocia_btn',
            text    : 'Rimuovi associazione',
            tooltip : 'Rimuovo il dato specifico dalla relazione',
            iconCls: 'delete-icon',
            handler: function() {
                
                Ext.MessageBox.show({
                    title: 'Attenzione',
                    msg: 'Stai per eliminare l\'associazione con questo dato specifico.\nSei sicuro di voler continuare?',
                    buttons: Ext.MessageBox.OKCANCEL,
                    fn: function(btn){
                        if (btn == 'ok'){
                            
                            var selected = Ext.getCmp('wp_associarelgrid').getSelectionModel().selected.first();
                            
                            Ext.Ajax.request({
                                url: '/Parametri/DatiServlet',
                                params: {
                                    action: 'DISASSOCIA',
                                    codCampo: selected.get('cod_campo'),
                                    codTipint: selected.get('cod_tipint')
                                },
                                success: function(response){
                                    
                                    // aggiorno grid di relazione
                                    Ext.getCmp('wp_associarelgrid').store.load({
                                        params: {
                                            codTipint: selected.get('cod_tipint')                                       
                                        }
                                    });
                                    
                                    // aggiorno grid con disponibilità
                                    Ext.getCmp('wp_associadispgrid').store.load({
                                        params: {
                                            codTipint: selected.get('cod_tipint')                                       
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
                Ext.getCmp('wp_disassocia_btn').enable();
            } else {
                Ext.getCmp('wp_disassocia_btn').disable();
            }
        }
    }
    
});