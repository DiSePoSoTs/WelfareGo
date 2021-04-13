Ext.define('wp.store.ParamsFormStore',{
    
    extend: 'Ext.data.Store',
    model: 'wp.model.ParamsModel',
    successProperty: 'success',

    proxy: {
        type: 'ajax',
        url : '/Parametri/ParametriServlet?action=LOADFORM',
        reader: {
            type: 'json',
            root: 'data'
        }

    }	
	
    
});