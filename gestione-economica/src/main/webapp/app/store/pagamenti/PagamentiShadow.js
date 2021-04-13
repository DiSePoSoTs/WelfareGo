
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca per la
 * sezione Pagamenti
 */

Ext.define('Wp.store.pagamenti.PagamentiShadow', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.pagamenti.Pagamenti',
    
    pageSize:120,
    proxy: {
        type: 'ajax',
        timeout : 300000,
        api: {
            read: wp_url_servizi.CercaPagamenti
//            update: wp_url_servizi.SalvaStatoPagamenti
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
     /*   writer: {
            type: 'json',
            root: 'data',
            encode: true,
            allowSingle: false
        }*/
    }
});

