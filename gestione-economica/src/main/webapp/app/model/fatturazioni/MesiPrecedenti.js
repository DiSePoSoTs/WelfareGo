
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista dei dati dei mesi
 * precedenti di una nuova fattura
 */

Ext.define('Wp.model.fatturazioni.MesiPrecedenti', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'id_fattura', type: 'string' },
        { name: 'mese', type: 'int', useNull: true },
        { name: 'importo', type: 'float', useNull: true },
        { name: 'causale', type: 'string' },
        { name: 'inserimento', type: 'string' }
    ]
});

