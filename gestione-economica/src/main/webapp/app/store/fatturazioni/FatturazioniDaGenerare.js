
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca delle
 * fatture da generare per la sezione Fatturazioni
 */

Ext.define('Wp.store.fatturazioni.FatturazioniDaGenerare', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.FatturazioniDaGenerare',
    pageSize:10000,
    proxy: {
        type: 'ajax',
        url: wp_url_servizi.CercaFatturazioniDaGenerare,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

