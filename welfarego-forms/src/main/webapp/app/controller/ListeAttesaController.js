Ext.define('wf.controller.ListeAttesaController', {
    extend: 'Ext.app.Controller',
    views: ['liste_attesa.ListeAttesaGrid', 'liste_attesa.ListeAttesaContainer'],
    stores: [],
    models: ['ComboModel','ListeAttesaModel']
});