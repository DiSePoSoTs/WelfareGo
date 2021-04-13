
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce il contenuto del tab Pagamenti
 */

Ext.define('Wp.view.tabs.Pagamenti' ,{
    extend: 'Ext.container.Container',
    alias : 'widget.wptabspagamenti',

    initComponent: function() {
        this.items = [
            {
                xtype: 'wppagamentiform'
            },
            {
                xtype: 'wppagamentilist',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wppagamentilistdagenerare',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wppagamentinuovoselezionato',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wppagamentinuovo',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wppagamentimodifica',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wppagamentidettaglio',
                // inizialmente è nascosto
                hidden: true
            }
        ];

        this.callParent(arguments);
        this.layout='anchor';
    }
});

