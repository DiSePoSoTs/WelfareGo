Ext.define('wf.view.importazione.ImportazioneContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.wf_importazione_container',
    id:'importazionePanel',
    layout: 'anchor',
    fullscreen: true,
    items:[{
        xtype: 'form',
        layout: 'column',
        
        items: [{
                xtype: 'filefield',
                name: 'file',
                label: 'File',
                buttonText: 'Scegli file...',
                allowBlank: false,
                columnWidth: .40
            },{
                xtype: 'combobox',
                fieldLabel: 'Tipologia intervento',
                name: 'tipo_intervento',
                id: 'tipo_intervento',
                store: 'combo.TipologieInterventoStore',
                emptyText: 'Tipo intervento ...',
                displayField: 'name',
                valueField: 'value',
                 forceSelection: true,
                 columnWidth:.35,
                queryMode: 'local',
                listeners: {
                 	  beforequery: function(q) {
                        if (q.query) {
                            var length = q.query.length;
                            q.query = new RegExp(Ext.escapeRe(q.query),'i');
                            q.query.length = length;
                        }
                    }
          }
//    
            },{
                xtype: 'combobox',
                fieldLabel: 'Tipologia importazione',
                name: 'tipo_import',
                id: 'tipo_import',
                store:  new Ext.data.SimpleStore({
					data : [['I', 'Interventi'], ['P', 'Pagamenti']
							],
					id : 0,
					fields : ['value', 'text']
				}),
                emptyText: 'Seleziona..',
                displayField: 'text',
                valueField: 'value',
                 forceSelection: true,
                 columnWidth:.35,
                queryMode: 'local'
            }],
        buttons: [{
                text: 'Carica',
                handler: function(button) {
               
                    this.up('form').submit({
                        url: '/WelfaregoForms/ImportazioneServlet',
                       success: function(form, action) {
                        	
                        	
                       //  Ext.Msg.alert('OK', action.result.message);                                                
                        },
                        failure: function(form, action) { 
                           Ext.Msg.alert('ERROR', action.result.message);     
                        }
                    });
                }
            }]
    }
    ]
 
});
