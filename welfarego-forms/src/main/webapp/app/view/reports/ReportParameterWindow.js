Ext.define('wf.view.reports.ReportParameterWindow',{
	extend: 'Ext.window.Window',
	alias: 'widget.wr_par_win',
	width:600,
	title:'Parametri',
	layout: 'fit',
	border:false,
	modal:true,
	autoScroll:true,
	config:{
		report:null,
		parameterItems:[]
	},
	constructor: function(config) {
		this.initConfig(config);
		//		this.parameterItems= config.parameterItems;
		//		this.report= config.report;
		this.callParent(arguments);
	//		wf.view.reports.ReportParameterWindow.superclass.constructor.apply(this, arguments);
	},
	initComponent: function() {
		var me = this;
		this.height= Math.min(78+(24*this.parameterItems.length),200);
		this.items=[{
			xtype:'form',
			layout:'anchor',
			defaults: {
				anchor: '100%',
				labelWidth :150
			},
			frame:true,
			autoScroll:true,
			items:this.parameterItems
		}];
		this.buttons=[{
			text:'Annulla',
			handler:function(){
				me.close();
			}
		},{
			text:'OK',
			handler:function(){
				me.submit();
			}
		}];
		this.keys= [{
			key: [Ext.EventObject.ENTER], 
			handler: function() {
				me.submit();
			}
		}];
		this.listeners= {
			activate: function() {
				me.down('field').focus(false, 100);
			}
		};
        
		this.callParent(arguments);
	},
	submit:function(){
		var me = this;
		var reportParameters = me.down('form').getForm().getValues();
		reportParameters['report_name'] = me.report.report;
		reportParameters['report_type'] = 'html';
                
		me.report.reportParameters = reportParameters;
                
		me.report.getLoader().load({
			url: me.report.viewUrl,
			params: {
				report_parameters:Ext.JSON.encode(me.report.reportParameters)
			}
		});
                
		me.close();
        
	}
});
