Ext.define('wf.controller.ValidaEsecutivitaController', {
    extend: 'Ext.app.Controller' ,
    views: ['valida_esecutivita.ValidaEsecutivitaPanel'],
    stores: ['combo.CauseRespingimentoStore'],
    models: ['ImpegnoModel', 'MessaggioBreModel']
});