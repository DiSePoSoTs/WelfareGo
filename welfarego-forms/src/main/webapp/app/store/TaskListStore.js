Ext.define('wf.store.TaskListStore',{
    extend: 'wf.store.GenericStore',
    config:{
        model: 'wf.model.TaskModel',
        //    autoLoad: true,
        pageSize: 24,
        remoteSort: true,
        autoLoad:false,
        name:'taskListStore',
        id:'TaskListStore',
        proxy: {
            type: 'ajax',
            url: wf.config.path.base+'/TaskServlet',
            timeout: 120000,
            extraParams: {
                action:"LOAD"
            },
            reader: {
                type: 'json',
                root: 'data',    
                successProperty: 'success'
            }
        },
        sorters: [{
            property: 'timestamp',
            direction: 'DESC'
        }]
    }
});