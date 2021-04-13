
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questa classe implementa un tipo di colonna della griglia che visualizza il
 * nome di un mese a partire dal numero del mese
 */

Ext.define('Wp.library.grid.column.Month', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.wpmonthcolumn',

    renderer: function(value) {
        return (value >= 1 && value <= 12) ? Ext.Date.monthNames[value - 1] : 'Mese sconosciuto';
    }
});
