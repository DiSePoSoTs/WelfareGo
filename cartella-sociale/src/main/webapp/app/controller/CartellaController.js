Ext.define('wcs.controller.CartellaController', {
    extend: 'Ext.app.Controller' ,
    views: [
    'cartella.CartellaForm',
    'cartella.CartellaTab',
    'cartella.Trasferimento',
    'cartella.ContattoForm',
    'cartella.BreForm',
    'cartella.BreComponent'
    ],
    models: ['MessaggioBreModel']

});