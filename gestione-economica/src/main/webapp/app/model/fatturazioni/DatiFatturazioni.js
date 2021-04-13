
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta un record dei dati di una nuova fattura
 */

Ext.define('Wp.model.fatturazioni.DatiFatturazioni', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'id_fatt', type: 'string' }, //id della fattura dopo il salvataggio
        { name: 'cod_ana', type: 'string' }, //id dell'anagrafica associata (per la nuova vuota)
        { name: 'totale_fattura', type: 'float', useNull: true },
        { name: 'contributo', type: 'float', useNull: true },
        { name: 'iva', type: 'int' },
        { name: 'importo_iva', type: 'float' },
        { name: 'imponibile', type: 'float', useNull: true },
        { name: 'bollo', type: 'float', useNull: true },
        { name: 'totale_periodo', type: 'float', useNull: true },
        { name: 'causale', type: 'string' },
        { name: 'note', type: 'string' },
        { name: 'da_pagare', type: 'float' },
        { name: 'scadenza', type: 'date', dateFormat:'d/m/Y' },
        { name: 'codice_fiscale', type: 'string' },
        { name: 'modalita_pagamento', type: 'int' },
        { name: 'fatturaUnica', type: 'string' },
        { name: 'numeroFattura', type: 'int' }
    ]
});

