
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca dei
 * pagamenti da generare per la sezione Pagamenti
 */

Ext.define('Wp.store.pagamenti.PagamentiDaGenerare', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.PagamentiDaGenerare',

    pageSize:120,
    proxy: {
        type: 'ajax',
        timeout : 300000,
        url: wp_url_servizi.CercaPagamentiDaGenerare,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});