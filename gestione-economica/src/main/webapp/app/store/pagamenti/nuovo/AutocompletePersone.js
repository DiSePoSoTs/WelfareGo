
/*
 * @author Luca Battistin - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare le persona a cui fatturare
 */

Ext.define('Wp.store.pagamenti.nuovo.AutocompletePersone', {
    extend: 'Ext.data.Store',
    pageSize: 10,
    model: 'Wp.model.AutocompletePersone',
    proxy: {
        type: 'ajax',
        url : wp_url_servizi.CercaAnagraficaPagamenti,
        reader: {
            type: 'json',
            root: 'persone',
            totalProperty: 'totalCount'
        }
    }
});

