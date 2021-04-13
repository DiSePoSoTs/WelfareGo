
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca per la
 * sezione Fatturazioni
 */

Ext.define('Wp.store.fatturazioni.Fatturazioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.Fatturazioni',
    pageSize:10000,
    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.CercaFatturazioni
           // update: wp_url_servizi.SalvaStatoFatturazioni
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

