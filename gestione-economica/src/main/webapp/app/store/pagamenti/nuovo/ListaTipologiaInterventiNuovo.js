
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Tipologiad'intervento" nella tabella delle voci di pagamento
 */

Ext.define('Wp.store.pagamenti.nuovo.ListaTipologiaInterventiNuovo', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.pagamenti.nuovo.ComboBoxTipologiaInterventiNuovo'],

    model: 'Wp.model.pagamenti.nuovo.ComboBoxTipologiaInterventiNuovo',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaTipologiaInterventiPagamentiNuovo,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

