
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare i risultati della ricerca per la
 * sezione Acquisizione dati
 */

Ext.define('Wp.store.acquisizioni.Acquisizioni', {
    extend: 'Ext.data.Store',

    model: 'Wp.model.acquisizioni.Acquisizioni',
    remoteSort : true,
    sortOnLoad: true,
    proxy: {
        type: 'ajax',
        timeout : 300000,
        url: wp_url_servizi.CercaAcquisizioni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        },
        sorters:[
                 { 
                     property:'cognome',         
                     direction:'ASC'
                 }
             ],      
        listeners:{
            exception:wpStoreExceptionFunction
        }
    },
    
    pageSize: 30
});

