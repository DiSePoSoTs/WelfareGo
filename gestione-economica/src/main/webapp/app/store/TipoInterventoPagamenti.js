
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Tipo intervento" nelle form di ricerca
 */

Ext.define('Wp.store.TipoInterventoPagamenti', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    autoLoad: true,
    model: 'Wp.model.ComboBoxTipoIntervento',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaTipoInterventoPagamenti,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

