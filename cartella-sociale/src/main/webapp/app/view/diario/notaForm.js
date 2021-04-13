Ext.define('wcs.view.diario.notaForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.wcs_notaform',
    bodyStyle:'padding:5px 5px 0',
    frame: true,
    autoScroll: true,
    layout:'anchor',            
    defaults:{                
        anchor:'100%'
    },
    initComponent: function() {
       
        this.items=[

    {
    xtype: 'container',
    columnWidth:.3,
    layout: 'anchor',
    items: [{
        id:'notaTitolo',
    	fieldLabel: 'Titolo',
        name: 'titolo',
        xtype:'textfield',
        anchor:'95%'
    }]
},{
    xtype: 'container',
    columnWidth:.3,
    layout: 'anchor',
    items: [{
        xtype:'textarea',
        id: 'notaEsteso',
        fieldLabel: 'Nota',
        name: 'esteso',
        maxLength: 5000,
        width: 800,
        maxLengthText: 'Lunghezza massima 5000 caratteri',
        anchor:'97%'
    }]
}

];
        
 var me= this;       
 this.buttons=[
             {text:"Salva",
              id: "salvaNotaButton",
               handler:function(){
               log('premuto il bottone di salvataggio');
               var form = me.getForm();
               var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
               var win = this.up('window');
               form.submit({
            	   url:'/CartellaSociale/anagrafica',
            	   waitTitle: 'Salvataggio',
                   waitMsg:'Sto salvando...',
                   params: {
                       action: 'salvaNota',
                       codAna: codAnag
                       
                                            	
                   },
                   success: function(form, action){
                	   var json = Ext.JSON.decode(action.response.responseText);
                	    var noteGrid =Ext.getCmp('wcs_diarioTab').items.get('noteFieldset').items.get('wcs_noteCondiviseList');
                	    if (json.success) {
                            Ext.MessageBox.show({
                                title: 'Esito operazione',
                                msg: json.message,
                                buttons: Ext.MessageBox.OK,
                                fn: function(){
                              	    win.close();
                                   noteGrid.store.load();
                                    
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: 'Errore',
                                msg: json.message,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.window.MessageBox.ERROR
                            });
                        }
                   },
                   failure: function (form, action, a, b, c, d){
                       Ext.MessageBox.show({
                           title: 'Esito operazione',
                           msg: 'Operazione non riuscita:controllare che tutti i campi siano compilati',
                           buttons: Ext.MessageBox.OK,
                           icon: Ext.window.MessageBox.ERROR
                       });
                   }
               });
               
           }
         }];       
    
     
        this.callParent(arguments);
    }
});