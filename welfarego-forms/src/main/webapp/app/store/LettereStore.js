Ext.define('wf.store.LettereStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.LettereModel',
    
    pageSize: 100,
    autoLoad: true,
    remoteSort: true,
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/LettereServlet',
        action:"load",
       
        
   
    reader: {
        type: 'json',
        root: 'data', 
        successProperty: 'success'
       
    }
    },
    sorters: [{
        property: 'cognome',
        direction: 'ASC'
    }]
   
    
});