
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "IVA" nella riga dei totali di una nuova fattura
 */

Ext.define('Wp.store.pagamenti.nuovo.Decreti', {
    extend: 'Ext.data.Store',

    autoLoad: false,
    model: 'Wp.model.ComboBox',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaDecretiNuovo,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

