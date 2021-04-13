Ext.define('wf.model.LettereModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'task_id','cognome', 'nome', 'attivita', 'pai', 'intervento','uot',{
             dateFormat:'timestamp',
             name:'data_task',
             type:'date'}]

});