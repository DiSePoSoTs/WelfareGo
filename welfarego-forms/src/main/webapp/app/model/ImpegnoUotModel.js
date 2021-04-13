Ext.define('wf.model.ImpegnoUotModel', {          //definizione modello Impegno
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },
    'anno', 'capitolo', 'impegno','uot',
    
    {
        name: 'budget_iniziale',
        type: 'number'
    },
    {
        name: 'imp_disp',
        type: 'number'
    },
    {
        name: 'imp_disp_netto',
        type: 'number'
    },
    {
        name: 'imp_disp_proroghe',
        type: 'number'
    },
    {
        name: 'totale_prenotato',
        type: 'number'
    },
       {
        name: 'bdgDispQta',
        type: 'number'
    },
    {
        name: 'bdgDispQtaCons',
        type: 'number'
    }]
});