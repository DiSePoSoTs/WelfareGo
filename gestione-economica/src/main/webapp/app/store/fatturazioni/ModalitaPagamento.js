
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Modalità di pagamento" nelle form delle fatture
 */

Ext.define('Wp.store.fatturazioni.ModalitaPagamento', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    autoLoad: true,
    model: 'Wp.model.ComboBox',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaModalitaPagamentoFatturazioni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

