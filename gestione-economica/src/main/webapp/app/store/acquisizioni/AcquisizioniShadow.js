
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca per la
 * sezione Acquisizione dati

 NOTA:
 se metto i sorters dentro il proxy, per qualche motivo non mi ordinava i record.....
 */

Ext.define('Wp.store.acquisizioni.AcquisizioniShadow', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.acquisizioni.Acquisizioni',
    remoteSort : true,
    sortOnLoad: true,
    sorters: [{
        property: 'cognome',
        direction: 'ASC'
    }],
    proxy: {
        type: 'ajax',
        timeout : 300000,
        url: wp_url_servizi.CercaAcquisizioni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        },
        listeners:{
            exception:wpStoreExceptionFunction
        }
    },

    pageSize: 1000
});

