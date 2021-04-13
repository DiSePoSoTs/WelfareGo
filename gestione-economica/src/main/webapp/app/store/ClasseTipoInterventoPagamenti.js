
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Classe Tipo intervento" nelle form di ricerca delle pagamenti
 */

Ext.define('Wp.store.ClasseTipoInterventoPagamenti', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    autoLoad: true,
    model: 'Wp.model.ComboBox',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaClasseTipoInterventoPagamenti,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

