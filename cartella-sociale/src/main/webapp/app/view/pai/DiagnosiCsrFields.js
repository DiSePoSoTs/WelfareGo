Ext.define('wcs.view.pai.DiagnosiCsrFields',{
    extend: 'Ext.container.Container',
    laout:'anchor',
    defaults:{
        anchor:'97%'
    },
    initComponent:function(){
        var macroProblematicheStore=Ext.getStore('macroProblematicheStore'),
        microProblematicheStore=Ext.getStore('microProblematicheStore');
        var items=[];
        
        log('building DiagnosiCsrFields form macro/micro',macroProblematicheStore,microProblematicheStore);
        
        macroProblematicheStore.each(function(record){
            var fieldSetId=Ext.id(),macroProbCode=record.data.code;
            var microProbs=[];
            microProblematicheStore.each(function(record){
                if(record.data.param==macroProbCode){
                    microProbs.push({
                        boxLabel: record.data.name, 
                        name: 'microProblematiche_'+macroProbCode+'_'+record.data.code, 
                        inputValue: record.data.value
                    })
                }
            });
            var fieldSetItems=[{
                    xtype:'combo',
                    fieldLabel:'Rilevanza*',
                    name:'rilevanza_'+macroProbCode,
                      store:'problematicheRilevanzaComboStore',
                    displayField: 'name',
                    valueField: 'value',
                    allowBlank:false,
                    editable:false
                },{
                    xtype:'combo',
                    fieldLabel:'Fronteggiamento*',
                    name:'fronteggiamento_'+macroProbCode,
                    id:'fronteggiamento_'+macroProbCode,
                    store:'problematicheFronteggiamentoComboStore',
                    displayField: 'name',
                    valueField: 'value',
                    allowBlank:false,
                    editable:false
                },{
                    xtype:'combo',
                    fieldLabel:'Obiettivo prevalente*',
                    name:'obiettivoPrevalente_'+macroProbCode,
                    id:'obiettivoPrevalente_'+macroProbCode,
                    store:'problematicheObiettivoPrevalenteComboStore',
                    displayField: 'name',
                    valueField: 'value',
                    allowBlank:false,
                    editable:false
                },{
                    xtype:'textarea',
                    fieldLabel:'Dettaglio/note',
                    name:'dettaglioNote_'+macroProbCode
                },{            
                    xtype: 'checkboxgroup',
                    fieldLabel: 'Micro Problematiche',
                    columns: 2,
                    vertical: true,
                    items: microProbs
                }];
            items.push({
                xtype:'checkbox',
                padding:'0 0 0 15',
                boxLabel:record.data.name,
                name:'macroProblematiche_'+macroProbCode,
                inputValue:record.data.value,
                handler:function(checkbox,checked){
                    var fieldSet=Ext.getCmp(fieldSetId);
                    if(checked){
                        fieldSet.add(fieldSetItems);
                        fieldSet.setVisible(true);
                        //valori di default voluti da Chicco
                         component1= Ext.getCmp('obiettivoPrevalente_'+macroProbCode);
                         component1.setValue(621);
                         component2= Ext.getCmp('fronteggiamento_'+macroProbCode);
                         component2.setValue(613);
                         //                        fieldSet.setDisabled(false);
                    }else{
//                        fieldSet.setDisabled(true);
                        fieldSet.setVisible(false);     
                        fieldSet.removeAll();
                    }
                }
            },{
                xtype:'fieldset',
                hidden:true,
//                disabled:true,
                id:fieldSetId,
                items:[]
            })
        });            
        
        this.items=items;
        
        this.callParent();
    //        this.callParent(arguments);
    }
});