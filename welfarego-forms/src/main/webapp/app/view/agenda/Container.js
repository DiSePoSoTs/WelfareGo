Ext.define('wf.view.agenda.Container',{
    extend: 'Ext.Panel',
    alias: 'widget.wftl_agenda_container',
    title: 'Agenda',
    autoHeight:true,
    layout: 'border',
    requires:['wf.view.agenda.AgendaNew'],
    initComponent: function() {
        
        function reloadAgenda(){
            var a=Ext.getCmp('extensibleCalendar');
            try{
                a.setStartDate(a.startDate);
            }catch(e){
                log('initial ignorable error',e);
            }
        }
        function setAssocFilter(value){
            log("set assoc filter : "+value);
            var store=Ext.getStore('extensibleAgendaEventStore');
            if(store){
                if(value && value !="all"){
                    store.proxy.extraParams.assSocFilter=value;
                }else{
                    delete store.proxy.extraParams.assSocFilter;
                }
                reloadAgenda();
            }
        }
        function setUotFilter(value){
            log("set uot filter : "+value);
            var store=Ext.getStore('extensibleAgendaEventStore');
            if(store){
                if(value && value !="all"){
                    store.proxy.extraParams.uotFilter=value;
                }else{
                    delete store.proxy.extraParams.uotFilter;
                } 
                reloadAgenda();                   
            }
        }
        
        var uotComboConfig={
            xtype:'combo',
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            id:'wf_agenda_uot_combo',
            //store: Ext.create('wf.store.combo.UotStore',{addValueAll:true}),
            store:'combo.UotStore',
            emptyText: 'UOT...',
            columnWidth:0.5,
            selectOnFocus:true,
            editable: false,
            listeners: {
                change:function(combo) {
                    setUotFilter(combo.getValue());
                //                    var store=Ext.getCmp('wtl_agenda_data_view').getStore();
                //                    if(!combo.getValue()||combo.getValue()=="all"){
                //                        delete store.proxy.extraParams.uotFilter;
                //                    }else{
                //                        store.proxy.extraParams.uotFilter=combo.getValue();
                //                    }
                //                    store.load();
                }
            }
        },assocComboConfig={
            xtype:'combo',
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            //store: Ext.create('wf.store.combo.AssistenteStore',{addValueAll:true}),
            id:'wf_agenda_assoc_combo',
            store:'combo.AssistenteStore',
            emptyText: 'Assistente sociale...',
            columnWidth:0.5,
            selectOnFocus:true,
            editable: false,
            listeners: {
                change:function(combo) {
                    setAssocFilter(combo.getValue());
                //                    var store=Ext.getCmp('wtl_agenda_data_view').getStore();
                //                    if(!combo.getValue()||combo.getValue()=="all"){
                //                        delete store.proxy.extraParams.asSocFilter;					
                //                    }else{
                //                        store.proxy.extraParams.asSocFilter=combo.getValue();
                //                    }
                //                    store.load();
                }
            }
        };		
		
        var uotFilter,assocFilter;
        if(wf.info.user){
            log("initializing agenda with user : "+wf.info.user.user_name);
            if(wf.info.user.uot.length==1){
                uotFilter=wf.info.user.uot[0];
                log("user uot : "+uotFilter);
                uotComboConfig.value=uotFilter;
            }
            var val=wf.info.user.id;
            if(Ext.getStore(assocComboConfig.store).find('value',val)!=-1){
                assocFilter=val;
                log("user id : "+assocFilter);
                assocComboConfig.value=assocFilter;
            }
        }
			
        this.items =  [{
            xtype:'panel',
            layout:'column',
            region:'north',
            items:[uotComboConfig,assocComboConfig]
        },{
            region: 'center',
            xtype: 'wftl_agenda_new'		
        }];

        this.callParent(arguments);
		
        //		if(wf.info.user){
        //			log("initializing agenda with user : "+wf.info.user.user_name);
        //			if(wf.info.user.uot.length==1){
        //				var cmp=Ext.getCmp('wf_agenda_uot_combo');
        //				log("user uot : "+wf.info.user.uot[0]);
        //				cmp.setValue(wf.info.user.uot[0]);
        //				cmp.fireEvent('change',cmp);
        //			}
        //			var val=wf.info.user.id,cmp=Ext.getCmp('wf_agenda_assoc_combo');
        //			if(cmp.store.find('value',val)!=-1){
        //				log("user id : "+val);
        //				cmp.setValue(val);
        //				cmp.fireEvent('change',cmp);
        //			}
        //		}

        //		var cmp1=Ext.getCmp('wf_agenda_assoc_combo'),cmp2=Ext.getCmp('wf_agenda_uot_combo');
        //		cmp1.fireEvent('change',cmp1);
        //		cmp2.fireEvent('change',cmp2);

        if(assocFilter){ 
            setAssocFilter(assocFilter);
        }
        if(uotFilter){ 
            setUotFilter(uotFilter);
        }
    }
});