Ext.define('wf.store.LogCassaStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.LogCassaModel',
    
    pageSize: 24,
    autoLoad: true,
    remoteSort: true,
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/CassaServlet',
        action:"load",
        extraParams: {
            type: 'logCassa'
           
        },
        
   
    reader: {
        type: 'json',
        root: 'data', 
        successProperty: 'success'
       
    }
    },
    sorters: [{
        property: 'data_operazione',
        direction: 'DESC'
    }]
    
});