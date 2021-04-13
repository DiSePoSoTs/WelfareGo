
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "IVA" nella riga dei totali di una nuova fattura
 */

Ext.define('Wp.store.fatturazioni.ValoriIva', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.fatturazioni.ComboBoxIva'],

    autoLoad: true,
    model: 'Wp.model.fatturazioni.ComboBoxIva',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaValoriIvaFatturazioni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

