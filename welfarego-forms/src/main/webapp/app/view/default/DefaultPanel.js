Ext.define('wf.view.default.DefaultPanel',{
	extend: 'wf.view.GenericFormPanel',
	alias: 'widget.wa_default_panel',
	layout: 'fit',
	config:{
		autoScroll:false 
	},
	constructor: function(config) {
		this.initConfig(config);
		this.codForm = 'default';
		this.items = [{
			xtype: 'container',
			html:'<h1>Selezionare un\'azione dalla lista delle attivit&agrave;...</h1>'
		}];
		this.callParent(arguments);
	}
});