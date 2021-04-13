Ext.define('wf.model.ImpegnoModel', {          //definizione modello Impegno
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },
    'anno', 'capitolo', 'impegno','unitaDiMisuraDesc',
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
        name: 'a_carico',
        type: 'number'
    }, 
    {
        name: 'bdgPrevQta',
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