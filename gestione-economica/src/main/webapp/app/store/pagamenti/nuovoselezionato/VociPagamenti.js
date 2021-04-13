
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le voci di un nuovo pagamenti
 */

Ext.define('Wp.store.pagamenti.nuovoselezionato.VociPagamenti', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.VociPagamenti',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.CercaVociPagamentiNuovoSelezionato,
            update: wp_url_servizi.SalvaVociPagamentiNuovoSelezionato
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