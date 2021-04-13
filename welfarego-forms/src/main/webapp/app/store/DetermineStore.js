Ext.define('wf.store.DetermineStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.DetermineModel',
    
    pageSize: 10000,
   
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/DetermineServlet',
        action:"load",
   
    reader: {
        type: 'json',
        root: 'data'    
       
    }
    },
    load:function(){
        if(this.proxy.extraParams.tipo_intervento){
            this.callParent(arguments);
        }
    }
});