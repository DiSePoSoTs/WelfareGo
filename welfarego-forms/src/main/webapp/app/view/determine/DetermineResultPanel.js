Ext.require('wf.utils.WfUtils');
Ext.define('wf.view.determine.DetermineResultPanel',{
    extend: 'Ext.Panel',
    alias: 'widget.wf_determine_result_panel',
    frame:true,
    layout:'anchor',
    initComponent: function() {
        this.items=[{
            xtype:'container',
            layout: 'column',
            style:{
                "padding-top": '270px'
            },
            items:[{
                xtype:'container',
                layout:'anchor',
                columnWidth: .55,
                items:[{
                    xtype:'displayfield',
                    value:'<b>&Egrave; stata prodotta la seguente determina</b>',
                    anchor:'100%',
                    cls:'determine_result'
                },{
                    xtype:'displayfield',
                    value:'<b>&Egrave; stato prodotto il seguente allegato</b>',
                    anchor:'100%',
                    cls:'determine_result'
                },{
                    xtype:'displayfield',
                    value:'<b>Torna indietro</b>',
                    anchor:'100%',
                    cls:'determine_result'
                }]
            },{
                xtype:'container',
                layout:'anchor',
                columnWidth: .45,
                items:[{
                    xtype:'container',
                    anchor:'100%',
                    items:[{
                        xtype:'button',
                        text:'Apri',
                        width:100,
                        handler:wf.utils.WfUtils.getLoadDavDocumentFunc({
                                url: wf.config.path.determine,
                                params: {
                                    parameters:wf.determine.parameters,
                                    action:"LOAD",
                                    requireDocument:"determina"
                                }
                            })
                    }]
                },{
                    xtype:'container',
                    anchor:'100%',
                    items:[{
                        xtype:'button',
                        text:'Apri',
                        width:100,
                        handler:wf.utils.WfUtils.getLoadDavDocumentFunc({
                                url: wf.config.path.determine,
                                params: {
                                    parameters:wf.determine.parameters,
                                    action:"LOAD",
                                    requireDocument:"allegato"
                                }
                            })
                    }]
                }, {
                    xtype:'container',
                    anchor:'100%',
                    items:[{
                        xtype:'button',
                        text:'indietro',
                        width:100,
                        handler:function(){
                            var container=Ext.getCmp('wf_determine_container');
                            var activeItem = container.getLayout().getActiveItem();
                            container.getLayout().prev();
                            Ext.getStore('DetermineStore').load();
                            container.remove(activeItem);
                        }
                    }]
                }]
            }]
        }];
        this.callParent(arguments);
    }
});