Ext.define('wf.store.AgendaStore',{
    extend: 'wf.store.GenericStore',
    model: 'wf.model.TaskImpegnoModel',
//    model: 'Ext.data.Model',
//    autoLoad: true,
    pageSize: 20,
    remoteSort: true,
    autoLoad:true,
    proxy: {
        type: 'ajax',
        url: wf.config.path.base+'/AgendaServlet',
        extraParams: {
			  action:"LOAD"
        }//,
  //      reader: {
 //           type: 'json',
 //           root: 'impegno',
  //          successProperty: 'success'
  //      }
    }
});