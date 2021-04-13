
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista delle quote obbligati
 * di una nuova fattura
 */

Ext.define('Wp.model.fatturazioni.QuoteObbligati', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'id_fattura', type: 'string' },
        { name: 'codice_fiscale', type: 'string' },
        { name: 'importo', type: 'float', useNull: true }
    ]
});

