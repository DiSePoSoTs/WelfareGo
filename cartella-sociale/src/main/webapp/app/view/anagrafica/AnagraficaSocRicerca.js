Ext.define('wcs.view.anagrafica.AnagraficaSocRicerca', {
    extend: 'Ext.window.Window',
    title : 'Ricerca',
    layout: 'fit',
    modal: true,
    width: 700,
    height: 300,
    autoShow: true,
    initComponent: function() {
        
        var store=Ext.create('Ext.data.Store', {
            autoLoad:false,
            fields:['codiceAnagrafica','ragioneSociale', 'codiceFiscale', 'partitaIva'],
            proxy: {
                type: 'ajax',
                url:'/CartellaSociale/anagraficaSoc',
                extraParams:{
                    action:'search'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        });     
        
        var searchField;
        
        var search=function(){
            var filter=searchField.getValue();
            if(filter!==''){
                store.proxy.extraParams.filter=filter;
                store.load();
            }          
        };
        
        var me=this;
        
        this.items= {
            xtype: 'gridpanel',
            store:store,
            columns : [{
                header: 'Ragione Sociale',
                dataIndex: 'ragioneSociale',
                sortable: true,
                flex:1
            },{
                header: 'Codice Fiscale',
                dataIndex: 'codiceFiscale',
                sortable: true,
                flex:1
            },{
                header: 'Partita Iva',
                dataIndex: 'partitaIva',
                sortable: true,
                flex:1
            }],
            listeners:{
                itemdblclick:function(view,record){
                    me.anagraficaSocForm.loadAnagrafica(record.data.codiceAnagrafica);
                    me.close();
                }
            },
            bbar:{
                xtype: 'pagingtoolbar',
                store: store,
                items:['-',{
                    xtype:'label',
                    text:'ricerca:  '
                },searchField=Ext.create('Ext.form.field.Text',{
                    enableKeyEvents:true,
                    listeners:{
                        specialkey: function(field, e){
                            if (e.getKey() == e.ENTER) {
                                search();
                            }
                        }
                    }
                }),{
                    xtype:'button',
                    text:'cerca',
                    handler:search
                },{
                    xtype:'button',
                    text:'chiudi',
                    handler:function(){
                        me.close();
                    }
                }]
            }
        };
        
        
        this.callParent(arguments);
    }
});