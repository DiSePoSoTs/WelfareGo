
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questa classe implementa un tipo di colonna della griglia che visualizza un
 * numero float formattandolo con al più due decimali ed aggiungendo i separatori
 * delle migliaia
 */

Ext.define('Wp.library.grid.column.FloatNumber', {
    extend: 'Ext.grid.column.Number',
    alias: 'widget.wpfloatnumbercolumn',

    format: '0,000.00'
});
