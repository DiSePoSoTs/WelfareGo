Ext.define('wp.model.DatiModel', {
    extend: 'Ext.data.Model',
    
    fields: [ 
    'cod_campo',
    'des_campo',
    'tipo_campo',
    'val_amm',
    'val_def',
    'cod_campo_csr',
    
    'flg_obb',
    'flg_edit',
    'flg_vis',
    'reg_expr',
    'msg_errore',
    'lunghezza',
    'decimali',
    
    // valgono solo per la grid relazionati
    'cod_tipint',
    'col_campo',
    'row_campo'
    ]

});