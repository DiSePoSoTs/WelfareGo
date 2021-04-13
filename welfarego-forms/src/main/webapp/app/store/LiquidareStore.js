Ext.define('wf.store.LiquidareStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.LiquidareModel',
    
    pageSize: 10000,
    autoLoad: true,
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/CassaServlet',
        action:"load",
        extraParams: {
            type: 'daLiquidare'
           
        },
        
   
    reader: {
        type: 'json',
        root: 'data',
        totalProperty: 'total',
        successProperty: 'success'
       
    }
    }
    
});