
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i dati di una fattura
 */

Ext.define('Wp.store.pagamenti.dettaglio.DatiPagamenti', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.DatiPagamenti',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioDatiPagamenti,
            update: wp_url_servizi.SalvaDettaglioDatiPagamenti
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        },
        writer: {
            type: 'json',
            root: 'data',
            encode: true,
            allowSingle: true
        }
    }
});

