Ext.define('wf.model.TaskModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'task_id','cognome',
     'nome', 'attivita', 'pai', 'intervento', 'uot', 'assistente','servizio',
     'ruolo','urgente','approvato','templateFile','templateDesc',
    {
        dateFormat:'timestamp',
        name:'timestamp',
        type:'date'
    }]
});