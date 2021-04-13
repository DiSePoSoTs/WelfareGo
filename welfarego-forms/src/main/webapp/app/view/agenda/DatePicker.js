/**
 * LEGACY, should be safe to delete
 */
Ext.define('wf.view.agenda.DatePicker',{
	extend: 'Ext.Panel',
	alias: 'widget.wtl_agenda_date_picker',
	title: 'Calendario',
	bodyCls:'standardBgColor',
	initComponent: function() {
		this.items = [{
			xtype:'container',
			items:[{
				xtype: 'datepicker',
				width:'100%',
				format: 'd/m/Y',
				dataViewId:this.dataViewId,
				listeners: {
					select: {
						fn:function(combo, value) {
							if(this.dataViewId!=null){
								var dvCmp = Ext.getCmp(this.dataViewId);
								var store = dvCmp.getStore();
								store.proxy.extraParams['agenda_current_date'] = value;
								store.load();
							}
						}
					}
				}
			},{
				xtype:'container',
				html:'<div>\n\
                        <div class="agenda_legenda_indisponibile">Indisponibile</div>\n\
                        <div class="agenda_legenda_appuntamento">Appuntamento</div>\n\
                      </div>'

			}]
		}]

		this.callParent(arguments);
		
		Ext.ns('wf.utils');	
		var item=this;
		wf.utils.reloadAgenda=function(){
			log('reloading agenda');
			Ext.getCmp(item.dataViewId).getStore().load();
		};
	}
});