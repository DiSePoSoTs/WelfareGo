
Ext.define('wp.store.BudgetUotStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.BudgetUotModel',
    pageSize: 20,
    successProperty: 'success',
    autoLoad: false,
    

    proxy: {
        type: 'ajax',
        url : '/Parametri/BudgetServlet?action=LOADUOT',
        reader: {
            type: 'json',
            root: 'data'
        }

    },
    
    listeners: {
        load: function()
        {
            Ext.getCmp('wp_budgetgriduot').onLoadSuccess();
        }
    },
    sorters: [{
        property : 'id_param_uot',
        direction: 'ASC'
    }]


});