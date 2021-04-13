
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * 
 */

Ext.define('Wp.controller.Tabs', {
    extend: 'Ext.app.Controller',

    views: [
        'tabs.Tabs',
        'tabs.Acquisizioni',
        'tabs.Fatturazioni',
        'tabs.Pagamenti'
    ],
    models: [
        'AutocompletePersone',
        'ComboBoxTipoIntervento'
    ],

    // instanzia gli store utilizzati dalle form di ricerca in modo che
    // vengano registrati all'interno dello StoreManager e possano così
    // essere riutilizzati
    stores: [
        'Mese',
        'Anno',
        'StatiPagamenti',
        'StatiFatturazioni',
        'ClasseTipoInterventoAcquisizioni',
        'ClasseTipoInterventoFatturazioni',
        'ClasseTipoInterventoPagamenti',
        'TipoInterventoAcquisizioni',
        'TipoInterventoFatturazioni',
        'TipoInterventoPagamenti',
        'UotStruttura',
        'MotivazioneVariazioneSpesaStore'
    ]
});