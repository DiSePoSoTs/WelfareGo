Ext.define('wp.view.parametri.ParametriPanel',{

    extend: 'Ext.panel.Panel',
    alias:  'widget.wp_main_panel',
    title:  'Gestione Parametri',
    frame: false,
    border: 0,

    initComponent: function() {
        
        var tabs_height = 600;
        
        this.items = [ 
        {
            xtype:      'tabpanel',
            activeTab:  0,
            
            items: [{
                xtype: 'wp_paramstab',
                title: 'Parametri',
                height: tabs_height
            }, {
                title: 'Tipologie Interventi',
                xtype: 'wp_tipologietab',
                height: tabs_height
            }, {
                title: 'Dati Specifici',
                xtype: 'wp_datitab',
                height: tabs_height
            }, {
                title: 'Associazione Dati Specifici',
                xtype: 'wp_associatab',
                height: tabs_height
            }, {
                title: 'Utenti',
                xtype: 'wp_utentitab',
                height: tabs_height
            }, {
                title: 'Template',
                xtype: 'wp_templatestab',
                height: tabs_height
            }, {
                title: 'Liste d\'attesa',
                xtype: 'wp_listetab',
                height: tabs_height
            }, {
                title: 'Budget',
                xtype: 'wp_budgettab',
                height: tabs_height
            },{
                title: 'Strutture',
                xtype: 'wp_struttab',
                height: tabs_height
            }]
        
        }];

        this.callParent(arguments);       

    }
    
});