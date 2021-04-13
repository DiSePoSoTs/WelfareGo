
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le quote obbligati di una nuova
 * fattura
 */

Ext.define('Wp.store.fatturazioni.nuova.QuoteObbligati', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.QuoteObbligati',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.CercaQuoteObbligatiFatturazioniNuova,
            update: wp_url_servizi.SalvaQuoteObbligatiFatturazioniNuova
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

