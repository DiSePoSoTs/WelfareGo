
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce il contenuto del tab Acquisizione dati
 */

Ext.define('Wp.view.tabs.Acquisizioni' ,{
    extend: 'Ext.container.Container',
    alias : 'widget.wptabsacquisizioni',

    initComponent: function() {
        this.items = [
            {
                xtype: 'wpacquisizioniform'
            },
            {
                xtype: 'wpacquisizionilist',
                // inizialmente è nascosto
                hidden: true
            },
            {
                xtype: 'wpacquisizionidetail',
                // inizialmente è nascosto
                hidden: true
            }
        ];

        this.callParent(arguments);
        this.layout='anchor';
    }
});

