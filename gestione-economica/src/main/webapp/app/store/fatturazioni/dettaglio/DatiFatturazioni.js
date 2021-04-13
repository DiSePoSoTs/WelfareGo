
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i dati di una fattura.
 */

Ext.define('Wp.store.fatturazioni.dettaglio.DatiFatturazioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.DatiFatturazioni',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioDatiFatturazioni,
            update: wp_url_servizi.SalvaDettaglioDatiFatturazioni
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

