Ext.define('wf.controller.AgendaController', {
    extend: 'Ext.app.Controller' ,
    views: [
        'agenda.Container',
        'agenda.DatePicker',
        'agenda.Detail',
        'agenda.AgendaDataView'
    ],
    stores: ['AgendaStore'],
    models: ['ComboModel','TaskImpegnoModel']
});