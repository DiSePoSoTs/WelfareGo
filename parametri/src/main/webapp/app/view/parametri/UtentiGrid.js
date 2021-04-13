Ext.define('wp.view.parametri.UtentiGrid',{
   
    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_utentigrid',
    id: 'wp_utentigrid',
    
    bindForm: 'wp_utentiform',
    
    height: 365,
    store: 'UtentiStore',
    frame: false,

    columns: [{
        text: 'Cod_Ute',
        flex: 1,
        hidden: true,
        dataIndex: 'cod_ute'
    },{
        text: 'Username',
        flex: 1,
        dataIndex: 'username'
    },{
        text: 'Nome',
        flex: 1,
        dataIndex: 'nome'
    },{
        text: 'Cognome',
        flex: 1,
        dataIndex: 'cognome'
    },{
        text: 'UOT',
        flex: 1,
        dataIndex: 'id_param_uot_des'
    },{
        text: 'Livello',
        flex: 1,
        dataIndex: 'id_param_lvl_abil_des'
    },{
        text: 'Id Liferay',
        width: 75,
        dataIndex: 'liferay_user_id',
        renderer:function(value){
            if(value){
                return '<span style="color:green;">' + value + '</span>';
            }else{
                return '<span style="color:red;">N/A</span>';
            }
        }
    }],

    dockedItems: [{
        xtype: 'toolbar',
        dock: 'bottom',
        items: [{
            text    : 'Aggiungi Utente',
            tooltip : 'Aggiungi un nuovo utente',
            handler: function() {
                Ext.getCmp('wp_utentiform').setActiveRecord(null); // form vuoto per insert
            }
        }]
    }, {
        xtype: 'pagingtoolbar',
        store: 'UtentiStore',   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true,
        items:['-','filtra per:',{
            xtype:'textfield',
            enableKeyEvents:true,
            size:50,
            listeners:{
                keydown:function(field,event){
                    if(event.getKey()==event.ENTER){
                        var store=Ext.getStore('UtentiStore'),proxy=store.getProxy(),value=field.getValue();
                        if(!proxy.extraParams)
                            proxy.extraParams={};
                        if(value&&value!='')
                            proxy.extraParams.filter=value;
                        else
                            delete proxy.extraParams.filter;
                        store.load();
                    }
                }
            }
        }]
    }],


    listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
                Ext.getCmp('wp_utentiform').setActiveRecord(records[0]);
                Ext.getCmp('wp_utenti_btn_elimina').enable();
            } else {
                Ext.getCmp('wp_utentiform').setActiveRecord(null);
                Ext.getCmp('wp_utenti_btn_elimina').disable();
            }
        }
    }
    
});