
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Classe Tipo intervento" nelle form di ricerca delle fatturazioni
 */

Ext.define('Wp.store.ClasseTipoInterventoFatturazioni', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    autoLoad: true,
    model: 'Wp.model.ComboBox',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaClasseTipoInterventoFatturazioni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

