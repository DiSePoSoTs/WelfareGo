
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le voci di una fattura.
 */

Ext.define('Wp.store.fatturazioni.dettaglio.VociFatturazioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.VociFatturazioni',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioVociFatturazioni,
            update: wp_url_servizi.SalvaDettaglioVociFatturazioni
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

