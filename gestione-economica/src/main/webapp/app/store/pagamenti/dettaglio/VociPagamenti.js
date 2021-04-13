
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le voci di un pagamento
 */

Ext.define('Wp.store.pagamenti.dettaglio.VociPagamenti', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.VociPagamenti',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.DettaglioVociPagamenti,
            update: wp_url_servizi.SalvaDettaglioVociPagamenti
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