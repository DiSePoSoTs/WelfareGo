
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta un record dei dati di una nuova fattura
 */

Ext.define('Wp.model.pagamenti.DatiPagamenti', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'id_pagam', type: 'int' }, //id della fattura dopo il salvataggio
        { name: 'cod_ana', type: 'string' }, //id dell'anagrafica associata (per la nuova vuota)
        { name: 'decreto_impegno', type: 'string'},
        { name: 'da_liquidare', type: 'float'},
        { name: 'cf_beneficiario', type: 'string' },
        { name: 'iban_beneficiario', type: 'string' },
        { name: 'cf_delegante', type: 'string' },
        { name: 'nome_delegante', type: 'string'},
        { name: 'cognome_delegante', type: 'string'},
        { name: 'note', type: 'string' },
        { name: 'modalita_erogazione', type: 'string' }
    ]
});

