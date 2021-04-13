Ext.define('wf.controller.VerificaDatiController', {
    extend: 'Ext.app.Controller' ,
    views: [ 'verifica_dati.VerificaDatiPanel'],
    stores: ['combo.CauseRespingimentoStore','combo.TipologieInterventoStore'],
    models: ['ImpegnoModel','ImpegnoUotModel','MessaggioBreModel'],
    init: function(){ },
    onPanelRendered: function() { }
});