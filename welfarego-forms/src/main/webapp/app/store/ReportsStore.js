Ext.define('wf.store.ReportsStore',{
    extend: 'wf.store.GenericStore',
    fields: ['report'],
    
    constructor:function(){
        this.callParent(arguments);
        this.setStoreName("report_list");
    }
});