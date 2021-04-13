Ext.define('wp.view.parametri.TemplatesGrid',{
   
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_templatesgrid',
    id: 'wp_templatesgrid',
    
    store: 'TemplateStore',
    frame: false,
    
    columns: [{
        text: 'Codice',
        hideable: false,
        width: 100,
        dataIndex: 'cod_tmpl'
    },{
        text: 'Descrizione',
        flex: 1,
        dataIndex: 'des_tmpl'
    },{
        text: 'File Template',
        width: 300,
        dataIndex: 'download_url'
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text    : 'Nuovo Template',
            tooltip : 'Aggiungi un nuovo template',
            handler: function() {
                Ext.getCmp('wp_templatesform').setActiveRecord(null);
            }
        }]
    },
    {
        xtype: 'pagingtoolbar',
        store: 'TemplateStore',   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true
    }],


    listeners: {
        selectionchange: function(model, records) {
            
            if (records[0]) {
                // imposto il form
                Ext.getCmp('wp_templatesform').setActiveRecord(records[0]);
                Ext.getCmp('wp_templates_btn_elimina').enable();

            } 
//            else {
//                Ext.getCmp('wp_templatesform').setActiveRecord(null);
//                Ext.getCmp('wp_templates_btn_elimina').disable();
//            }
        }
    }
    
});