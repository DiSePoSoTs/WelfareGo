Ext.define('wp.store.TemplateStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.TemplateModel',
    pageSize: 999,
    successProperty: 'success',
    autoLoad: true,

    proxy: {
        type: 'ajax',
        url : '/Parametri/TemplateServlet?action=LOAD',
        reader: {
            type: 'json',
            root: 'data'
        }

    }

});