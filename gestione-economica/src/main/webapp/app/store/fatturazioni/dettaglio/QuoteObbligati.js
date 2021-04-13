
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le quote obbligati di nuova
 * fattura
 */

Ext.define('Wp.store.fatturazioni.dettaglio.QuoteObbligati', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.QuoteObbligati',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioQuoteObbligateFatturazioni,
            update: wp_url_servizi.SalvaDettaglioQuoteObbligateFatturazioni
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
            allowSingle: false
        }
    }
});

