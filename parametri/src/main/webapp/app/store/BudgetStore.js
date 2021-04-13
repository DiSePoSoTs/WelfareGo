
Ext.define('wp.store.BudgetStore',{

    extend: 'Ext.data.Store',
    model: 'wp.model.BudgetModel',
    pageSize: 20,
    successProperty: 'success',
    autoLoad: false,
    

    proxy: {
        type: 'ajax',
        url : '/Parametri/BudgetServlet?action=LOAD',
        reader: {
            type: 'json',
            root: 'data'
        },
        params: {
            codTipint: null
        }

    },
    sorters: [{
        property : 'cod_anno',
        direction: 'DESC'
    },{
        property : 'cod_cap',
        direction: 'ASC'
    },{
        property : 'cod_impe',
        direction: 'ASC'
    },{
        property : 'cod_conto',
        direction: 'ASC'
    }]

});