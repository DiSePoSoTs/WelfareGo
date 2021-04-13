Ext.define('wcs.controller.AnagraficaController', {
    extend: 'Ext.app.Controller' ,
    views: [
        'anagrafica.AnagraficaForm',
        'anagrafica.RicercaPopup',
        'anagrafica.StatoRemoteCombo',
        'anagrafica.ComuneRemoteCombo',
        'anagrafica.ViaRemoteCombo',
        'anagrafica.CivicoRemoteCombo',
        'anagrafica.ProvinciaRemoteCombo'
    ],
    stores: ['StatoStore','ComuneStore','ViaStore','CivicoStore', 'ProvinciaStore'],
    models: ['StatoModel','ComuneModel','ViaModel','CivicoModel', 'RisultatiRicercaModel', 'ProvinciaModel', 'AssistenteSocialeModel','EducatoreModel', 'AnagraficaModel']

});