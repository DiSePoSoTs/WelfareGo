Ext.define('wf.controller.ReportsController', {
    extend: 'Ext.app.Controller' ,
    views: [
    'reports.ReportCenterPanel',
    'reports.ReportListPanel',
    'reports.ReportParameterWindow'//,
//    'reports.ReportParameterFields'
    ],
    stores: [
        'ReportsStore',
        'combo.TipologieInterventoStore',
        'combo.UotStore',
        'combo.CondFamiliareStore',
        'combo.GiuridicheStore'
    ],
    models: ['ComboModel']
});