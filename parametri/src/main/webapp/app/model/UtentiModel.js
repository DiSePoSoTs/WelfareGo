Ext.define('wp.model.UtentiModel', {
    extend: 'Ext.data.Model',
    
    fields: [ 
    'cod_ute',
    'cod_fisc',
    'nome',
    'cognome',
    'username',
    'id_param_uot',
    'id_param_ser',
    'id_param_po',
    'id_param_lvl_abil',
    'id_param_uot_des',
    'id_param_ser_des',
    'id_param_po_des',
    'id_param_lvl_abil_des',
    'num_tel',
    'num_cell',
    'email', 
    'note',
    'liferay_user_id',
    'motivazione',
    'problematiche',
    'profilo',
	'id_associazione'
    ]

});