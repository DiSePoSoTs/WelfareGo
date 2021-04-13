
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i dati dei mesi precedenti di una
 * nuova fattura
 */

Ext.define('Wp.store.fatturazioni.nuova.MesiPrecedenti', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.fatturazioni.MesiPrecedenti',

    proxy: {
        type: 'ajax',
        api: {
            read: wp_url_servizi.CercaMesiPrecedentiFatturazioniNuova,
            update: wp_url_servizi.SalvaMesiPrecedentiFatturazioniNuova
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        },
        writer: {
            type: 'json',
            root: 'data',
            encode: true,
            allowSingle: false
        }
    }
});

