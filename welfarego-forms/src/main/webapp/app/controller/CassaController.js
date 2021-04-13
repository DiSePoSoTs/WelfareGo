Ext.define('wf.controller.CassaController', {
	extend: 'Ext.app.Controller',
	views:  ['cassa.CassaContainer','cassa.LiquidareGrid','cassa.LogCassaGrid'],
	stores: ['LiquidareStore','LogCassaStore','combo.CassaSearchStore'],
	models: ['LiquidareModel','LogCassaModel','ComboModel']
});