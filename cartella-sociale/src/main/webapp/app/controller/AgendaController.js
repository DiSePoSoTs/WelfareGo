Ext.define('wcs.controller.AgendaController', {
    extend: 'Ext.app.Controller' ,
    views: [
        'agenda.Container',
        'agenda.DatePicker',
        'agenda.Detail',
        'agenda.AgendaDataView'
    ],
    stores: ['AgendaStore'],
    models: ['ImpegnoModel']
});