
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le voci di un pagamento
 */

Ext.define('Wp.store.pagamenti.nuovo.VociPagamenti', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.VociPagamenti',

    proxy: {
        type: 'ajax',
        api: {
            create: wp_url_servizi.SalvaVociPagamentiNuovo
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