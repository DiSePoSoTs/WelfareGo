Ext.define('wf.model.ImpegnoProrogheDetermineModel', {          //definizione modello ImpegnoProroghe
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },
    'anno','anno_erogazione', 'capitolo', 'impegno',
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
    }
    ]
});