Ext.define('wf.controller.ImportazioneController', {
	extend: 'Ext.app.Controller',
	views:  ['importazione.ImportazioneContainer'],
	stores: ['combo.TipologieInterventoStore'],
	models: ['ComboModel']
});