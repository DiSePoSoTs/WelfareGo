
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i dati di una nuova fattura
 */

Ext.define('Wp.store.fatturazioni.nuovaselezionata.DatiFatturazioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.DatiFatturazioni',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.CercaDatiFatturazioniNuovaSelezionata,
            update: wp_url_servizi.SalvaDatiFatturazioniNuovaSelezionata
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

