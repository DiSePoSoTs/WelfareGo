Ext.define('wcs.controller.ReferentiController', {
    extend: 'Ext.app.Controller' ,
    views: [
    'referenti.ReferentiBar',
    'referenti.ReferentiList',
    'referenti.ReferentiRicercaPopup',
    'referenti.ReferentiForm'
    ],
    stores: ['ReferentiStore'],
    models: ['ReferentiModel']

});