
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nelle ComboBox che rappresentano la selezione di un anno
 */

Ext.define('Wp.store.Anno', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    model: 'Wp.model.ComboBox',

    constructor: function(config) {
        var today = new Date(),
            year = today.getFullYear(),
            lastYear = year + 2;

        config.data = [];

        // calcola gli anni che dovranno essere restituiti dallo store
        // dall'anno 2010 all'anno attuale + 2
        for (year = 2010; year <= lastYear; year++) {
            config.data.push({id: year.toString(10), label: year.toString(10)});
        }

        this.callParent(arguments);
    }

});

