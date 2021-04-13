Ext.define('wf.controller.LettereController', {
	extend: 'Ext.app.Controller',
	views:  ['lettere.LettereContainer','lettere.LettereGrid','lettere.LettereTopBar'],
	stores: ['LettereStore','combo.UotStore','combo.LettereSearchStore'],
	models: ['LettereModel','ComboModel']
});