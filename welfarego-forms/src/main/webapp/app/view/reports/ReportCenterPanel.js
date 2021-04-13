Ext.define('wf.view.reports.ReportCenterPanel',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wr_center_panel',
    layout:'fit',
    closable:true,
    autoScroll:true,
    viewUrl:'/WelfaregoForms/ViewReport',
    reportParameters : new Object(),
    initComponent: function() {	
        var me = this;
        this.tbar=[{
            iconCls: 'icon-export-refresh',
            text: 'Ricarica',
            scope: this,
            handler: function(){
                this.loadReport();
            }
        },
        '->',
        {
            iconCls: 'icon-export-xls',
            text: 'Esporta ODS',
            scope: this,
            handler: function(){
                me.reportParameters['report_type'] = 'xls';
                var urlParameters = '?report_parameters='+Ext.JSON.encode(me.reportParameters);
                window.open(this.viewUrl+urlParameters,'_blank','');
            }
        },{
            iconCls: 'icon-export-rtf',
            text: 'Esporta Rtf',
            scope: this,
            handler: function(){
                me.reportParameters['report_type'] = 'rtf';
                var urlParameters = '?report_parameters='+Ext.JSON.encode(me.reportParameters);
                window.open(this.viewUrl+urlParameters,'_blank','');
            }
        },{
            iconCls: 'icon-export-pdf',
            text: 'Esporta Pdf',
            scope: this,
            handler: function(){
                me.reportParameters['report_type'] = 'pdf';
                var urlParameters = '?report_parameters='+Ext.JSON.encode(me.reportParameters);
                window.open(this.viewUrl+urlParameters,'_blank','');
            }
        }];
        this.loader={
            contentType: 'html',
            autoLoad: false,
            loadMask: true
        }
        this.callParent(arguments);
    },
    loadReport:function(){
        var me = this;
        Ext.Ajax.request({
            url: wf.config.path.report,
            params: {
                report_name:me.report,
                action:"LOAD"
            },
            success: function(response){
                var data=Ext.JSON.decode(response.responseText);
                if(data.length==0){
                    var obj = new Object();
                    obj.report_name = me.report;
                    obj.report_type = 'html';
                    me.reportParameters['report_name']=me.report;
                    me.getLoader().load({
                        url: me.viewUrl,
                        params: {
                            report_parameters:Ext.JSON.encode(obj)
                        }
                    });
                }else{
                    var items = new Array();
                    for(i=0;i<data.length;i++){
                        items.push({
                            xtype:'wr_'+data[i]
                        });
                    }
                    Ext.create('wf.view.reports.ReportParameterWindow', {
                        parameterItems:items,
                        report:me
                    }).show();
                }
            }
        });
    }
});
