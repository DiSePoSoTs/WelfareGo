
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce il contenuto del tab Fatturazioni
 */

Ext.define('Wp.view.tabs.Fatturazioni' ,{
    extend: 'Ext.container.Container',
    alias : 'widget.wptabsfatturazioni',
    autoHeight:true,
    initComponent: function() {
        this.items = [
            {
                xtype: 'wpfatturazioniform'
            },
            {
                xtype: 'wpfatturazionilist',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpfatturazionilistdagenerare',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpfatturazioninuovaselezionata',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpfatturazioninuova',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpfatturazionimodifica',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpfatturazionidettaglio',
                // inizialmente è nascosto
                hidden: true
            }
        ];

        this.callParent(arguments);
        this.layout='anchor';
    }
});

