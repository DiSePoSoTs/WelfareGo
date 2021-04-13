Ext.define('wf.store.ListeAttesaStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.ListeAttesaModel',
    autoLoad: true,
    pageSize: 20,
    remoteSort: true,
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/ListeAttesaServlet?action=LOAD'
    }
});