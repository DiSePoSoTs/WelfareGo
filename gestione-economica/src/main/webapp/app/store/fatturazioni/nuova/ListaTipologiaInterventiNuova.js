
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nella ComboBox "Tipologiad'intervento" nella tabella delle voci di fattura
 */

Ext.define('Wp.store.fatturazioni.nuova.ListaTipologiaInterventiNuova', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.fatturazioni.nuova.ComboBoxTipologiaInterventiNuova'],

    model: 'Wp.model.fatturazioni.nuova.ComboBoxTipologiaInterventiNuova',

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaTipologiaInterventiFatturazioniNuova,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});

