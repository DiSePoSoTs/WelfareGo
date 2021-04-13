Ext.define('wcs.controller.FamigliaController', {
    extend: 'Ext.app.Controller' ,
    views: [
    'famiglia.FamigliaBar',
    'famiglia.FamigliaAnagraficaForm',
    'famiglia.FamigliaAnagraficaList',
    'famiglia.FamigliaSocialeForm',
    'famiglia.FamigliaSocialeRicercaPopup',
    'famiglia.FamigliaSocialeList'
    ],
    stores: ['FamigliaSocialeStore','FamigliaAnagraficaStore'],
    models: ['FamigliaModel']

});