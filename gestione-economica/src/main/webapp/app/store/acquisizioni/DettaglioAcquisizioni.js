
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare il dettaglio di una entry della
 * ricerca per la sezione Acquisizione dati
 */

Ext.define('Wp.store.acquisizioni.DettaglioAcquisizioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.acquisizioni.DettaglioAcquisizioni',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioAcquisizioni,
            update: wp_url_servizi.SalvaDettaglioAcquisizioni
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
        },
        listeners:{
            exception:wpStoreExceptionFunction
        }
    },
    pageSize: 10
});

