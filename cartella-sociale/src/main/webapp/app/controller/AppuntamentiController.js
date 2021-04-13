Ext.define('wcs.controller.AppuntamentiController', {
    extend: 'Ext.app.Controller' ,
    views: [
    'appuntamenti.AppuntamentiList',
    'appuntamenti.AppuntamentiBar'
    ],
    stores: ['AppuntamentiStore'],
    models: ['AppuntamentoModel']
});