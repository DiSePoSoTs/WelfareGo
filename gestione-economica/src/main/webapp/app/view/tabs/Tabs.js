
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa è la view principale dell'applicazione, definisce i tab e i loro
 * contenuti
 */

Ext.define('Wp.view.tabs.Tabs' ,{
    extend: 'Ext.tab.Panel',
    alias : 'widget.wptabs',

    initComponent: function() {
        this.bodyPadding = 10;
        this.flex = 0;
        this.activeTab = 0;

        this.items = [
            {
                title: 'Acquisizione dati',
                xtype: 'wptabsacquisizioni'
            },
            {
                title: 'Fatturazioni',
                xtype: 'wptabsfatturazioni'
            },
            {
                title: 'Pagamenti',
                xtype: 'wptabspagamenti'
            }
        ];
        
        this.listeners = {
        		 'tabchange': {fn: function(panel) {
                     panel.doLayout();
                 }
                 ,scope: this}
        };

        this.callParent(arguments);
    }
    }
);

